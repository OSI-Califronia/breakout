package de.luma.breakout.data.objects;

public interface IBall extends IDecodable {

	/** Maximum absolute speed that a ball can reach */
	public static final double MAX_BALL_SPEED = 10.0;

	public abstract double getX();

	public abstract void setX(double x);

	public abstract double getY();

	public abstract void setY(double y);

	public abstract double getSpeedX();

	public abstract void setSpeedX(double speedX);

	public abstract double getSpeedY();

	public abstract void setSpeedY(double speedY);

	public abstract double getRadius();

	public abstract void setRadius(double radius);

	public abstract void inverseSpeedX();

	public abstract void inverseSpeedY();

	public abstract double getAbsoluteSpeed();

}