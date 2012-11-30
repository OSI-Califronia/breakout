package de.luma.breakout.view.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

import de.luma.breakout.data.objects.AbstractBrick;

public class BtnEditorBrick extends JButton {

	private IGuiManager guiManager;
	private AbstractBrick brickInstance;
	
	public BtnEditorBrick(IGuiManager guiManager, AbstractBrick brickInstance) {
		super();
		this.guiManager = guiManager;
		this.brickInstance = brickInstance;
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		String imgPath = brickInstance.getProperties().getProperty(AbstractBrick.PROP_IMG_PATH, "");
		g2d.drawImage(guiManager.getGameImage(imgPath), 0, 0, getWidth(), getHeight(), this);
	}

	public AbstractBrick getBrickInstance() {
		return brickInstance;
	}
	
}
