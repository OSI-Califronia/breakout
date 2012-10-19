package data.bricks;

import data.Ball;


public abstract class AbstractBrick {
	
	private int x;
	private int y;
	private int width;
	private int height;
	private int hitCount;
	
	public AbstractBrick(int x, int y, int width, int height) {
		super();
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
		setHitCount(0);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getHitCount() {
		return hitCount;
	}

	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}	
	
	public abstract boolean tryCollision(Ball b);
	
	/**
	 * Hit test helper for rectangular bricks. Changes ball movement
	 * when a collision was detected.
	 * @param b
	 * @return True if ball was hit, false if not.
	 */
	protected boolean tryCollisionRectangle(Ball b) {
		boolean isHit = false;
		int ballx = (int) b.getX();
		int bally = (int) b.getY();
		int ballr = (int) b.getRadius();
		
		// Collision with top border
		if (ballx > this.getX() && ballx < this.getX() + this.getWidth()  // ballx matches bricks width
			&& bally + ballr == this.getY()) {
			b.setSpeedY(b.getSpeedY() * (-1)); // invert speedy
			isHit = true;
		}
		
		// Collision with bottom border
		if (ballx > this.getX() && ballx < this.getX() + this.getWidth()  // ballx matches bricks width
			&& bally - ballr == this.getY() + this.getHeight()) {
			b.setSpeedY(b.getSpeedY() * (-1)); // invert speedy
			isHit = true;
		}
		
		// Collision with left border
		if (bally > this.getY() && bally < this.getY() + this.getHeight()  // bally matches bricks height
			&& ballx + ballr == this.getX()) {
			b.setSpeedX(b.getSpeedX() * (-1)); // invert speedx
			isHit = true;
		}
		
		
		// Collision with right border
		if (bally > this.getY() && bally < this.getY() + this.getHeight()  // bally matches bricks height
			&& ballx - ballr == this.getX() + this.getWidth()) {
			b.setSpeedX(b.getSpeedX() * (-1)); // invert speedx
			isHit = true;
		}
		
		return isHit; 
	}
	
}
