package wolkvlak;

import java.awt.*;

import java.io.*;
import java.util.*;
import java.awt.geom.Rectangle2D;

public class Wolkvlak extends Canvas {
	static final long serialVersionUID = 1984978437;

	int offsetx, offsety;
	double scale;
	
	Wolk wolk;
	
	final int VECTORSCHAAL = 10;
	
	public Wolkvlak (String filename) {		
		wolk = new Wolk(new File(filename));		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		initCoordinateSystem(g, wolk.getBoundingRectangle());
		
		Iterator<Wolkpunt> iter = wolk.iterator();
		
		while (iter.hasNext()) {
			Wolkpunt point = iter.next();
			
			
			//Teken wolkpunten:
			g.setColor(Color.BLUE);
			g.fillRect(toX(point.x)-1,toY(point.y)-1,3,3);
			
			//Teken centroïden:			
			g.setColor(Color.RED);
			g.fillRect(toX(point.centroid.x)-1,toY(point.centroid.y)-1,3,3);
			

			//Teken tangentlijnen:			
			g.setColor(Color.PINK);					
			
			double x0 = point.eigenVector1.x * VECTORSCHAAL / 2;
			double x1 = point.centroid.x - x0;
			double x2 = point.centroid.x + x0;
			
			double y0 = point.eigenVector1.y * VECTORSCHAAL / 2;
			double y1 = point.centroid.y - y0;
			double y2 = point.centroid.y + y0;
			
			g.drawLine(toX(x1), toY(y1), toX(x2), toY(y2));

			//Teken normaalvectoren:
			g.setColor(Color.ORANGE);	
			
			x0 = point.eigenVector2.x * VECTORSCHAAL / 2;
			x1 = point.centroid.x;
			x2 = point.centroid.x + x0;
			
			y0 = point.eigenVector2.y * VECTORSCHAAL / 2;
			y1 = point.centroid.y;
			y2 = point.centroid.y + y0;
			
			g.drawLine(toX(x1), toY(y1), toX(x2), toY(y2));
			
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
