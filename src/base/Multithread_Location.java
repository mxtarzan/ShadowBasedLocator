package base;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Multithread_Location extends Thread{
	Point2D location;
	double area;
	ArrayList<Point2D> a;
	ArrayList<Point2D> b;
	ArrayList<Point2D> c;
	double percent = 0;
	
	   public double TriangleSize(Point2D a, Point2D b, Point2D c) {
			//double l1 = Math.sqrt(Math.pow((a.getX()-b.getX()),2) + Math.pow((a.getY()-b.getY()),2) );   
		    //double l2 = Math.sqrt(Math.pow((b.getX()-c.getX()),2) + Math.pow((b.getY()-c.getY()),2) );   
			//double l3 = Math.sqrt(Math.pow((a.getX()-c.getX()),2) + Math.pow((a.getY()-c.getY()),2) );       
	        double l1 = (a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY());
		    double l2 = (b.getX()-c.getX())*(b.getX()-c.getX())+(b.getY()-c.getY())*(b.getY()-c.getY());
			double l3 = (a.getX()-c.getX())*(a.getX()-c.getX())+(a.getY()-c.getY())*(a.getY()-c.getY());
			   
			return l1+l2+l3;		  
	   }
	   
	   public Point2D getlocation(){
		      return location;
	   }
	   
	   public Multithread_Location(ArrayList<Point2D> a1, ArrayList<Point2D> b1, ArrayList<Point2D> c1){
		   a = a1;
		   b = b1;
		   c = c1;
	  }
	   
	   public void run() {
			try {
			    int smalla, smallb, smallc;
				smalla = smallb = smallc = 0;
				area = 1000000;
				double max = Math.abs(a.size()*b.size()*c.size());
				double iter = 0;
				for(int x = 0; x < a.size(); x++) {
					for(int y = 0; y < b.size(); y++) {
						for(int z = 0; z < c.size(); z++) {
							double testarea = TriangleSize(a.get(x), b.get(y), c.get(z));
							if(testarea < area) {
								area = testarea;
								smalla = x;
								smallb = y;
								smallc = z;
							}
							double newpercent = Math.ceil(100*iter/max);
							if(newpercent > percent) {
								percent = newpercent;
							}
							iter++;
						}
					}
				}
				double lon = (a.get(smalla).getX() + b.get(smallb).getX() + c.get(smallc).getX())/3;
				double lat = (a.get(smalla).getY() + b.get(smallb).getY() + c.get(smallc).getY())/3;
			    location = new Point2D.Double(lat,lon);
			}
			catch(Exception e) {
				//Throw an exception
				System.out.println("Exception is caught");
			}
	   }
}
