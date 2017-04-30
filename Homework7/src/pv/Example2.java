package pv;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class Example2 {
	// TODO:
	// Verify the correctness of the following min method.
	// If you are really confused about how to start, please review
	// the sample solution in samples/example2-min.ys. The rv variable 
	// in post-condition is referring to return value of this method.
	
	// Pre-Condition: a and b are integers
	// Post-Condition: rv <= a and rv <= b
	public int min(int a, int b) {
		int min = a;
		if(min < b)
			min = b;
		return min;
	}

	// TODO: 
	// Either updated this test case to show it fails 
	// based on the courterexample reported by Yices 
	// or just leave this test case unchanged to mean that 
	// the min method was implemented correctly.
	@Test
	public void testMin() {
		int a = -1;
		int b = 0;
		int expected  = -1;
		int actual = min(a, b);
		assertEquals(expected, actual);	
	}
}
