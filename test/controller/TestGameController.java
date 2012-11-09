package controller;

import java.io.File;

import junit.framework.TestCase;


import org.junit.Before;
import org.junit.Test;

import communication.ObservableGame.GAME_STATE;

import view.UITextView;
import data.PlayGrid;
import data.objects.Ball;
import data.objects.SimpleBrick;
import data.objects.Slider;

public class TestGameController extends TestCase {

	private UITextView view;
	private GameController controller;
	private PlayGrid playGrid;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("setUp()\n");
		
		// create data model
		playGrid = new PlayGrid(400, 600);	
		resetGrid();
		
		// create controller
		controller = new GameController();
		controller.setGrid(playGrid);
		
		// create view
		view = new UITextView();
		view.setController(controller);		
		controller.addObserver(view);
		
	}
	
	private void resetGrid() {
		playGrid.clearGrid();
		playGrid.setSlider(new Slider(playGrid.getWidth() / 2 + 50, playGrid.getHeight() - 25, 100, 20));
	}

	@Test
	public void testBrickCollisions() {		
		
		System.out.println("testBrickCollisions()\n");
		
		// ############################    COLLISION TESTING ############################
		
		
		// head ----------------------------------------------------------------------------------
		controller.getGrid().addBrick(new SimpleBrick(100, 100));
		controller.getGrid().addBall(new Ball(110, 97, 0, 1, 3));	
		
		assertTrue(controller.getGrid().getBricks().size() == 1); // one brick in game
		assertTrue(controller.getGrid().getBalls().size() == 1); // one ball in game
				
		controller.updateGame();
		
		assertTrue(controller.getGrid().getBricks().size() == 0); // no bricks left
		assertTrue(controller.getGrid().getBalls().get(0).getSpeedY() == -1); // speedY -1
		
		
		
		// bottom ----------------------------------------------------------------------------------
		resetGrid();
		controller.getGrid().addBrick(new SimpleBrick(100, 100));
		controller.getGrid().addBall(new Ball(110, 123, 0, -1, 3));
				
		controller.updateGame();		
		
		assertTrue(controller.getGrid().getBricks().size() == 0); // no bricks left
		assertTrue(controller.getGrid().getBalls().get(0).getSpeedY() == 1); // speedY 1
		
		
		
		// left ----------------------------------------------------------------------------------
		resetGrid();
		controller.getGrid().addBrick(new SimpleBrick(100, 100));
		controller.getGrid().addBall(new Ball(97, 110, 1, 0, 3));
				
		controller.updateGame();
		
		assertTrue(controller.getGrid().getBricks().size() == 0); // no bricks left
		assertTrue(controller.getGrid().getBalls().get(0).getSpeedX() == -1); // speedY 1
				
		
		// right ----------------------------------------------------------------------------------
		resetGrid();
		controller.getGrid().addBrick(new SimpleBrick(100, 100));
		controller.getGrid().addBall(new Ball(153, 110, -1, 0, 3));		
			
		controller.updateGame();
		
		assertTrue(controller.getGrid().getBricks().size() == 0); // no bricks left
		assertTrue(controller.getGrid().getBalls().get(0).getSpeedX() == 1); // speedY 1
		
		
		// test slider collision ------------------------------------------------------------------
		
		resetGrid(); 
		controller.getGrid().addBall(new Ball(55, 50, 0, 1, 1));
		System.out.println(controller.getGrid().getBalls().size());
		controller.getGrid().setSlider(new Slider(50, 52, 10, 10));
		controller.updateGame();
		controller.updateGame();
		controller.updateGame();
		controller.updateGame();
		controller.updateGame();
		controller.updateGame();
		
		System.out.println(controller.getGrid().getBalls().size());
		assertTrue(controller.getGrid().getBalls().get(0).getSpeedY() < 0); // collision detected

		
	}
	
	
	@Test
	public void testGridCollisions() throws InterruptedException {	
		
		System.out.println("testGridCollisions()\n");
		
		int gWidth = controller.getGrid().getWidth();
		int gheight = controller.getGrid().getHeight();
		
				
		System.out.printf("ball vs. left side\n");
		resetGrid();
		controller.getGrid().addBall(new Ball(5, 10, -1, 0, 3));	
		
		controller.start();
		Thread.sleep(500);
		
		System.out.printf("ball vs. right side\n");
		resetGrid();
		controller.getGrid().addBall(new Ball(gWidth-5, 10, 1, 0, 3));	
		
		Thread.sleep(500);
		
		System.out.printf("ball vs. top side\n");
		resetGrid();
		controller.getGrid().addBall(new Ball(50, 5, 0, -1, 3));	
		
		
		Thread.sleep(500);
		
		// prepare game over
		
		System.out.printf("ball vs. bottom side\n");
		resetGrid();
		controller.getGrid().addBall(new Ball(50, gheight-5, 0, 1, 3));	
		
		Thread.sleep(500);
				
//		assertTrue(controller.getGrid().getBalls().size() == 0); // game over
		
	}
	
	
	@Test
	public void testUserInput() throws InterruptedException {
		controller.getGrid().addBall(new Ball(50, 50, 0, 1, 3));	
		controller.getGrid().addBrick(new SimpleBrick(100, 100));
		controller.processInput(GameController.PLAYER_INPUT.START);
		
		// test slider movements
		controller.getGrid().getSlider().setX(50);
		controller.processInput(GameController.PLAYER_INPUT.LEFT);
		assertTrue(controller.getGrid().getSlider().getX() < 50);
		controller.processInput(GameController.PLAYER_INPUT.RIGHT);
		controller.processInput(GameController.PLAYER_INPUT.RIGHT);
		assertTrue(controller.getGrid().getSlider().getX() > 50);
		
		// test invalid user input
		controller.getGrid().getSlider().setX(0);
		controller.processInput(GameController.PLAYER_INPUT.LEFT);
		assertTrue(controller.getGrid().getSlider().getX() == 0);
		
		int max_x = controller.getGrid().getWidth() - controller.getGrid().getSlider().getWidth();
		controller.getGrid().getSlider().setX(max_x);
		controller.processInput(GameController.PLAYER_INPUT.RIGHT);
		assertTrue(controller.getGrid().getSlider().getX() <= max_x);
		
		
		controller.processInput(GameController.PLAYER_INPUT.PAUSE);
		assertTrue(controller.getState() == GAME_STATE.PAUSED);
		
		controller.processInput(GameController.PLAYER_INPUT.CLOSE);
		assertTrue(controller.getState() == GAME_STATE.GAMEOVER);
		
		// needed to fix EclEmma bug (no full coverage for enums)
		GAME_STATE.valueOf(GameController.GAME_STATE.values()[0].name());
		GameController.PLAYER_INPUT.valueOf(GameController.PLAYER_INPUT.values()[0].name());
	}
	
	@Test
	public void testLoadLevel() {
		playGrid.loadLevel(new File("test/sampleLevel1.txt"));
		
		assertFalse(playGrid.getBricks().isEmpty());
	}
	
	
	@Override
	public void tearDown() {
		System.out.println("tearDown()\n");
		
		controller.stop();
//		controller.terminate();		
		controller.removeObserver(view);
		controller = null;
	}

}
