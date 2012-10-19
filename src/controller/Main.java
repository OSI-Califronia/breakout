package controller;

import view.UITextView;

public class Main {
	
	public Main() {
		super();
	}
	
	public static void main(String[] args) {
		// Controller
		GameController controller = new GameController();		
		
		// TUI
		UITextView view = new UITextView();
		view.setController(controller);
				
	}
	
}
