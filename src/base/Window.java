package base;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.Point;

public class Window{
   
   ArrayList<Point> OHnSL;
   @SuppressWarnings("deprecation")
   public Window(){
      System.out.println("making window");
      //1. Create the frame.
      JFrame frame = new JFrame("Shadow Based Locator");
      frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      frame.setLayout(new FlowLayout());
      frame.setSize(500,250);
      frame.setVisible(true);
      frame.show();     
   } 
}

