package view;

import java.util.Locale;

import communication.IGameObserver;
import communication.ObservableGame.GAME_STATE;

import controller.GameController;
import data.objects.AbstractBrick;
import data.objects.Ball;
import data.objects.Slider;


public class UITextView implements IGameObserver {
		
	protected GameController controller;	
	
	public UITextView() {
		super();
		Locale.setDefault(new Locale("en", "US"));
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
	

}
