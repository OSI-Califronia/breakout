package de.luma.breakout.view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.luma.breakout.communication.ObservableGame.MENU_ITEM;
import de.luma.breakout.data.objects.IBrick;
import de.luma.breakout.data.objects.impl.Slider;

@SuppressWarnings("serial")
public class BpaEditorToolbar extends JPanel {

	private JTextField tfiWidth;
	private JTextField tfiHeight;
	private BtnEditor btnSave;
	private BtnEditor btnLoad;
	private BtnEditor btnReset;

	private IGuiManager guiManager;
	private GameView2D gameView;

	private JPanel bpaSize;
	private JPanel bpaBricks;
	private JPanel bpaButtons;

	private ActionListener btnBricksActionListener;
	private ActionListener resizeActionListener;

	public BpaEditorToolbar(IGuiManager guiManager, GameView2D gameView) {
		super();
		this.guiManager = guiManager;
		this.gameView = gameView;
		initializeComponents();
	}

	private void initializeComponents() {
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(200, 300));
		this.add(getBpaSize());
		this.add(getBpaBricks());
		this.add(getBpaButtons());		

	}

	private JPanel getBpaSize() {
		if (bpaSize == null) {
			bpaSize = new JPanel();
			bpaSize.setLayout(new FlowLayout(FlowLayout.CENTER));
			bpaSize.setBackground(Color.BLACK);
			bpaSize.setPreferredSize(new Dimension(200, 100));

			// width textbox
			JLabel lblWidth = new JLabel("Width");
			lblWidth.setPreferredSize(new Dimension(80, 20));
			tfiWidth = new JTextField();
			tfiWidth.setPreferredSize(new Dimension(80, 20));
			setColors(lblWidth);
			setColors(tfiWidth);
			tfiWidth.addActionListener(getResizeActionListener());


			// height textbox
			JLabel lblHeight = new JLabel("Height");
			lblHeight.setPreferredSize(new Dimension(80, 20));
			tfiHeight = new JTextField();
			tfiHeight.setPreferredSize(new Dimension(80, 20));
			setColors(lblHeight);
			setColors(tfiHeight);		
			tfiHeight.addActionListener(getResizeActionListener());

			bpaSize.add(lblHeight);
			bpaSize.add(lblWidth);
			bpaSize.add(tfiHeight);		
			bpaSize.add(tfiWidth);			
		}
		return bpaSize;
	}

	private JPanel getBpaButtons() {
		if (bpaButtons == null) {
			bpaButtons = new JPanel();
			bpaButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
			bpaButtons.setBackground(Color.BLACK);
			bpaButtons.setPreferredSize(new Dimension(200, 300));


			// save button
			btnSave = new BtnEditor(guiManager);			
			btnSave.setPreferredSize(new Dimension(200, 80));
			btnSave.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					guiManager.getGameController().saveLevel();
				}
			});
			btnSave.setText("Save");

			// load button
			btnLoad = new BtnEditor(guiManager);
			btnLoad.setPreferredSize(new Dimension(200, 80));
			btnLoad.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					guiManager.getGameController().processMenuInput(MENU_ITEM.MNU_LEVEL_CHOOSE);
//					JOptionPane.showMessageDialog(BpaEditorToolbar.this, "load level");
				}
			});
			btnLoad.setText("Load");


			// reset button
			btnReset = new BtnEditor(guiManager);
			btnReset.setPreferredSize(new Dimension(200, 80));
			btnReset.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					guiManager.getGameController().clearGrid();
					guiManager.getGameController().setSlider(new Slider(0, 0, 100, 20));
					guiManager.getGameController().setGridSize(500, 500);
				}
			});
			btnReset.setText("Reset");

			bpaButtons.add(btnSave);
			bpaButtons.add(btnLoad);
			bpaButtons.add(btnReset);
		}
		return bpaButtons;
	}

	private JPanel getBpaBricks() {
		if (bpaBricks == null) {
			bpaBricks = new JPanel();
			bpaBricks.setLayout(new FlowLayout(FlowLayout.CENTER));
			bpaBricks.setBackground(Color.BLACK);
			bpaBricks.setPreferredSize(new Dimension(200, 100));
			bpaBricks.setBorder(BorderFactory.createLineBorder(IGuiManager.TEXT_COLOR));

			BtnEditorBrick btn = null;
			for (IBrick brick : guiManager.getGameController().getBrickClasses()) {				
				btn = new BtnEditorBrick(guiManager, brick);
				btn.setPreferredSize(new Dimension(50, 20));
				btn.addActionListener(getBtnBrickActionListener());
				bpaBricks.add(btn);
			}			
		}
		return bpaBricks;
	}

	private ActionListener getBtnBrickActionListener() {
		if (btnBricksActionListener == null) {
			btnBricksActionListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					BtnEditorBrick btn = (BtnEditorBrick) e.getSource();
					gameView.setNewBrickClassName(btn.getBrickInstance().getClass().getName());
				}
			};			
		}
		return btnBricksActionListener;
	}

	private ActionListener getResizeActionListener() {
		if (resizeActionListener == null) {
			resizeActionListener = new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					Dimension gridDim = guiManager.getGameController().getGridSize();

					if (!getTfiHeight().getText().trim().equals("")) {
						int newHeight = Integer.valueOf(getTfiHeight().getText());
						if (newHeight >= 500) {
							gridDim.setSize(gridDim.getWidth(), newHeight);
						}
					}

					if (!getTfiWidth().getText().trim().equals("")) {
						gridDim.setSize(Integer.valueOf(getTfiWidth().getText()), gridDim.getHeight());
					}					

					guiManager.getGameController().setGridSize(gridDim.width, gridDim.height);	

					gameView.requestFocusInWindow();
				}		
			};
		}
		return resizeActionListener;		
	}

	private void setColors(JComponent c) {
		c.setForeground(IGuiManager.TEXT_COLOR);
		c.setBackground(Color.BLACK);
	}

	public JTextField getTfiWidth() {
		return tfiWidth;
	}

	public JTextField getTfiHeight() {
		return tfiHeight;
	}


}
