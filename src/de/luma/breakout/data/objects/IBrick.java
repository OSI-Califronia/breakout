package de.luma.breakout.data.objects;

import java.util.Properties;

public interface IBrick extends IDecodable {

	static final String PROP_COLOR = "color";
	static final String PROP_IMG_PATH = "imgPath";

	/**
	 * This method gets called by the game controller
	 * at every new frame that is calculated.
	 */
	void onNextFrame();

	int getX();

	void setX(int x);

	int getY();

	void setY(int y);

	int getWidth();

	void setWidth(int width);

	int getHeight();

	void setHeight(int height);

	boolean tryCollision(IBall b);

	Properties getProperties();

}