package de.luma.breakout.data.objects.impl;

import java.awt.Color;

import de.luma.breakout.data.objects.IBall;

public class MovingBrick extends AbstractBrick {

	private int frameCounter = 0;
	private int movingRangeX;
	
	public MovingBrick() {
		super();
		setX(50);
		setY(20);
		movingRangeX = 300;
		frameCounter = 0;
		getProperties().setProperty(PROP_COLOR, Color.blue.toString());
		getProperties().setProperty(PROP_IMG_PATH, "resources\\movingBrick.png");
	}	
	
	@Override
	public void decode(String line) {
		String[] s = decodeBasic(line);
		
		setFrameCounter(Integer.valueOf(s[4]));
		setMovingRangeX(Integer.valueOf(s[5]));
	
	}
	
	@Override
	public String encode() {
		StringBuilder sb = super.encodeBasic();
		sb.append(String.format(",%d,", this.getFrameCounter()));  // comma at start
		sb.append(String.format("%d", this.getMovingRangeX())); // no comma at end
		
		return sb.toString();
	}

	@Override
	public boolean tryCollision(IBall b) {
		return super.tryCollisionRectangle(b);
	}
	
	@Override
	public void onNextFrame() {
		super.onNextFrame();
		
		frameCounter = (frameCounter + 1) % movingRangeX;
		double factor = (double) frameCounter / movingRangeX * 2 * Math.PI;
		this.setX(this.getX() + (int) (Math.sin(factor) * 2));
	}
	

	public int getFrameCounter() {
		return frameCounter;
	}

	public void setFrameCounter(int frameCounter) {
		this.frameCounter = frameCounter;
	}

	public int getMovingRangeX() {
		return movingRangeX;
	}

	public void setMovingRangeX(int movingRangeX) {
		this.movingRangeX = movingRangeX;
	}

}
