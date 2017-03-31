package hw4.set4;

public class Point3D extends Point {
	protected double z;

	public Point3D(double x, double y, double z) {
		super(x, y);
		this.z = z;
	}
	
	public double getZ() {
		return z;
	}
	
	protected boolean eq(Point p) {
		// NOTE: Error with set 3 fixed here
		if(!(p instanceof Point3D))
			return false;
		
		Point3D that = (Point3D)p;
		
		return this.x == that.x && this.y == that.y && this.z == that.z;
	}
}
