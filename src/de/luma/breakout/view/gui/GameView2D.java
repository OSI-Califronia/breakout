package de.luma.breakout.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import de.luma.breakout.communication.IGameObserver;
import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.ObservableGame.MENU_ITEM;
import de.luma.breakout.communication.TextMapping;
import de.luma.breakout.controller.IGameController;
import de.luma.breakout.data.PlayGrid;
import de.luma.breakout.data.objects.AbstractBrick;
import de.luma.breakout.data.objects.Ball;
import de.luma.breakout.data.objects.IDecodable;
import de.luma.breakout.data.objects.SimpleBrick;
import de.luma.breakout.data.objects.Slider;


@SuppressWarnings("serial")
public class GameView2D extends JPanel implements IGameObserver {	

	private class GameView2DMouseListener extends MouseInputAdapter  {

		@Override
		public void mousePressed(MouseEvent e) {
			// ignore mouse actions outside the game area
			Dimension gridSize = getController().getGridSize();
			if (e.getX() > gridSize.getWidth() || e.getY() > gridSize.getHeight()) {
				return;
			}

			if (getController().getState() == GAME_STATE.RUNNING && getController().getCreativeMode()) {

				switch (e.getButton()) {

				case MouseEvent.BUTTON1:		// left mouse, create brick
					if (brickPreview == null) {
						brickPreview = new Rectangle(e.getX(), e.getY(), 50, 20);
					}
					break;

				case MouseEvent.BUTTON3:		// right mouse, create ball
					if (createdBall == null) {
						createdBall = new Ball(e.getX(), e.getY(), 0, 0, 5);
						getController().addBall(createdBall);
					}
					break;
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			super.mouseDragged(e);

			// brick is being created
			if (brickPreview != null) {
				brickPreview.width = e.getX() - brickPreview.x;
				brickPreview.height = e.getY() - brickPreview.y;

				// ball is being created
			} else if (createdBall != null) {
				createdBall.setSpeedX((e.getX() - createdBall.getX()) / VECTOR_LENGTH);
				createdBall.setSpeedY((e.getY() - createdBall.getY()) / VECTOR_LENGTH);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// creating new game object is finished
			switch (e.getButton()) {
			case MouseEvent.BUTTON1:		// left mouse
				if (brickPreview != null && newBrickClassName != null) {					
					Class<?> classObj;
					try {
						classObj = this.getClass().getClassLoader().loadClass(newBrickClassName);
						AbstractBrick obj = (AbstractBrick) classObj.newInstance();

						obj.setX(brickPreview.x);
						obj.setY(brickPreview.y);
						obj.setWidth(brickPreview.width);
						obj.setHeight(brickPreview.height);

						getController().addBrick(obj);
						brickPreview = null;

					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					} catch (InstantiationException e1) {
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						e1.printStackTrace();
					}						
				}
				break;
			case MouseEvent.BUTTON3:		// right mouse
				createdBall = null;
				break;
			}
		}

	}

	private class GameView2DKeyListener implements KeyListener {
		public void keyPressed(KeyEvent e) {				
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				leftKeyPressed = true;
				break;
			case KeyEvent.VK_RIGHT:
				rightKeyPressed = true;
				break;
			case KeyEvent.VK_ESCAPE:
				getController().processGameInput(IGameController.PLAYER_INPUT.PAUSE);
				break;
			case KeyEvent.VK_UP:
				selectPreviousMenuItem();
				break;
			case KeyEvent.VK_DOWN:
				selectNextMenuItem();
				break;
			case KeyEvent.VK_ENTER:
				selectCurrentMenuItem();
				break;
			}
		}

		public void keyReleased(KeyEvent e) {  
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				leftKeyPressed = false;
				break;
			case KeyEvent.VK_RIGHT:
				rightKeyPressed = false;
				break;
			case KeyEvent.VK_C: 
				// switch creative mode
				//				getController().setCreativeMode(!getController().getCreativeMode());
			case KeyEvent.VK_SHIFT:
				// save level
				//				getController().getGrid().saveLevel(new File("test/level_" + System.currentTimeMillis() + ".lvl"));
			}
		}

		public void keyTyped(KeyEvent e) { 	}
	}

