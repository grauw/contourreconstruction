package wolkvlak;

public class EMST {


	private int	n = 10;				// the current number of terminals
	private Punt p[];				// the terminals
	private boolean m[][];			// the minimum spanning tree edges

	//mst - compute a minimum spanning tree using Prim's algorithm with "dumb"
	//heaps.  This is the fastest graph algorithm for complete graphs, though
	//we could do better geometrically - but with 10 terminals, why bother?
	private void mst() {
		
		int closest; 
		double minDist, d;
		
		double[] dist = new double[n];
		int[] neigh = new int[n];
		
		// initialize data structures
		for (int i = 0; i < n; i++) {
			dist[i] = p[0].distance(p[i]); 
			neigh[i] = 0;
			for (int j = 0; j < n; j++) {
				m[i][j] = false;
			}
		}
		
		// find terminal closest to current partial tree
		for (int i = 1; i < n; i++) {
			closest = -1;
			minDist = Integer.MAX_VALUE;
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
				d = p[j].distance(p[closest]);
				if (d < dist[j]) {
					dist[j] = d;
					neigh[j] = closest;
				}
			}
		}
	}

}
