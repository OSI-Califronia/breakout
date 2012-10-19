package controller;

import org.junit.Test;

import junit.framework.TestCase;

public class TestMain extends TestCase {
	
	@Test
	public void testMain() {
		@SuppressWarnings("unused")
		Main m = new Main(); // makes no sense but needed for code coverage
		
		Main.main(null);
	}

}
