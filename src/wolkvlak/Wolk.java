package wolkvlak;

import java.awt.geom.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Wolk extends Vector<Wolkpunt> {
	static final long serialVersionUID = 123172398;
	
	double maxx = Double.NEGATIVE_INFINITY;
	double maxy = Double.NEGATIVE_INFINITY;
	double minx = Double.POSITIVE_INFINITY;
	double miny = Double.POSITIVE_INFINITY;
	EMST emst = new EMST();
	
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
		
		for (int i=0; i<this.size(); i++) {
			this.get(i).calcCentroid(this, 4);
		}
		
		emst.mst(this);
//		emst.enrich(this);
	}
	
	public void addPoint(double x, double y) {
		Wolkpunt point = new Wolkpunt();
		point.setLocation(x, y);
		this.add(point);
		maxx = Math.max(x, maxx);
		maxy = Math.max(y, maxy);
		minx = Math.min(x, minx);
		miny = Math.min(y, miny);
	}
	
	public Wolkpunt getPoint(int p) {
		return this.get(p);
	}
	
	public Rectangle2D.Double getBoundingRectangle() {
		return new Rectangle2D.Double(minx, miny, maxx - minx, maxy - miny);
	}
	
}
