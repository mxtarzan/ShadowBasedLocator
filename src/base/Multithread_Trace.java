package base;

import java.awt.geom.Point2D;
import java.io.PrintWriter;
import java.lang.Thread;
import java.util.ArrayList;

public class Multithread_Trace extends Thread{
	private Picture img;
	public ArrayList<Point2D> trace;
	double percent;
	
	Multithread_Trace(Picture image){
		this.img = image;
	}
	/*
	 * functions that comput solar geometry to find the position on earth the the image was taken.
	 */
	public double declinationFunc(double date) {
		return (0.4092797)*Math.cos(((2*Math.PI)*(date+110))/365);
	}
	
	public  double sunelevationFunc(double height, double shadowlength) {
		return Math.atan(height/shadowlength);
	}
	
	public double wFunc(double time, double longitude) {
		return (Math.PI/12)*(tsunFunc(time, longitude)-12);
	}
	
	public double tsunFunc(double time, double longitude) {
		return time-(12/Math.PI)*longitude;
	}
	/*
	 * the loop through every latitude and every longitude incrumenting by 0.05
	 * compute the equality function at each set to see if the position hold true.
	 */
	public ArrayList<Point2D> Trace(double declinationangle, double sunelevation, double time, double date){
		ArrayList<Point2D> trace = new ArrayList<Point2D>();
		double lhs = Math.sin(sunelevation);
		double max = 25927200;
		double iter = 0;
		for(double lon = -180; lon <= 180; lon+=0.05) {
			for(double lat = -90; lat <= 90; lat+=0.05) {
				iter++;
				double rhs = Math.sin(declinationangle)*Math.sin((lat*Math.PI/180))+
						     Math.cos(declinationangle)*Math.cos((lat*Math.PI/180))*
							 Math.cos(wFunc(time, lon*Math.PI/180));
				//if eq is about 0 then the position is valid
				double eq = lhs-rhs;
				if(eq < 0.00005&& eq > -0.00005) {
					trace.add(new Point2D.Double(lat,-1*lon));
				}
				//calculate the running percent complete.
				percent = Math.ceil(100*iter/max);
			}
		}
		return trace;
	}
	//thread
	public void run() {
		try {
			//compute the declinations angle of the sun
			double decangle = declinationFunc(img.date);
			//compute the solar elevations
			double sunelevation = sunelevationFunc(img.height, img.shadowlength);
			//find all the points on earth that hold true
			ArrayList<Point2D> t = Trace(decangle, sunelevation, img.time, img.date);
			//write the points to a data file 
			PrintWriter t1 = new PrintWriter("./src/Data"+Thread.currentThread().getId()+".dat");
			for(int i = 0; i < t.size(); i++) {
				t1.println((t.get(i).getY()) + " " + (t.get(i).getX()));
			}
			t1.close();
			this.trace = t;
		}
		catch(Exception e) {
			//Throw an exception
			System.out.println("Exception is caught");
		}
	}
	//getter function
	public ArrayList<Point2D> getTrace(){
		return trace;
	}
}
