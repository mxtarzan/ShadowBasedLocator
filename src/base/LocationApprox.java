package base;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class LocationApprox {
	Point2D location;
	int cores = Runtime.getRuntime().availableProcessors();
	boolean failed = false;
	//print the location of the image to a file in gnuplot format
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
   //getter for the location
   public Point2D getlocation(){
	      return location;
   }
	/*
	 * Takes three lists of points and a boolean to print progress
	 * splits the first list into into equal parts based on the number of cores the computer running the 
	 * program has. then multithreads this process.
	 */
	public LocationApprox(ArrayList<Point2D> a, ArrayList<Point2D> b, ArrayList<Point2D> c, boolean print){
		
		ArrayList<ArrayList<Point2D>> asplit = new ArrayList<ArrayList<Point2D>>();
		int inc = a.size()/cores;
		//splitting the first array of points evenly.
		ArrayList<Point2D> temp = new ArrayList<Point2D>();
		for(int i = 0; i < a.size(); i++) {
			temp.add(a.get(i));
			if(i != 0 && i%inc == 0) {
				asplit.add(temp);
				temp = new ArrayList<Point2D>();
			}
			if(i == a.size()-1 && asplit.size() < cores)asplit.add(temp);
		}
		
	    ArrayList<Multithread_Location> threads = new ArrayList<Multithread_Location>();
	    //generate the treads
	    for(int i = 0; i < cores; i++) {
	    	threads.add(new Multithread_Location(asplit.get(i), b, c));
	    }
	    //start the treads
	    for (int i = 0; i < cores; i++) {
	    	threads.get(i).start();
	    }
	    //compute the progress the treads are at and print to the terminal.
	    System.out.println("Location Threads Running");
	    if(print){	    
	    	int oldpercent = 0;
	    	while(true) {
	    		int newpercent = 0;
	    		for (int i = 0; i < cores; i++) {
	    			newpercent += threads.get(i).percent;
	    			failed = threads.get(i).failed;
	    			if(failed) return;
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
	    //wait till all the treads have finished
	    try { 
	    	for(int i = 0; i < threads.size(); i++) {
	    		threads.get(i).join();
	    	}
	    } 
	    catch (Exception e) { 
	    	System.out.println(e); 
	    } 
	     
	    System.out.println("Location Threads Finished");
	    //calculate the best location of the threads based on triable parimeter.
	    double best = 1000000;
	    int index = 0;
	    for(int i = 0; i < cores; i++) {
	    	if(threads.get(i).area < best) {
	    		best = threads.get(i).area;
	    		index = i;
	    	}
	    }
		
	    location = threads.get(index).location;
   }
}
