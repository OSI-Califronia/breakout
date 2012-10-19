package communication;

import java.util.List;
import java.util.Vector;

import controller.GameController.GAME_STATE;

public class Observable {
	
	protected List<IObserver> observerList = new Vector<IObserver>();
	
	public Observable() {
		super();
	}

	public void addObserver(IObserver obs) {
		observerList.add(obs);
	}
	
	public void removeObserver(IObserver obs) {
		observerList.remove(obs);
	}
	
	public void notifyRepaintPlayGrid() {		
		for (IObserver obs : observerList) {
			obs.updateRepaintPlayGrid();
		}		
	}
	
	public void notifyGameStateChanged(GAME_STATE state) {
		for (IObserver obs : observerList) {
			obs.updateGameState(state);
		}	
	}
}
