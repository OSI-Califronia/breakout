package de.luma.breakout.controller;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.luma.breakout.communication.ObservableGame;
import de.luma.breakout.communication.TextMapping;
import de.luma.breakout.data.PlayGrid;
import de.luma.breakout.data.objects.AbstractBrick;
import de.luma.breakout.data.objects.Ball;
import de.luma.breakout.data.objects.Slider;

/**
 * TODO:
 * - level laden
 * - hitcount von bricks irgendwo erhöhen
 * - konstanten einführen (gewünschte fps usw.)
 */

public class GameController extends ObservableGame {	

	/**
	 * Input options while the game is running.
	 */
	public enum PLAYER_INPUT {
		LEFT,
		RIGHT,
		PAUSE
	}

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
	
	/** Maximum absolute speed that a ball can reach */
	public static final double MAX_BALL_SPEED = 10.0;
	
	public static final String LEVEL_PATH = "levels\\";

	/* #######################################  GAME INFRASTRUCTURE #######################################   */
	/* ###############################    Basics to make the game a game     ##############################  */
	
	/**
	 *  Initialize the game. Has to be called only one time when the game starts running 
	 */
	public void initialize() {		
		showMainMenu();
	}
	
	/**
	 * Prepares the next frame of the game:
	 * - Move balls and do collision tests
	 * - Check game rules (game over etc.)
	 * - Request repaint
	 */
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
	public void start() {
		// timer 
		resetTimer();
		timer.scheduleAtFixedRate(task, 0, 10);
		setState(GAME_STATE.RUNNING);

	}

	/**
	 * Pause the game and display the pause menu.
	 */
	public void pause() {
		cancelTimer();

		setState(GAME_STATE.PAUSED);		
		notifyGameMenu(new MENU_ITEM[] {MENU_ITEM.MNU_NEW_GAME, MENU_ITEM.MNU_CONTINUE, MENU_ITEM.MNU_BACK_MAIN_MENU, MENU_ITEM.MNU_END},  
				TextMapping.getTextForIndex(TextMapping.TXT_GAME_PAUSED));
	}

	/**
	 * Stop the game and display the game over menu.
	 */
	public void gameOver() {
		cancelTimer();

		setState(GAME_STATE.MENU_GAMEOVER);
		notifyGameMenu(new MENU_ITEM[]{MENU_ITEM.MNU_NEW_GAME, MENU_ITEM.MNU_END},
				TextMapping.getTextForIndex(TextMapping.TXT_YOU_LOSE));		
	}

	/**
	 * Stop the game and set game state to 'killed'.
	 */
	public void terminate() {
		cancelTimer();

		setState(GAME_STATE.KILLED);		
	}

	/**
	 * Stop the game and display the game won menu.
	 */
	public void winGame() {
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
	public void processMenuInput(MENU_ITEM indexOfMenuItem) {
		switch (indexOfMenuItem) {
		case MNU_NEW_GAME:
			this.setCreativeMode(false);
			setGrid(new PlayGrid(500, 500));
			// TODO: Richtiges level laden!!!
			getGrid().loadLevel(new File("test/sampleLevel1.txt"));
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
		case MNU_LEVEL_CHOOSE:
			//TODO
			break;
		case MNU_LEVEL_EDITOR: 
			setGrid(new PlayGrid(500, 500));
			this.setCreativeMode(true);
			this.start();
			break;
		}		
	}
	

	/* #######################################  GETTERS & SETTERS #######################################   */
	/* ############################   the same procedure as every year...    ###########################  */

	public GameController() {
		super();		
	}
	
	public PlayGrid getGrid() {
		return grid;
	}

	public void setGrid(PlayGrid grid) {
		this.grid = grid;
	}

	public GAME_STATE getState() {
		return state;
	}

	public void setState(GAME_STATE state) {
		this.state = state;
		notifyGameStateChanged(state);
	}

	public boolean getCreativeMode() {
		return isInCreativeMode;
	}
	
	/**
	 * Enable creative mode to display the running game without
	 * moving the balls.
	 */

	public void setCreativeMode(boolean enableCreativeMode) {
		this.isInCreativeMode = enableCreativeMode;
	}
	
	public List<AbstractBrick> getBrickClasses() {
		return getGrid().getBrickClasses();
	}
	
	public void saveLevel() {		
		getGrid().saveLevel(new File(LEVEL_PATH + "userLevel" + System.nanoTime() + ".lvl"));
	}

	
}
