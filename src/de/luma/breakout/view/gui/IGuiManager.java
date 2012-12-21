package de.luma.breakout.view.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import de.luma.breakout.controller.IGameController;

public interface IGuiManager {
	// Default values for Text
	final Font TEXT_FONT = new Font("Impact", Font.ITALIC, 30);
	final Color TEXT_COLOR = new Color(43, 247, 255);
	
	// Default values for Button
	final Font BUTTON_FONT = new Font("Impact", Font.ITALIC, 24);
	final Color BUTTON_COLOR = Color.WHITE;
	
	// Menu window size
	static final int MENU_WIDTH = 800;
	static final int MENU_HEIGHT = 800;
	
	Image getGameImage(String filePath);	

	void updateLayout();
	
	void kill();
	
	IGameController getGameController();
}
