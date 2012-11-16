package controller;

import java.io.File;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import communication.ObservableGame;
import communication.TextMapping;

import data.PlayGrid;
import data.objects.AbstractBrick;
import data.objects.Ball;
import data.objects.Slider;


public class GameController extends ObservableGame {	
	
	/**
	 * TODO:
	 * - Nur reagieren wenn Spiel läuft (ProcessInput)
	 * - maximale Ballgeschwindigkeit einführen
	 * - level laden
	 * - neustart ermöglichen
	 * - timer thread richtig beenden
	 */

	public enum PLAYER_INPUT {
		LEFT,
		RIGHT,
		CLOSE,
		PAUSE,
		KILL,
		START
	}

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

	public GameController() {
		super();		
	}

	public void initialize() {		
		setState(GAME_STATE.MENU_MAIN);
		notifyGameMenu(new MENU_ITEM[]{MENU_ITEM.MNU_NEW_GAME, MENU_ITEM.MNU_LEVEL_CHOOSE, MENU_ITEM.MNU_END},
				TextMapping.getTextForIndex(TextMapping.TXT_MAIN_MENU));
	}

	/**
	 * Prepares the next frame of the game:
	 * - Move balls and do collision tests
	 * - Check game rules (game over etc.)
	 * - Request repaint
	 */
	public void updateGame() {
		moveBalls();	
		
		// Check if no ball on game grid
		if (getGrid().getBalls().isEmpty()) {
			gameOver();
		}
		
		// check if no more bricks left
		if (getGrid().getBricks().isEmpty()) {
			winGame();
		}
		
		notifyRepaintPlayGrid();
	}
	
	public void processMenuInput(MENU_ITEM indexOfMenuItem) {
		switch (indexOfMenuItem) {
		case MNU_NEW_GAME:
			setGrid(new PlayGrid(500, 500));
			getGrid().loadLevel(new File("test/sampleLevel1.txt"));
			this.start();
			break;
		case MNU_END:
			//TODO save level and gameprocess
			terminate();	
			break;
		case MNU_CONTINUE:
			start();
			break;
		case MNU_LEVEL_CHOOSE:
			//TODO
			break;
		}		
	}

	public void start() {
		// timer 
		resetTimer();
		timer.scheduleAtFixedRate(task, 0, 10);
		setState(GAME_STATE.RUNNING);
		
	}

	public void pause() {
		cancelTimer();
		
		setState(GAME_STATE.PAUSED);		
		notifyGameMenu(new MENU_ITEM[] {MENU_ITEM.MNU_NEW_GAME, MENU_ITEM.MNU_CONTINUE, MENU_ITEM.MNU_END},  
				TextMapping.getTextForIndex(TextMapping.TXT_GAME_PAUSED));
	}

	public void gameOver() {
		cancelTimer();
		
		setState(GAME_STATE.MENU_GAMEOVER);
		notifyGameMenu(new MENU_ITEM[]{MENU_ITEM.MNU_NEW_GAME, MENU_ITEM.MNU_END},
				TextMapping.getTextForIndex(TextMapping.TXT_YOU_LOSE));		
	}
	
	public void terminate() {
		cancelTimer();
		
		setState(GAME_STATE.KILLED);		
	}
	
	public void winGame() {
		pause();
		setState(GAME_STATE.MENU_WINGAME);
		notifyGameMenu(new MENU_ITEM[]{MENU_ITEM.MNU_NEW_GAME, MENU_ITEM.MNU_END}, 
				TextMapping.getTextForIndex(TextMapping.TXT_YOU_WIN));		
	}
	
	
	
	/**
	 * Process interactive user input (e.g. from key hits)
	 */
	public void processInput(PLAYER_INPUT input) {
		switch (input) {
		case START:
			start();
			break;
		case LEFT:
			moveSlider(-20);
			break;
		case RIGHT:
			moveSlider(+20);
			break;
		case PAUSE:
			pause();
			break;
		case CLOSE:
			gameOver();
			break;
		case KILL:
			terminate();
			break;
		}
	}

	/**
	 * Control slider movements since slider has no information about the grid.
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
	
	protected Timer resetTimer() {
		task = new GameTimerTask();		
		timer = new Timer("Game Timer");
		return timer;
	}

	protected void cancelTimer()  {
		if (timer != null) {
			timer.cancel();
		}
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
}
