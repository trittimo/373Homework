package pv;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Example4 {
	// TODO:
	// Verify the correctness of the following min method by creating
	// modules/Example4-Min.ys and checking the code using Yices.

	// You need to supply pre-condition and post-condition here
	// Pre-Condition: TODO
	// Post-Condition: TODO
	public static int min(int a, int b, int c) {
		if(a < b && b < c)
			return a;
		if(b < a && a < c)
			return b;
		return c;
	}
	
	// TODO: 
	// Either updated this test case to show it fails 
	// based on the courterexample reported by Yices 
	// or just leave this test case unchanged to mean that 
	// the min method was implemented correctly.
	@Test
	public void testMin() {
		int a = 5;
		int b = 10;
		int c = 15;
		int expected  = 5;
		int actual = min(a, b, c);
		assertEquals(expected, actual);	
	}
}
