package de.luma.breakout.launcher;


import org.junit.Test;
import junit.framework.TestCase;
import de.luma.breakout.controller.GameController;
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
		
		// create controller
		
		GameController controller = new GameController();		
		
		// TUI
		UITextView view = new UITextView();
		view.setController(controller);
		controller.addObserver(view);		
		
		// GUI
		MainWindow mainWindow = new MainWindow();
		mainWindow.setController(controller);
		controller.addObserver(mainWindow);
		mainWindow.setVisible(true);
		
		controller.initialize();
	}
	
}
