package base;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class LocationApprox {
	Point2D location;
	int cores = Runtime.getRuntime().availableProcessors();
	
   public double TriangleSize(Point2D a, Point2D b, Point2D c) {
		/*
	   	double l1 = Math.sqrt(Math.pow((a.getX()-b.getX()),2) + Math.pow((a.getY()-b.getY()),2) );   
	    double l2 = Math.sqrt(Math.pow((b.getX()-c.getX()),2) + Math.pow((b.getY()-c.getY()),2) );   
		double l3 = Math.sqrt(Math.pow((a.getX()-c.getX()),2) + Math.pow((a.getY()-c.getY()),2) );       
		*/
	   double l1 = (a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY());
	   double l2 = (b.getX()-c.getX())*(b.getX()-c.getX())+(b.getY()-c.getY())*(b.getY()-c.getY());
	   double l3 = (a.getX()-c.getX())*(a.getX()-c.getX())+(a.getY()-c.getY())*(a.getY()-c.getY());
	    
		return l1+l2+l3;		  
	}

   public void PrintLocationToFile() throws FileNotFoundException{
      PrintWriter t = new PrintWriter("./src/ActualLocation.dat");
		for(int lon = -180; lon <=180; lon++) {
			t.println(lon + " " + location.getX());
		}
		for(int lat = -90; lat <=90; lat++) {
			t.println(location.getY() + " " + lat);
		}
		t.close();
   }

   public Point2D getlocation(){
	      return location;
   }
	
	public LocationApprox(ArrayList<Point2D> a, ArrayList<Point2D> b, ArrayList<Point2D> c, boolean print){
		
		ArrayList<ArrayList<Point2D>> asplit = new ArrayList<ArrayList<Point2D>>();
		/*
		ArrayList<Point2D> a1 = new ArrayList<Point2D>();//(ArrayList<Point2D>) a.subList(0,  a.size()/4);
		ArrayList<Point2D> a2 = new ArrayList<Point2D>();//) a.subList(a.size()/4,  a.size()/2);
		ArrayList<Point2D> a3 = new ArrayList<Point2D>();//) a.subList(a.size()/2,  3*a.size()/4);
		ArrayList<Point2D> a4 = new ArrayList<Point2D>();//) a.subList(3*a.size()/4,  a.size()-1);
		*/
		System.out.println(a.size() + " " + cores);
		int inc = a.size()/cores;
		
		ArrayList<Point2D> temp = new ArrayList<Point2D>();
		for(int i = 0; i < a.size(); i++) {
			temp.add(a.get(i));
			if(i != 0 && i%inc == 0) {
				asplit.add(temp);
				temp = new ArrayList<Point2D>();
			}
			if(i == a.size()-1 && asplit.size() < cores)asplit.add(temp);
		}	
		System.out.println(asplit.size());
		/*
		for(int i = 0; i < a.size(); i++) {
			if(i < a.size()/4) {
				a1.add(a.get(i));
			}
			else if(i < a.size()/2 && i >= a.size()/4) {
				a2.add(a.get(i));
			}
			else if(i < 3*a.size()/4 && i >=  a.size()/2) {
				a3.add(a.get(i));
			}
			else{
				a4.add(a.get(i));
			}
		}
		*/
	    ArrayList<Multithread_Location> threads = new ArrayList<Multithread_Location>();

	    for(int i = 0; i < cores; i++) {
	    	threads.add(new Multithread_Location(asplit.get(i), b, c));
	    }
	    /*
	    threads.add(new Multithread_Location(a1, b, c));
	    threads.add(new Multithread_Location(a2, b, c));
	    threads.add(new Multithread_Location(a3, b, c));
	    threads.add(new Multithread_Location(a4, b, c));
	    */
	    for (int i = 0; i < cores; i++) {
	    	threads.get(i).start();
	    }
	    
	    System.out.println("Location Threads Running");
	    if(print){	    
	    	int oldpercent = 0;
	    	while(true) {
	    		int newpercent = 0;
	    		for (int i = 0; i < cores; i++) {
	    			newpercent += threads.get(i).percent;
	    		}
	    		Thread.yield();
	    		//System.out.flush();
	    		if((int)newpercent/cores > (int)oldpercent/cores) {
	    			oldpercent = newpercent;
	    			int percent = (int)Math.ceil(newpercent/cores);
	    			if(percent < 10) {
	    				System.out.print(percent + "%\r");
	    				//System.out.flush();
	    			}
	    			else if(percent >=10 && percent < 100) {
	    				System.out.print(percent + "%\r");
	    				//System.out.flush();
	    			}
	    			else{
	    				System.out.print(percent + "%\r");
	    				//System.out.flush();
	    				break;
	    			}
	    		}
	    	}
	    }
	    try { 
	    	for(int i = 0; i < threads.size(); i++) {
	    		threads.get(i).join();
	    	}
	    } 
	    catch (Exception e) { 
	    	System.out.println(e); 
	    } 
	      
	    System.out.println("Location Threads Finished");
	    
	    double best = 1000000;
	    int index = 0;
	    for(int i = 0; i < threads.size(); i++) {
	    	if(threads.get(i).area < best) {
	    		best = threads.get(i).area;
	    		index = i;
	    	}
	    }
		
	    location = threads.get(index).location;
   }
}
