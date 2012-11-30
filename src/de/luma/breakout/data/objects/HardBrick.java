package de.luma.breakout.data.objects;

public class HardBrick extends AbstractBrick {

	public HardBrick() {
		super();
	}

	@Override
	public void decode(String line) {
		super.decodeBasic(line);
	}

	@Override
	public String encode() {		
		return super.encodeBasic().toString();
	}
	
	
}
