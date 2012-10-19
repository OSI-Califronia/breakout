package controller;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import view.AbstractView;
import data.Ball;
import data.PlayGrid;
import data.bricks.AbstractBrick;


public class GameController {
	
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
		timer.schedule(task, 10000);
	}
	
	public void pause() {
		timer.cancel();
	}
	
	
	public void updateGame() {
		moveBalls();	
		
//		view.repaintPlayGrid();
	}
	

	protected void moveBalls() {
		Iterator<AbstractBrick> iterator;
		AbstractBrick currentBrick; 
		
		for (Ball ball : grid.getBalls()) { 
			
			// check for collisions and change direction
			iterator = grid.getBricks().iterator();
			while (iterator.hasNext()) {
				currentBrick = iterator.next();
				if (currentBrick.tryCollision(ball))
					iterator.remove();
			}
			
			// move Balls
			ball.setX(ball.getX() + ball.getSpeedX());
			ball.setY(ball.getY() + ball.getSpeedY());
		}
		
		
	}

//	public AbstractView getView() {
//		return view;
//	}
//
//	public void setView(AbstractView view) {
//		if (view.getController() == null) {
//			view.setController(this);
//		}
//		this.view = view;
//	}


	public PlayGrid getGrid() {
		return grid;
	}


	public void setGrid(PlayGrid grid) {
		this.grid = grid;
	}
}
