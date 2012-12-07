package de.luma.breakout.view.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import de.luma.breakout.controller.IGameController;

public interface IGuiManager {
	// Default values for Text
	public final Font TEXT_FONT = new Font("Impact", Font.ITALIC, 30);
	public final Color TEXT_COLOR = new Color(43, 247, 255);
	
	// Default values for Button
	public final Font BUTTON_FONT = new Font("Impact", Font.ITALIC, 24);
	public final Color BUTTON_COLOR = Color.WHITE;
	
	public Image getGameImage(String filePath);	

	public void updateLayout();
	
	public void kill();
	
	public IGameController getGameController();
}
