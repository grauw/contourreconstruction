package wolkvlak;

import java.awt.geom.Point2D;
import java.util.Arrays;

public class Wolkpunt extends Point2D.Double {
	
	Point2D.Double centroid;
	
	public void calcCentroid(Wolk wolk, int k) {
		Wolkpuntpaar[] paren = vatDichsteBijPunten(wolk, k);
		centroid = new Point2D.Double();
		
		for (int i=0; i<k; i++) {
			centroid.x += paren[i].twee.x;
			centroid.y += paren[i].twee.y;
		}
		
//		public void findTangentLine(Wolkpunt point, int k)
		
		centroid.x /= k;
		centroid.y /= k;
		
//		System.out.println("Centroid: ("+centroid.x+", "+centroid.y+")");
		
		//vat k punten dichtste bij point
		//reken lijn uit
		
		
	}
	
	public Wolkpuntpaar[] vatDichsteBijPunten(Wolk wolk, int k) {
		Wolkpuntpaar[] paren = makePairs(wolk);
		
		Arrays.sort(paren);
		
		return paren;
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
	
}
