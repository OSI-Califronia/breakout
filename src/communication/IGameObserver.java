package communication;

import communication.ObservableGame.GAME_STATE;

public interface IGameObserver {
	
	public void updateRepaintPlayGrid();
	public void updateGameState(GAME_STATE state);
	
}
