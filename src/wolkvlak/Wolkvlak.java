package wolkvlak;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Wolkvlak extends Canvas {
	static final long serialVersionUID = 1984978437;

	int offsetx, offsety;
	double scale;
	
	Wolk wolk;
	
	public Wolkvlak () {
		
	}
	
	public Wolkvlak (String filename) {
		
		wolk = new Wolk(new File(filename));
		
		
		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		initCoordinateSystem(g, wolk.getBoundingRectangle());
		
		Iterator<Point2D.Double> iter = wolk.iterator();
		
		while (iter.hasNext()) {
			Point2D.Double point = iter.next();
			
			g.fillRect(toX(point.x)-1,toY(point.y)-1,3,3);
		}
	}
	
	/**
	 * Initialises the coordinate system & graphics object
	 * @param g the Graphics object
	 */
	private void initCoordinateSystem(Graphics g, Rectangle2D.Double rect) {
		int width = getWidth();
		int height = getHeight();
		
		scale = Math.min(width / rect.width, height / rect.height);
		
		offsetx = (int) Math.round((width - Math.min(width, height)) / 2.0 - rect.x*scale);
		offsety = height - (int) Math.round((height - Math.min(width, height)) / 2.0 - rect.y*scale);
		
		// draw coordinate system
/*		g.setColor(Color.LIGHT_GRAY);
		g.drawLine(offset_x, 0, offset_x, height);
		g.drawLine(0, offset_y, width, offset_y);
*/		
	}
	
	/**
	 * transform unit coordinates into pixel coordinates
	 */
	private int toX(double x) {
		return (int) Math.round(offsetx + x * scale);
	}
	private int toY(double y) {
		return (int) Math.round(offsety - y * scale);
	}
	
	
}
