package de.luma.breakout.view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.ObservableGame.MENU_ITEM;
import de.luma.breakout.communication.TextMapping;
import de.luma.breakout.data.PlayGrid;
import de.luma.breakout.data.objects.AbstractBrick;
import de.luma.breakout.data.objects.Ball;
import de.luma.breakout.data.objects.Slider;


@SuppressWarnings("serial")
public class GameView2D extends JPanel {	

	private MainWindow mainWindow;
	private int selectedItem = 0;

	public GameView2D(MainWindow mainWindow) {
		super();		
		this.mainWindow = mainWindow;
		initializeComponents();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);		

		Graphics2D g2d = (Graphics2D) g;
		
		if (mainWindow.getController() == null) {
			return;
		}
		
		// Paint Running Game
		if (mainWindow.getController().getState() == GAME_STATE.RUNNING) {
			paintGame(g2d);			
		
		// Paint Menu
		} else {			
			paintMenu(g2d);			
		}
	}

		
	private void paintMenu(Graphics2D g) {
		if (mainWindow.getMenuItems() == null) { 
			return;
		}
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// background black
		g.setColor(Color.black);
		g.fillRect(0, 0, mainWindow.getWidth(), mainWindow.getHeight());
		
		Image img = mainWindow.getMapImages().get("resources/breakout_logo.png");		
		g.drawImage(img, 50, 50, Color.black, null);
		
		Image imgBtn = mainWindow.getMapImages().get("resources/button.png");			
		Image imgBtnSelected = mainWindow.getMapImages().get("resources/button_selected.png");
				
		int x = (this.getWidth() - imgBtn.getWidth(null)) / 2;
		int y = 250;
		
		// print Title
		g.setFont(new Font("Impact", Font.ITALIC, 30));
		g.setColor(new Color(43, 247, 255));
		
		g.drawString(mainWindow.getMenuTitle(), x + 50, y -10);
		
		// print Menu Items
		g.setFont(new Font("Impact", Font.ITALIC, 24));
		g.setColor(Color.WHITE);
		
		
		
		for (int i = 0; i < mainWindow.getMenuItems().length; ++i) {
			if (i == selectedItem) {
				g.drawImage(imgBtnSelected, x, y, Color.black, null);
			} else {
				g.drawImage(imgBtn, x, y, Color.black, null);
			}
			
			g.drawString(TextMapping.getTextForMenuEnum(mainWindow.getMenuItems()[i]), x + 150, y +75);
			y += imgBtn.getHeight(null);
		}
		
	}
	
	private void paintGame(Graphics2D g) {
		PlayGrid grid = mainWindow.controller.getGrid();

		// print Grid
		g.setColor(Color.black);
		g.fillRect(0, 0, grid.getWidth(), grid.getHeight());

		// print balls
		for (Ball b : grid.getBalls()) {
			g.setColor(Color.red);
			g.fillOval(Double.valueOf(b.getX() - b.getRadius()).intValue() ,
					Double.valueOf(b.getY() - b.getRadius()).intValue(),
					Double.valueOf(b.getRadius() * 2).intValue(),
					Double.valueOf(b.getRadius() * 2).intValue());
		}

		// print bricks
		for (AbstractBrick b : grid.getBricks()) {			
			g.setColor(Color.blue);
			g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
		}

		// print slider
		Slider s = grid.getSlider();
		g.setColor(Color.gray);
		g.fillRect(s.getX(), s.getY(), s.getWidth(), s.getHeight());
	}

	private void initializeComponents() {		
		this.setPreferredSize(new Dimension(800, 800));
		this.addKeyListener(mainWindow.getGameKeyListener());

	}
	
	public void selectNextMenuItem() {
		if (selectedItem + 1 < mainWindow.getMenuItems().length)
			selectedItem++;
		this.repaint();
	}
	
	public void selectPreviousMenuItem() {
		if (selectedItem - 1 >= 0)
			selectedItem--;
		this.repaint();
	}
	
	public void selectCurrentMenuItem() {
		mainWindow.getController().processMenuInput(mainWindow.getMenuItems()[selectedItem]);
	}


}
