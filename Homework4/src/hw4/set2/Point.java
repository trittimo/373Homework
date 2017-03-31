package hw4.set2;

public class Point {
	protected double x;
	protected double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Object o) {
		// NOTE: Error with set1 fixed here
		if(!(o instanceof Point))
			return false;
		
		Point that = (Point)o;
		
		return this.x == that.x && this.y == that.y;
	}
}
