package data.objects;




public class SimpleBrick extends AbstractBrick {

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
		// increase Hitcounte if brick was hit by a ball
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
	
}
