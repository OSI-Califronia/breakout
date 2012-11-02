package view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;


import data.PlayGrid;
import data.objects.AbstractBrick;
import data.objects.Ball;
import data.objects.Slider;


public class GameView2D extends JPanel {	

	private MainWindow mainWindow;

	public GameView2D(MainWindow mainWindow) {
		super();		
		this.mainWindow = mainWindow;
		initializeComponents();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);		

		if (mainWindow.getController() == null) {
			return;
		}

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
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		this.setPreferredSize(new Dimension(500, 500));
		this.addKeyListener(mainWindow.getGameKeyListener());

	}


}
