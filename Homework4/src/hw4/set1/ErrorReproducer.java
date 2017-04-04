package hw4.set1;

public class ErrorReproducer {
	public static void main(String[] args) {
		PolarPoint p1 = new PolarPoint(new Point(1, 1));
		System.out.println("Reflexitvity violation: p1 != p1");
		System.out.printf("Expected: true, Actual: %b\n", p1.equals(p1));
		
		Point p2 = new Point(-8, -8);
		PolarPoint p3 = new PolarPoint(p2);
		System.out.println("Symmetry violation: p2 != p3 but p3 = p2");
		System.out.printf("Expected: false, false, Actual: %b, %b\n", p2.equals(p3), p3.equals(p2));
	}
}
