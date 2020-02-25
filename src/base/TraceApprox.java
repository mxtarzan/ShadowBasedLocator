package base;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class TraceApprox {
	ArrayList<Point2D> a;
	ArrayList<Point2D> b;
	ArrayList<Point2D> c;
	long aid;
	long bid;
	long cid;
	
	public TraceApprox(Frontend F, boolean print) {
		
		ArrayList<Picture> pics = new ArrayList<Picture>();
	    for (int i = 0; i < F.data.size(); i++) {
	    	double info[] = F.data.get(i);
	    	pics.add(new Picture(info[0], info[1], info[2], info[3]));
	    }


	      
	    ArrayList<Multithread_Trace> threads = new ArrayList<Multithread_Trace>();
	    for (int i = 0; i < pics.size(); i++) {
	    	threads.add(new Multithread_Trace(pics.get(i)));
	    	threads.get(i).start();
	    }
	    
	    System.out.println("Trace Threads Running");
	    
	    if(print) {
	    	double oldpercent = 0;
	    	while(true) {
	    		double newpercent = 0;
	    		for (int i = 0; i < 3; i++) {
	    			newpercent += threads.get(i).percent;
	    		}
	    		Thread.yield();
	    		//System.out.flush();
	    		if((int)newpercent/3 > (int)oldpercent/3) {
	    			oldpercent = newpercent;
	    			int percent = (int)Math.ceil(newpercent/3);
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
	      
	    System.out.println("Trace Threads Finished");
	       
	    a = threads.get(0).trace;
	    b = threads.get(1).trace;
	    c = threads.get(2).trace;
	    aid = threads.get(0).getId();
	    bid = threads.get(1).getId();
	    cid = threads.get(2).getId();
	}
}
