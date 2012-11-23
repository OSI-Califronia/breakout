package de.luma.breakout.launcher;


import java.io.File;

import org.junit.Test;

import junit.framework.TestCase;
import de.luma.breakout.controller.GameController;
import de.luma.breakout.data.PlayGrid;
import de.luma.breakout.data.objects.Ball;
import de.luma.breakout.data.objects.SimpleBrick;
import de.luma.breakout.data.objects.Slider;
import de.luma.breakout.view.gui.MainWindow;
import de.luma.breakout.view.tui.UITextView;

// Kein Test vorgesehen da prinzipiell unbegrenzte Laufzeit
public class Launcher extends TestCase {
	
	public Launcher() {
		super();
	}
	
	@Test
	public void testLauncher() {
		main(null);
		assertEquals(true, true);
	}
	
	/**
	 * Game launcher.
	 */
	public static void main(String[] args) {
		// create data model
//		PlayGrid grid = new PlayGrid(500, 500);
//		grid.loadLevel(new File("test/sampleLevel1.txt"));
//		grid.addBrick(new SimpleBrick(50, 50));
//		grid.addBrick(new SimpleBrick(100, 50));
//		grid.addBrick(new SimpleBrick(150, 50));
//		grid.addBall(new Ball(60, 80, 0, -1, 5));
//		grid.addBall(new Ball(80, 80, 2, -2, 5));
//		grid.addBall(new Ball(100, 80, 3, 1.5, 5));
//		grid.setSlider(new Slider(200, 450, 100, 30));	
		
		// create controller
		
		GameController controller = new GameController();		
//		controller.setGrid(grid);
		
		// TUI
		UITextView view = new UITextView();
		view.setController(controller);
		controller.addObserver(view);		
		
//		// GUI
		MainWindow mainWindow = new MainWindow();
		mainWindow.setController(controller);
		controller.addObserver(mainWindow);
		mainWindow.setVisible(true);
		
		controller.initialize();
	}
	
}
