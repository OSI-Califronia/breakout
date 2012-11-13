package data.objects;



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
		double tol_y = 5;
		double tol_x = 5;
		
		// Collision with top border
		if (ballx > this.getX() && ballx < this.getX() + this.getWidth()  // ballx matches bricks width
			&& bally + ballr >= this.getY() && bally + ballr < this.getY() + tol_y) {
			b.setSpeedY(b.getSpeedY() * (-1)); // invert speedy
			b.setY(this.getY() - b.getRadius() - 1);
			isHit = true;
		}
		
		// Collision with bottom border
		if (ballx > this.getX() && ballx < this.getX() + this.getWidth()  // ballx matches bricks width
			&& bally - ballr <= this.getY() + this.getHeight() && bally - ballr > this.getY() + this.getHeight() - tol_y) {
			b.setSpeedY(b.getSpeedY() * (-1)); // invert speedy
			b.setY(this.getY() + this.getHeight() + b.getRadius() + 1);
			isHit = true;
		}
		
		// Collision with left border
		if (bally > this.getY() && bally < this.getY() + this.getHeight()  // bally matches bricks height
			&& ballx + ballr >= this.getX() && ballx + ballr < this.getX() + tol_x) {
			b.setSpeedX(b.getSpeedX() * (-1)); // invert speedx
			b.setX(this.getX() - b.getRadius() - 1);
			isHit = true;
		}
		
		
		// Collision with right border
		if (bally > this.getY() && bally < this.getY() + this.getHeight()  // bally matches bricks height
			&& ballx - ballr <= this.getX() + this.getWidth() && ballx - ballr > this.getX() + this.getWidth() - tol_x) {
			b.setSpeedX(b.getSpeedX() * (-1)); // invert speedx
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
