package org.voxforge;
import java.applet.*;
import java.awt.*;
 
@SuppressWarnings("serial")
public class JavaVersionDisplayApplet extends Applet { 
	private Label m_labVersionVendor; 

	public JavaVersionDisplayApplet()  { //constructor
		Color colFrameBackground = Color.lightGray;
     	this.setBackground(colFrameBackground);
     	m_labVersionVendor = new Label (" Java Version: " +
                                    System.getProperty("java.version")+
                           " from "+System.getProperty("java.vendor"));
     	this.add(m_labVersionVendor);
   	}
 }