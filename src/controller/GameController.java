package controller;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import communication.Observable;

import data.Ball;
import data.PlayGrid;
import data.bricks.AbstractBrick;


public class GameController extends Observable {
	
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
		
	public GameController() {
		super();		
		
		initialize();
	}
	

	public void initialize() {
		// init timer
		task = new GameTimerTask();
		timer = new Timer("Game_timer");		
	}
	
	public void start() {
		timer.scheduleAtFixedRate(task, 0, 50);
	}
	
	public void stop() {
		timer.cancel();
	}
	
	public void terminate() {
		timer.cancel();
		//TODO
	}
	
	public void updateGame() {
		moveBalls();	
		
		notifyRepaintPlayGrid();
		
		//TODO check gameover
	}
	

	protected void moveBalls() {
		Iterator<AbstractBrick> itbrick;
		Iterator<Ball> itball;
		AbstractBrick currentBrick; 
		Ball currentBall;
		
		itball = getGrid().getBalls().iterator();
		while (itball.hasNext()){ 
			currentBall = itball.next();
			
			// check for collisions and change direction
			itbrick = getGrid().getBricks().iterator();
			while (itbrick.hasNext()) {
				currentBrick = itbrick.next();
				if (currentBrick.tryCollision(currentBall))
					itbrick.remove();
			}
			
			if (getGrid().tryCollision(currentBall)) {
				itball.remove();
			}			
			
			// move Balls
			currentBall.setX(currentBall.getX() + currentBall.getSpeedX());
			currentBall.setY(currentBall.getY() + currentBall.getSpeedY());
		}
		
		
	}

	public PlayGrid getGrid() {
		return grid;
	}


	public void setGrid(PlayGrid grid) {
		this.grid = grid;
	}
}
