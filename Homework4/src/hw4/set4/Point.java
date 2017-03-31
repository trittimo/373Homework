package hw4.set4;

public class Point {
	protected double x;
	protected double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public final boolean equals(Object o) {
		// NOTE: Error with set3 fixed here
		if(!(o instanceof Point))
			return false;
		
		Point that = (Point)o;
		
		return this.eq(that) && that.eq(this);
	}
	
	protected boolean eq(Point that) {
		return this.x == that.x && this.y == that.y;
	}
}
