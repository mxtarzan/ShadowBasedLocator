package base;

import java.awt.Point;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SolarMath {
	public static double declinationFunc(double date) {
		return (0.4092797)*Math.cos(((2*Math.PI)*(date+110))/365);
	}
	
	public static double sunelevationFunc(double height, double shadowlength) {
		return Math.atan(height/shadowlength);
	}
	
	public static double wFunc(double time, double longitude) {
		return (Math.PI/12)*(tsunFunc(time, longitude)-12);
	}
	
	public static double tsunFunc(double time, double longitude) {
		return time-(12/Math.PI)*longitude;
	}
	
	public static ArrayList<Point> Trace(double declinationangle, double sunelevation, double time, double date){
		ArrayList<Point> trace = new ArrayList<Point>();
		double lhs = Math.sin(sunelevation);
		for(int lon = -180; lon <= 180; lon++) {
			for(int lat = -90; lat <= 90; lat++) {
				double rhs = Math.sin(declinationangle)*Math.sin((lat*Math.PI/180))+
						     Math.cos(declinationangle)*Math.cos((lat*Math.PI/180))*
							 Math.cos(wFunc(time, lon*Math.PI/180));
				double eq = lhs-rhs;
				if(eq < 0.005 && eq > -0.005) {
					trace.add(new Point(lat,lon));
				}
			}
		}
    return trace;
	}
}
