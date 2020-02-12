package base;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.awt.Point;
import java.util.ArrayList;


public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		PrintWriter gnu = new PrintWriter("./src/gnuscript");
		gnu.println("#first trace");
		gnu.println("set xrange [-180:180]");
		gnu.println("set yrange [-90:90]");
		gnu.println("set ylabel \"Latitude\"");
		gnu.println("set xlabel \"Longitude\"");
		gnu.println("set size ratio -1\n");
		gnu.println("set style line 2 \\\n" + 
					"    linecolor rgb '#dd181f' \\\n" + 
					"    linetype 1 linewidth 2 \\\n" + 
					"    pointtype 5 pointsize 1");
		gnu.println("set encoding utf8\n");
		gnu.println("set minussign");
		gnu.println("set grid ytics lt 0 lw 1 lc rgb \"#bbbbbb\"\n" + 
				    "set grid xtics lt 0 lw 1 lc rgb \"#bbbbbb\"");
		gnu.println("plot 'trace9.dat' with points linestyle 2");


		Picture img1 = new Picture(2,7,14,10,0,3,6);
		
		Multithread thread = new Multithread(img1);
		thread.start();

		gnu.close()
;	}

}
