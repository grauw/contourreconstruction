package wolkvlak;

import java.awt.*;

import java.io.*;
import java.util.*;
import java.awt.geom.Rectangle2D;

public class Wolkvlak extends Canvas {
	static final long serialVersionUID = 1984978437;

	int offsetx, offsety;
	double scale;
	
	public boolean drawwolkpunten = true;
	public boolean drawcentroids = true;
	public boolean drawtangentlines = true;
	public boolean drawnormalvectors = true;
	public boolean drawspanningtree = true;
	public boolean drawsigneddistance = true;
	
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
			if (drawwolkpunten) {
				g.setColor(Color.BLUE);
				g.fillRect(toX(point.x)-1,toY(point.y)-1,3,3);
			}
			
			//Teken centroïden:
			if (drawcentroids) {
				g.setColor(Color.RED);
				g.fillRect(toX(point.centroid.x)-1,toY(point.centroid.y)-1,3,3);
			}

			//Teken tangentlijnen:
			if (drawtangentlines) {
				g.setColor(Color.PINK);
				
				double x0 = point.tangentVector.x * VECTORSCHAAL / 2;
				double x1 = point.centroid.x - x0;
				double x2 = point.centroid.x + x0;
				
				double y0 = point.tangentVector.y * VECTORSCHAAL / 2;
				double y1 = point.centroid.y - y0;
				double y2 = point.centroid.y + y0;
				
				g.drawLine(toX(x1), toY(y1), toX(x2), toY(y2));
			}

			//Teken normaalvectoren:
			if (drawnormalvectors) {
				g.setColor(Color.ORANGE);
				
				double x0 = point.normalVector.x * VECTORSCHAAL / 2;
				double x1 = point.centroid.x;
				double x2 = point.centroid.x + x0;
				
				double y0 = point.normalVector.y * VECTORSCHAAL / 2;
				double y1 = point.centroid.y;
				double y2 = point.centroid.y + y0;
				
				g.drawLine(toX(x1), toY(y1), toX(x2), toY(y2));
			}
		}
		
		if (drawspanningtree) {
			g.setColor(Color.BLACK);
			boolean edge[][] = wolk.emst.edge;
			for (int i=0; i<edge.length; i++) {
				for (int j=0; j<edge[i].length; j++) {
					
					if (edge[i][j]) {
						double x1 = wolk.get(i).x;
						double y1 = wolk.get(i).y;
						double x2 = wolk.get(j).x;
						double y2 = wolk.get(j).y;
						
						g.drawLine(toX(x1), toY(y1), toX(x2), toY(y2));
					}
				}
			}
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
