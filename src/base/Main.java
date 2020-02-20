package base;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.Point;
import java.util.ArrayList;

public class Main {

   public static void main(String[] args) throws IOException{
      
      Frontend F = new Frontend();
      while(F.isOpen) {
          Thread.yield();
      }

      ArrayList<Picture> pics = new ArrayList<Picture>();
      for (int i = 0; i < F.data.size(); i++) {
    	  double info[] = F.data.get(i);
    	  pics.add(new Picture(info[0], info[1], info[2], info[3]));
      }

      ArrayList<Multithread> threads = new ArrayList<Multithread>();
      for (int i = 0; i < pics.size(); i++) {
    	  threads.add(new Multithread(pics.get(i)));
    	  threads.get(i).start();
      } 
      
      try { 
    	  for(int i = 0; i < threads.size(); i++) {
    		  threads.get(i).join();
    	  }
      } 
      catch (Exception e) { 
    	  System.out.println(e); 
      } 
      
      ArrayList<Point> a = threads.get(0).trace;
      ArrayList<Point> b = threads.get(1).trace;
      ArrayList<Point> c = threads.get(2).trace;
		
      LocationApprox location = new LocationApprox(a,b,c);
      
      location.PrintLocationToFile();
      
      Point pt = location.getlocation();
      
      System.out.println(pt.x + " " + pt.y);
      
      SetupGnuplotFile((int)threads.get(0).getId(), (int)threads.get(1).getId(), (int)threads.get(2).getId());
      
      try {
		Runtime.getRuntime().exec("./gnuplot.sh -x");
      } 
      catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  }
   }
   
   public static void SetupGnuplotFile(int a, int b, int c) throws FileNotFoundException{
	   PrintWriter gnu = new PrintWriter("./src/gnuscript");
	   gnu.println("set title \"Shadow Based Locator\"");
	   gnu.println("set xrange [-180:180]");
	   gnu.println("set yrange [-90:90]");
	   gnu.println("set ylabel \"Latitude\"");
	   gnu.println("set xlabel \"Longitude\"");
	   gnu.println("set size ratio -1\n");
	   gnu.println("set encoding utf8\n");
	   gnu.println("set minussign");
	   gnu.println("set grid ytics lt 0 lw 1 lc rgb \"#bbbbbb\"\n" + 
			   	   "set grid xtics lt 0 lw 1 lc rgb \"#bbbbbb\"");
	   gnu.println("plot './src/Data"+a+".dat'  	    with points linecolor rgb '#ff0000'  pointtype 2 pointsize 0.25," 
     	      		  + "'./src/Data"+b+".dat' 		with points linecolor rgb '#00ff00'  pointtype 3 pointsize 0.25,"
				      + "'./src/Data"+c+".dat' 		with points linecolor rgb '#0000ff'  pointtype 4 pointsize 0.25,"
					  + "'./src/ActualLocation.dat' with points linecolor rgb '#000000' pointtype 1 pointsize 0.25");
	   gnu.close();
   }
}

