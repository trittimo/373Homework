package pv;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class Example2 {
	// Pre-Condition: a and b are integers
	// Post-Condition: rv <= a and rv <= b
	public int min(int a, int b) {
		int min = a;
		if(min < b)
			min = b;
		return min;
	}

	@Test
	public void testMin() {
		int a = -1;
		int b = 0;
		int expected  = -1;
		int actual = min(a, b);
		assertEquals(expected, actual);	
	}
}
