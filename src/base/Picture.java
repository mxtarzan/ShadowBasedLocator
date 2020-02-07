package base;

public class Picture {
	public double date;
	public double time;
	public double height;
	public double shadowlength;
	// days in each month jan  feb mar apr may jun jul aug sep oct nov dec
	private int months[]= {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	
	public Picture(int m, int d, int hours, int minutes, int seconds, double h, double sl) {
		int totalmonthdays = 0;
		for(int i = 1; i < m; i++) {
			totalmonthdays += months[i-1];
		}
		date = d + totalmonthdays;
		time = hours + minutes/24 + ((seconds/60)/24);
		height = h;
		shadowlength = sl;
	}
	
	public Picture(int m, int d, int hours, int minutes, double h, double sl) {
		int totalmonthdays = 0;
		for(int i = 1; i < m; i++) {
			totalmonthdays += months[i-1];
		}
		date = d + totalmonthdays;
		time = hours + minutes/24;
		height = h;
		shadowlength = sl;
	}
}
