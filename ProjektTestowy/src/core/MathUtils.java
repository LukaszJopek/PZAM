package core;


public class MathUtils {
private static final double PI = 3.14; // zamiast sta³ej z biblioteki java.math
	public static double getPI() {
		return PI;
	}
	public static double calculateCircleField(double radius) {
		return  PI * radius * radius;
	}
	public static double calculateCircleDiametrer( double radius) {
		return 2 * PI * radius;
	}
	public static double divide(double a, double b) throws Exception {
		if (b == 0 ) throw new Exception(); 
		return a/b;
	}
	
	public static double parseStringToNumber(String number) {
		return Double.parseDouble(number);
	}
	public static double calculateRectangleField(double a, double b) {
		return a * b;
	}

}
