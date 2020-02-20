package base;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
public class Frontend{
	
   public ArrayList<double[]> data = new ArrayList<double[]>();
   
   static String Times[];
   static String DatesM[];
   static String DatesD[];

   JLabel picLabel;
 
   public boolean isOpen;

   public Frontend() throws IOException {
	   
	   SetupFrame();
	   
	  JFrame frame = new JFrame();

	  JFrame.setDefaultLookAndFeelDecorated(true); 
      frame.setTitle("Shadow Based Locator");
	  
	  GridBagConstraints grid = new GridBagConstraints();
	  grid.fill = GridBagConstraints.VERTICAL;
	  JPanel pane = new JPanel();
	  
	  JLabel Title = new JLabel("Please get measurements at three different times");
      grid.gridx = 0;
      grid.gridy = 0;
      pane.add(Title, grid);
      
      JLabel label = new JLabel("Input:                       Time                Month      Day");
      grid.gridx = 0;
      grid.gridy = 1;
      pane.add(label, grid);
      
      JLabel m1 = new JLabel("When it was taken: ");
      grid.gridx = 0;
      grid.gridy =2;
      pane.add(m1, grid);
      
      JComboBox<String> t1 = new JComboBox<String>(Times);
      grid.gridx = 1;
      grid.gridy = 2;
      pane.add(t1, grid);
      
      JComboBox<String> dm1 = new JComboBox<String>(DatesM);
      grid.gridx = 2;
      grid.gridy = 2;
      pane.add(dm1, grid);
      
      JComboBox<String> dd1 = new JComboBox<String>(DatesD);
      grid.gridx = 3;
      grid.gridy = 2;
      pane.add(dd1, grid);
      
      JButton storeB = new JButton("Store Data");
      storeB.addActionListener(new ActionListener() {	
    	  public void actionPerformed(ActionEvent e){  
    		  double[] info = new double[4];
    		  info[0] = datetodouble((String)dm1.getSelectedItem(), (String)dd1.getSelectedItem());
    		  info[1] = timetodouble((String)t1.getSelectedItem());
    		  info[2] = 1;
    		  info[3] = 2;
    		  data.add(info);
    		  pane.remove(picLabel);
    		  pane.updateUI();
    		  grid.gridy -= 1;
      	  }
   	  });
      grid.gridx = 0;
      grid.gridy = 3;
      pane.add(storeB, grid);     
      
      JButton filesystemB = new JButton("Import Image");
      filesystemB.addActionListener(new ActionListener() {	
    	  @SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e){  
    		   JFileChooser j = new JFileChooser(); 
    		   j.showSaveDialog(null);
    		   BufferedImage myPicture = null;
    		   try {
				myPicture = ImageIO.read(new File(j.getSelectedFile().toString()));
    		   } catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
    		   }
    		   picLabel = new JLabel(new ImageIcon(myPicture));
    		   grid.gridy += 1;
    		   pane.add(picLabel, grid);
     		   pane.updateUI();
      	  }
   	  });
      grid.gridx = 1;
      grid.gridy = 3;
      pane.add(filesystemB);
      
      JButton exitB = new JButton("Done");
      exitB.addActionListener(new ActionListener() {	
    	  public void actionPerformed(ActionEvent e){ 
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
    		  info[2] = 2.5;
    		  info[3] = 8.75;
    		  data.add(info);
    		  isOpen = false;
    		  frame.hide();
      	  }
   	  });
      grid.gridx = 2;
      grid.gridy = 3;
      pane.add(exitB);

      frame.add(pane);

      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setSize(350, 150);
      frame.setResizable(true);
      frame.setVisible(true);  
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
   
}

