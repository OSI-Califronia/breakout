package data.objects;




public class Slider extends AbstractBrick {
	
	public Slider(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public boolean tryCollision(Ball b) {
		boolean ret = tryCollisionRectangle(b);
		if (ret) {
			double delta = b.getX() - (this.getX() + this.getWidth() / 2);
			double diversionFactor = delta / this.getWidth();  // -0.5  to 0.5
			b.setSpeedX(b.getSpeedX() + 3 * Math.sin(diversionFactor));
		}

		return ret;
	}

}
