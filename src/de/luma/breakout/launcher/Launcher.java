package de.luma.breakout.launcher;


import com.google.inject.Guice;
import com.google.inject.Injector;

import de.luma.breakout.controller.IGameController;
import de.luma.breakout.view.gui.MainWindow;
import de.luma.breakout.view.tui.UITextView;

// Kein Test vorgesehen da prinzipiell unbegrenzte Laufzeit
public class Launcher {
	
	public Launcher() {
		super();
	}
	
	/**
	 * Game launcher.
	 */
	public static void main(String[] args) {
		
		// create controller by dependency injection
		Injector injector = Guice.createInjector(new DefaultModule());
		IGameController controller = injector.getInstance(IGameController.class);	
		
		// TUI
		UITextView view = new UITextView();
		view.setController(controller);
		controller.addObserver(view);		
		
		// GUI
		MainWindow mainWindow = new MainWindow(controller);		
		controller.addObserver(mainWindow.getBpaGameView2D());
		mainWindow.setVisible(true);
		
		controller.initialize();
	}
	
}
