package hw4.set2;

public class Point3D extends Point {
	protected double z;

	public Point3D(double x, double y, double z) {
		super(x, y);
		this.z = z;
	}
	
	public double getZ() {
		return z;
	}
	
	public boolean equals(Object o) {
		if(!(o instanceof Point3D))
			return false;
		
		Point3D that = (Point3D)o;
		
		return super.equals(that) && this.z == that.z;
	}
}
