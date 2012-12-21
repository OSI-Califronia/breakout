package de.luma.breakout.communication;

import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.ObservableGame.MENU_ITEM;

public interface IGameObserver {
	
	/**
	 * process a repaint grid notification.
	 */
	void updateRepaintPlayGrid();
	
	/**
	 * process a game state changed notification.
	 */
	void updateGameState(GAME_STATE state);
	
	/**
	 * process a show menu notification.
	 */
	void updateGameMenu(MENU_ITEM[] menuItems, String title);
	
	/**
	 * process a next game frame notification.
	 */
	void updateGameFrame();
	
	/**
	 * process a grid resized notification.
	 */
	void updateOnResize();
	
}
