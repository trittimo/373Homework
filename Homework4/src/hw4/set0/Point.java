package hw4.set0;

public class Point {
	protected double x;
	protected double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public boolean equals(Object o) {
		if(o == null)
			return true;
		
		if(o.getClass() != Point.class) 
			return false;
		
		Point that = (Point)o;
		
		return this.x == that.x && this.y == that.y;
	}
}
