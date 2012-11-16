package communication;

import java.util.LinkedList;
import java.util.List;

public class ObservableGame {
	
	public enum GAME_STATE {
		RUNNING,
		PAUSED,
		GAMEOVER,
		WINGAME
	}
	
	protected List<IGameObserver> observerList = new LinkedList<IGameObserver>();
	
	public ObservableGame() {
		super();
	}

	public void addObserver(IGameObserver obs) {
		observerList.add(obs);
	}
	
	public void removeObserver(IGameObserver obs) {
		observerList.remove(obs);
	}
	
	public void notifyRepaintPlayGrid() {		
		for (IGameObserver obs : observerList) {
			obs.updateRepaintPlayGrid();
		}		
	}
	
	public void notifyGameStateChanged(GAME_STATE state) {
		for (IGameObserver obs : observerList) {
			obs.updateGameState(state);
		}	
	}
}


