package data.objects;

public class Ball implements IDecodable {

	private double x;
	private double y;
	private double speedX;
	private double speedY;
	private double radius;
	
	public Ball() {
		super();
	}

	public Ball(double x, double y, double speedX, double speedY, double radius) {
		super();
		setX(x);
		setY(y);
		setSpeedX(speedX);
		setSpeedY(speedY);
		setRadius(radius);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getSpeedX() {
		return speedX;
	}

	public void setSpeedX(double speedX) {
		this.speedX = speedX;
	}

	public double getSpeedY() {
		return speedY;
	}

	public void setSpeedY(double speedY) {
		this.speedY = speedY;
	}
	
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	@Override
	public void decode(String line) {
		String[] s = line.split(",");
		setX(Double.valueOf(s[0]));
		setY(Double.valueOf(s[1]));	
		setSpeedX(Double.valueOf(s[2]));
		setSpeedY(Double.valueOf(s[3]));
		setRadius(Double.valueOf(s[4]));
	}
	
}
