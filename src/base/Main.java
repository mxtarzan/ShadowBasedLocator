package base;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.awt.Point;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Picture img1 = new Picture(2,7,14,10,0,3,6);
		double decangle = SolarMath.calcdeclinationangle(img1.date);
		double sunelevation = SolarMath.calcsunelevation(img1.height, img1.shadowlength);
		ArrayList<Point> trace1 = SolarMath.Trace(decangle, sunelevation, img1.time, img1.date);
		
		System.out.println(Math.sin(Math.PI/2) + " " + Math.sin(90));
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
		gnu.println("plot 'trace1.dat' with points linestyle 2");

		PrintWriter t1 = new PrintWriter("./src/trace1.dat");
		t1.println("#x y");
		for(int i = 0; i < trace1.size(); i++) {
			//System.out.println("("+trace1.get(i).x+", "+trace1.get(i).y+")");
			t1.println((trace1.get(i).x) + " " + (trace1.get(i).y));
		}
		t1.close();
		gnu.close()
;	}

}
