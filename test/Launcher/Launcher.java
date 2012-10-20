package Launcher;


import controller.GameController;
import controller.GameController.GAME_STATE;
import view.UITextView;
import data.Ball;
import data.PlayGrid;
import data.bricks.SimpleBrick;
import data.bricks.Slider;

// Kein Test vorgesehen da prinzipiell unbegrenzte Laufzeit
public class Launcher {
	
	public Launcher() {
		super();
	}
	
	/**
	 * Game launcher.
	 */
	public static void main(String[] args) {
		// create data model
		PlayGrid grid = new PlayGrid(500, 500);
		
		// create controller
		GameController controller = new GameController();		
		controller.setGrid(grid);
		
		// TUI
		UITextView view = new UITextView();
		view.setController(controller);
		controller.addObserver(view);
		
		controller.getGrid().addBrick(new SimpleBrick(50, 50));
		controller.getGrid().addBall(new Ball(60, 80, 0, -1, 1));
		controller.getGrid().setSlider(new Slider(200, 450, 100, 30));

		controller.processInput(GameController.PLAYER_INPUT.START);
		try {
			while (controller.getState() == GAME_STATE.RUNNING) {
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
