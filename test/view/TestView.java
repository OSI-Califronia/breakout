package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.junit.Before;
import org.junit.Test;

import communication.ObservableGame.GAME_STATE;

import view.gui.GameView2D;
import view.gui.MainWindow;

import controller.GameController;
import data.PlayGrid;
import data.objects.Ball;
import data.objects.SimpleBrick;
import data.objects.Slider;

import junit.framework.TestCase;

public class TestView extends TestCase {
	
	private UITextView viewText;
	private GameView2D view2D;
	private GameController controller;
	private PlayGrid playGrid;
	private MainWindow mainWindow;
	
	@Before
	public void setUp() throws Exception {
				
		// create data model
		playGrid = new PlayGrid(400, 600);	
		
		
		// create controller
		controller = new GameController();
		controller.setGrid(playGrid);
		
		// create view
		viewText = new UITextView();
		viewText.setController(controller);
		
		mainWindow = new MainWindow();
		mainWindow.setController(controller);
		view2D = new GameView2D(mainWindow);
		
		
		controller.addObserver(viewText);
		controller.addObserver(mainWindow);
	}
	
	
	@Test
	public void testMainWindow() {
		mainWindow.setVisible(true);
		
		controller.getGrid().addBrick(new SimpleBrick());
		controller.getGrid().addBall(new Ball(10, 10, 0, 0, 1));
		controller.getGrid().setSlider(new Slider(20, 50, 10, 10));
		
		controller.updateGame();
		
		// fire key pressed events		
		KeyEvent event = new KeyEvent(mainWindow, 0, 0L, 0, KeyEvent.VK_LEFT, ' ');
		for (KeyListener keyListener : mainWindow.getKeyListeners()) {
			keyListener.keyPressed(event);
		}		
		
		assertTrue(controller.getGrid().getSlider().getX() < 20);
		
		
		controller.getGrid().getSlider().setX(20);
		
		event = new KeyEvent(mainWindow, 0, 0L, 0, KeyEvent.VK_RIGHT, ' ');
		for (KeyListener keyListener : mainWindow.getKeyListeners()) {
			keyListener.keyPressed(event);
		}		
		
		assertTrue(controller.getGrid().getSlider().getX() > 20);
		
		
		// click buttons
		mainWindow.getBtnStart().doClick();
		
		assertTrue(controller.getState() == GAME_STATE.RUNNING);
		
		mainWindow.getBtnStop().doClick();
		
		assertTrue(controller.getState() == GAME_STATE.MENU_GAMEOVER);
		
		
		mainWindow.dispose();
	}

}
