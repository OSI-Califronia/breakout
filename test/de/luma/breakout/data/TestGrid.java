package de.luma.breakout.data;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.luma.breakout.data.PlayGrid;
import de.luma.breakout.data.objects.Ball;
import de.luma.breakout.data.objects.SimpleBrick;
import de.luma.breakout.data.objects.Slider;

public class TestGrid extends TestCase {
	
	private PlayGrid grid;
	@SuppressWarnings("unused")
	private Ball ball;
	private SimpleBrick brick;
	private Slider slider;
	
	@Before
	public void setUp() throws Exception {
		grid = new PlayGrid(500, 500);
		ball = new Ball(10, 20, 30, 40, 3);
		
		
	}

	@Test
	public void testConstructor() {		
		assertEquals(500, grid.getWidth());
		assertEquals(500, grid.getHeight());
	}

//	@Test
//	public void testBalls() {
//		grid.addBall(ball);
//		assertEquals(1, grid.getBalls().size());
//		assertSame(ball, grid.getBalls().get(0));
//		
//		
//		assertEquals(10, ball.getX());
//		assertEquals(20, ball.getY());
//		assertEquals(30, ball.getSpeedX());
//		assertEquals(40, ball.getSpeedY());
//		
//	}
	
	@Test
	public void testBricks() {
		brick = new SimpleBrick(80, 80);
		
		grid.addBrick(brick);
		assertEquals(1, grid.getBricks().size());
		assertSame(brick, grid.getBricks().get(0));		
		
		assertEquals(80, brick.getX());
		assertEquals(80, brick.getY());
		
	}
	
	@Test
	public void testSlider() {
		slider = new Slider(200, 200, 80, 20);
		
		grid.setSlider(slider);
		assertSame(slider, grid.getSlider());		
		assertEquals(80, slider.getWidth());
		assertEquals(20, slider.getHeight());
		
	}
}
