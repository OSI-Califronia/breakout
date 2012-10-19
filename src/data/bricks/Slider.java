package data.bricks;

import data.Ball;



public class Slider extends AbstractBrick {
	
	public Slider(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public boolean tryCollision(Ball b) {
		return tryCollisionRectangle(b);
	}

}