	// Menu Variables for printing
	private MENU_ITEM[] menuItems;
	private String menuTitle;
	private int selectedItem = 0;

	// brick or ball that is being created while in level editor mode
	private Rectangle brickPreview;
	private String newBrickClassName;
	private Ball createdBall;

	// length of ball speed vector
	private final static int VECTOR_LENGTH = 20;

	// key input
	private KeyListener keyListener;	
	private boolean leftKeyPressed = false;
	private boolean rightKeyPressed = false;

	// Grafikal components
	private BpaEditorToolbar bpaEditorComps;

	private IGuiManager guiManager;


	public GameView2D(IGuiManager resources) {
		super();		
		this.guiManager = resources;
		this.setFocusable(true);
		initializeComponents();
	}

	private void initializeComponents() {		
		//		this.setPreferredSize(new Dimension(800, 800));
		this.addKeyListener(getGameKeyListener());
		MouseInputAdapter mouseHandler = new GameView2DMouseListener();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);	
	}


	@Override
	public void updateRepaintPlayGrid() {	
		repaint();		
	}

	@Override
	public void updateGameFrame() {
		if (leftKeyPressed) {
			getController().processGameInput(IGameController.PLAYER_INPUT.LEFT);
		}
		if (rightKeyPressed) {
			getController().processGameInput(IGameController.PLAYER_INPUT.RIGHT);
		}
	}

	@Override
	public void updateGameState(GAME_STATE state) {

		// remove editor toolbar if still there
		removeEditorToolbar();

		switch (state) {
		case MENU_GAMEOVER:
		case MENU_MAIN:
		case MENU_WINGAME:
		case PAUSED:
			this.setPreferredSize(new Dimension(800, 800));
			guiManager.updateLayout();
			break;
		case RUNNING:
			// editor mode
			if (getController().getCreativeMode()) {
				startEditorMode();
			}
			break;

		case KILLED:
			guiManager.kill();		
			break;
		}
	}
	
	private void startEditorMode() {
		// add editor visual components
		addEditorToolbar();
		
		// create new Slider
		getController().clearGrid();
		getController().setSlider(new Slider(220, 470, 100, 30));
	}

	@Override
	public void updateGameMenu(MENU_ITEM[] menuItems, String title) {
		this.menuItems = menuItems;
		this.menuTitle = title;
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		this.paintComponents(g);
		this.paintBorder(g);
		this.paintChildren(g);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paintComponents(Graphics g) {
		//super.paintComponents(g);

		if (!this.isFocusOwner() && !getController().getCreativeMode()) {
			this.requestFocusInWindow();
		}

		Graphics2D g2d = (Graphics2D) g;

		if (getController() == null) {
			return;
		}

		// Paint Running Game
		if (getController().getState() == GAME_STATE.RUNNING) {
			paintGame(g2d);			

			// Editor
			if (getController().getCreativeMode()) {
				paintEditor(g2d);
			}			

			// Paint Menu
		} else {			
			paintMenu(g2d);			
		}


	}

	private void paintEditor(Graphics2D g) {
		// paint save and load Button
		Image menuBackg = guiManager.getGameImage("resources/menu_background.png");

		g.drawImage(menuBackg, this.getWidth() - 220, 0, menuBackg.getWidth(null), this.getHeight(), this);


		if (brickPreview != null) {
			g.setColor(Color.RED);
			g.drawRect(brickPreview.x, brickPreview.y, brickPreview.width, brickPreview.height);	
		}
	}

	private void paintMenu(Graphics2D g) {
		if (menuItems == null) { 
			return;
		}

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// background black
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());

		Image img = guiManager.getGameImage("resources/breakout_logo.png");		
		g.drawImage(img, (this.getWidth() - img.getWidth(null)) / 2, 20, Color.black, null);

		Image imgBtn = guiManager.getGameImage("resources/button.png");			
		Image imgBtnSelected = guiManager.getGameImage("resources/button_selected.png");

		int x = (this.getWidth() - imgBtn.getWidth(null)) / 2;
		int y = 220;

		// print Title
		g.setFont(IGuiManager.TEXT_FONT);
		g.setColor(IGuiManager.TEXT_COLOR);

		int stringWidth = (int) g.getFontMetrics().getStringBounds(menuTitle, g).getWidth();

		g.drawString(menuTitle, (this.getWidth() - stringWidth) / 2, y - 20);

		// print Menu Items
		g.setFont(IGuiManager.BUTTON_FONT);
		g.setColor(IGuiManager.BUTTON_COLOR);

		for (int i = 0; i < menuItems.length; ++i) {
			if (i == selectedItem) {
				g.drawImage(imgBtnSelected, x, y, Color.black, null);
			} else {
				g.drawImage(imgBtn, x, y, Color.black, null);
			}

			g.drawString(TextMapping.getTextForMenuEnum(menuItems[i]), x + 60, y + 75);
			y += imgBtn.getHeight(null);
		}

	}


