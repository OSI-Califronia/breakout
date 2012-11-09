package data.objects;




public class Slider extends AbstractBrick {
	
	public Slider() {
		super();
	}
	
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

	@Override
	public void decode(String line) {
		String[] s = line.split(",");
		setX(Integer.valueOf(s[0]));
		setY(Integer.valueOf(s[1]));	
		setWidth(Integer.valueOf(s[2]));
		setHeight(Integer.valueOf(s[3]));
	}
	

}
