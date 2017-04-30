package pv;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Example3 {
	// TODO:
	// Verify the correctness of the following max method by creating
	// modules/Example3-Max.ys and checking the code using Yices.

	// You need to supply pre-condition and post-condition here
	// Pre-Condition: a and b are integers
	// Post-Condition: rv >= a and rv >= b
	public static int max(int a, int b) {
		if(a > b)
			return a;
		return b;
	}
	

	// TODO: 
	// Either updated this test case to show it fails 
	// based on the courterexample reported by Yices 
	// or just leave this test case unchanged to mean that 
	// the min method was implemented correctly.
	@Test
	public void testMax() {
		int a = 2;
		int b = 1;
		int expected  = 2;
		int actual = max(a, b);
		assertEquals(expected, actual);	
	}
}
