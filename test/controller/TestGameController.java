package controller;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import data.Ball;
import data.PlayGrid;
import data.bricks.SimpleBrick;

import view.UITextView;

public class TestGameController extends TestCase {

	private UITextView view;
	private GameController controller;
	private PlayGrid playGrid;
	
	@Before
	public void setUp() throws Exception {
		controller = new GameController();
		view = new UITextView();
		view.setController(controller);
		playGrid = new PlayGrid(600, 400);	
		controller.setGrid(playGrid);
	}

	@Test
	public void testCollisionControl() {		
		
		
		// ############################    COLLISION TESTING ############################
		
		
		// head ----------------------------------------------------------------------------------
		controller.getGrid().addBrick(new SimpleBrick(100, 100));
		controller.getGrid().addBall(new Ball(110, 97, 0, 1, 3));	
		
		assertTrue(controller.getGrid().getBricks().size() == 1); // one bricks in Game
		assertTrue(controller.getGrid().getBalls().size() == 1); // one Ball in Game
				
		controller.updateGame();
		
		assertTrue(controller.getGrid().getBricks().size() == 0); // no bricks left
		assertTrue(controller.getGrid().getBalls().get(0).getSpeedY() == -1); // speedY -1
		
		
		
		// bottom ----------------------------------------------------------------------------------
		controller.getGrid().clearGrid();
		controller.getGrid().addBrick(new SimpleBrick(100, 100));
		controller.getGrid().addBall(new Ball(110, 123, 0, -1, 3));
				
		controller.updateGame();		
		
		assertTrue(controller.getGrid().getBricks().size() == 0); // no bricks left
		assertTrue(controller.getGrid().getBalls().get(0).getSpeedY() == 1); // speedY 1
		
		
		
		// left ----------------------------------------------------------------------------------
		controller.getGrid().clearGrid();
		controller.getGrid().addBrick(new SimpleBrick(100, 100));
		controller.getGrid().addBall(new Ball(97, 110, 1, 0, 3));
				
		controller.updateGame();
		
		assertTrue(controller.getGrid().getBricks().size() == 0); // no bricks left
		assertTrue(controller.getGrid().getBalls().get(0).getSpeedX() == -1); // speedY 1
				
		
		// right ----------------------------------------------------------------------------------
		controller.getGrid().clearGrid();
		controller.getGrid().addBrick(new SimpleBrick(100, 100));
		controller.getGrid().addBall(new Ball(153, 110, -1, 0, 3));		
			
		controller.updateGame();
		
		assertTrue(controller.getGrid().getBricks().size() == 0); // no bricks left
		assertTrue(controller.getGrid().getBalls().get(0).getSpeedX() == 1); // speedY 1
	}

}
