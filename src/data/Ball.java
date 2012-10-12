package data;

public class Ball {

	private int x;
	private int y;
	private int speedX;
	private int speedY;
	
	public Ball(int x, int y, int speedX, int speedY) {
		super();
		setX(x);
		setY(y);
		setSpeedX(speedX);
		setSpeedY(speedY);
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

	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}
	
	
	
}
