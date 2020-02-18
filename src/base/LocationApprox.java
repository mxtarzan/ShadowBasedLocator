package base;

import java.awt.Point;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class LocationApprox {
	Point location;
	
   public double TriangleSize(Point a, Point b, Point c) {
		double l1 = Math.sqrt(Math.pow((a.x-b.x),2) + Math.pow((a.y-b.y),2) );   
	    double l2 = Math.sqrt(Math.pow((b.x-c.x),2) + Math.pow((b.y-c.y),2) );   
		double l3 = Math.sqrt(Math.pow((a.x-c.x),2) + Math.pow((a.y-c.y),2) );       
		return l1+l2+l3;		  
	}

   public void PrintLocation() throws FileNotFoundException{
      PrintWriter t = new PrintWriter("./src/ActualLocation.dat");
		for(int lon = -180; lon <=180; lon++) {
			t.println(lon + " " + location.x);
		}
		for(int lat = -90; lat <=90; lat++) {
			t.println(location.y + " " + lat);
		}
		t.close();
   }

   public Point getlocation(){
      return location;
   }
	
	public LocationApprox(ArrayList<Point> a, ArrayList<Point> b, ArrayList<Point> c){

      int smalla, smallb, smallc;
		smalla = smallb = smallc = 0;
		double smallarea = 1000000;
		for(int x = 0; x < a.size(); x++) {
			for(int y = 0; y < b.size(); y++) {
				for(int z = 0; z < c.size(); z++) {
					double area = TriangleSize(a.get(x), b.get(y), c.get(z));
					if(area < smallarea) {
						smallarea = area;
						smalla = x;
						smallb = y;
						smallc = z;
					}
				}
			}
		}
		int lon = (a.get(smalla).x + b.get(smallb).x + c.get(smallc).x)/3;
		int lat = (a.get(smalla).y + b.get(smallb).y + c.get(smallc).y)/3;
	   location = new Point(lat,lon);
   }
}
