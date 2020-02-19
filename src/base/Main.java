package base;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.awt.Point;
import java.util.ArrayList;

public class Main {

	public static void SetupGnuplotFile() throws FileNotFoundException{
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
		gnu.println("plot './src/Data17.dat'  	     with points linecolor rgb '#ff0000'  pointtype 2 pointsize 0.25," 
					   + "'./src/Data18.dat' 		 with points linecolor rgb '#00ff00'  pointtype 3 pointsize 0.25,"
					   + "'./src/Data19.dat' 		 with points linecolor rgb '#0000ff'  pointtype 4 pointsize 0.25,"
					   + "'./src/ActualLocation.dat' with points linecolor rgb '#000000' pointtype 1 pointsize 0.25");
		gnu.close();


   }

   public static void main(String[] args) throws FileNotFoundException{
      
      SetupGnuplotFile();
      
      Frontend F = new Frontend();
      while(F.isOpen) {
          System.out.print("");
      }
      /*
       * realdata
       * 3,6,7:10,2,7
       * 6, 13, 7:45, 2, 14
       * 2.5, 8.75, 9:30, 2, 14
      */

      Picture data1 = new Picture(F.data1[3], F.data1[2], F.data1[0], F.data1[1]);
      Picture data2 = new Picture(F.data2[3], F.data2[2], F.data2[0], F.data2[1]);
      Picture data3 = new Picture(F.data3[3], F.data3[2], F.data3[0], F.data3[1]);
      
      Multithread thread1 = new Multithread(data1);
      thread1.start();
      Multithread thread2 = new Multithread(data2);
      thread2.start();
      Multithread thread3 = new Multithread(data3);
      thread3.start();
	    
      try { 
    	 thread1.join();
    	 thread2.join(); 
    	 thread3.join(); 
      } 
      catch (Exception e) { 
    	  System.out.println(e); 
      } 
		
      ArrayList<Point> a = thread1.trace;
      ArrayList<Point> b = thread2.trace;
      ArrayList<Point> c = thread3.trace;
		
      LocationApprox location = new LocationApprox(a,b,c);
      location.PrintLocation();
      Point pt = location.getlocation();
      System.out.println(pt.x + " " + pt.y);
      try {
		Runtime.getRuntime().exec("./gnuplot.sh -x");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}

