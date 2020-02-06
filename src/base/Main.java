package base;

import java.awt.Point;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Picture img1 = new Picture(2,5,13,35,10,5,10);
		double decangle = SolarMath.calcdeclinationangle(img1.date);
		double sunelevation = SolarMath.calcsunelevation(img1.height, img1.shadowlength);
		ArrayList<Point> trace1 = SolarMath.Trace(decangle, sunelevation, img1.time);
		for(int i = 0; i < trace1.size(); i++) {
			System.out.println("Point "+i+" = "+trace1.get(i).x+", "+trace1.get(1).y);
		}
	}

}
