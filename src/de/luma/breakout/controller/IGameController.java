package de.luma.breakout.controller;

import java.awt.Dimension;
import java.io.File;
import java.util.List;

import de.luma.breakout.communication.IObservableGame;
import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.ObservableGame.MENU_ITEM;
import de.luma.breakout.data.objects.AbstractBrick;
import de.luma.breakout.data.objects.Ball;
import de.luma.breakout.data.objects.Slider;

public interface IGameController extends IObservableGame {
	
	/**
	 * Input options while the game is running.
	 */
	public enum PLAYER_INPUT {
		LEFT,
		RIGHT,
		PAUSE
	}
	
	/* #######################################  USER INPUT HANDLING #######################################   */
	/* ###############################          menus & key events      ##############################  */

	/**
	 *  Initialize the game. Has to be called only one time when the game starts running 
	 */
	public void initialize();
	
	/**
	 * Calculates the next Game Frame.
	 *  (only specified for testing)
	 */
	public void updateGame();
	
	/**
	 * Process interactive user input during the running game (e.g. from key hits)
	 */
	public void processGameInput(PLAYER_INPUT input);
	
	/**
	 * Process the given menu input. 
	 * It is not checked whether the given menu item 
	 * is valid at the current game status.
	 * @param indexOfMenuItem one of the menu items that were 
	 * proposed by notifyGameMenu().
	 */
	public void processMenuInput(MENU_ITEM indexOfMenuItem);
	
	/**
	 * Returns true if controller is in level editing mode.
	 */
	public boolean getCreativeMode();
	
	/**
	 * Return current game state.
	 */
	public GAME_STATE getState();
	

	/* #######################################  LEVEL HANDLING #######################################   */
	
	/**
	 * Loads level with specified location.
	 */
	public boolean loadLevel(File f);
	
	
	/**
	 * Save level with default filename and location.
	 */
	public void saveLevel();
	
	/**
	 * Save level to the specified location.
	 */
	public boolean saveLevel(File f);
	
	/**
	 * Get a list of file paths of available levels.
	 * @return
	 */
	public List<String> getLevelList();
	

	/* #######################################  GRID ACCESS HANDLING #######################################   */
	/* ############################   the same procedure as every year...    ###########################  */
	
	/**
	 * Return a list with an instance of every known brick type.
	 * @return
	 */
	public List<AbstractBrick> getBrickClasses();
	
	/**
	 * Get a list of all balls.
	 */
	public List<Ball> getBalls();

	/**
	 * Get a list of all bricks.
	 */
	public List<AbstractBrick> getBricks();

	/**
	 * Get the slider.
	 */
	public Slider getSlider();

	/**
	 * Set the slider.
	 */
	public void setSlider(Slider slider);

	/**
	 * Add a brick to the grid.
	 */
	public void addBrick(AbstractBrick brick);
	
	/**
	 * Add a ball to the grid.
	 */
	public void addBall(Ball ball);
	
	
	/**
	 * Change play grid size.
	 */
	public void setGridSize(int width, int height);
	
	/**
	 * Returns size of play grid.
	 */
	public Dimension getGridSize();
	
	/** 
	 * Deletes all Objects on Grid.
	 */
	public void clearGrid();
	
	
	
	
	
}
