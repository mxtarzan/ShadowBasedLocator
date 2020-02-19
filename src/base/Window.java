package base;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.Point;

public class Window{
   
   ArrayList<Point> OHnSL;
   
   static String Times[];
   static String Dates[];
   
   public boolean isOpen;
   
   public Window() {
	   Times = new String[24*12];
	   Dates = new String[365];
	   isOpen = true;
	   int count = 0;
	   for (int i = 0; i < 24; i++) {
		   for (int j = 0; j < 60; j += 5) {
			   String hours = Integer.toString(i);
			   String minutes = Integer.toString(j);
			   if (hours.length() == 1) hours = "0"+hours;
			   if (minutes.length() == 1) minutes = "0"+minutes;
			   Times[count] = hours+":"+minutes;
			   count++;
		   }
	   }
	   count = 0;
	   for(int i = 0; i < 365; i++) {
		   Dates[count] = Integer.toString(i);
		   count++;
;   }
	   
      JFrame frame = new JFrame("Shadow Based Locator");

      JPanel top = new JPanel();
      JPanel info = new JPanel();
      JPanel ln1 = new JPanel();
      JPanel ln2 = new JPanel();
      JPanel ln3 = new JPanel();
      JPanel ln4 = new JPanel();
      
      JLabel Title = new JLabel("Please get measurements at three different times");
      JLabel label = new JLabel("Height of object, Length of shadow, Time, Date");
      JLabel m1 = new JLabel("Measurement 1: ");
      JLabel m2 = new JLabel("Measurement 2: ");
      JLabel m3 = new JLabel("Measurement 3: ");
      JTextField h1 = new JTextField(10);
      JTextField l1 = new JTextField(10);
      JComboBox<String> t1 = new JComboBox<String>(Times);
      JComboBox<String> d1 = new JComboBox<String>(Dates);
      JTextField h2 = new JTextField(10);
      JTextField l2 = new JTextField(10);
      JComboBox<String> t2 = new JComboBox<String>(Times);
      JComboBox<String> d2 = new JComboBox<String>(Dates);
      JTextField h3 = new JTextField(10);
      JTextField l3 = new JTextField(10);
      JComboBox<String> t3 = new JComboBox<String>(Times);
      JComboBox<String> d3 = new JComboBox<String>(Dates);
      
      JButton b = new JButton("Done");
      b.addActionListener(new ActionListener() {	
    	  public void actionPerformed(ActionEvent e){  
    		  System.out.println("Measurement 1: " + h1.getText() + " " + l1.getText() + " " + t1.getSelectedItem() + " " + d1.getSelectedItem());
    		  System.out.println("Measurement 2: " + h2.getText() + " " + l2.getText() + " " + t2.getSelectedItem() + " " + d2.getSelectedItem());
    		  System.out.println("Measurement 3: " + h3.getText() + " " + l3.getText() + " " + t3.getSelectedItem() + " " + d3.getSelectedItem());
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
      ln1.add(d1);

      ln2.add(m2);
      ln2.add(h2);
      ln2.add(l2);
      ln2.add(t2);
      ln2.add(d2);

      ln3.add(m3);
      ln3.add(h3);
      ln3.add(l3);
      ln3.add(t3);
      ln3.add(d3);

      ln4.add(b);
      
      frame.add(top);
      frame.add(info);
      frame.add(ln1);
      frame.add(ln2);
      frame.add(ln3);
      frame.add(ln4);
      
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setLayout(new FlowLayout());
      frame.setSize(500,250);
      frame.setVisible(true);  
   } 
}

