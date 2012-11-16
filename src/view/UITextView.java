package view;

import java.util.Locale;
import java.util.Scanner;

import communication.IGameObserver;
import communication.ObservableGame.GAME_STATE;
import communication.ObservableGame.MENU_ITEM;
import communication.TextMapping;

import controller.GameController;
import controller.GameController.PLAYER_INPUT;
import data.objects.AbstractBrick;
import data.objects.Ball;
import data.objects.Slider;


public class UITextView implements IGameObserver {
		
	protected GameController controller;
	private Scanner input;
		
	public UITextView() {
		super();
		Locale.setDefault(new Locale("en", "US"));
		input = new Scanner(System.in);
	}

	/*
	 * (non-Javadoc)
	 * @see communication.IGameObserver#updateRepaintPlayGrid()
	 */
	@Override
	public void updateRepaintPlayGrid() {	
		for (AbstractBrick brick : getController().getGrid().getBricks()) {
			System.out.printf("TUI: brick (%d, %d)\n", brick.getX(), brick.getY());
		}
		Slider s = getController().getGrid().getSlider();
		if (s != null) {
			System.out.printf("TUI: slider (%d, %d)\n", s.getX(), s.getY());
		}

		for (Ball ball : getController().getGrid().getBalls()) {
			System.out.printf("TUI: ball (%.1f, %.1f)  speed: (%.1f, %.1f) \n---\n", ball.getX(), ball.getY(), ball.getSpeedX(), ball.getSpeedY());
		}
		
		String strInput = input.nextLine();
		
		if (strInput.equals("a")) {
			controller.processInput(PLAYER_INPUT.LEFT);
		} else if (strInput.equals("d")) {
			controller.processInput(PLAYER_INPUT.RIGHT);
		} else if (strInput.equals("p")) {
			controller.processInput(PLAYER_INPUT.PAUSE);
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
		
		while (input.hasNextInt()) {
			int in = input.nextInt();
		
			if (in >= 0 && in < MENU_ITEM.values().length) {
				controller.processMenuInput(MENU_ITEM.values()[in]);
				break;
			}
		}
		
	}
	

}
