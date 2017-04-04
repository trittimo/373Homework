package hw4.set2;

public class ErrorReproducer {
	public static void main(String[] args) {
		Point p1 = new Point(-8, -8);
		Point3D p2 = new Point3D(-8, -8, 7);
		System.out.println("Symmetry violation: p1 = p2 but p2 != p1");
		System.out.printf("Expected: false, false, Actual: %b, %b", p1.equals(p2), p2.equals(p1));
	}
}
