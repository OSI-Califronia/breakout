package de.luma.breakout.controller;

import org.junit.Before;
import org.junit.Test;

import de.luma.breakout.communication.ObservableGame.MENU_ITEM;
import de.luma.breakout.view.tui.UITextView;
import junit.framework.TestCase;

public class TestUITextView extends TestCase {

	private GameController controller;


	@Before
	public void setUp() throws Exception {
		System.out.println("setUp()\n");

		// create controller
		controller = new GameController();
		controller.clearGrid();



		controller.initialize();
	}


	@Test
	public void testConsoleInput() throws InterruptedException {
		// create view
		UITextView view;
		view = new UITextView("0\na\nd\np\n3\np\n4\n5\n");
		view.setController(controller);		
		controller.addObserver(view);
		
		Thread.sleep(2000); // wait 2 sek to let console work
	}

	@Override
	public void tearDown() {
		System.out.println("tearDown()\n");

		controller.processMenuInput(MENU_ITEM.MNU_END);
		controller = null;	
	}

}
