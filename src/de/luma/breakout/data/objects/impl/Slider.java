package de.luma.breakout.data.objects.impl;

import de.luma.breakout.data.objects.IBall;



public class Slider extends AbstractBrick {
	
	public Slider() {
		super();
	}
	
	public Slider(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public boolean tryCollision(IBall b) {
		boolean ret = tryCollisionRectangle(b);
		if (ret && b.getAbsoluteSpeed() < IBall.MAX_BALL_SPEED) {
			double delta = b.getX() - (this.getX() + this.getWidth() / 2);
			double diversionFactor = delta / this.getWidth();  // -0.5  to 0.5
			b.setSpeedX(b.getSpeedX() + 3 * Math.sin(diversionFactor));
		}

		return ret;
	}

	@Override
	public void decode(String line) {
		super.decodeBasic(line);
	}

	@Override
	public String encode() {		
		return super.encodeBasic().toString();
	}

}
