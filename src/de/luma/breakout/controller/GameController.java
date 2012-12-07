package de.luma.breakout.controller;

import java.awt.Dimension;
import java.awt.geom.Path2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.naming.spi.DirectoryManager;

import de.luma.breakout.communication.ObservableGame;
import de.luma.breakout.communication.TextMapping;
import de.luma.breakout.data.PlayGrid;
import de.luma.breakout.data.objects.AbstractBrick;
import de.luma.breakout.data.objects.Ball;
import de.luma.breakout.data.objects.IDecodable;
import de.luma.breakout.data.objects.Slider;

/**
 * TODO:
 * - level laden
 * - hitcount von bricks irgendwo erhöhen
 * - konstanten einführen (gewünschte fps usw.)
 */

public class GameController extends ObservableGame implements IGameController {	

	/**
	 * This task gets scheduled by start() to make the
	 * game run with a constant FPS.
	 */
	private class GameTimerTask extends TimerTask {		
		public GameTimerTask() {
			super();
		}		

		@Override
		public void run() {
			updateGame();			
		}		
	}	

	private PlayGrid grid;	
	private Timer timer;
	private GameTimerTask task;
	private GAME_STATE state;
	private boolean isInCreativeMode;
	
	public static final String LEVEL_PATH = "levels\\";
	
	/**
	 * Default Constructor
	 */
	public GameController() {
		super();		
	}


	/* #######################################  GAME INFRASTRUCTURE #######################################   */
	/* ###############################    Basics to make the game a game     ##############################  */
	
	/**
	 *  Initialize the game. Has to be called only one time when the game starts running 
	 */
	@Override
	public void initialize() {		
		showMainMenu();
	}
	
	/**
	 * Prepares the next frame of the game:
	 * - Move balls and do collision tests
	 * - Check game rules (game over etc.)
	 * - Request repaint
	 */
	@Override
	public void updateGame() {
		
		// move game objects only when not in creative mode
		if (!this.isInCreativeMode) {
			
			moveBalls();				
			
			// Check if no ball on game grid
			if (getGrid().getBalls().isEmpty()) {
				gameOver();
			}

			// check if no more bricks left
			if (getGrid().getBricks().isEmpty()) {
				winGame();
			}
		}
			
		// notify bricks of new frame (e.g. for moving bricks)
		for (AbstractBrick brick : getGrid().getBricks()) {
			brick.onNextFrame();
		}
		
		notifyNextGameFrame();
		notifyRepaintPlayGrid();
		
		
	}
	
	/**
	 * Moves all balls, regarding collisions with bricks, the grid borders and the slider.
	 * Balls and bricks get removed by this method when the grid or a brick signals to do so.
	 */
	private void moveBalls() {
		Iterator<AbstractBrick> itbrick;
		Iterator<Ball> itball;
		AbstractBrick currentBrick; 
		Ball currentBall;

		itball = getGrid().getBalls().iterator();
		while (itball.hasNext()){ 
			currentBall = itball.next();

			// check for collisions with bricks (and change direction)
			itbrick = getGrid().getBricks().iterator();
			while (itbrick.hasNext()) {
				currentBrick = itbrick.next();
				if (currentBrick.tryCollision(currentBall)) {
					itbrick.remove();
				}
			}

			// check for collisions with grid borders (and change direction)
			if (getGrid().tryCollision(currentBall)) {
				itball.remove();
			}			

			// check for collisions with slider (and change direction)
			Slider s = getGrid().getSlider();
			s.tryCollision(currentBall);

			// move balls
			currentBall.setX(currentBall.getX() + currentBall.getSpeedX());
			currentBall.setY(currentBall.getY() + currentBall.getSpeedY());
		}


	}

	/**
	 * Initialize a new game timer to automatically calculate the
	 * next game frame.
	 * @return
	 */
	protected Timer resetTimer() {
		task = new GameTimerTask();		
		timer = new Timer("Game Timer");
		return timer;
	}

	/**
	 * Stop the game timer. No more frames will be calculated automatically.
	 */
	protected void cancelTimer()  {
		if (timer != null) {
			timer.cancel();
		}
	}
	
	/**
	 * Start or resume the game.
	 */
	private void start() {
		// timer 
		resetTimer();
		timer.scheduleAtFixedRate(task, 0, 10);
		setState(GAME_STATE.RUNNING);

	}

