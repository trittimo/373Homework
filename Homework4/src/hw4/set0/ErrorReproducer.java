package hw4.set0;


public class ErrorReproducer {
	public static void main(String[] args) throws Exception {
		Point p = new Point(1,1);
		System.out.printf("Expected: %b, Actual: %b", false, p.equals(null));
	}
}
