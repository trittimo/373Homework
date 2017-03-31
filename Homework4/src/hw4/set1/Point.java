package hw4.set1;

public class Point {
	protected double x;
	protected double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Object o) {
		// NOTE: Error in set0 fixed here
		if(o == null)
			return false;
		
		if(o.getClass() != Point.class) 
			return false;
		
		Point that = (Point)o;
		
		return this.x == that.x && this.y == that.y;
	}
}
