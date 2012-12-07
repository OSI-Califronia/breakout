package de.luma.breakout.communication;

import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.ObservableGame.MENU_ITEM;

public interface IObservableGame {

	/**
	 * Add an Observer which listens to Game state changes
	 * @param obs
	 */
	public abstract void addObserver(IGameObserver obs);

	/**
	 * Remove an Observer
	 * @param obs
	 */
	public abstract void removeObserver(IGameObserver obs);

	/**
	 * performs am updateRepaintPlayGrid for all Observers
	 * which listen to updatRepaint method
	 * needed for GUI 
	 */
	public abstract void notifyRepaintPlayGrid();

	/**
	 * performs an updateGameFrame for all Observers
	 * which listen to updateGameFrame method
	 */
	public abstract void notifyNextGameFrame();

	/**
	 * notify all Observers which listen that game state has changed
	 */
	public abstract void notifyGameStateChanged(GAME_STATE state);

	/**
	 * notify all Observers which listen that the grid size has changed.
	 */
	public abstract void notifyOnResize();

	public abstract void notifyGameMenu(MENU_ITEM[] menuItems, String title);

}