package base;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.awt.Point;
import java.util.ArrayList;


public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		PrintWriter gnu = new PrintWriter("./src/gnuscript");
		gnu.println("set xrange [-180:180]");
		gnu.println("set yrange [-90:90]");
		gnu.println("set ylabel \"Latitude\"");
		gnu.println("set xlabel \"Longitude\"");
		gnu.println("set size ratio -1\n");
		gnu.println("set style line 2 \\\n" + 
				"    linecolor rgb '#000000' \\\n" + 
				"    linetype 1 linewidth 2 \\\n" + 
				"    pointtype 1 pointsize 0.75");
		gnu.println("set style line 3 \\\n" + 
				"    linecolor rgb '#ff00ff' \\\n" + 
				"    linetype 1 linewidth 2 \\\n" + 
				"    pointtype 1 pointsize 0.75");
		gnu.println("set style line 2 \\\n" + 
				"    linecolor rgb '#00ffff' \\\n" + 
				"    linetype 1 linewidth 2 \\\n" + 
				"    pointtype 1 pointsize 0.75");
		gnu.println("set encoding utf8\n");
		gnu.println("set minussign");
		gnu.println("set grid ytics lt 0 lw 1 lc rgb \"#bbbbbb\"\n" + 
				    "set grid xtics lt 0 lw 1 lc rgb \"#bbbbbb\"");
		gnu.println("plot 'trace1.dat' with points linestyle 2 linecolor rgb '#000000', 'trace9.dat' with points linestyle 3 linecolor rgb '#ff00ff', 'trace10.dat' with points linestyle 4 linecolor rgb '#00ffff'");
		gnu.close();

		Picture data1 = new Picture(2,7,19,10,0,3,6);
		Picture data2 = new Picture(2,14,19,42,0,6,13);

		Multithread thread1 = new Multithread(data1);
		thread1.start();
		Multithread thread2 = new Multithread(data2);
		thread2.start();
		
		
		PrintWriter t = new PrintWriter("./src/trace1.dat");
		for(int lon = -180; lon <=180; lon++) {
			t.println(lon + " " + 41);
		}
		for(int lat = -90; lat <=90; lat++) {
			t.println(-71 + " " + lat);
		}
		t.close();
		
		}

}
