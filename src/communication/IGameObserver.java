package communication;

import communication.ObservableGame.GAME_STATE;

public interface IGameObserver {
	
	void updateRepaintPlayGrid();
	void updateGameState(GAME_STATE state);
	
}
