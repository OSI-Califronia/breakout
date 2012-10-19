package controller;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import view.UITextView;
import data.Ball;
import data.PlayGrid;
import data.bricks.SimpleBrick;

public class TestGameController extends TestCase {

	private UITextView view;
	private GameController controller;
	private PlayGrid playGrid;
	
	@Before
	public void setUp() throws Exception {
		// create data
		playGrid = new PlayGrid(400, 600);	
		
		// controller
		controller = new GameController();
		controller.setGrid(playGrid);
		
		// View
		view = new UITextView();
		view.setController(controller);		
		controller.addObserver(view);
		
	}

	@Test
	public void _testCollisionControl() {		
		
		
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
	
	
	@Test
	public void testGameControl() throws InterruptedException {	
		int gWidth = controller.getGrid().getWidth();
		int gheight = controller.getGrid().getHeight();
		
		
		// collision Ball Brick
		controller.getGrid().addBrick(new SimpleBrick(100, 100));
		controller.getGrid().addBall(new Ball(110, 95, 0, 1, 3));			
		
		controller.start();
		
		Thread.sleep(3000);
		
		System.out.printf("ball vs. left side\n");
		controller.getGrid().clearGrid();
		controller.getGrid().addBall(new Ball(5, 10, -1, 0, 3));	
		
		Thread.sleep(3000);
		
		System.out.printf("ball vs. right side\n");
		controller.getGrid().clearGrid();
		controller.getGrid().addBall(new Ball(gWidth-5, 10, 1, 0, 3));	
		
		Thread.sleep(3000);
		
		System.out.printf("ball vs. top side\n");
		controller.getGrid().clearGrid();
		controller.getGrid().addBall(new Ball(50, 5, 0, -1, 3));	
		
		Thread.sleep(3000);
		
		
		System.out.printf("ball vs. bottom side\n");
		controller.getGrid().clearGrid();
		controller.getGrid().addBall(new Ball(50, gheight-5, 0, 1, 3));	
		
		Thread.sleep(5000);
				
		assertTrue(controller.getGrid().getBalls().size() == 0); // game over
		controller.stop();
		
		controller.terminate();		
	}
	
	@Override
	public void tearDown() {
		
		controller.removeObserver(view);
	}

}
