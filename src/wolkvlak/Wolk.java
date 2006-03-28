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
		computeDistances();
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
	
	public Double[][] computeDistances() {
		
		int n = this.elementCount;
		Double[][] distances = new Double[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {				
				distances[i][j] = this.get(i).distance(this.get(j));
			}
		}
		
		return distances;
		
		/*
		Iterator<Point2D.Double> iter1 = this.iterator();		
		while (iter1.hasNext()) {
			Point2D.Double point1 = iter1.next();	
			
			Iterator<Point2D.Double> iter2 = this.iterator();
			while (iter2.hasNext()) {				
				Point2D.Double point2 = iter2.next();				
				double d = point1.distance(point2);				
			}
			
					
		}
		*/
	}
	
	public void findTangentLine(Point2D.Double point, int k) {
		
		//vat k punten dichtste bij point
		//reken lijn uit
		
		
	}
}
