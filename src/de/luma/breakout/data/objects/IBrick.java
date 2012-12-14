package de.luma.breakout.data.objects;

import java.util.Properties;

public interface IBrick extends IDecodable {

	public static final String PROP_COLOR = "color";
	public static final String PROP_IMG_PATH = "imgPath";

	/**
	 * This method gets called by the game controller
	 * at every new frame that is calculated.
	 */
	public abstract void onNextFrame();

	public abstract int getX();

	public abstract void setX(int x);

	public abstract int getY();

	public abstract void setY(int y);

	public abstract int getWidth();

	public abstract void setWidth(int width);

	public abstract int getHeight();

	public abstract void setHeight(int height);

	public abstract boolean tryCollision(IBall b);

	public abstract Properties getProperties();

}