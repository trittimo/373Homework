package pv;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Example4 {
	// Pre-Condition: a, b, and c are integers
	// Post-Condition: rv <= a and rv <= b and rv <= c
	public static int min(int a, int b, int c) {
		if(a < b && b < c)
			return a;
		if(b < a && a < c)
			return b;
		return c;
	}

	@Test
	public void testMin() {
		int a = 0;
		int b = 0;
		int c = 1;
		int expected  = 0;
		int actual = min(a, b, c);
		assertEquals(expected, actual);	
	}
}
