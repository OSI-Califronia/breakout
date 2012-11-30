package de.luma.breakout.view.tui;

import java.util.Locale;
import java.util.Scanner;


import de.luma.breakout.communication.IGameObserver;
import de.luma.breakout.communication.TextMapping;
import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.ObservableGame.MENU_ITEM;
import de.luma.breakout.controller.GameController;
import de.luma.breakout.controller.GameController.PLAYER_INPUT;


public class UITextView implements IGameObserver {

	private class GameInput implements Runnable {
			
		@Override
		public void run() {	
			
			// exit condition for thread
			while (controller.getState() != GAME_STATE.KILLED && input.hasNextLine()) {
				
				String strInput = input.nextLine();

				// listen to Game Inputs
				if (controller.getState() == GAME_STATE.RUNNING) {
					
					if (strInput.equals("a")) {
						controller.processGameInput(PLAYER_INPUT.LEFT);
					} else if (strInput.equals("d")) {
						controller.processGameInput(PLAYER_INPUT.RIGHT);
					} else if (strInput.equals("p")) {
						controller.processGameInput(PLAYER_INPUT.PAUSE);
					}


					// listen to menu Inputs
				} else {	
					if (strInput.matches("[0-9]+")){
						int in = Integer.valueOf(strInput);

						if (in >= 0 && in < MENU_ITEM.values().length) {
							controller.processMenuInput(MENU_ITEM.values()[in]);						
						}
					}
				}

			}
			
		}

	}


	protected GameController controller;
	private Scanner input;
	private Thread gameInputThread;	

	public UITextView() {
		super();
		Locale.setDefault(new Locale("en", "US"));
		input = new Scanner(System.in);
		gameInputThread = new Thread(new GameInput());
		gameInputThread.setDaemon(true);
		gameInputThread.setName("GameInput_Thread");
		gameInputThread.start();
	}

	/*
	 * (non-Javadoc)
	 * @see communication.IGameObserver#updateRepaintPlayGrid()
	 */
	@Override
	public void updateRepaintPlayGrid() {	
		//		for (AbstractBrick brick : getController().getGrid().getBricks()) {
		//			System.out.printf("TUI: brick (%d, %d)\n", brick.getX(), brick.getY());
		//		}
		//		Slider s = getController().getGrid().getSlider();
		//		if (s != null) {
		//			System.out.printf("TUI: slider (%d, %d)\n", s.getX(), s.getY());
		//		}
		//
		//		for (Ball ball : getController().getGrid().getBalls()) {
		//			System.out.printf("TUI: ball (%.1f, %.1f)  speed: (%.1f, %.1f) \n---\n", ball.getX(), ball.getY(), ball.getSpeedX(), ball.getSpeedY());
		//		}		
	}

	/*
	 * (non-Javadoc)
	 * @see communication.IGameObserver#updateGameState(communication.ObservableGame.GAME_STATE)
	 */
	@Override
	public void updateGameState(GAME_STATE state) {
		System.out.println("TUI: game state changed: " + state.name());
	}

	public GameController getController() {
		return controller;
	}

	public void setController(GameController controller) {
		this.controller = controller;
	}

	@Override
	public void updateGameMenu(MENU_ITEM[] menuItems, String title) {		

		System.out.println("MENU: -----  " + title + " ----- ");
		for (MENU_ITEM m : menuItems) {
			System.out.printf("[%d] %s\n", m.ordinal(), TextMapping.getTextForMenuEnum(m));
		}

	}

	@Override
	public void updateGameFrame() {
		// not used yet
	}


}
