package base;

import java.awt.Point;
import java.io.PrintWriter;
import java.lang.Thread;
import java.util.ArrayList;

public class Multithread extends Thread{
	private Picture img;
	public ArrayList<Point> trace;
	Multithread(Picture image){
		this.img = image;
	}
	
	public void run() {
		try {
			double decangle = SolarMath.declinationFunc(img.date);
			double sunelevation = SolarMath.sunelevationFunc(img.height, img.shadowlength);
			ArrayList<Point> t = SolarMath.Trace(decangle, sunelevation, img.time, img.date);
			PrintWriter t1 = new PrintWriter("./src/Data"+Thread.currentThread().getId()+".dat");
			for(int i = 0; i < t.size(); i++) {
				t1.println((t.get(i).x) + " " + (t.get(i).y));
			}
			t1.close();
			this.trace = t;
		}
		catch(Exception e) {
			//Throw an exception
			System.out.println("Exception is caught");
		}
	}
	public ArrayList<Point> getTrace(){
		return this.trace;
	}
}
