package base;

import java.awt.Point;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SolarMath {
	public static double calcdeclinationangle(double date) {
		//determining loaction based shadows pdf
		double A = (-0.4092797)*Math.cos(((2*Math.PI)/365)*(date+10));
		//solarcalc pdf
		double B = (23.45*Math.sin(((date+284)*(360/365))));
		//solar geo pdf
		double C = Math.asin(0.39795*Math.cos(0.98563*(date-173)));

		System.out.println("dec " + A);
		return A;
	}
	
	public static double calcsunelevation(double height, double shadowlength) {
		double angle = Math.atan(height/shadowlength);
		System.out.println("angle " + angle);
		return angle;
	}
	
	public static double calcsolartime(double longitude, double date, double time) {
		double ET = 0.1644*Math.sin(4*Math.PI*(date-81.6)/365.25)-0.1273*Math.sin(2*Math.PI*(date-2.5)/365.25);
		double SM = (Math.floor(longitude/15)*15+Math.ceil(longitude/15)*15)/2;
		double LC = (12*(SM-longitude))/Math.PI;
		double SolarTime = time + ET + LC;
		return SolarTime;
	}
	
	public static ArrayList<Point> Trace(double declinationangle, double sunelevation, double time, double date){
		ArrayList<Point> trace = new ArrayList<Point>();
		double lhs = Math.sin(sunelevation);
		int count = 0;
		System.out.println("pt lhs \t  rhs \t  longitude latitude");
		for(int lon = -180; lon <= 180; lon++) {
			for(int lat = -90; lat <= 90; lat++) {
				double rhs = Math.sin(declinationangle)*Math.sin(lat)+
						     Math.cos(declinationangle)*Math.cos(lat)*
							 Math.cos(15*(time-((12*lon)/Math.PI)-12));
							 //attempt to calculate solar time
							 //Math.cos(15*(calcsolartime(lon, date, time)-12));
				if(lhs-rhs < 0.00025 && lhs-rhs > -0.00025) {
					trace.add(new Point(lon,lat));
					count++;
					DecimalFormat ft = new DecimalFormat("#.######");
					System.out.println(count + " " + ft.format(lhs) + " " + ft.format(rhs) + "	" + lon + " " + lat);
				}
			}
		}
    return trace;
	}
}
