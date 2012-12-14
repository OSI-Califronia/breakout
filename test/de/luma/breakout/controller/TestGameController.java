package de.luma.breakout.controller;

import java.io.File;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.ObservableGame.MENU_ITEM;
import de.luma.breakout.controller.IGameController.PLAYER_INPUT;
import de.luma.breakout.data.objects.Ball;
import de.luma.breakout.data.objects.SimpleBrick;
import de.luma.breakout.data.objects.Slider;
import de.luma.breakout.view.tui.UITextView;

public class TestGameController extends TestCase {

	private UITextView view;
//	private MainWindow mainWindow;
	private GameController controller;
//	private PlayGrid playGrid;
	
	@Before
	public void setUp() throws Exception {
		System.out.println("setUp()\n");
		
		// create controller
		controller = new GameController();
		controller.clearGrid();
		controller.setGridSize(400, 600);

		
		// create view
		view = new UITextView();
		view.setController(controller);		
		controller.addObserver(view);
		
//		mainWindow = new MainWindow(controller);
//		controller.addObserver(mainWindow.getBpaGameView2D());
//		mainWindow.setVisible(true);		
	}
	
	
	@Test
	public void testBrickCollisions() {		
		
		System.out.println("testBrickCollisions()\n");
		
		// ############################    COLLISION TESTING ############################
		
				
		// head ----------------------------------------------------------------------------------
		// this brick will not be removed to prevent game from changing to game_won state
		controller.addBrick(new SimpleBrick(0, 0));
		controller.addBrick(new SimpleBrick(100, 100));
		controller.addBall(new Ball(110, 97, 0, 1, 3));	
		
		assertTrue(controller.getBricks().size() == 2); // two bricks in game
		assertTrue(controller.getBalls().size() == 1); // one ball in game
				
		controller.updateGame();
		
		assertTrue(controller.getBricks().size() == 1); // only one brick left
		assertTrue(controller.getBalls().get(0).getSpeedY() == -1); // speedY -1
		
		
		
		// bottom ----------------------------------------------------------------------------------
		controller.clearGrid();
		// this brick will not be removed to prevent game from changing to game_won state
	    controller.addBrick(new SimpleBrick(0, 0));
				
		
		controller.addBrick(new SimpleBrick(100, 100));
		controller.addBall(new Ball(110, 123, 0, -1, 3));
				
		controller.updateGame();		
		
		assertTrue(controller.getBricks().size() == 1); // only one brick left
		assertTrue(controller.getBalls().get(0).getSpeedY() == 1); // speedY 1
		
		
		
		// left ----------------------------------------------------------------------------------
		controller.clearGrid();
		// this brick will not be removed to prevent game from changing to game_won state
	    controller.addBrick(new SimpleBrick(0, 0));
	    
		controller.addBrick(new SimpleBrick(100, 100));
		controller.addBall(new Ball(97, 110, 1, 0, 3));
				
		controller.updateGame();
		
		assertTrue(controller.getBricks().size() == 1); // only one brick left
		assertTrue(controller.getBalls().get(0).getSpeedX() == -1); // speedY 1
				
		
		// right ----------------------------------------------------------------------------------
		controller.clearGrid();
		// this brick will not be removed to prevent game from changing to game_won state
	    controller.addBrick(new SimpleBrick(0, 0));
	    
		controller.addBrick(new SimpleBrick(100, 100));
		controller.addBall(new Ball(153, 110, -1, 0, 3));		
			
		controller.updateGame();
		
		assertTrue(controller.getBricks().size() == 1); // only one brick left
		assertTrue(controller.getBalls().get(0).getSpeedX() == 1); // speedY 1
		
		
		// test slider collision ------------------------------------------------------------------
		
		controller.clearGrid();
		controller.addBall(new Ball(55, 50, 0, 1, 1));
		System.out.println(controller.getBalls().size());
		controller.setSlider(new Slider(50, 52, 10, 10));
		controller.updateGame();
		controller.updateGame();
		controller.updateGame();
		controller.updateGame();
		controller.updateGame();
		controller.updateGame();
		
		System.out.println(controller.getBalls().size());
		assertTrue(controller.getBalls().get(0).getSpeedY() < 0); // collision detected
	
		
	}
	
	
	@Test
	public void testBallCollision() {
		System.out.println("testBallCollisions()\n");
				
		
		// border Collision					
		// right border
		Ball ball1 = new Ball(controller.getGridSize().width -1, 20, 1, 0, 1);		
		controller.addBall(ball1);	
		
		// left border
		Ball ball2 = new Ball(1, 20, -1, 0, 1);		
		controller.addBall(ball2);
		
		// top border
		Ball ball3 = new Ball(20, 1, 0, -1, 1);
		controller.addBall(ball3);		
		
		// bottom
		Ball ball4 = new Ball(20, controller.getGridSize().height, 0, 1, 1);
		controller.addBall(ball4);
		
		controller.updateGame();
		controller.updateGame();
		
		assertTrue(ball1.getSpeedX() == -1);
		
		assertTrue(ball2.getSpeedX() == 1);
		
		assertTrue(ball3.getSpeedY() == 1);
		
		assertFalse(controller.getBalls().contains(ball4));
	}
	
	
	@Test
	public void testGridCollisions() throws InterruptedException {	
		
		System.out.println("testGridCollisions()\n");
		
		controller.clearGrid();
		int gWidth = controller.getGridSize().width;
		int gheight = controller.getGridSize().height;
		
				
		System.out.printf("ball vs. left side\n");
		
		controller.addBall(new Ball(5, 10, -1, 0, 3));	
		
		controller.processMenuInput(MENU_ITEM.MNU_NEW_GAME);
		Thread.sleep(500);
		
		System.out.printf("ball vs. right side\n");
		controller.clearGrid();
		controller.addBall(new Ball(gWidth-5, 10, 1, 0, 3));	
		
		Thread.sleep(500);
		
		System.out.printf("ball vs. top side\n");
		controller.clearGrid();
		controller.addBall(new Ball(50, 5, 0, -1, 3));	
		
		
		Thread.sleep(500);
		
		// prepare game over
		
		System.out.printf("ball vs. bottom side\n");
		controller.clearGrid();
		controller.addBall(new Ball(50, gheight-5, 0, 1, 3));	
		
		Thread.sleep(500);
				
//		assertTrue(controller.getBalls().size() == 0); // game over
		
	}
	
