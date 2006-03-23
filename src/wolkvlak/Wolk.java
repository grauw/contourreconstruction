package wolkvlak;

import java.awt.geom.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Wolk extends Vector<Point2D.Double> {
	static final long serialVersionUID = 123172398;
	
	double maxx = Double.NEGATIVE_INFINITY;
	double maxy = Double.NEGATIVE_INFINITY;
	double minx = Double.POSITIVE_INFINITY;
	double miny = Double.POSITIVE_INFINITY;
	
	public Wolk(File file) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
			
			int length = Integer.parseInt(reader.readLine());
			for (int i=0; i<length; i++) {
				String line = reader.readLine();
				String[] points = line.split(" ");
				double x = Double.parseDouble(points[0]);
				double y = Double.parseDouble(points[1]);
				this.addPoint(x, y);
				
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found ("+file.getName()+")");
		} catch (IOException e) {
			System.out.println("Fout");
		}
	}
	
	public void addPoint(double x, double y) {
		Point2D.Double point = new Point2D.Double();
		point.setLocation(x, y);
		this.add(point);
		maxx = Math.max(x, maxx);
		maxy = Math.max(y, maxy);
		minx = Math.min(x, minx);
		miny = Math.min(y, miny);
	}
	
	public Point2D.Double getPoint(int p) {
		return this.get(p);
	}
	
	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double(minx, miny, maxx - minx, maxy - miny);
	}
}
