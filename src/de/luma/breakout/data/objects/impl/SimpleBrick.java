package de.luma.breakout.data.objects.impl;

import java.awt.Color;


public class SimpleBrick extends AbstractBrick {	
	
	public SimpleBrick(int x, int y) {
		super(x, y, 50, 20);
		getProperties().setProperty(PROP_COLOR, Color.blue.toString());
		getProperties().setProperty(PROP_IMG_PATH, "resources\\simpleBrick.png");
	}
	
	public SimpleBrick() {
		this(0, 0);
	}
	

	@Override
	public void decode(String line) {
		super.decodeBasic(line);
	}

	@Override
	public String encode() {		
		return super.encodeBasic().toString();
	}

	
	/**
	 * SimpleBrick is not resizable, setWidth will be ignored.
	 */
	@Override
	public void setWidth(int width) {
		return;
	}
	/**
	 * SimpleBrick is not resizable, setHeight will be ignored.
	 */
	@Override
	public void setHeight(int height) {
		return;
	}


}
