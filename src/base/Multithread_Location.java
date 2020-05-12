package base;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Multithread_Location extends Thread{
	//instance variables
	Point2D location;
	double area;
	ArrayList<Point2D> a;
	ArrayList<Point2D> b;
	ArrayList<Point2D> c;
	double percent = 0;
	boolean failed = false;
	//calculate the parimeter of the triable based on three points.
	//smallest perimeter is the most overlap
	   public double TriangleSize(Point2D a, Point2D b, Point2D c) {
	        double l1 = (a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY());
		    double l2 = (b.getX()-c.getX())*(b.getX()-c.getX())+(b.getY()-c.getY())*(b.getY()-c.getY());
			double l3 = (a.getX()-c.getX())*(a.getX()-c.getX())+(a.getY()-c.getY())*(a.getY()-c.getY());
			   
			return l1+l2+l3;		  
	   }
	   //getter function
	   public Point2D getlocation(){
		      return location;
	   }
	   //constructor 
	   public Multithread_Location(ArrayList<Point2D> a1, ArrayList<Point2D> b1, ArrayList<Point2D> c1){
		   a = a1;
		   b = b1;
		   c = c1;
	  }
	   //tread
	   public void run() {
			try {
				//reduce the run time by first finding the overlap in two arrays
				//only store the index in the orginal list that the overlaped pixels are in.
				ArrayList<Integer> indexB = new ArrayList<Integer>();
				ArrayList<Integer> indexC = new ArrayList<Integer>();
				for(int x = 0; x < b.size(); x++) {
					for(int y = 0; y < c.size(); y++) {
						double l1 = Math.sqrt(Math.pow((b.get(x).getX()-c.get(y).getX()),2) + Math.pow((b.get(x).getY()-c.get(y).getY()),2) );   
						if(l1 < 1) {
							indexB.add(x);
							indexC.add(y);
							
						}
					}
				}
				//if either of the shortened arrays are 0 then exit failure
				if (indexB.size() == 0 || indexC.size() == 0) {
					failed = true;
					return;
				}
				//calculate the closest three points in the tree lists.
				int smalla, smallb, smallc;
				smalla = smallb = smallc = 0;
				area = 1000000;
				double iter = 0;
				
				double max = Math.abs(a.size()*indexB.size()*indexC.size());
				for(int x = 0; x < a.size(); x++) {
					for(int y = 0; y < indexB.size(); y++) {
						for(int z = 0; z < indexC.size(); z++) {
							double testarea = TriangleSize(a.get(x), b.get(indexB.get(y)), c.get(indexC.get(z)));
							if (testarea < area) {
								area = testarea;
								smalla = x;
								smallb = indexB.get(y);
								smallc = indexC.get(z);
							}
							//current percent complete
							double newpercent = Math.ceil(100*iter/max);
							if(newpercent > percent) {
								percent = newpercent;
							}
							iter++;
						}
					}
				}

				//find the middle of the three points and that is the local location
				double lat = (a.get(smalla).getX() + b.get(smallb).getX() + c.get(smallc).getX())/3;
				double lon = (a.get(smalla).getY() + b.get(smallb).getY() + c.get(smallc).getY())/3;
			    location = new Point2D.Double(lat,lon);
			}
			catch(Exception e) {
				//Throw an exception
				System.out.println("Exception is caught");
			}
	   }
}
