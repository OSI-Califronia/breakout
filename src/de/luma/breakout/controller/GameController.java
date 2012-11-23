package de.luma.breakout.controller;

import java.io.File;

import java.util.Iterator;
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
 * - Nur reagieren wenn Spiel läuft (ProcessInput)
 * - maximale Ballgeschwindigkeit einführen
 * - level laden
 * - neustart ermöglichen
 * - timer thread richtig beenden
 */


public class GameController extends ObservableGame {	


	public enum PLAYER_INPUT {
		LEFT,
		RIGHT,
		PAUSE
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
	
	public static final double MAX_BALL_SPEED = 10.0;

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
		
		notifyNextGameFrame();		
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

	/**
	 * Set game state to running and start a timer which
	 * calls updateGame() continuosly.
	 */
	public void start() {
		// timer 
		resetTimer();
		timer.scheduleAtFixedRate(task, 0, 10);
		setState(GAME_STATE.RUNNING);

	}

	/**
	 * Stop the game timer and display the pause menu.
	 */
	public void pause() {
		cancelTimer();

		setState(GAME_STATE.PAUSED);		
		notifyGameMenu(new MENU_ITEM[] {MENU_ITEM.MNU_NEW_GAME, MENU_ITEM.MNU_CONTINUE, MENU_ITEM.MNU_END},  
				TextMapping.getTextForIndex(TextMapping.TXT_GAME_PAUSED));
	}

	/**
	 * Stop the game timer and display the game over menu.
	 */
	public void gameOver() {
		cancelTimer();

		setState(GAME_STATE.MENU_GAMEOVER);
		notifyGameMenu(new MENU_ITEM[]{MENU_ITEM.MNU_NEW_GAME, MENU_ITEM.MNU_END},
				TextMapping.getTextForIndex(TextMapping.TXT_YOU_LOSE));		
	}

	/**
	 * Stop the game timer and set game state to killed.
	 */
	public void terminate() {
		cancelTimer();

		setState(GAME_STATE.KILLED);		
	}

	/**
	 * Stop the game timer display the game won menu.
	 */
	public void winGame() {
		cancelTimer();
		setState(GAME_STATE.MENU_WINGAME);
		notifyGameMenu(new MENU_ITEM[]{MENU_ITEM.MNU_NEW_GAME, MENU_ITEM.MNU_END}, 
				TextMapping.getTextForIndex(TextMapping.TXT_YOU_WIN));		
	}



	/**
	 * Process interactive user input (e.g. from key hits)
	 */
	public void processInput(PLAYER_INPUT input) {
		switch (input) {
		case LEFT:
			moveSlider(-5);
			break;
		case RIGHT:
			moveSlider(+5);
			break;
		case PAUSE:
			pause();
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
