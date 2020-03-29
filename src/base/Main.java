package base;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.geom.Point2D;

public class Main {
	
	static boolean show_status = true;

   public static void main(String[] args) throws IOException{
      
      Frontend F = new Frontend();
      while(F.isOpen) {
          Thread.yield();
      }
      
      TraceApprox traces = new TraceApprox(F, show_status);
      
      LocationApprox location = new LocationApprox(traces.a, traces.b, traces.c, show_status);
      
      if(location.failed) {
    	  System.out.println("Unable to calculate location");
    	  System.exit(1);
      }
      
      location.PrintLocationToFile();
      
      Point2D pt = location.getlocation();

      System.out.printf("https://www.google.com/maps/place/%.5f, %.5f\n", pt.getX(), pt.getY());
      System.out.printf("[%.5f, %.5f]", pt.getX(), pt.getY());
      
      SetupGnuplotFile((int)traces.aid, (int)traces.bid, (int)traces.cid);
      
      try {
		Runtime.getRuntime().exec("./gnuplot.sh -x");
      } 
      catch (IOException e) {
		e.printStackTrace();
	  }
      System.exit(0);
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

