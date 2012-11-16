package communication;

import java.util.LinkedList;
import java.util.List;

public class ObservableGame {
	
	public enum GAME_STATE {
		RUNNING,
		PAUSED,
		GAMEOVER,
		WINGAME,
		MENU
	}
	
	protected List<IGameObserver> observerList = new LinkedList<IGameObserver>();
	
	public ObservableGame() {
		super();
	}

	/**
	 * Add an Observer which listens to Game state changes
	 * @param obs
	 */
	public void addObserver(IGameObserver obs) {
		observerList.add(obs);
	}
	
	/**
	 * Remove an Observer
	 * @param obs
	 */
	public void removeObserver(IGameObserver obs) {
		observerList.remove(obs);
	}
	
	/**
	 * performs am updateRepaintPlayGrid for all Observers
	 * which listen to updatRepaint method
	 * needed for GUI 
	 */
	public void notifyRepaintPlayGrid() {		
		for (IGameObserver obs : observerList) {
			obs.updateRepaintPlayGrid();
		}		
	}
	
	/**
	 * notify all Observers which listen that game state has changed
	 * @param state
	 */
	public void notifyGameStateChanged(GAME_STATE state) {
		for (IGameObserver obs : observerList) {
			obs.updateGameState(state);
		}	
	}
}


