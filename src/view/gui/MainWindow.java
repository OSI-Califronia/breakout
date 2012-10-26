package view.gui;

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
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import communication.IObserver;

import controller.GameController;
import controller.GameController.GAME_STATE;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements IObserver {
	
	protected GameController controller;
	
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
		
		this.addKeyListener(new KeyListener() {
		    public void keyPressed(KeyEvent e) {
		    	if (e.getKeyCode() == KeyEvent.VK_LEFT) {
		    		getController().processInput(GameController.PLAYER_INPUT.LEFT);
		    	}
		    	
		    	if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
		    		getController().processInput(GameController.PLAYER_INPUT.RIGHT);
		    	}
		    }

		    public void keyReleased(KeyEvent e) { }

		    public void keyTyped(KeyEvent e) { }
		});
	}
	
	private JPanel getBpaButtons() {
		if (bpaButtons == null) {
			bpaButtons = new JPanel();
			bpaButtons.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			bpaButtons.setLayout(new FlowLayout(FlowLayout.LEFT));
			bpaButtons.add(getBtnStart());
			bpaButtons.add(getBtnStop());
		}
		return bpaButtons;
	}
	
	protected JButton getGameButton(String text) {
		JButton btn = new JButton();
		btn.setPreferredSize(new Dimension(100, 20));
		btn.setText(text);
		return btn;
	}
	
	private JButton getBtnStart() {
		if (btnStart == null) {
			btnStart = getGameButton("New Game");			
			btnStart.addActionListener(new ActionListener() {						
				@Override
				public void actionPerformed(ActionEvent e) {
					getController().processInput(GameController.PLAYER_INPUT.START);
				}
			});
		}

		return btnStart;
	}
	
	private JButton getBtnStop() {
		if (btnStop == null) {
			btnStop = getGameButton("End Game");			
			btnStop.addActionListener(new ActionListener() {						
				@Override
				public void actionPerformed(ActionEvent e) {
					getController().processInput(GameController.PLAYER_INPUT.CLOSE);
				}
			});
		}

		return btnStop;
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
	public void updateGameState(GAME_STATE state) {
		// TODO Auto-generated method stub
		
	}
}