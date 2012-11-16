package communication;

import communication.ObservableGame.GAME_STATE;
import communication.ObservableGame.MENU_ITEM;

public interface IGameObserver {
	
	void updateRepaintPlayGrid();
	void updateGameState(GAME_STATE state);
	void updateGameMenu(MENU_ITEM[] menuItems, String title);
	
}
