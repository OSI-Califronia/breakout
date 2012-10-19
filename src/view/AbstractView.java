package view;

import communication.IObserver;

import controller.GameController;

public abstract class AbstractView implements IObserver {
	
	protected GameController controller;
	
	public AbstractView() {
		super();				
	}
	
	public GameController getController() {
		return controller;
	}

	public void setController(GameController controller) {
		this.controller = controller;
	}
	

}
