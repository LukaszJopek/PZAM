package application.src.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import core.ImageInfo;

public class Display {

	private static Display display = null;
	private static JFrame frame = null; 
	private static JPanel pan1 = null;
	
	private Display() {
		
	} 
	public static Display getInstance() {
		if (display == null) {
			frame = new JFrame();
	        frame.setLayout(new FlowLayout());
	        frame.setSize(800,800);
	        frame.setBackground(new Color(0, 0, 0));
	        pan1 = new JPanel();
	        frame.add(pan1);
			display = new Display();
		}
		return display;
	}
	
    public void DisplayImage(byte[][] imageInByte, ImageInfo imageInfo,String label) throws IOException
    {
    	BufferedImage b = new BufferedImage(imageInfo.getHeigth(), imageInfo.getWidth(), BufferedImage.TYPE_USHORT_GRAY);
    	for (int i=0;i<imageInfo.getHeigth();i++) {
    		for (int j=0;j<imageInfo.getWidth();j++) {
    			b.setRGB(i, j, (byte)(imageInByte[i][j] * 255.0));
    		}
    	}
    	pan1.removeAll();
        ImageIcon icon=new ImageIcon(b);
        JLabel lbl=new JLabel();
        lbl.setIcon(icon);
        frame.repaint();       
        pan1.add(lbl);
        frame.setTitle(label);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	
}
