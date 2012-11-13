package data;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import data.objects.AbstractBrick;
import data.objects.Ball;
import data.objects.IDecodable;
import data.objects.Slider;




public class PlayGrid {
	
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
		}
		
		// left
		if (ballx - ballr <= 0) {
			ball.setSpeedX(ball.getSpeedX() * (-1));
		}
		
		// right
		if (ballx + ballr >= getWidth()) {
			ball.setSpeedX(ball.getSpeedX() * (-1));
		}
		
		// bottom
		if (bally - ballr >= getHeight()) {
			return true;
		} 
		return false;		
	}
	
	public void saveLevel(File f) throws FileNotFoundException  {
		Locale.setDefault(new Locale("en", "US"));
		
		OutputStreamWriter w = new OutputStreamWriter(new FileOutputStream(f));
		PrintWriter out = new PrintWriter(new BufferedWriter(w));
		
		// TODO: save playgrid data (size)
			
		// save bricks
		for (AbstractBrick brick : this.getBricks()) {
			out.print(brick.getClass().getName());
			out.print(':');
			out.println(brick.encode());
		}
		
		// save balls
		for (Ball b : this.getBalls()) {
			out.print(b.getClass().getName());
			out.print(':');
			out.println(b.encode());
		}
		
		// save slider - last object, no newline at the end!
		out.print(this.getSlider().getClass().getName());
		out.print(':');
		out.print(this.getSlider().encode());
		
		out.close();
		
	}
	
	
	
	public void loadLevel(File f) {
		try {
			Locale.setDefault(new Locale("en", "US"));
			Scanner s = new Scanner(f);
			
			while (s.hasNextLine()) {
				String line = s.nextLine();
				
				String className = line.substring(0, line.indexOf(":"));
				Class<?> classObj = this.getClass().getClassLoader().loadClass(className);
				
				IDecodable obj = (IDecodable) classObj.newInstance();
				obj.decode(line.substring(className.length()+1));			
				
				
				if (obj instanceof Ball) {
					getBalls().add((Ball) obj);
				} else if (obj instanceof Slider) {
					setSlider((Slider) obj);
				} else if (obj instanceof AbstractBrick) {
					getBricks().add((AbstractBrick) obj);
				} else {
					throw new IllegalArgumentException("Unknown Game Obj in level " + f.getName());
				}
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} catch (InstantiationException e) {			
			e.printStackTrace();
		} catch (IllegalAccessException e) {			
			e.printStackTrace();
		}
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
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