	/**
	 * Pause the game and display the pause menu.
	 */
	private void pause() {
		cancelTimer();

		setState(GAME_STATE.PAUSED);		
		notifyGameMenu(new MENU_ITEM[] {MENU_ITEM.MNU_NEW_GAME, MENU_ITEM.MNU_CONTINUE, MENU_ITEM.MNU_BACK_MAIN_MENU, MENU_ITEM.MNU_END},  
				TextMapping.getTextForIndex(TextMapping.TXT_GAME_PAUSED));
	}

	/**
	 * Stop the game and display the game over menu.
	 */
	private void gameOver() {
		cancelTimer();

		setState(GAME_STATE.MENU_GAMEOVER);
		notifyGameMenu(new MENU_ITEM[]{MENU_ITEM.MNU_NEW_GAME, MENU_ITEM.MNU_END},
				TextMapping.getTextForIndex(TextMapping.TXT_YOU_LOSE));		
	}

	/**
	 * Stop the game and set game state to 'killed'.
	 */
	private void terminate() {
		cancelTimer();

		setState(GAME_STATE.KILLED);		
	}

	/**
	 * Stop the game and display the game won menu.
	 */
	private void winGame() {
		cancelTimer();
		setState(GAME_STATE.MENU_WINGAME);
		notifyGameMenu(new MENU_ITEM[]{MENU_ITEM.MNU_NEW_GAME, MENU_ITEM.MNU_END}, 
				TextMapping.getTextForIndex(TextMapping.TXT_YOU_WIN));		
	}


	/* #######################################  USER INPUT HANDLING #######################################   */
	/* ###############################          menus & key events      ##############################  */

	/**
	 * Process interactive user input during the running game (e.g. from key hits)
	 */
	@Override
	public void processGameInput(PLAYER_INPUT input) {
		switch (input) {
		case LEFT:
			moveSlider(-5);
			break;
		case RIGHT:
			moveSlider(+5);
			break;
		case PAUSE:
			if (getState() == GAME_STATE.RUNNING) {
				pause();
			}
		}
	}
	
	/**
	 * Control slider movements since the slider has no information about the grid.
	 * @param delta Positive or negative value to move slider.
	 */
	private void moveSlider(int delta) {
		int newx = getGrid().getSlider().getX() + delta;
		if (newx < 0) {
			return;
		} else if (newx > getGrid().getWidth() - getGrid().getSlider().getWidth()) {
			return;
		} else {
			getGrid().getSlider().setX(newx);
		}
	}
	
	/**
	 * Display the main game menu.
	 */
	private void showMainMenu() {
		setState(GAME_STATE.MENU_MAIN);
		notifyGameMenu(new MENU_ITEM[]{MENU_ITEM.MNU_NEW_GAME, MENU_ITEM.MNU_LEVEL_CHOOSE, MENU_ITEM.MNU_LEVEL_EDITOR, MENU_ITEM.MNU_END},
				TextMapping.getTextForIndex(TextMapping.TXT_MAIN_MENU));
	}
	
	/**
	 * Process the given menu input. 
	 * It is not checked whether the given menu item 
	 * is valid at the current game status.
	 * @param indexOfMenuItem one of the menu items that were 
	 * proposed by notifyGameMenu().
	 */
	@Override
	public void processMenuInput(MENU_ITEM indexOfMenuItem) {
		switch (indexOfMenuItem) {
		case MNU_NEW_GAME:
			this.setCreativeMode(false);			
			loadLevel(new File(getLevelList().get(0)));
			this.start();
			break;
		case MNU_END:
			// TODO save level and gameprocess
			terminate();	
			break;
		case MNU_CONTINUE:
			start();
			break;
		case MNU_BACK_MAIN_MENU:
			if (getState() != GAME_STATE.RUNNING) {
				showMainMenu();
			}
			break;
		case MNU_LEVEL_CHOOSE:
			this.setState(GAME_STATE.MENU_LEVEL_SEL);			
			break;
		case MNU_LEVEL_EDITOR: 
			this.setCreativeMode(true);
			clearGrid();
			setGridSize(500, 500);			
			this.start();			
			break;
		}		
	}
	
	@Override
	public GAME_STATE getState() {
		return state;
	}
	
	private void setState(GAME_STATE state) {
		this.state = state;
		notifyGameStateChanged(state);
	}

	@Override
	public boolean getCreativeMode() {
		return isInCreativeMode;
	}
	
	/**
	 * Enable creative mode to display the running game without
	 * moving the balls.
	 */

	private void setCreativeMode(boolean enableCreativeMode) {
		this.isInCreativeMode = enableCreativeMode;
	}
	
	/* #######################################  LEVEL HANDLING #######################################   */
	
