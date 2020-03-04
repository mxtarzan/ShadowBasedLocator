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

public class Frontend{
	
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

   public Frontend() throws IOException {
	   
	   SetupFrame();
	   
	  frame = new JFrame();

	  JFrame.setDefaultLookAndFeelDecorated(true); 
      frame.setTitle("Shadow Based Locator");
	  
	  grid = new GridBagConstraints();
	  pane = new JPanel(new GridBagLayout());
	  
      JLabel spacer = new JLabel(" ");
      grid.gridx = 0;
      grid.gridy = 0;
      pane.add(spacer, grid);
	  
	  JLabel Title = new JLabel("Please get measurements at three different times");
      grid.gridx = 0;
      grid.gridy = 1;
      grid.gridwidth = 4;
      pane.add(Title, grid);
      grid.gridwidth = 1;
      
      JLabel label1 = new JLabel("Input:");
      grid.gridx = 0;
      grid.gridy = 2;
      pane.add(label1, grid);
      
      JLabel label2 = new JLabel("Time");
      grid.gridx = 1;
      grid.gridy = 2;
      pane.add(label2, grid);
      
      JLabel label3 = new JLabel("Month");
      grid.gridx = 2;
      grid.gridy = 2;
      pane.add(label3, grid);
      
      JLabel label4 = new JLabel("Day");
      grid.gridx = 3;
      grid.gridy = 2;
      pane.add(label4, grid);
      
      JLabel m1 = new JLabel("When it was taken: ");
      grid.gridx = 0;
      grid.gridy = 3;
      pane.add(m1, grid);
      
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
    		  info[2] = path.get(1).y - path.get(0).y;
    		  info[3] = path.get(2).x - path.get(1).x;
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
      
      JButton exitB = new JButton("Done");
      exitB.addActionListener(new ActionListener() {	
    	  public void actionPerformed(ActionEvent e){ 
    		  if(data.size() < 3) {
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
    		  isOpen = false;
    		  frame.setVisible(false);
      	  }
   	  });
      grid.gridx = 2;
      grid.gridy = 4;
      grid.gridwidth = 2;
      pane.add(exitB, grid);

	  JLabel Instr1 = new JLabel("Click top then bottem of the object then the edge of shadow");
      grid.gridx = 0;
      grid.gridy = 5;
      grid.gridwidth = 4;
      pane.add(Instr1, grid);
      grid.gridwidth = 1;
      
	  JLabel Instr2 = new JLabel("Right click to reset lines");
      grid.gridx = 0;
      grid.gridy = 6;
      grid.gridwidth = 4;
      pane.add(Instr2, grid);
      grid.gridwidth = 1;
      
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
   
   private class MouseHandler extends MouseAdapter {

       @Override
       public void mousePressed(MouseEvent e) {
    	   	if(SwingUtilities.isLeftMouseButton(e)){
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
    	   	if (SwingUtilities.isRightMouseButton(e)){
    	   		System.out.println("mouse right clicked");
    	   		if(path.size()!=0) {
    	   			path = new ArrayList<Point>();
    	   			frame.pack();
    	   		}
    	   	}
        }
   }
   
   void SetupFrame() {
	   Times = new String[24*12];
	   DatesM = new String[12];
	   DatesD = new String[31];
	   isOpen = true;
	   int count = 0;
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
	   for(int i = 0; i < 12; i++) {
		   DatesM[count] = Integer.toString(i+1);
		   count++;
       }
	   count = 0;
	   for(int i = 0; i < 31; i++) {
		   DatesD[count] = Integer.toString(i+1);
		   count++;
       }
   }
   
   double timetodouble(String time) {
		time = time.substring(0, time.length()-2);
	    String[] result = time.split(":");
	    double newtime = Double.parseDouble(result[0]) + Double.parseDouble(result[1])/60;
	    return newtime;
   }
   double datetodouble(String m, String d) {
	   int months[]= {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	   int totalmonthdays = 0;
		for(int i = 1; i < Double.parseDouble(m); i++) {
			totalmonthdays += months[i-1];
		}
		double date = Double.parseDouble(d) + totalmonthdays;
		return date;
   }
   void parseimagedata(JComboBox<String> t1, JComboBox<String> dm1, JComboBox<String> dd1,  String s) {
	   String info;
	   String[] first = s.split("DateTimeDigitized: ");
	   if(first.length == 1) return;
	   String[] second = first[1].split("exif:DateTimeOriginal:"); 
	   info = second[0];

	   System.out.println(info);

	   String[] init = info.split(" ");
	   String[] date = init[0].split(":");
	   
	   dm1.addItem(date[1]);
	   dm1.setSelectedItem(date[1]);
	   dd1.addItem(date[2]);
	   dd1.setSelectedItem(date[2]);

	   String[] time = init[1].split(":");
	   String hour = time[0];
	   String min = time[1];
	   if(Integer.parseInt(hour) > 12) {
		   hour = Integer.toString(Integer.parseInt(hour)-12);
		   min += "pm";
	   }
	   else {
		   min += "am";
	   }
	   String t = hour+":"+min;
	   t1.addItem(t);
	   t1.setSelectedItem(t);
   }
   BufferedImage resizeImage(BufferedImage img) {
	   int w = img.getWidth();
	   int h = img.getHeight();
	   if(w <= 500)return img;
	   int scale = w/500;
	   return resize(img, w/scale, h/scale);
   }
   private static BufferedImage resize(BufferedImage img, int height, int width) {
       Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
       BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
       Graphics2D g2d = resized.createGraphics();
       g2d.drawImage(tmp, 0, 0, null);
       g2d.dispose();
       return resized;
   }
}

