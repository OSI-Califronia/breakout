package communication;

import controller.GameController.GAME_STATE;

public interface IObserver {
	
	public void updateRepaintPlayGrid();
	public void updateGameState(GAME_STATE state);
}
