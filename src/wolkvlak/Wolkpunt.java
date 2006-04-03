package wolkvlak;

import java.awt.geom.Point2D;
import java.util.Arrays;

public class Wolkpunt extends Punt {
	
	Punt centroid;
	Punt tangentVector;
	Punt normalVector;

	public double signedDistance(Punt p) {		
		return p.minus(centroid).dot(normalVector);		
	}
	
	public double signedDistance(Punt p, double delta) {
		double sdistance = signedDistance(p);
		Punt z = centroid.minus(normalVector.times(sdistance));
		//XXX sheet zegt d(z,X) < d, niet d(z,p_i)...
		// (maar ik denk dat het een aardige benadering is)
		if (z.distance(this) < delta) return sdistance;
		else return java.lang.Double.POSITIVE_INFINITY;
	}
	
	public void calcCentroid(Wolk wolk, int k) {
		Wolkpunt[] punten = vatDichstbijPunten(wolk, k);
		
		centroid = new Punt();
		for (int i=0; i<k; i++) {
			centroid.x += punten[i].x;
			centroid.y += punten[i].y;
		}
		
		centroid.x /= k;
		centroid.y /= k;
		
		//vat k punten dichtste bij point
		//reken lijn uit		
		
		double covXX = covarianceXX(punten, centroid);
		double covYY = covarianceYY(punten, centroid);
		double covXY = covarianceXY(punten, centroid);
		
		Point2D.Double eigenValues = eigenValues(covXX, covYY, covXY);
		
		tangentVector = eigenVector(covXX, covXY, eigenValues.x);
		normalVector = eigenVector(covXX, covXY, eigenValues.y);		
		
	}
	
	public Wolkpunt[] vatDichstbijPunten(Wolk wolk, int k) {
		Wolkpuntpaar[] paren = makePairs(wolk);
		Wolkpunt[] punten = new Wolkpunt[k];
		
		Arrays.sort(paren);
		
		for(int i = 0; i < k; i++) {
			punten[i] = paren[i].twee;
		}
		
		return punten;
	}
	
	public Wolkpunt[] vatDichstbijCentroids(Wolk wolk, int k) {
		Wolkpuntpaar[] paren = makePairs(wolk);
		Wolkpunt[] punten = new Wolkpunt[k];
		
		Arrays.sort(paren);
		
		for(int i = 0; i < k; i++) {
			punten[i] = paren[i].twee;
		}
		
		return punten;
	}
	
	public Wolkpuntpaar[] makePairs(Wolk wolk) {
		int n = wolk.size();
		Wolkpuntpaar[] paar = new Wolkpuntpaar[n-1];
		
		int pos = 0;
		for (int i = 0; i < n; i++) {
			if (wolk.get(i) != this) {
				paar[pos++] = new Wolkpuntpaar(this, wolk.get(i));
			}
		}
		
		return paar;
	}	
	
	private double covarianceXX(Wolkpunt[] punten, Point2D.Double centroid)
	{
		double total = 0;
		for(int i = 0; i < punten.length; i++) {
			total += Math.pow(punten[i].x - centroid.x, 2);
		}
		return total / (punten.length - 1);
	}
		
	private double covarianceYY(Wolkpunt[] punten, Point2D.Double centroid)
	{
		double total = 0;
		for(int i = 0; i < punten.length; i++) {
			total += Math.pow(punten[i].y - centroid.y, 2);
		}
		return total / (punten.length - 1);
	}
		
	private double covarianceXY(Wolkpunt[] punten, Point2D.Double centroid)
	{
		double total = 0;
		for(int i = 0; i < punten.length; i++) {
			total += (punten[i].x - centroid.x) * (punten[i].y - centroid.y);
		}
		return total / (punten.length - 1);
	}
		
	private Punt eigenValues(double covXX, double covYY, double covXY)
	{
		double a = 1;
		double b = -(covXX + covYY);
		double c = covXX*covYY - covXY*covXY;
		
		double D = Math.sqrt(b*b - 4*a*c);
		
		double eigenValue1, eigenValue2;
		
/*		if (D < 0) {
			//geen uitkomst
		} else if (D == 0) {
			//één uitkomst
		} else {
			//twee uitkomsten
		}
*/		
		eigenValue1 = (-b + D) / (2 * a);
		eigenValue2 = (-b - D) / (2 * a);
		
		Punt eigenValues = new Punt();
		
		if (eigenValue1 < eigenValue2) {
			eigenValues.x = eigenValue2;
			eigenValues.y = eigenValue1;
		} else {
			eigenValues.x = eigenValue1;
			eigenValues.y = eigenValue2;
		}
		
		return eigenValues;
	}
	
	private Punt eigenVector(double covXX, double covXY, double eigenValue) {
		Punt vector;
		if (covXY == 0) {
			vector = new Punt(1.0, Math.signum(-(covXX - eigenValue)));
		} else {
			vector = new Punt(1.0, -(covXX - eigenValue) / covXY);
		}
		return vector.normalize();
	}
	
	
	
}
