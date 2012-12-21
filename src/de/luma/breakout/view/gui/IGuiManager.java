package de.luma.breakout.view.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import de.luma.breakout.controller.IGameController;

/**
 * Interface to GUI.
 */
public interface IGuiManager {
	
	/**
	 * Default text font for menus.
	 */
	final Font TEXT_FONT = new Font("Impact", Font.ITALIC, 30);
	
	/**
	 * Default text color for menus.
	 */
	final Color TEXT_COLOR = new Color(43, 247, 255);
	
	/**
	 * Default text font for buttons.
	 */
	final Font BUTTON_FONT = new Font("Impact", Font.ITALIC, 24);
	
	/**
	 * Default text color for buttons.
	 */
	final Color BUTTON_COLOR = Color.WHITE;
	
	/**
	 * GUI window width when a menu is displayed.
	 */
	static final int MENU_WIDTH = 800;
	
	/**
	 * GUI window height when a menu is displayed.
	 */
	static final int MENU_HEIGHT = 800;
	
	/**
	 * Loads an image from the specified file path.
	 */
	Image getGameImage(String filePath);	

	/**
	 * Invoke update of GUI layout.
	 */
	void updateLayout();
	
	/**
	 * Dispose GUI.
	 */
	void kill();
	
	/**
	 * Return game controller.
	 */
	IGameController getGameController();
}
