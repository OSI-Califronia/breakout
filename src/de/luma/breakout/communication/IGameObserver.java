package de.luma.breakout.communication;

import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.ObservableGame.MENU_ITEM;

public interface IGameObserver {
	
	void updateRepaintPlayGrid();
	void updateGameState(GAME_STATE state);
	void updateGameMenu(MENU_ITEM[] menuItems, String title);
	void updateGameFrame();
	void updateOnResize();
	
}
