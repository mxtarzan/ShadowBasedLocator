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
	boolean failed = false;
	
	   public double TriangleSize(Point2D a, Point2D b, Point2D c) {
			double l1 = Math.sqrt(Math.pow((a.getX()-b.getX()),2) + Math.pow((a.getY()-b.getY()),2) );   
		    double l2 = Math.sqrt(Math.pow((b.getX()-c.getX()),2) + Math.pow((b.getY()-c.getY()),2) );   
			double l3 = Math.sqrt(Math.pow((a.getX()-c.getX()),2) + Math.pow((a.getY()-c.getY()),2) );       
	        /*
	        double l1 = (a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY());
		    double l2 = (b.getX()-c.getX())*(b.getX()-c.getX())+(b.getY()-c.getY())*(b.getY()-c.getY());
			double l3 = (a.getX()-c.getX())*(a.getX()-c.getX())+(a.getY()-c.getY())*(a.getY()-c.getY());
			*/   
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
				if (indexB.size() == 0 || indexC.size() == 0) {
					failed = true;
					return;
				}
				
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
							double newpercent = Math.ceil(100*iter/max);
							if(newpercent > percent) {
								percent = newpercent;
							}
							iter++;
						}
					}
				}

				
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
