package base;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
/*
 * This is the Shadow based locator frontend code
 * It will allow the users to click and upload and image to the screen
 * then set the time and date the image was taken if the metadata is not availble
 * for that then they will be blank. This is adjested for UTC time based on the longitiude.
 */
public class Frontend{
	/*
	 * instance variables
	 */
   JFrame frame;   
   JLabel picLabel;
   JPanel pane;
   GridBagConstraints grid;
   
   ArrayList<Point> path = new ArrayList<Point>();
   private MouseHandler mouseHandler = new MouseHandler();
	
   public ArrayList<double[]> data = new ArrayList<double[]>();
   
   static String Times[];
   static String DatesM[];
   static String DatesD[];

   public boolean isOpen;
   /*
    * takes not parameters and returns and and Arraylist of arrays of doubles
    */
   public Frontend() throws IOException {
	   
	  SetupFrame();
	  //init the window 
	  frame = new JFrame();

	  JFrame.setDefaultLookAndFeelDecorated(true); 
      frame.setTitle("Shadow Based Locator");
	  
	  grid = new GridBagConstraints();
	  pane = new JPanel(new GridBagLayout());
	  //top blank bar
      JLabel spacer = new JLabel(" ");
      grid.gridx = 0;
      grid.gridy = 0;
      pane.add(spacer, grid);
	  //instructions
	  JLabel Title = new JLabel("Please get measurements at three different times");
      grid.gridx = 0;
      grid.gridy = 1;
      grid.gridwidth = 4;
      pane.add(Title, grid);
      grid.gridwidth = 1;
      //instructions
      JLabel label1 = new JLabel("Input:");
      grid.gridx = 0;
      grid.gridy = 2;
      pane.add(label1, grid);
      //instructions
      JLabel label2 = new JLabel("Time");
      grid.gridx = 1;
      grid.gridy = 2;
      pane.add(label2, grid);
      //instructions
      JLabel label3 = new JLabel("Month");
      grid.gridx = 2;
      grid.gridy = 2;
      pane.add(label3, grid);
      //instructions
      JLabel label4 = new JLabel("Day");
      grid.gridx = 3;
      grid.gridy = 2;
      pane.add(label4, grid);
      //instructions
      JLabel m1 = new JLabel("When it was taken: ");
      grid.gridx = 0;
      grid.gridy = 3;
      pane.add(m1, grid);
      /*
       *these will be the boxes that the user will modify to fit the time the image was taken
       */
      JComboBox<String> t1 = new JComboBox<String>(Times);
      grid.gridx = 1;
      grid.gridy = 3;
      pane.add(t1, grid);
      
      JComboBox<String> dm1 = new JComboBox<String>(DatesM);
      grid.gridx = 2;
      grid.gridy = 3;
      pane.add(dm1, grid);
      
      JComboBox<String> dd1 = new JComboBox<String>(DatesD);
      grid.gridx = 3;
      grid.gridy = 3;
      pane.add(dd1, grid);
      //save the given boxes into an array and adds it to the arraylist called data
      //this also clears the screen and resets the path
      JButton storeB = new JButton("Store Data");
      storeB.addActionListener(new ActionListener() {	
    	  public void actionPerformed(ActionEvent e){  
    		  if(path.size() != 3) {
    			  path.clear();
    			  frame.pack();
    			  return;
    		  }
    		  double[] info = new double[4];
    		  info[0] = datetodouble((String)dm1.getSelectedItem(), (String)dd1.getSelectedItem());
    		  info[1] = timetodouble((String)t1.getSelectedItem());
    		  info[2] = Math.abs(path.get(1).y - path.get(0).y);
    		  info[3] = Math.abs(path.get(2).x - path.get(1).x);
    		  path = new ArrayList<Point>();
    		  data.add(info);
    		  pane.remove(picLabel);
    		  pane.updateUI();
    		  frame.pack();
      	  }
   	  });
      grid.gridx = 0;
      grid.gridy = 4;
      pane.add(storeB, grid);     
      //used to search through the users directories and find an image
      JButton filesystemB = new JButton("Import Image");
      filesystemB.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){  
    		   JFileChooser j = new JFileChooser(); 
    		   j.showSaveDialog(null);
    		   BufferedImage myPicture = null;
    		   try {
    			   myPicture = ImageIO.read(new File(j.getSelectedFile().toString()));
    		   } catch (IOException e1) {
    			   e1.printStackTrace();
    		   }
    		   System.out.println(j.getSelectedFile().toString());
			   String s = "";
			   try {
    				Process p = Runtime.getRuntime().exec("identify -verbose " + j.getSelectedFile().toString());
    				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
    				String temp;
    				while ((temp = br.readLine()) != null) {
    		        	s += temp; 
    		        }
    		        p.waitFor();
    		        p.destroy();
    		   } 
    		   catch (IOException | InterruptedException e1) {
    			   e1.printStackTrace();
    		   }
			   
			   parseimagedata(t1,dm1,dd1,s);
			   myPicture = resizeImage(myPicture);
    		   picLabel = new JLabel(new ImageIcon(myPicture));
     		   grid.gridx = 0;
    		   grid.gridy = 7;
     	       grid.gridwidth = 5;
    		   pane.add(picLabel, grid);
     		   pane.updateUI();
     		   frame.pack();
      	  }
   	  });
      grid.gridx = 1;
      grid.gridy = 4;
      pane.add(filesystemB, grid);
      //when the users has finished this will send the data back to the main program 
      // this will also hide the frame window
      JButton exitB = new JButton("Done");
      exitB.addActionListener(new ActionListener() {	
    	  public void actionPerformed(ActionEvent e){ 
    		  if(data.size() == 0) {
    			  double[] info = new double[4];
    			  info[0] = 38;
    			  info[1] = 19.17;
    			  info[2] = 3;
    			  info[3] = 6;
    			  data.add(info);
    			  info = new double[4];
    			  info[0] = 45;
    			  info[1] = 19.75;
    			  info[2] = 6;
    			  info[3] = 13;
    			  data.add(info);
    			  info = new double[4];
    			  info[0] = 38;
    			  info[1] = 21.5;
    			  info[2] = 2.2;
    			  info[3] = 8.65;
    			  data.add(info);
    		  }
    		  if(data.size() == 1) {
    			  double[] info = new double[4];
    			  info[0] = 45;
    			  info[1] = 19.75;
    			  info[2] = 6;
    			  info[3] = 13;
    			  data.add(info);
    			  info = new double[4];
    			  info[0] = 38;
    			  info[1] = 21.5;
    			  info[2] = 2.2;
    			  info[3] = 8.65;
    			  data.add(info);
    		  }
    		  if(data.size() == 2) {
    			  double[] info = new double[4];
    			  info[0] = 38;
    			  info[1] = 21.5;
    			  info[2] = 2.2;
    			  info[3] = 8.65;
    			  data.add(info);
    		  }
    		  isOpen = false;
    		  frame.setVisible(false);
      	  }
   	  });
      grid.gridx = 2;
      grid.gridy = 4;
      grid.gridwidth = 2;
      pane.add(exitB, grid);
      //instructions for measuring the images object and shadow
	  JLabel Instr1 = new JLabel("Click top then bottem of the object then the edge of shadow");
      grid.gridx = 0;
      grid.gridy = 5;
      grid.gridwidth = 4;
      pane.add(Instr1, grid);
      grid.gridwidth = 1;
      //instructions for measuring the images object and shadow
	  JLabel Instr2 = new JLabel("Right click to reset lines");
      grid.gridx = 0;
      grid.gridy = 6;
      grid.gridwidth = 4;
      pane.add(Instr2, grid);
      grid.gridwidth = 1;
      //add all the buttons and boxes to the window
      frame.add(pane);

      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setSize(350, 125);
      frame.setMinimumSize(new Dimension(350, 125));
      frame.pack();
      frame.setResizable(true);
      frame.setVisible(true);  

      frame.addMouseListener(mouseHandler);
      frame.addMouseMotionListener(mouseHandler);
   } 
   /*
    * add a visual line graph on the screen of where the user has clicked 
    * left click will add a new point to the path and right click will reset.
    */
  
   private class MouseHandler extends MouseAdapter {

       @Override
       public void mousePressed(MouseEvent e) {
    	   	if(SwingUtilities.isLeftMouseButton(e)){
    	   		//the path should not exced 3 points
    	   		if(path.size() < 3) {
    	   			Point p = e.getPoint();
    	   			System.out.println("mouse left clicked " + p.x + " " + p.y);
    	   			path.add(p);
    	   			Graphics2D g = (Graphics2D) frame.getGraphics();
    	   			g.setStroke(new BasicStroke(3));
    	   			g.setColor(Color.gray);
    	   			g.drawOval(p.x-3, p.y-3, 6, 6);
    	   			
    	   			if(path.size() >= 2 && path.size() < 4) {
    	   				Point start = path.get(path.size()-2);
    	   				Point end = path.get(path.size()-1);
    	   				//if it is the last point change the color of the stoke making two different line colors
    	   				if(path.size() < 3) {
    	   					g.setColor(Color.red);
    	   				}
    	   				else if(path.size() == 3) {
    	   					g.setColor(Color.green);
    	   				}
    	   			g.drawLine(start.x, start.y, end.x, end.y);
    	   			}
    	   		}
            }
    	   	//clear the path
    	   	if (SwingUtilities.isRightMouseButton(e)){
    	   		System.out.println("mouse right clicked");
    	   		if(path.size()!=0) {
    	   			path = new ArrayList<Point>();
    	   			frame.pack();
    	   		}
    	   	}
        }
   }
   // fill all the boxed with temporary input to allow easy user usage.
   void SetupFrame() {
	   Times = new String[24*12];
	   DatesM = new String[12];
	   DatesD = new String[31];
	   isOpen = true;
	   int count = 0;
	   //set the and array full of strings with the time with am and pm
	   for (int i = 0; i < 24; i++) {
		   for (int j = 0; j < 60; j += 5) {
			   boolean after12 = false;
			   String hours;
			   if(i>12)after12 = true;
			   if(i == 0) {
				   hours = Integer.toString(12);
			   }
			   else {
				   if(i%12 == 0) {
					   hours = "12";
				   }
				   else{
					   hours = Integer.toString(i%12);
				   }
			   }
			   String minutes = Integer.toString(j);
			   if (minutes.length() == 1) minutes = "0"+minutes;
			   String time = hours + ":" + minutes;
			   if(after12){ 
				   time += "pm";
			   }
			   else{ 
				   time += "am";
			   }
			   Times[count] = time;
			   count++;
		   }
	   }
	   count = 0;
	   //set the numbers for the month
	   for(int i = 0; i < 12; i++) {
		   DatesM[count] = Integer.toString(i+1);
		   count++;
       }
	   count = 0;
	   //sets the numbers for the day
	   for(int i = 0; i < 31; i++) {
		   DatesD[count] = Integer.toString(i+1);
		   count++;
       }
   }
   //parse the text box with time to remove the am/pm and : and
   //convert the time to a double from 0 to 24
   double timetodouble(String time) {
	    char tm = time.charAt(time.length()-2);
		time = time.substring(0, time.length()-2);
	    String[] result = time.split(":");
	    double newtime;
	    if(tm == 'a') {
	    	newtime = Double.parseDouble(result[0]) + Double.parseDouble(result[1])/60;
	    }
	    else {
	    	newtime = Double.parseDouble(result[0])+12 + Double.parseDouble(result[1])/60;
	 	   
	    }
	    	return newtime;
   }
   //parse the month and date to get the day of the year.
   double datetodouble(String m, String d) {
	   int months[]= {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	   int totalmonthdays = 0;
		for(int i = 1; i < Double.parseDouble(m); i++) {
			totalmonthdays += months[i-1];
		}
		double date = Double.parseDouble(d) + totalmonthdays;
		return date;
   }
   //reads meta data from the image and corrects the time and date from that.
   void parseimagedata(JComboBox<String> t1, JComboBox<String> dm1, JComboBox<String> dd1,  String s) {
	   String info;
	   String[] first = s.split("exif:DateTimeDigitized:");
	   if(first.length == 1) {
		   System.out.println("Unable to get time and date from image");
		   return;
	   }
	   String[] second = first[1].split("exif:DateTimeOriginal:"); 
	   info = second[0];

	   String[] init = info.split(" ");
	   String[] date = init[0].split(":");
	   String[] time = init[1].split(":");
	   int dm = Integer.parseInt(date[1]);
	   int dd = Integer.parseInt(date[2]);
	   int h = Integer.parseInt(time[0]);
	   int m = Integer.parseInt(time[1]);
	   String[] third = second[1].split("exif:GPSLongitude: ");
	   if(third.length == 1) {
		    System.out.println("Unable to adjest time for timezone");
	   }
	   else {
	   		String[] forth = third[1].split("exif:GPSLongitudeRef: ");
	   		String longitude = forth[0];
	   		String[] firth = forth[1].split("exif:GPSTimeStamp: ");
	   		String dir = firth[0].strip();
	   		String[] sixth = longitude.split("/1, ");
	   		int deltalon = Integer.parseInt(sixth[0])*4;
	   		int deltahour = deltalon/60;
	   		int deltamin = deltalon%60;
	   		if(dir.equals("W")) {
		   		m = m + deltamin;
		   		if(m >=60) {
			   		h+=1;
			   		m = m % 60;
		   		}
		   		h = h + deltahour;
		   		if(h >= 24) {
			   		dd += 1;
			   		h = 12;
			   		if(dd > 31) {
				   		dd = 1;
				   		dm += 1;
				   		if(dm > 12) dm = 0;
			   		}
		   		}
	   		}
	   		else if(dir.equals("E")){
		   		m = m - deltamin;
		   		if(m < 0) {
			   		h -= 1;
			   		m = m % 60;
		   		}
		   		h = h - deltahour;
		   		if(h < 0) {
		   			h = 0;
			   		dd -= 1;
			   		if(dd < 0) {
				   		dd = 31;
				   		dm -= 1;
				   		if(dm == 0) dm = 12;
			   		}
		   		}
	   		}
	   }
	   String hour;
	   String min;
	   if(h > 12) {
		   hour = Integer.toString(h-12);
		   min = Integer.toString(m)+"pm";
	   }
	   else {
		   hour = Integer.toString(h);
		   min = Integer.toString(m)+"am";
	   }
	   String t = hour+":"+min;
	   t1.addItem(t);
	   t1.setSelectedItem(t);
	   dm1.addItem(Integer.toString(dm));
	   dm1.setSelectedItem(Integer.toString(dm));
	   dd1.addItem(Integer.toString(dd));
	   dd1.setSelectedItem(Integer.toString(dd));
   }
   //resize the image if it is bigger than 500 pixels wide
   BufferedImage resizeImage(BufferedImage img) {
	   if(img.getWidth() <= 500)return img;
	   BufferedImage resized = resize(img);
	   return resized;
   }
   private static BufferedImage resize(BufferedImage img) {
	   int IMG_WIDTH = img.getWidth()/(img.getWidth()/500);
	   int IMG_HEIGHT = img.getHeight()/(img.getWidth()/500);
	   BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, img.getType());
	   Graphics2D g = resizedImage.createGraphics();
	   g.drawImage(img, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
	   g.dispose();	
       return resizedImage;
   }
}