	@Override
	public void saveLevel() {		
		saveLevel(new File(LEVEL_PATH + "userLevel" + System.nanoTime() + ".lvl"));
	}	
	
	@Override
	public boolean saveLevel(File f)  {
		PrintWriter out = null;
		try {
			Locale.setDefault(new Locale("en", "US"));			
			OutputStreamWriter w;			
			w = new OutputStreamWriter(new FileOutputStream(f));
			
			out = new PrintWriter(new BufferedWriter(w));
			
			// save Grid Properties
			out.println(getGrid().encode());
				
			// save bricks
			for (AbstractBrick brick : getGrid().getBricks()) {
				out.print(brick.getClass().getName());
				out.print(':');
				out.println(brick.encode());
			}
			
			// save balls
			for (Ball b : getGrid().getBalls()) {
				out.print(b.getClass().getName());
				out.print(':');
				out.println(b.encode());
			}
			
			// save slider - last object, no newline at the end!
			out.print(getGrid().getSlider().getClass().getName());
			out.print(':');
			out.print(getGrid().getSlider().encode());
			
			return true;
			
		} catch (FileNotFoundException e) {					
			return false;
		} finally {
			if (out != null) {
				out.close();
			}
		}		
	}
	
	@SuppressWarnings("unused")
	private boolean loadLevel() {
		//TODO load next level after win game z.b.
		return false;
	}
	
	@Override
	public boolean loadLevel(File f) {
		Scanner s = null;
		try {
			Locale.setDefault(new Locale("en", "US"));
			s = new Scanner(f);
					
			// decode Grid Properties
			String line = s.nextLine();			
			this.getGrid().decode(line);
			
			while (s.hasNextLine()) {
				line = s.nextLine();
				
				String className = line.substring(0, line.indexOf(':'));				
				Class<?> classObj = this.getClass().getClassLoader().loadClass(className);
				
				IDecodable obj = (IDecodable) classObj.newInstance();
				obj.decode(line.substring(className.length()+1));			
								
				if (obj instanceof Ball) {
					getGrid().getBalls().add((Ball) obj);
				} else if (obj instanceof Slider) {
					getGrid().setSlider((Slider) obj);
				} else if (obj instanceof AbstractBrick) {
					getGrid().getBricks().add((AbstractBrick) obj);
				} else {
					throw new IllegalArgumentException("Unknown Game Obj in level " + f.getName());					
				}
			}
			
			notifyOnResize();			
			
		} catch(Exception e) {
			return false;
		} finally {
			s.close();
		} 
		return true;
	}
	
	/**
	 * Get a list of file paths of available levels.
	 * @return
	 */
	@Override
	public List<String> getLevelList() {
		File f = new File(LEVEL_PATH);
		List<String> retVal = new ArrayList<String>();
		
		for (String s : f.list()) {
			if (s.endsWith(".lvl")) {
				retVal.add(f.getPath() + "\\" + s);
			}
		}
		return retVal;
	}
	
	/* #######################################  GRID ACCESS HANDLING #######################################   */
	/* ############################   the same procedure as every year...    ###########################  */


	
	private PlayGrid getGrid() {
		if (grid == null) {
			grid = new PlayGrid(500, 500);
		}
		return grid;
	}

	private void setGrid(PlayGrid grid) {
		this.grid = grid;
	}
	
	/**
	 * Change play grid size.
	 */
	public void setGridSize(int width, int height) {
		// set grid size
		getGrid().setWidth(width);
		getGrid().setHeight(height);
		
		// fit slider position to new size
		if (getSlider() != null) {
			getSlider().setY(height - getSlider().getHeight() -5);
			getSlider().setX(0);
		}
		
		notifyOnResize();
	}
	
	public Dimension getGridSize() {
		return new Dimension(getGrid().getWidth(), getGrid().getHeight());
	}

	
	
	public List<AbstractBrick> getBrickClasses() {
		return getGrid().getBrickClasses();
	}
	
	
	

	public List<Ball> getBalls() {	
		return getGrid().getBalls();
	}

	public void addBall(Ball ball) {
		getGrid().addBall(ball);		
	}

	public List<AbstractBrick> getBricks() {
		return getGrid().getBricks();
	}

	public void addBrick(AbstractBrick brick) {
		getGrid().addBrick(brick);
	}

	public Slider getSlider() {
		return getGrid().getSlider();
	}

	public void setSlider(Slider slider) {
		getGrid().setSlider(slider);
	}

	@Override
	public void clearGrid() {
		getGrid().clearGrid();	
		
	}

	
	
}
