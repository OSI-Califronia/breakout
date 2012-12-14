package de.luma.breakout.view.tui;

import java.util.Locale;
import java.util.Scanner;


import de.luma.breakout.communication.IGameObserver;
import de.luma.breakout.communication.TextMapping;
import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.ObservableGame.MENU_ITEM;
import de.luma.breakout.controller.IGameController;
import de.luma.breakout.controller.IGameController.PLAYER_INPUT;
import de.luma.breakout.data.objects.AbstractBrick;
import de.luma.breakout.data.objects.Ball;
import de.luma.breakout.data.objects.Slider;


public class UITextView implements IGameObserver {

	private class GameInput implements Runnable {
		
		private static final String CHAR_MOVE_LEFT = "a";
		private static final String CHAR_MOVE_RIGHT = "d";
		private static final String CHAR_PAUSE_GAME = "p";
		
			
		@Override
		public void run() {	
			
			// exit condition for thread
			while (controller.getState() != GAME_STATE.KILLED && input.hasNextLine()) {
				
				String strInput = input.nextLine();

				// listen to Game Inputs
				if (controller.getState() == GAME_STATE.RUNNING) {
					
					if (strInput.equals(CHAR_MOVE_LEFT)) {
						controller.processGameInput(PLAYER_INPUT.LEFT);
					} else if (strInput.equals(CHAR_MOVE_RIGHT)) {
						controller.processGameInput(PLAYER_INPUT.RIGHT);
					} else if (strInput.equals(CHAR_PAUSE_GAME)) {
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

	private IGameController controller;
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
				for (AbstractBrick brick : getController().getBricks()) {
					System.out.printf("TUI: brick (%d, %d)\n", brick.getX(), brick.getY());
				}
				Slider s = getController().getSlider();
				if (s != null) {
					System.out.printf("TUI: slider (%d, %d)\n", s.getX(), s.getY());
				}
		
				for (Ball ball : getController().getBalls()) {
					System.out.printf("TUI: ball (%.1f, %.1f)  speed: (%.1f, %.1f) \n---\n", ball.getX(), ball.getY(), ball.getSpeedX(), ball.getSpeedY());
				}		
	}

	/*
	 * (non-Javadoc)
	 * @see communication.IGameObserver#updateGameState(communication.ObservableGame.GAME_STATE)
	 */
	@Override
	public void updateGameState(GAME_STATE state) {
		System.out.println("TUI: game state changed: " + state.name());
	}

	public IGameController getController() {
		return controller;
	}

	public void setController(IGameController controller) {
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

	@Override
	public void updateOnResize() {
		// TODO Auto-generated method stub
		
	}


}
