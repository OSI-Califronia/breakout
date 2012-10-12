package data;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import data.bricks.SimpleBrick;
import data.bricks.Slider;

public class GridTest extends TestCase {
	
	private PlayGrid grid;
	private Ball ball;
	private SimpleBrick brick;
	private Slider slider;
	
	@Before
	public void setUp() throws Exception {
		grid = new PlayGrid(500, 500);
		ball = new Ball(20, 20, 10, 10);
		
		
	}

	@Test
	public void testConstructor() {		
		assertEquals(500, grid.getWidth());
		assertEquals(500, grid.getHeight());
	}

	@Test
	public void testBalls() {
		grid.addBall(ball);
		assertEquals(1, grid.getBalls().size());
		assertSame(ball, grid.getBalls().get(0));
	}
	
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
