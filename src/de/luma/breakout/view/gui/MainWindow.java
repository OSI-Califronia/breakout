package de.luma.breakout.view.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import de.luma.breakout.communication.IGameObserver;
import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.ObservableGame.MENU_ITEM;
import de.luma.breakout.controller.GameController;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements IGameObserver {

	protected GameController controller;

	private MediaTracker mediaTracker;
	
	private MENU_ITEM[] menuItems;
	private String menuTitle;
	
	private KeyListener keyListener;
	
	private GameView2D bpaGameView;	

	private boolean leftKeyPressed = false;
	private boolean rightKeyPressed = false;
	
	private final Map<String, Image> mapImages = new HashMap<String, Image>();
	
	public MainWindow() {
		super();
		initializeComponents();
	}

	private void initializeComponents() {
		//this.setUndecorated(true);
		this.setTitle("Breakout");		
		this.setVisible(true);		
		this.add(getBpaGameView2D(), BorderLayout.CENTER);
		this.setSize(800, 700);
		this.pack();		

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		loadImageResources();
	}


	public KeyListener getGameKeyListener() {
		if (keyListener == null) {
			keyListener = new KeyListener() {
				public void keyPressed(KeyEvent e) {				
					switch (e.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						leftKeyPressed = true;
						break;
					case KeyEvent.VK_RIGHT:
						rightKeyPressed = true;
						break;
					case KeyEvent.VK_ESCAPE:
						getController().processGameInput(GameController.PLAYER_INPUT.PAUSE);
						break;
					case KeyEvent.VK_UP:
						getBpaGameView2D().selectPreviousMenuItem();
						break;
					case KeyEvent.VK_DOWN:
						getBpaGameView2D().selectNextMenuItem();
						break;
					case KeyEvent.VK_ENTER:
						getBpaGameView2D().selectCurrentMenuItem();
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
						getController().setCreativeMode(!getController().getCreativeMode());
					case KeyEvent.VK_SHIFT:
						// save level
						getController().getGrid().saveLevel(new File("test/level_" + System.currentTimeMillis() + ".lvl"));
					}
				}

				public void keyTyped(KeyEvent e) { 	}
			};
		}
		return keyListener;

	}


	private GameView2D getBpaGameView2D() {
		if (bpaGameView == null) {
			bpaGameView = new GameView2D(this);			
		}
		return bpaGameView;
	}

	public GameController getController() {
		return controller;
	}

	public void setController(GameController controller) {
		this.controller = controller;		
	}


	@Override
	public void updateRepaintPlayGrid() {	

		getBpaGameView2D().repaint();		
	}
	
	@Override
	public void updateGameFrame() {
		if (leftKeyPressed) {
			getController().processGameInput(GameController.PLAYER_INPUT.LEFT);
		}
		if (rightKeyPressed) {
			getController().processGameInput(GameController.PLAYER_INPUT.RIGHT);
		}
	}

	@Override
	public void updateGameState(GAME_STATE state) {

		if (state == GAME_STATE.MENU_WINGAME) {
//			JOptionPane.showMessageDialog(this, "You win the Game");

		} else if (state == GAME_STATE.RUNNING) {
			
			Insets insets = this.getInsets();
			this.setSize(new Dimension(insets.left + insets.right + controller.getGrid().getWidth(), 
					insets.top + insets.bottom + controller.getGrid().getHeight()));
			this.pack();
			
			
		} else if (state == GAME_STATE.KILLED) {
			this.dispose();			
		}

	}

	@Override
	public void updateGameMenu(MENU_ITEM[] menuItems, String title) {
		this.menuItems = menuItems;
		this.menuTitle = title;
		repaint();
	}
	
	
	private boolean loadImageResources() {		
		Image img = null;
		String[] images = new String[] {
				"resources/button.png",
				"resources/button_selected.png",
				"resources/breakout_logo.png"				
		};
		
		for (String str : images) {
			img = getToolkit().getImage(str);	
			mapImages.put(str, img);
			getMediaTracker().addImage(img, str.hashCode());
		}
				
		try { 
			getMediaTracker().waitForAll(); 
			return true;
		} catch (InterruptedException ex) { 
			return false;
		}
	}
	
	private MediaTracker getMediaTracker(){
		if (mediaTracker == null) {
			mediaTracker = new MediaTracker(this);
		}
		return mediaTracker;
	}

	public MENU_ITEM[] getMenuItems() {
		return menuItems;
	}

	public String getMenuTitle() {
		return menuTitle;
	}

	public Map<String, Image> getMapImages() {
		return mapImages;
	}
	
}




