package de.luma.breakout.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.luma.breakout.communication.ObservableGame.MENU_ITEM;

@SuppressWarnings("serial")
public class BpaLevelSelection extends JPanel {
	
	//private GameView2D gameView;
	private IGuiManager guiManager;
	private JScrollPane scrollPane;
	private JPanel buttonsPanel;
	private ActionListener lvlBtnListener;

	public BpaLevelSelection(GameView2D gView, IGuiManager guiMgr) {
		super();
		//this.gameView = gView;
		this.guiManager = guiMgr;
		initializeComponents();
	}
	
	private void initializeComponents() {
		this.setBackground(Color.BLACK);
		
		this.setPreferredSize(new Dimension(800, 800));
		buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		buttonsPanel.setBackground(Color.BLACK);
		
		scrollPane = new JScrollPane(buttonsPanel);
		scrollPane.setPreferredSize(new Dimension(800, 800));
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void loadLevels() {
		buttonsPanel.removeAll();
		
		int i = 1;
		for (String filepath : guiManager.getGameController().getLevelList()) {
			BtnLevelSelection btn = new BtnLevelSelection(filepath, guiManager);
			btn.addActionListener(getLevelButtonListener());
			btn.setText(String.valueOf(i));
			i++;
			buttonsPanel.add(btn);
		}
	}
	
	private ActionListener getLevelButtonListener() {
		if (lvlBtnListener == null) {
			lvlBtnListener = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() instanceof BtnLevelSelection) {
						BtnLevelSelection btn = (BtnLevelSelection) e.getSource();						
						
						guiManager.getGameController().loadLevel(new File(btn.getFilePath()));
						guiManager.getGameController().processMenuInput(MENU_ITEM.MNU_CONTINUE);
						
//						JOptionPane.showMessageDialog(BpaLevelSelection.this, btn.getFilePath());
					}
				}
			};
			
		}

		return lvlBtnListener;
	}
	
}