	private void paintGame(Graphics2D g) {
	
		// print Grid
		g.setColor(Color.black);
		Dimension gridSize = getController().getGridSize();
		g.fillRect(0, 0, (int) gridSize.getWidth(), (int) gridSize.getHeight());

		// print balls
		for (Ball b : getController().getBalls()) {

			// if in creative mode, display ball speed vector
			if (getController().getCreativeMode()) {
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
		for (AbstractBrick b : getController().getBricks()) {			


			// print Image if one is defined			
			Image brickImg = guiManager.getGameImage(b.getProperties().getProperty(AbstractBrick.PROP_IMG_PATH, ""));			
			if (brickImg != null) {								
				g.drawImage(brickImg, b.getX(), b.getY(), b.getWidth(), b.getHeight(), Color.black, null);

				// print simple rectangle with color	
			}  else {
				g.setColor(Color.getColor(b.getProperties().getProperty(AbstractBrick.PROP_COLOR, Color.blue.toString())));			
				g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
			}
		}

		// print slider
		Slider s = getController().getSlider();
		if (s != null) {
			g.setColor(Color.gray);
			g.fillRect(s.getX(), s.getY(), s.getWidth(), s.getHeight());
		}

	}




	public void selectNextMenuItem() {
		if (selectedItem + 1 < menuItems.length)
			selectedItem++;
		this.repaint();
	}

	public void selectPreviousMenuItem() {
		if (selectedItem - 1 >= 0)
			selectedItem--;
		this.repaint();
	}

	public void selectCurrentMenuItem() {
		getController().processMenuInput(menuItems[selectedItem]);
	}




	public KeyListener getGameKeyListener() {
		if (keyListener == null) {
			keyListener = new GameView2DKeyListener();
		}
		return keyListener;

	}

	public IGameController getController() {
		return guiManager.getGameController();
	}

	private void addEditorToolbar() {
		if (bpaEditorComps == null) {
			bpaEditorComps = new BpaEditorToolbar(guiManager, this);
			this.setLayout(new BorderLayout());			
		}
		this.add(bpaEditorComps, BorderLayout.EAST);
	}

	private void removeEditorToolbar() {
		if (bpaEditorComps != null) {
			this.remove(bpaEditorComps);
		}
	}

	public void setNewBrickClassName(String newBrickClassName) {
		this.newBrickClassName = newBrickClassName;
	}

	@Override
	public void updateOnResize() {
		Dimension viewSize = getController().getGridSize();
		if (getController().getCreativeMode()) {
			viewSize.setSize(viewSize.getWidth() + 200, viewSize.getHeight());
		}
		this.setPreferredSize(viewSize);
		guiManager.updateLayout();
	}

	


}
