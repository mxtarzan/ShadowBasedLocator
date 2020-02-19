package base;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
public class Frontend{
	
   public double data1[];
   public double data2[];
   public double data3[];
   
   static String Times[];
   static String DatesM[];
   static String DatesD[];
   
   public boolean isOpen;
   
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
   
   public Frontend() {
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
	   
      JFrame frame = new JFrame("Shadow Based Locator");

      JPanel top = new JPanel();
      JPanel info = new JPanel();
      JPanel ln1 = new JPanel();
      JPanel ln2 = new JPanel();
      JPanel ln3 = new JPanel();
      JPanel ln4 = new JPanel();
      
      JLabel Title = new JLabel("Please get measurements at three different times");
      JLabel label = new JLabel("Input:                    Height of object      Length of shadow       Time             Month          Day");
      JLabel m1 = new JLabel("Measurement 1: ");
      JLabel m2 = new JLabel("Measurement 2: ");
      JLabel m3 = new JLabel("Measurement 3: ");
      JTextField h1 = new JTextField(10);
      JTextField l1 = new JTextField(10);
      JComboBox<String> t1 = new JComboBox<String>(Times);
      JComboBox<String> dm1 = new JComboBox<String>(DatesM);
      JComboBox<String> dd1 = new JComboBox<String>(DatesD);
      JTextField h2 = new JTextField(10);
      JTextField l2 = new JTextField(10);
      JComboBox<String> t2 = new JComboBox<String>(Times);
      JComboBox<String> dm2 = new JComboBox<String>(DatesM);
      JComboBox<String> dd2 = new JComboBox<String>(DatesD);
      JTextField h3 = new JTextField(10);
      JTextField l3 = new JTextField(10);
      JComboBox<String> t3 = new JComboBox<String>(Times);
      JComboBox<String> dm3 = new JComboBox<String>(DatesM);
      JComboBox<String> dd3 = new JComboBox<String>(DatesD);
      
      JButton b = new JButton("Done");
      b.addActionListener(new ActionListener() {	
    	  public void actionPerformed(ActionEvent e){  
    		  System.out.println("Measurement 1: " + h1.getText() + " " + l1.getText() + " " + t1.getSelectedItem() + " " + dm1.getSelectedItem() + " " + dd1.getSelectedItem());
    		  System.out.println("Measurement 2: " + h2.getText() + " " + l2.getText() + " " + t2.getSelectedItem() + " " + dm2.getSelectedItem() + " " + dd2.getSelectedItem());
    		  System.out.println("Measurement 3: " + h3.getText() + " " + l3.getText() + " " + t3.getSelectedItem() + " " + dm3.getSelectedItem() + " " + dd3.getSelectedItem());

    		  data1 = new double[4];
    		  data2 = new double[4];
    		  data3 = new double[4];
    		  
    		  data1[0] = Double.parseDouble(h1.getText());
    		  data2[0] = Double.parseDouble(h2.getText());
    		  data3[0] = Double.parseDouble(h3.getText());
    		  data1[1] = Double.parseDouble(l1.getText());
    		  data2[1] = Double.parseDouble(l2.getText());
    		  data3[1] = Double.parseDouble(l3.getText());
    		  data1[2] = timetodouble((String)t1.getSelectedItem());
    		  data2[2] = timetodouble((String)t2.getSelectedItem());
    		  data3[2] = timetodouble((String)t3.getSelectedItem());
    		  data1[3] = datetodouble((String)dm1.getSelectedItem(), dd1.getSelectedItem().toString());
    		  data2[3] = datetodouble((String)dm2.getSelectedItem(), (String)dd2.getSelectedItem());
    		  data3[3] = datetodouble((String)dm3.getSelectedItem(), (String)dd3.getSelectedItem());
    		  
    		  isOpen = false;
    		  frame.hide();
      	  }
   	  });
      
      JButton testing = new JButton("Import Data");
      testing.addActionListener(new ActionListener() {	
    	  public void actionPerformed(ActionEvent e){  
    		  data1 = new double[4];
    		  data2 = new double[4];
    		  data3 = new double[4];
    		  
    		  data1[0] = 3;
    		  data2[0] = 6;
    		  data3[0] = 2.5;
    		  data1[1] = 6;
    		  data2[1] = 13;
    		  data3[1] = 8.75;
    		  data1[2] = 19.1775;
    		  data2[2] = 19.75;
    		  data3[2] = 21.5;
    		  data1[3] = 38;
    		  data2[3] = 45;
    		  data3[3] = 45;
    		  
    		  isOpen = false;
    		  frame.hide();
      	  }
   	  });
      
      top.add(Title);
      
      info.add(label);
      
      ln1.add(m1);
      ln1.add(h1);
      ln1.add(l1);
      ln1.add(t1);
      ln1.add(dm1);
      ln1.add(dd1);

      ln2.add(m2);
      ln2.add(h2);
      ln2.add(l2);
      ln2.add(t2);
      ln2.add(dm2);
      ln2.add(dd2);

      ln3.add(m3);
      ln3.add(h3);
      ln3.add(l3);
      ln3.add(t3);
      ln3.add(dm3);
      ln3.add(dd3);
      
      ln4.add(b);
      ln4.add(testing);
      
      frame.add(top);
      frame.add(info);
      frame.add(ln1);
      frame.add(ln2);
      frame.add(ln3);
      frame.add(ln4);
      
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setLayout(new FlowLayout());
      frame.setSize(525,250);
      frame.setVisible(true);  
   } 
}

