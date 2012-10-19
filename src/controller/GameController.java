package controller;

import data.Ball;
import data.PlayGrid;
import data.bricks.AbstractBrick;
import data.bricks.SimpleBrick;


public class GameController {
	
	private PlayGrid grid;
	
	public GameController() {
		super();
		
		initialize();
	}
	

	private void initialize() {
		grid = new PlayGrid(600, 400);	
		
		grid.addBrick(new SimpleBrick(100, 100));
		
		grid.addBall(new Ball(10, 10, 5, 5, 3));
	}
	
	public void updateGame() {
		
		
		
	}
	

	protected void moveBalls() {
		for (Ball ball : grid.getBalls()) {
			for (AbstractBrick brick : grid.getBricks()) {
				
			}
		}
	}
}
