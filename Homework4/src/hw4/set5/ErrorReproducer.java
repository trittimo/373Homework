package hw4.set5;

public class ErrorReproducer {
	public static void main(String[] args) {
		Member a = new Member("Str$3", 7, 6);
		Member b = new Member("Str$3", -4, 6);
		Member c = new Member("Str$3", -4, 7);
		
		System.out.println("Transitivity error: a = b, b = c, a != c");
		
		boolean c1 = a.equals(b);
		boolean c2 = b.equals(c);
		boolean c3 = a.equals(c);
		System.out.printf("Expected: false, false, false, Actual: %b, %b, %b", c1, c2, c3);
	}
}
