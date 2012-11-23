package de.luma.breakout.controller;

import java.io.File;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.data.PlayGrid;
import de.luma.breakout.data.objects.Ball;
import de.luma.breakout.data.objects.SimpleBrick;
import de.luma.breakout.data.objects.Slider;
import de.luma.breakout.view.gui.MainWindow;
import de.luma.breakout.view.tui.UITextView;

public class TestGameController extends TestCase {

	private UITextView view;
	private MainWindow mainWindow;
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
		
		mainWindow = new MainWindow();
		mainWindow.setController(controller);
		controller.addObserver(mainWindow);
		mainWindow.setVisible(true);		
		
		controller.setState(GAME_STATE.RUNNING);
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
	public void testBallCollision() {
		System.out.println("testBallCollisions()\n");
				
		
		// border Collision					
		// right border
		Ball ball1 = new Ball(controller.getGrid().getWidth() -1, 20, 1, 0, 1);		
		controller.getGrid().addBall(ball1);	
		
		// left border
		Ball ball2 = new Ball(1, 20, -1, 0, 1);		
		controller.getGrid().addBall(ball2);
		
		// top border
		Ball ball3 = new Ball(20, 1, 0, -1, 1);
		controller.getGrid().addBall(ball3);		
		
		// bottom
		Ball ball4 = new Ball(20, controller.getGrid().getHeight(), 0, 1, 1);
		controller.getGrid().addBall(ball4);
		
		controller.updateGame();
		controller.updateGame();
		
		assertTrue(ball1.getSpeedX() == -1);
		
		assertTrue(ball2.getSpeedX() == 1);
		
		assertTrue(ball3.getSpeedY() == 1);
		
		assertFalse(controller.getGrid().getBalls().contains(ball4));
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
	public void testGameState() {
		// terminate
		SimpleBrick brick = new SimpleBrick(100, 100);
		controller.getGrid().addBrick(brick);
		Ball ball = new Ball(50, 50, 0, 1, 3);
		controller.getGrid().addBall(ball);	
		
		controller.getGrid().getBalls().remove(ball);
		
		controller.updateGame();
		controller.updateGame();
		
		assertTrue(controller.getState() == GAME_STATE.MENU_GAMEOVER);
		
		
		// winGame
		controller.getGrid().getBricks().remove(brick);
	
		controller.updateGame();
		controller.updateGame();
		
		assertTrue(controller.getState() == GAME_STATE.MENU_WINGAME);
	}
	
	@Test
	public void testUserInput() throws InterruptedException {
		controller.getGrid().addBall(new Ball(50, 50, 0, 1, 3));	
		controller.getGrid().addBrick(new SimpleBrick(100, 100));
//		controller.processInput(GameController.PLAYER_INPUT.START);
		
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
		
//		controller.processInput(GameController.PLAYER_INPUT.CANCEL_GAME);
//		assertTrue(controller.getState() == GAME_STATE.MENU_GAMEOVER);
//		
		// needed to fix EclEmma bug (no full coverage for enums)
		GAME_STATE.valueOf(GameController.GAME_STATE.values()[0].name());
		GameController.PLAYER_INPUT.valueOf(GameController.PLAYER_INPUT.values()[0].name());
	}
	
	@Test
	public void testLoadLevel() {
		playGrid.loadLevel(new File("test/sampleLevel1.txt"));
		
		assertFalse(playGrid.getBricks().isEmpty());
		
		// test invald game object						
		assertFalse(playGrid.loadLevel(new File("test/sampleLevelBug.txt")));	
		
		// test invalid file path
		assertFalse(playGrid.loadLevel(new File("test/notValidPath.txt")));		
	}
	
	@Test
	public void testSaveLevel() {
			testLoadLevel();
			assertTrue(controller.getGrid().saveLevel(new File ("test/sampleLevel1_out.txt")));
			
			PlayGrid grid2 = new PlayGrid(500, 500);
			grid2.loadLevel(new File("test/sampleLevel1_out.txt"));
			assertEquals(playGrid.getBalls().size(), grid2.getBalls().size());
			assertEquals(playGrid.getBricks().size(), grid2.getBricks().size());	
			
			// test save level FileNotFound
			assertFalse(controller.getGrid().saveLevel(new File ("NonExistingFolder/testLevelBug.txt")));
	}
	
	
	
	@Override
	public void tearDown() {
		System.out.println("tearDown()\n");
		
		controller.pause();
//		controller.terminate();		
		controller.removeObserver(view);
		controller.terminate();
		controller = null;	
	}

}
