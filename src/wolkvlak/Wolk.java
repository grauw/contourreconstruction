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
	
	int grid = 100;
	double delta = 1;
	Vector<Punt> squarepunten;
	
	public Wolk(File file, int k, int k_emst, int grid, double delta) {
		this.grid = grid;
		this.delta = delta;
		
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
			this.get(i).calcCentroid(this, k);
		}
		
		emst.mst(this);
		emst.enrich(this, k_emst);
		emst.klusCosts(this);
		emst.doeNormaals(this);
		
		squarepunten = zetMeDieSquaresJeWéétToch(grid, delta);
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
	
	public double signedDistance(Punt p) {
		Iterator<Wolkpunt> iter = iterator();
		Wolkpunt dichtstebij = null;
		double dichtstebijdistance = Double.POSITIVE_INFINITY;
		
		while (iter.hasNext()) {
			Wolkpunt punt = iter.next();
			double distance = p.distance(punt);
			if (distance < dichtstebijdistance) {
				dichtstebij = punt;
				dichtstebijdistance = distance;
			}
		}
		double ofthespacecowboy = dichtstebij.signedDistance(p);
		return ofthespacecowboy;
	}
	
	public Vector<Punt> zetMeDieSquaresJeWéétToch(double grid, double delta) {
		Vector<Punt> punten = new Vector<Punt>();
		
		double stepx = (maxx-minx) / grid;
		double stepy = (maxy-miny) / grid;
		
		for (int i=0; i<grid; i++) {
			for (int j=0; j<grid; j++) {
				Punt puntTL = new Punt(minx+i*stepx,miny+j*stepy);
				Punt puntTR = new Punt(minx+(i+1)*stepx,miny+j*stepy);
				Punt puntBL = new Punt(minx+i*stepx,miny+(j+1)*stepy);
				Punt puntBR = new Punt(minx+(i+1)*stepx,miny+(j+1)*stepy);
				
				boolean topleft = signedDistance(puntTL) < delta;
				boolean topright = signedDistance(puntTR) < delta;
				boolean bottomleft = signedDistance(puntBL) < delta;
				boolean bottomright = signedDistance(puntBR) < delta;
				
				if (topleft    != topright)    punten.add(new Punt(minx+(i+.5)*stepx,miny+ j    *stepy));
				if (bottomleft != bottomright) punten.add(new Punt(minx+(i+.5)*stepx,miny+(j+1) *stepy));
				if (topleft    != bottomleft)  punten.add(new Punt(minx+ i    *stepx,miny+(j+.5)*stepy));
				if (topright   != bottomright) punten.add(new Punt(minx+(i+1) *stepx,miny+(j+.5)*stepy));
			}
		}
		
		return punten;
	}
	
}
