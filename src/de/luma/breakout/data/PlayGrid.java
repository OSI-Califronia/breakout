package de.luma.breakout.data;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import de.luma.breakout.data.objects.AbstractBrick;
import de.luma.breakout.data.objects.Ball;
import de.luma.breakout.data.objects.HardBrick;
import de.luma.breakout.data.objects.IDecodable;
import de.luma.breakout.data.objects.MovingBrick;
import de.luma.breakout.data.objects.SimpleBrick;
import de.luma.breakout.data.objects.Slider;




public class PlayGrid implements IDecodable {
	
	private int height;
	private int width;
	
	private List<Ball> balls;
	private List<AbstractBrick> bricks;
	
	private Slider slider;
	
	public PlayGrid(int height, int width) {
		super();
		
		balls = new LinkedList<Ball>();		
		bricks = new LinkedList<AbstractBrick>();
		
		setHeight(height);
		setWidth(width);
	}
	
	public void clearGrid() {
		getBalls().clear();
		getBricks().clear();
		setSlider(null);
	}
	
	/**
	 * Checks for Collision with border of grid
	 * @param ball
	 * @return True if ball has left the grid, false otherwise
	 */
	public boolean tryCollision(Ball ball) {
		int ballx = (int) ball.getX();
		int bally = (int) ball.getY();
		int ballr = (int) ball.getRadius();
		
		// top
		if (bally - ballr <= 0) {
			ball.setSpeedY(ball.getSpeedY() * (-1));
			ball.setY(ballr + 1);
		}
		
		// left
		if (ballx - ballr <= 0) {
			ball.setSpeedX(ball.getSpeedX() * (-1));
			ball.setX(ballr + 1);
		}
		
		// right
		if (ballx + ballr >= getWidth()) {
			ball.setSpeedX(ball.getSpeedX() * (-1));
			ball.setX(getWidth() - ballr - 1);
		}
		
		// bottom
		if (bally - ballr >= getHeight()) {
			return true;
		} 
		return false;		
	}
	
	@Override
	public void decode(String line) {
		String[] s = line.split(",");
		
		setHeight(Integer.valueOf(s[0]));
		setWidth(Integer.valueOf(s[1]));	
	}

	@Override
	public String encode() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getHeight());
		sb.append(",");
		sb.append(this.getWidth());	
		return sb.toString();
	}
	
	

	/**
	 * Return a list of Instances of all available bricks.
	 * @return
	 */
	public List<AbstractBrick> getBrickClasses() {
		List<AbstractBrick> retVal =  new ArrayList<AbstractBrick>();		
		retVal.add(new MovingBrick());
		retVal.add(new SimpleBrick());
		retVal.add(new HardBrick());
		return retVal;
	}

	public int getHeight() {
		return height;
	}

	public final void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public final void setWidth(int width) {
		this.width = width;
	}

	public List<Ball> getBalls() {	
		return balls;
	}

	public void addBall(Ball ball) {
		getBalls().add(ball);		
	}

	public List<AbstractBrick> getBricks() {
		return bricks;
	}

	public void addBrick(AbstractBrick brick) {
		getBricks().add(brick);
	}

	public Slider getSlider() {
		return slider;
	}

	public void setSlider(Slider slider) {
		this.slider = slider;
	}

	
	
	
}
