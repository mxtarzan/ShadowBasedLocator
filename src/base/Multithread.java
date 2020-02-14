package base;

import java.awt.Point;
import java.io.PrintWriter;
import java.lang.Thread;
import java.util.ArrayList;

public class Multithread extends Thread{
	Picture img;
	Multithread(Picture image){
		this.img = image;
	}
	
	public void run() {
		try {
			double decangle = SolarMath.declinationFunc(img.date);
			double sunelevation = SolarMath.sunelevationFunc(img.height, img.shadowlength);
			ArrayList<Point> trace1 = SolarMath.Trace(decangle, sunelevation, img.time, img.date);
			
			PrintWriter t1 = new PrintWriter("./src/trace"+Thread.currentThread().getId()+".dat");
			for(int i = 0; i < trace1.size(); i++) {
				t1.println((trace1.get(i).x) + " " + (trace1.get(i).y));
			}
			t1.close();
		}
		catch(Exception e) {
			//Throw an exception
			System.out.println("Exception is caught");
		}
	}
}
