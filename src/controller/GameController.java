package controller;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;


import communication.ObservableGame;

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

		initialize();
	}

	public void initialize() {		
		setState(GAME_STATE.PAUSED);
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
			terminate();
		}
		
		// check if no more bricks left
		if (getGrid().getBricks().isEmpty()) {
			winGame();
		}
		
		notifyRepaintPlayGrid();
	}


	public void start() {
		// timer 
		resetTimer();
		timer.scheduleAtFixedRate(task, 0, 10);
		setState(GAME_STATE.RUNNING);
		notifyGameStateChanged(state);
	}

	public void stop() {
		if (timer != null) {
			timer.cancel();
		}
		
		setState(GAME_STATE.PAUSED);
		notifyGameStateChanged(state);
	}

	public void terminate() {
		stop();
		setState(GAME_STATE.GAMEOVER);
		notifyGameStateChanged(state);
	}
	
	public void winGame() {
		stop();
		setState(GAME_STATE.WINGAME);
		notifyGameStateChanged(state);
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
			stop();
			break;
		case CLOSE:
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
	}
}
