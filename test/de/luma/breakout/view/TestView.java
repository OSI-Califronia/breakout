package de.luma.breakout.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.luma.breakout.controller.GameController;
import de.luma.breakout.data.objects.Ball;
import de.luma.breakout.data.objects.SimpleBrick;
import de.luma.breakout.data.objects.Slider;
import de.luma.breakout.view.gui.MainWindow;
import de.luma.breakout.view.tui.UITextView;

public class TestView extends TestCase {
	
	private UITextView viewText;
	private GameController controller;	
	private MainWindow mainWindow;
	
	@Before
	public void setUp() throws Exception {
		
		// create controller
		controller = new GameController();
		controller.clearGrid();
		controller.setGridSize(400, 600);		
		
		// create view
		viewText = new UITextView();
		viewText.setController(controller);
		
		mainWindow = new MainWindow(controller);	
		
		controller.addObserver(mainWindow.getBpaGameView2D());
		controller.addObserver(viewText);
		
	}
	
	
	@Test
	public void testMainWindow() {
		mainWindow.setVisible(true);
		
		controller.addBrick(new SimpleBrick());
		controller.addBall(new Ball(10, 10, 0, 0, 1));
		controller.setSlider(new Slider(20, 50, 10, 10));
		
		controller.updateGame();
		
		// fire key pressed events		
		KeyEvent event = new KeyEvent(mainWindow, 0, 0L, 0, KeyEvent.VK_LEFT, ' ');
		for (KeyListener keyListener : mainWindow.getKeyListeners()) {
			keyListener.keyPressed(event);
		}		
		
		controller.updateGame();
		
		assertTrue(controller.getSlider().getX() < 20);
		
		for (KeyListener keyListener : mainWindow.getKeyListeners()) {
			keyListener.keyReleased(event);
		}
		
		controller.getSlider().setX(20);
		
		event = new KeyEvent(mainWindow, 0, 0L, 0, KeyEvent.VK_RIGHT, ' ');
		for (KeyListener keyListener : mainWindow.getKeyListeners()) {
			keyListener.keyPressed(event);
		}		
		
		controller.updateGame();
		
		assertTrue(controller.getSlider().getX() > 20);
		
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
