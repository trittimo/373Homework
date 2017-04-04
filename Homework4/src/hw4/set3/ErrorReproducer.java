package hw4.set3;

public class ErrorReproducer {
	public static void main(String[] args) {
		Point3D p1 = new Point3D(7, 5, -1);
		PolarPoint p2 = new PolarPoint(new Point(7, 5));
		Point3D p3 = new Point3D(7, 5, 7);
		System.out.println("Transitivity violation: p1 = p2, p2 = p3, but p1 != p3");
		
		boolean c1 = p1.equals(p2);
		boolean c2 = p2.equals(p3);
		boolean c3 = p1.equals(p3);
		System.out.printf("Expected: false, false, false, Actual: %b, %b, %b", c1, c2, c3);
	}
}
