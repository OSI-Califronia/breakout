package de.luma.breakout.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.luma.breakout.controller.GameController;
import de.luma.breakout.data.PlayGrid;
import de.luma.breakout.data.objects.Ball;
import de.luma.breakout.data.objects.SimpleBrick;
import de.luma.breakout.data.objects.Slider;
import de.luma.breakout.view.gui.MainWindow;
import de.luma.breakout.view.tui.UITextView;

public class TestView extends TestCase {
	
	private UITextView viewText;
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
//		
//		mainWindow = new MainWindow();
//		mainWindow.setController(controller);
//		
//		
//		controller.addObserver(viewText);
//		controller.addObserver(mainWindow);
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
		
		controller.updateGame();
		
		assertTrue(controller.getGrid().getSlider().getX() < 20);
		
		for (KeyListener keyListener : mainWindow.getKeyListeners()) {
			keyListener.keyReleased(event);
		}
		
		controller.getGrid().getSlider().setX(20);
		
		event = new KeyEvent(mainWindow, 0, 0L, 0, KeyEvent.VK_RIGHT, ' ');
		for (KeyListener keyListener : mainWindow.getKeyListeners()) {
			keyListener.keyPressed(event);
		}		
		
		controller.updateGame();
		
		assertTrue(controller.getGrid().getSlider().getX() > 20);
		
		for (KeyListener keyListener : mainWindow.getKeyListeners()) {
			keyListener.keyReleased(event);
		}
		
		
		// click buttons
//		mainWindow.getBtnStart().doClick();
		
//		assertTrue(controller.getState() == GAME_STATE.RUNNING);
		
//		mainWindow.getBtnStop().doClick();
		
//		assertTrue(controller.getState() == GAME_STATE.MENU_GAMEOVER);
		
		
		mainWindow.dispose();
	}

}