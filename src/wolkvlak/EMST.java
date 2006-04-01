package wolkvlak;

public class EMST {


	public int	n = 10;				// the current number of terminals
	public boolean m[][];			// the minimum spanning tree edges

	//mst - compute a minimum spanning tree using Prim's algorithm with "dumb"
	//heaps.  This is the fastest graph algorithm for complete graphs, though
	//we could do better geometrically - but with 10 terminals, why bother?
	public void mst(Wolk wolk) {
		n = wolk.size();
		
		int closest; 
		double minDist, d;
		
		m = new boolean[n][n];
		
		double[] dist = new double[n];
		int[] neigh = new int[n];
		
		// initialize data structures
		for (int i = 0; i < n; i++) {
			dist[i] = wolk.get(0).distance(wolk.get(i)); 
			neigh[i] = 0;
			for (int j = 0; j < n; j++) {
				m[i][j] = false;
			}
		}
		
		// find terminal closest to current partial tree
		for (int i = 1; i < n; i++) {
			closest = -1;
			minDist = Double.POSITIVE_INFINITY;
			for (int j = 1; j < n; j++) {
				if ((dist[j] != 0) && (dist[j] < minDist)) {
					closest = j;
					minDist = dist[j];
				}
			}
	  
			// set an edge from it to its nearest neighbor
			m[neigh[closest]][closest] = true;
			m[closest][neigh[closest]] = true;
		
			// update nearest distances to current partial tree
			for (int j = 1; j < n; j++) {
				d = wolk.get(j).distance(wolk.get(closest));
				if (d < dist[j]) {
					dist[j] = d;
					neigh[j] = closest;
				}
			}
		}
	}
	
	public void enrich(Wolk wolk) {
		for (int i=0; i<wolk.size(); i++) {
			Wolkpunt wolkpunt = wolk.get(i);
			for (int j=0; j<wolkpunt.dichtstebijpunten.length; j++) {
				int jindex = vindPuntIndex(wolk, wolkpunt.dichtstebijpunten[j]);
				m[i][jindex] = true;
				m[jindex][i] = true;
			}
		}
	}
	
	// vind positie van punt in wolk
	public int vindPuntIndex(Wolk wolk, Wolkpunt wolkpunt) {
		for (int i=0; i<wolk.size(); i++) {
			if (wolk.get(i) == wolkpunt) return i;
		}
		return -1;
	}

}
