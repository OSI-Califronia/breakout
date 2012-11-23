package de.luma.breakout.view.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;


import de.luma.breakout.communication.IGameObserver;
import de.luma.breakout.communication.ObservableGame.GAME_STATE;
import de.luma.breakout.communication.ObservableGame.MENU_ITEM;
import de.luma.breakout.controller.GameController;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements IGameObserver {

	protected GameController controller;

	private KeyListener keyListener;

	private JPanel bpaButtons;	
	private JButton btnStart;
	private JButton btnStop;
	private GameView2D bpaGameView;

	public MainWindow() {
		super();
		initializeComponents();
	}

	private void initializeComponents() {
		this.setTitle("Breakout");
		this.setSize(800, 600);
		this.setVisible(true);
		this.add(getBpaButtons(), BorderLayout.NORTH);
		this.add(getBpaGameView2D(), BorderLayout.CENTER);
		this.pack();		

		this.addKeyListener(getGameKeyListener());

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	private boolean leftKeyPressed = false;
	private boolean rightKeyPressed = false;


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
						getController().processInput(GameController.PLAYER_INPUT.PAUSE);
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
					}
				}

				public void keyTyped(KeyEvent e) { 	}
			};
		}
		return keyListener;

	}

	private JPanel getBpaButtons() {
		if (bpaButtons == null) {
			bpaButtons = new JPanel();
			bpaButtons.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			bpaButtons.setLayout(new FlowLayout(FlowLayout.LEFT));
			//			bpaButtons.add(getBtnStart());
			//			bpaButtons.add(getBtnStop());
		}
		return bpaButtons;
	}

	protected JButton getGameButton(String text) {
		JButton btn = new JButton();
		btn.setPreferredSize(new Dimension(100, 20));
		btn.setText(text);
		return btn;
	}

	//	/**
	//	 * public because of debugging and testing
	//	 * @return
	//	 */
	//	public JButton getBtnStart() {
	//		if (btnStart == null) {
	//			btnStart = getGameButton("New Game");	
	//			btnStart.addKeyListener(getGameKeyListener());
	//			btnStart.addActionListener(new ActionListener() {						
	//				@Override
	//				public void actionPerformed(ActionEvent e) {
	//					getController().processInput(GameController.PLAYER_INPUT.START);
	//				}
	//			});
	//		}
	//
	//		return btnStart;
	//	}
	//	
	//	/**
	//	 * public because of debugging and testing
	//	 * @return
	//	 */
	//	public JButton getBtnStop() {
	//		if (btnStop == null) {
	//			btnStop = getGameButton("End Game");			
	//			btnStop.addKeyListener(getGameKeyListener());
	//			btnStop.addActionListener(new ActionListener() {						
	//				@Override
	//				public void actionPerformed(ActionEvent e) {
	//					getController().processInput(GameController.PLAYER_INPUT.CANCEL_GAME);
	//				}
	//			});
	//		}
	//
	//		return btnStop;
	//	}


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
		// check if panel has to resize
		getBpaGameView2D().setPreferredSize(new Dimension(getController().getGrid().getWidth(),
				getController().getGrid().getHeight()));


		getBpaGameView2D().repaint();
		
		
	}
	
	@Override
	public void updateGameFrame() {
		if (leftKeyPressed) {
			getController().processInput(GameController.PLAYER_INPUT.LEFT);
		}
		if (rightKeyPressed) {
			getController().processInput(GameController.PLAYER_INPUT.RIGHT);
		}
	}

	@Override
	public void updateGameState(GAME_STATE state) {

		if (state == GAME_STATE.MENU_WINGAME) {
//			JOptionPane.showMessageDialog(this, "You win the Game");

		} else if (state == GAME_STATE.KILLED) {
			this.dispose();
		}

	}

	@Override
	public void updateGameMenu(MENU_ITEM[] menuItems, String title) {
		// TODO Auto-generated method stub

	}
}
