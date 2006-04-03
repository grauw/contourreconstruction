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
	
	Vector<Punt> squarepunten;
	
	public Wolk(File file, int k, int k_emst, int grid, double delta) {
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
	
	double[][] signeddistances;
	public boolean[][] visitedsquares;
	
	// Create vector with pairs of points
	public Vector<Punt> zetMeDieSquaresJeWéétToch(int grid, double delta) {
		double stepx = (maxx-minx) / grid;
		double stepy = (maxy-miny) / grid;
		
		// initialise arrays
//		signeddistances = new double[2*grid+1][2*grid+1];
//		for (int i=0; i<signeddistances.length; i++)
//			for (int j=0; j<signeddistances[i].length; j++)
//				signeddistances[i][j] = Double.NaN;
		visitedsquares = new boolean[2*grid][2*grid];
		// (initialised to ‘false’)
		int array_offset = grid/2;
		
		// try to find a starting point randomly
		int i = 0, j = 0, q;
		Random r = new Random();
		for (q=0; q<10000; q++) { //try max. 10000 times
			i = r.nextInt(grid);
			j = r.nextInt(grid);
			boolean topleft = signedDistance(i, j, grid, stepx, stepy) < delta;
			boolean topright = signedDistance(i, j+1, grid, stepx, stepy) < delta;
			boolean bottomleft = signedDistance(i+1, j, grid, stepx, stepy) < delta;
			boolean bottomright = signedDistance(i+1, j+1, grid, stepx, stepy) < delta;
			if (!(topleft == topright == bottomleft == bottomright)) break;
		}
		System.out.println("Found square after "+q+" tries");
		
		Vector<Punt> punten = new Vector<Punt>();
		return zetMeDieSquaresRecur(i, j, punten, array_offset, stepx, stepy, delta);
	}
	
	// modifies punten object
	private Vector<Punt> zetMeDieSquaresRecur(int i, int j, Vector<Punt> punten, int array_offset, double stepx, double stepy, double delta) {
		System.out.print("Square: "+i+", "+j);
		if (!visitedsquares[array_offset+i][array_offset+j]) {
			System.out.println(" - not visited");
			visitedsquares[array_offset+i][array_offset+j] = true;
			
			boolean topleft = signedDistance(i, j, array_offset, stepx, stepy) < delta;
			boolean topright = signedDistance(i+1, j, array_offset, stepx, stepy) < delta;
			boolean bottomleft = signedDistance(i, j+1, array_offset, stepx, stepy) < delta;
			boolean bottomright = signedDistance(i+1, j+1, array_offset, stepx, stepy) < delta;
			
			if (topleft    != topright)    punten.add(new Punt(minx+(i+.5)*stepx,miny+ j    *stepy));
			if (bottomleft != bottomright) punten.add(new Punt(minx+(i+.5)*stepx,miny+(j+1) *stepy));
			if (topleft    != bottomleft)  punten.add(new Punt(minx+ i    *stepx,miny+(j+.5)*stepy));
			if (topright   != bottomright) punten.add(new Punt(minx+(i+1) *stepx,miny+(j+.5)*stepy));
			
			// recur (if not visited previously)
			if (topleft    != topright)    punten = zetMeDieSquaresRecur(i, j-1, punten, array_offset, stepx, stepy, delta);
			if (bottomleft != bottomright) punten = zetMeDieSquaresRecur(i, j+1, punten, array_offset, stepx, stepy, delta);
			if (topleft    != bottomleft)  punten = zetMeDieSquaresRecur(i-1, j, punten, array_offset, stepx, stepy, delta);
			if (topright   != bottomright) punten = zetMeDieSquaresRecur(i+1, j, punten, array_offset, stepx, stepy, delta);
		} else {
			System.out.println(" - already visited");
		}
		return punten;
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
	
	public double signedDistance(int i, int j, int array_offset, double stepx, double stepy) {
		// cache signed distances
		// XXX CACHING doesn’t work!?
//		if (Double.isNaN(signeddistances[array_offset+i][array_offset+j])) {
//			signeddistances[array_offset+i][array_offset+j] = signedDistance(new Punt(minx+i*stepx,miny+j*stepy));
//		}
//		return signeddistances[array_offset+i][array_offset+j];
		return signedDistance(new Punt(minx+i*stepx,miny+j*stepy));
	}
	
}
