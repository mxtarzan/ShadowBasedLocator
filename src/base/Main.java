package base;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.geom.Point2D;
/*
 * Shadow Based Loactor
 * This program will use a gui that the user will use and upload three pictures to and then
 * set the time and date that it was taken at for each image along with using the gui to click the 
 * top and bottom of the object, and the end of the shadow to measure their lengths in pixels
 * The it will multithread the process based on the number of core the computer has to find all the possible places 
 * that those values could be true based on time and date. after that it will multithread the process of finding the 
 * point which they all overlap.  then finally it will run a gnu plot of the infomation to show the user where the
 * image was taken on earth
 */
public class Main {
	//printing variable
	static boolean show_status = true;

   public static void main(String[] args) throws IOException, InterruptedException{
      //run the front end
      Frontend F = new Frontend();
      while(F.isOpen) {
          Thread.yield();
      }
      //pass the frontend infomation to the traceApprox function with the print status also.
      TraceApprox traces = new TraceApprox(F, show_status);
      //pass the three arrays of points the the locatino appox function along with the print status also.
      LocationApprox location = new LocationApprox(traces.a, traces.b, traces.c, show_status);
      //if no location can be fount withing the given set bound exit
      if(location.failed) {
    	  System.out.println("Unable to calculate location");
    	  System.exit(1);
      }
      //write the location to a file
      location.PrintLocationToFile();
      //write the location to terminal and give a googlemaps link to the locations
      Point2D pt = location.getlocation();

      System.out.printf("https://www.google.com/maps/place/%.5f, %.5f\n", pt.getX(), pt.getY());
      System.out.printf("[%.5f, %.5f]", pt.getX(), pt.getY());
      //create the gnu plot script for printing each of the arrays of points
      SetupGnuplotFile((int)traces.aid, (int)traces.bid, (int)traces.cid);
      //execute the gnuplot script.
	  String [] cmdArray = new String[2];
	  cmdArray[0] = "/Users/tarzan/eclipse-workspace/ShadowBasedLocatorMac/gnuplot.sh";
	  cmdArray[1] = "-x";
	  Process proc = null;
      try {
    	  
    	  proc = Runtime.getRuntime().exec(cmdArray);

          int exitVal = proc.waitFor();
          System.out.println("Process exitValue: " + exitVal);
      } 
      catch (IOException e) {
		e.printStackTrace();
	  }
      System.exit(0);
   }
   //gnu plot script generator
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

