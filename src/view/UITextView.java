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
		System.out.println("Grid Breite: " + getController().getGrid().getWidth() + " Höhe: " + getController().getGrid().getHeight());
		
		for (AbstractBrick brick : getController().getGrid().getBricks()) {
			System.out.printf("Stein %d %d \n", brick.getX(), brick.getY());
		}
		
		for (Ball ball : getController().getGrid().getBalls()) {
			System.out.printf("Ball %f %f  speed: %f %f \n", ball.getX(), ball.getY(), ball.getSpeedX(), ball.getSpeedY());
		}
	}

	@Override
	public void updateGameState(GAME_STATE state) {
		// TODO Auto-generated method stub
		
	}
	

}
