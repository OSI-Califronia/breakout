package view;

import controller.GameController.GAME_STATE;
import data.Ball;
import data.bricks.AbstractBrick;


public class UITextView extends AbstractView {
		
	public UITextView() {
		super();
	}

	@Override
	public void updateRepaintPlayGrid() {	
		for (AbstractBrick brick : getController().getGrid().getBricks()) {
			System.out.printf("TUI: brick (%d, %d)\n", brick.getX(), brick.getY());
		}
		
		for (Ball ball : getController().getGrid().getBalls()) {
			System.out.printf("TUI: ball (%.1f, %.1f)  speed: (%.1f, %.1f) \n---\n", ball.getX(), ball.getY(), ball.getSpeedX(), ball.getSpeedY());
		}
	}

	@Override
	public void updateGameState(GAME_STATE state) {
		System.out.println("TUI: game state changed: " + state.name());
	}
	

}
