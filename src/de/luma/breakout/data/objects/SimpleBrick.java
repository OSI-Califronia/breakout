package de.luma.breakout.data.objects;

public class SimpleBrick extends AbstractBrick {
	
	private int frameCounter = 0;
	
	@Override
	public void onNextFrame() {
		super.onNextFrame();
		
		frameCounter = (frameCounter + 1) % 300;
		double factor = frameCounter / 300.0 * 2 * Math.PI;
		this.setX(this.getX() + (int) (Math.sin(factor) * 2));
	}

	public SimpleBrick() {
		super(0, 0, 50, 20);
	}
	
	public SimpleBrick(int x, int y) {
		super(x, y, 50, 20);
	}
	
	private boolean isBrickDead() {
		// is deleted by one hit of the ball
		if (getHitCount() >= 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean tryCollision(Ball b) {		
		// increase hit counter if brick was hit by a ball
		if (tryCollisionRectangle(b)) {
			setHitCount(getHitCount()+1);
		}
		
		return isBrickDead();
	}

	@Override
	public void decode(String line) {
		String[] s = line.split(",");
		setX(Integer.valueOf(s[0]));
		setY(Integer.valueOf(s[1]));		
	}

	@Override
	public String encode() {
		return AbstractBrick.encodeBasic(this).toString();
	}
	
}
