package de.luma.breakout.data.objects;



public abstract class AbstractBrick implements IDecodable {

	private int x;
	private int y;
	private int width;
	private int height;
	private int hitCount;

	public AbstractBrick() {
		super();
	}

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

	/**
	 * Get count of previous collisions with a ball.
	 */
	public int getHitCount() {
		return hitCount;
	}

	/**
	 * Set count of previous collisions with a ball.
	 */
	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;
	}	

	public abstract boolean tryCollision(Ball b);

	/**
	 * This mthod return ture if a balls X is Between left and right border of this brick
	 * @param ballx
	 * @return
	 */
	protected boolean matchesXRange(int ballx) {
		return ballx > this.getX() && ballx < this.getX() + this.getWidth();		
	}

	/**
	 * This method returns true if a balls Y is Between top and bottom of this brick
	 * @param bally
	 * @return  
	 */
	protected boolean matchesYRange(int bally) {
		return bally > this.getY() && bally < this.getY() + this.getHeight();
	}

	/**
	 * This mthod checks if the value is betweeen min and max
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	protected boolean isBetween(int value, int min, int max) {
		return value >= min && value <= max;
	}

	/**
	 * Hit-test helper for rectangular bricks. Changes ball movement
	 * when a collision was detected.
	 * @param b
	 * @return True if ball was hit, false if not.
	 */
	protected boolean tryCollisionRectangle(Ball b) {
		boolean isHit = false;
		int ballx = (int) b.getX();
		int bally = (int) b.getY();
		int ballr = (int) b.getRadius();
		int tolY = 5;
		int tolX = 5;


		// Collision with top border
		if (matchesXRange(ballx) 
				&& isBetween(bally + ballr, this.getY(), this.getY() + this.getHeight())
				&& b.getSpeedY() > 0) {// ballx matches bricks width
			
			b.inverseSpeedY(); // invert speedy
			b.setY(this.getY() - b.getRadius() - 1);
			isHit = true;
		}

		// Collision with bottom border
		if (matchesXRange(ballx) 
				&& isBetween(bally - ballr, this.getY(), this.getY() + this.getHeight())
				&& b.getSpeedY() < 0) {// ballx matches bricks width
			
			b.inverseSpeedY();// invert speedy
			b.setY(this.getY() + this.getHeight() + b.getRadius() + 1);
			isHit = true;
		}

		// Collision with left border
		if (matchesYRange(bally) 
				&& isBetween(ballx + ballr, this.getX(), this.getX() + this.getWidth())
				&& b.getSpeedX() > 0) { 
			
			b.inverseSpeedX(); // invert speedx
			b.setX(this.getX() - b.getRadius() - 1);
			isHit = true;
		}


		// Collision with right border
		if (matchesYRange(bally) 
				&& isBetween(ballx - ballr, this.getX(), this.getX() + this.getWidth())
				&& b.getSpeedX() < 0) { 
			
			b.inverseSpeedX(); // invert speedx
			b.setX(this.getX() + this.getWidth() + b.getRadius() + 1);
			isHit = true;
		}

		return isHit; 
	}

	/**
	 * Default brick->string encoder.
	 * Override if custom brick properties need to be encoded.
	 */
	public String encode() {
		return AbstractBrick.encodeBasic(this).toString();
	}

	/**
	 * Encodes the basic parameters of a brick (x, y, width, height) as a string.
	 * 
	 * @param b Brick to be encoded as a string.
	 * @return Returns a string builder that can be used to append custom 
	 * brick properties. No comma is inserted at the end of the string.
	 */
	public static StringBuilder encodeBasic(AbstractBrick b) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%d,", b.getX()));
		sb.append(String.format("%d,", b.getY()));
		sb.append(String.format("%d,", b.getWidth()));
		sb.append(String.format("%d", b.getHeight()));

		return sb;
	}

}