	@Test 
	public void testGameState() {
		// terminate
		SimpleBrick brick = new SimpleBrick(100, 100);
		controller.addBrick(brick);
		Ball ball = new Ball(50, 50, 0, 1, 3);
		controller.addBall(ball);	
		
		controller.getBalls().remove(ball);
		
		controller.updateGame();
		controller.updateGame();
		
		assertTrue(controller.getState() == GAME_STATE.MENU_GAMEOVER);
		
		
		// winGame
		controller.getBricks().remove(brick);
	
		controller.updateGame();
		controller.updateGame();
		
		assertTrue(controller.getState() == GAME_STATE.MENU_WINGAME);
		
	
	}
	
	@Test
	public void testUserInput() throws InterruptedException {
		controller.addBall(new Ball(50, 50, 0, 1, 3));	
		controller.addBrick(new SimpleBrick(100, 100));
		controller.setSlider(new Slider(0, 0, 1, 1));
		
		// test slider movements
		controller.getSlider().setX(50);
		controller.processGameInput(GameController.PLAYER_INPUT.LEFT);
		assertTrue(controller.getSlider().getX() < 50);
		controller.processGameInput(GameController.PLAYER_INPUT.RIGHT);
		controller.processGameInput(GameController.PLAYER_INPUT.RIGHT);
		assertTrue(controller.getSlider().getX() > 50);
		
		// test invalid slider movements
		controller.getSlider().setX(0);
		controller.processGameInput(GameController.PLAYER_INPUT.LEFT);
		assertTrue(controller.getSlider().getX() == 0);
		
		int max_x = controller.getGridSize().width - controller.getSlider().getWidth();
		controller.getSlider().setX(max_x);
		controller.processGameInput(GameController.PLAYER_INPUT.RIGHT);
		assertTrue(controller.getSlider().getX() <= max_x);
		
		// test menu input
		controller.processMenuInput(MENU_ITEM.MNU_NEW_GAME);
		assertTrue(controller.getState() == GAME_STATE.RUNNING);
		
		controller.processGameInput(GameController.PLAYER_INPUT.PAUSE);
		assertTrue(controller.getState() == GAME_STATE.PAUSED);
		
		controller.processMenuInput(MENU_ITEM.MNU_CONTINUE);
		assertTrue(controller.getState() == GAME_STATE.RUNNING);
		
		
		
		
//		controller.processInput(GameController.PLAYER_INPUT.CANCEL_GAME);
//		assertTrue(controller.getState() == GAME_STATE.MENU_GAMEOVER);
//		
		// needed to fix EclEmma bug (no full coverage for enums)
		GAME_STATE.valueOf(GameController.GAME_STATE.values()[0].name());
		GameController.PLAYER_INPUT.valueOf(GameController.PLAYER_INPUT.values()[0].name());
	}
	
	@Test
	public void testLoadLevel() {
		assertTrue(controller.loadLevel(new File("levels/sampleLevel1.lvl")));
		
		assertFalse(controller.getBricks().isEmpty());
		
		// test invalid game object						
		assertFalse(controller.loadLevel(new File("levels/sampleLevelBug.lvl")));	
		
		// test invalid file path
		assertFalse(controller.loadLevel(new File("levels/notValidPath.lvl")));		
	}
	
	@Test
	public void testSaveLevel() {
			testLoadLevel();
			int oldBrickCount = controller.getBricks().size();
			int oldBallCount = controller.getBalls().size();
			
			assertTrue(controller.saveLevel(new File ("levels/sampleLevel1_out.lvl")));
			
			controller.loadLevel(new File("test/sampleLevel1_out.lvl"));
			assertEquals(controller.getBalls().size(), oldBallCount);
			assertEquals(controller.getBricks().size(), oldBrickCount);	
			
			// test save level FileNotFound
			assertFalse(controller.saveLevel(new File ("NonExistingFolder/testLevelBug.lvl")));
			
			// automatic save
			String filepath = controller.saveLevel();
			File file = new File(filepath);
			assertTrue(file.exists());
			file.delete();
	}
	
	
	
	@Override
	public void tearDown() {
		System.out.println("tearDown()\n");
		
		controller.processGameInput(PLAYER_INPUT.PAUSE);
//		controller.terminate();		
		controller.removeObserver(view);
		controller.processMenuInput(MENU_ITEM.MNU_END);
		controller = null;	
	}

}
