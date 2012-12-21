package de.luma.breakout.data.objects.impl;

import java.awt.Color;

import de.luma.breakout.data.objects.IBall;

/**
 * Data class for a Moving brick.
 * @author mabausch
 *
 */
public class MovingBrick extends AbstractBrick {

	private static final int DEFAULT_MOVING_RANGE = 300;
	
	private int frameCounter = 0;
	private int movingRangeX;
	
	/**
	 * Default constructor
	 */
	public MovingBrick() {
		super();
		movingRangeX = DEFAULT_MOVING_RANGE;
		frameCounter = 0;
		getProperties().setProperty(PROP_COLOR, Color.blue.toString());
		getProperties().setProperty(PROP_IMG_PATH, "resources\\movingBrick.png");
	}	
	
	/*
	 * (non-Javadoc)
	 * @see de.luma.breakout.data.objects.IDecodable#decode(java.lang.String)
	 */
	@Override
	public void decode(String line) {
		String[] s = decodeBasic(line);
		
		setFrameCounter(Integer.valueOf(s[4]));
		setMovingRangeX(Integer.valueOf(s[5]));
	
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.luma.breakout.data.objects.IDecodable#encode()
	 */
	@Override
	public String encode() {
		StringBuilder sb = super.encodeBasic();
		sb.append(String.format(",%d,", this.getFrameCounter()));  // comma at start
		sb.append(String.format("%d", this.getMovingRangeX())); // no comma at end
		
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see de.luma.breakout.data.objects.impl.AbstractBrick#tryCollision(de.luma.breakout.data.objects.IBall)
	 */
	@Override
	public boolean tryCollision(IBall b) {
		return super.tryCollisionRectangle(b);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.luma.breakout.data.objects.impl.AbstractBrick#onNextFrame()
	 */
	@Override
	public void onNextFrame() {
		super.onNextFrame();
		
		frameCounter = (frameCounter + 1) % movingRangeX;
		double factor = (double) frameCounter / movingRangeX * 2 * Math.PI;
		this.setX(this.getX() + (int) (Math.sin(factor) * 2));
	}
	
	/** */
	public int getFrameCounter() {
		return frameCounter;
	}

	/** */
	public void setFrameCounter(int frameCounter) {
		this.frameCounter = frameCounter;
	}

	/** */
	public int getMovingRangeX() {
		return movingRangeX;
	}

	/** */
	public void setMovingRangeX(int movingRangeX) {
		this.movingRangeX = movingRangeX;
	}

}
