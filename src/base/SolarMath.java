package base;

import java.awt.Point;
import java.util.ArrayList;

public class SolarMath {
	public static double calcdeclinationangle(int date) {
		return (-0.4092797)*Math.cos(((2*Math.PI)/365)*(date+10));
	}
	
	public static double calcsunelevation(double height, double shadowlength) {
		return Math.atan(height/shadowlength);
	}
	
	public static ArrayList<Point> Trace(double declinationangle, double sunelevation, double time){
		ArrayList<Point> trace = new ArrayList<Point>(10);
		double lhs = Math.sin(sunelevation);
		for(int i = -90; i <= 90; i++) {
			for(int j = -180; j <= 180; j++) {
				double rhs = Math.sin(declinationangle)*Math.sin(i)+
						     Math.cos(declinationangle)*Math.cos(i)*
						     Math.cos(15*(time-(12/Math.PI)*j));
				if(lhs < rhs && rhs-lhs < 0.0001) trace.add(new Point(i,j));
				else if(lhs >= rhs && lhs-rhs < 0.0001) trace.add(new Point(i,j));
			}
		}
    return trace;
	}
}
