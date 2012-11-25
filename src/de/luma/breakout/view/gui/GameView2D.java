package de.luma.breakout.view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.TextMapping;
import de.luma.breakout.data.PlayGrid;
import de.luma.breakout.data.objects.AbstractBrick;
import de.luma.breakout.data.objects.Ball;
import de.luma.breakout.data.objects.SimpleBrick;
import de.luma.breakout.data.objects.Slider;


@SuppressWarnings("serial")
public class GameView2D extends JPanel {	
	
	private class GameView2DMouseListener extends MouseInputAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			mainWindow.setTitle(e.paramString());

			if (mainWindow.getController().getState() == GAME_STATE.RUNNING && mainWindow.getController().getCreativeMode()) {

				switch (e.getButton()) {
				
				case MouseEvent.BUTTON1:		// left mouse, create brick
					if (createdBrick == null) {
						createdBrick = new SimpleBrick(e.getX(), e.getY());	
						mainWindow.controller.getGrid().addBrick(createdBrick);
					}
					break;
					
				case MouseEvent.BUTTON3:		// right mouse, create ball
					if (createdBall == null) {
						createdBall = new Ball(e.getX(), e.getY(), 0, 0, 5);
						mainWindow.controller.getGrid().addBall(createdBall);
					}
					break;
				}
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			super.mouseDragged(e);
			
			// brick is being created
			if (createdBrick != null) {
				createdBrick.setWidth(e.getX() - createdBrick.getX());
				createdBrick.setHeight(e.getY() - createdBrick.getY());
				
			// ball is being created
			} else if (createdBall != null) {
				createdBall.setSpeedX((e.getX() - createdBall.getX()) / VECTOR_LENGTH);
				createdBall.setSpeedY((e.getY() - createdBall.getY()) / VECTOR_LENGTH);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			switch (e.getButton()) {
				case MouseEvent.BUTTON1:		// left mouse
					createdBrick = null;
					break;
				case MouseEvent.BUTTON3:		// right mouse
					createdBall = null;
					break;
			}
		}
		
	}

	private MainWindow mainWindow;
	private int selectedItem = 0;
	private AbstractBrick createdBrick = null;
	private Ball createdBall = null;
	private final static int VECTOR_LENGTH = 20;

	public GameView2D(MainWindow mainWindow) {
		super();		
		this.mainWindow = mainWindow;
		this.setFocusable(true);
		initializeComponents();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);		

		if (!this.isFocusOwner()) {
			this.requestFocusInWindow();
		}
		
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
		g.drawImage(img, (this.getWidth() - img.getWidth(null)) / 2, 20, Color.black, null);

		Image imgBtn = mainWindow.getMapImages().get("resources/button.png");			
		Image imgBtnSelected = mainWindow.getMapImages().get("resources/button_selected.png");

		int x = (this.getWidth() - imgBtn.getWidth(null)) / 2;
		int y = 220;

		// print Title
		g.setFont(new Font("Impact", Font.ITALIC, 30));
		g.setColor(new Color(43, 247, 255));

		int stringWidth = (int) g.getFontMetrics().getStringBounds(mainWindow.getMenuTitle(), g).getWidth();

		g.drawString(mainWindow.getMenuTitle(), (this.getWidth() - stringWidth) / 2, y - 20);

		// print Menu Items
		g.setFont(new Font("Impact", Font.ITALIC, 24));
		g.setColor(Color.WHITE);

		for (int i = 0; i < mainWindow.getMenuItems().length; ++i) {
			if (i == selectedItem) {
				g.drawImage(imgBtnSelected, x, y, Color.black, null);
			} else {
				g.drawImage(imgBtn, x, y, Color.black, null);
			}

			g.drawString(TextMapping.getTextForMenuEnum(mainWindow.getMenuItems()[i]), x + 60, y + 75);
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
			
			// if in creative mode, display ball speed vector
			if (mainWindow.getController().getCreativeMode()) {
				g.setColor(Color.GRAY);
				g.drawLine((int) b.getX(), (int) b.getY(), (int) (b.getX() + VECTOR_LENGTH * b.getSpeedX()), (int) (b.getY() + VECTOR_LENGTH * b.getSpeedY()));
			}
			
			// draw the ellipse
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
		MouseInputAdapter mouseHandler = new GameView2DMouseListener();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);

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
