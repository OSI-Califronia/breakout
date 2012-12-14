package de.luma.breakout.controller;

import org.junit.Before;
import org.junit.Test;

import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.ObservableGame.MENU_ITEM;
import de.luma.breakout.controller.IGameController.PLAYER_INPUT;
import de.luma.breakout.view.tui.UITextView;
import junit.framework.TestCase;

public class TestExtras extends TestCase {

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
	public void testLevelEditing() {
		controller.processMenuInput(MENU_ITEM.MNU_LEVEL_EDITOR);
		assertTrue(controller.getCreativeMode());
		
		// controller knows at least one type of brick
		assertFalse(controller.getBrickClasses().isEmpty());
		
		
	}
	
	
	@Test
	public void testMenuInputs() {
		
		assertTrue(controller.getState() == GAME_STATE.MENU_MAIN);
		
		controller.processMenuInput(MENU_ITEM.MNU_NEW_GAME);
		assertTrue(controller.getState() == GAME_STATE.RUNNING);
		
		controller.processGameInput(PLAYER_INPUT.PAUSE);
		assertTrue(controller.getState() == GAME_STATE.PAUSED);
		
		controller.processMenuInput(MENU_ITEM.MNU_CONTINUE);
		assertTrue(controller.getState() == GAME_STATE.RUNNING);
		
		controller.processGameInput(PLAYER_INPUT.PAUSE);
		controller.processMenuInput(MENU_ITEM.MNU_BACK_MAIN_MENU);
		assertTrue(controller.getState() == GAME_STATE.MENU_MAIN);
		
		controller.processMenuInput(MENU_ITEM.MNU_END);
		assertTrue(controller.getState() == GAME_STATE.KILLED);
	}
	
	
	@Override
	public void tearDown() {
		System.out.println("tearDown()\n");
		
		controller.processMenuInput(MENU_ITEM.MNU_END);
		controller = null;	
	}
	
}
