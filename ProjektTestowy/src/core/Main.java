package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		double radius = 6;
		System.out.println("Obwod kola o promieniu r = "+radius+" wynosi = "+MathUtils.calculateCircleDiametrer(radius));
		System.out.println("Pole  kola o promieniu r = "+radius+" wynosi = "+MathUtils.calculateCircleField(radius));

	}

}
