package wolkvlak;

import java.awt.geom.Point2D;
import java.util.Arrays;

public class Wolkpunt extends Punt {
	
	Punt centroid;
	Punt eigenVector1;
	Punt eigenVector2;
	
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
		
		double covXX = covariance(punten, centroid, true, true);
		double covYY = covariance(punten, centroid, false, false);
		double covXY = covariance(punten, centroid, true, false);
		
		Point2D.Double eigenValues = eigenValues(covXX, covYY, covXY);
		
		eigenVector1 = eigenVector(covXX, covXY, eigenValues.x);
		eigenVector2 = eigenVector(covXX, covXY, eigenValues.y);		
		
		
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
	
	private double covariance(Wolkpunt[] punten, Point2D.Double centroid, boolean x1, boolean x2)
	{
		//AAAAANNNPPAAASSSSEN, gestolen code!!!
		
		int i, cPoints = punten.length;
		double total = 0;
		
		if(x1 && x2)
		{
			for(i = 0; i < cPoints; i++)
			{
				total += Math.pow(punten[i].x - centroid.x, 2);
			}
		}
		else if(!x1 && !x2)
		{
			for(i = 0; i < cPoints; i++)
			{
				total += Math.pow(punten[i].y - centroid.y, 2);
			}
		}
		else
		{
			for(i = 0; i < cPoints; i++)
			{
				total += (punten[i].x - centroid.x) * (punten[i].y - centroid.y);
			}
		}

		return total / (double)(cPoints - 1);
	}
		
	private Punt eigenValues(double covXX, double covYY, double covXY)
	{
		//AAAAANNNPPAAASSSSEN, gestolen code!!!
		
		double eigenValue1, eigenValue2;
		
		// 2x2 Matrix:
		// a-L	b
		// c	d-L
		
		// Determinant	:	(a-L)(d-L) - cb
		// Omgebouwd	:	L^2 - adL + ad - cb
		// Als polynoom met ABC -> AL^2 + BL + C
		// a = covXX
		// b = covXY
		// c = covYX
		// d = covYY
		// A = 1
		// B = -(covXX * covYY)
		// C = (covXX * covYY) - (covYX * covXY)
		
		double a				= 1;
		double b				= -(covXX + covYY);
		double c				= (covXX * covYY) - (covXY * covXY);
		
		double sqrt_det_abc	= (double)Math.sqrt((b * b) - (4 * a * c));
		double a2			= 2 * a;
				
		eigenValue1			= (-b + sqrt_det_abc) / a2;
		eigenValue2			= (-b - sqrt_det_abc) / a2;
		
		Punt eigenValues	= new Punt();
		
		if(eigenValue1 < eigenValue2) 
		{
			eigenValues.x = eigenValue2;
			eigenValues.y = eigenValue1;
		}
		else 
		{
			eigenValues.x = eigenValue1;
			eigenValues.y = eigenValue2;			
		}
		
		return eigenValues;
	}
	
	private Punt eigenVector(double covXX, double covXY, double eigenValue)
	{	

		return new Punt(1, -(covXX - eigenValue) / covXY).normalize();
	}
	
	
	
}
