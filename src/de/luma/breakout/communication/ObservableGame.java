package de.luma.breakout.communication;

import java.util.LinkedList;
import java.util.List;

public class ObservableGame {
	
	public enum GAME_STATE {
		RUNNING,
		PAUSED,		
		MENU_GAMEOVER,
		MENU_WINGAME,
		MENU_MAIN,
		KILLED
	}
	
	public enum MENU_ITEM {
		MNU_NEW_GAME,
		MNU_LEVEL_CHOOSE,
		MNU_LEVEL_EDITOR,
		MNU_CONTINUE,
		MNU_BACK_MAIN_MENU,
		MNU_END			
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
	 * performs an updateGameFrame for all Observers
	 * which listen to updateGameFrame method
	 */
	public void notifyNextGameFrame() {
		for (IGameObserver obs : observerList) {
			obs.updateGameFrame();
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
	
	
	public void notifyGameMenu(MENU_ITEM[] menuItems, String title) {
		for (IGameObserver obs : observerList) {
			obs.updateGameMenu(menuItems, title);
		}	
	}
}


