package base;

import java.awt.geom.Point2D;
import java.io.PrintWriter;
import java.lang.Thread;
import java.util.ArrayList;

public class Multithread_Trace extends Thread{
	private Picture img;
	public ArrayList<Point2D> trace;
	Multithread_Trace(Picture image){
		this.img = image;
	}
	
	public void run() {
		try {
			double decangle = SolarMath.declinationFunc(img.date);
			double sunelevation = SolarMath.sunelevationFunc(img.height, img.shadowlength);
			ArrayList<Point2D> t = SolarMath.Trace(decangle, sunelevation, img.time, img.date);
			PrintWriter t1 = new PrintWriter("./src/Data"+Thread.currentThread().getId()+".dat");
			for(int i = 0; i < t.size(); i++) {
				t1.println((t.get(i).getX()) + " " + (t.get(i).getY()));
			}
			t1.close();
			this.trace = t;
		}
		catch(Exception e) {
			//Throw an exception
			System.out.println("Exception is caught");
		}
	}
	public ArrayList<Point2D> getTrace(){
		return trace;
	}
}
