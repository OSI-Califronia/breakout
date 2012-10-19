package view;

import controller.GameController;

public abstract class AbstractView {
	
	protected GameController controller;
	
	public AbstractView() {
		super();				
	}
	
	public abstract void repaintPlayGrid();

	
	public GameController getController() {
		return controller;
	}

	public void setController(GameController controller) {
		this.controller = controller;
	}
	

}
