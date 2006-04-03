package wolkvlak;

public class EMST {


	public int n;					// the current number of terminals
	public boolean edge[][];		// the minimum spanning tree edges
	public double cost[][];			// the minimum spanning tree edges
	
	//mst - compute a minimum spanning tree using Prim's algorithm with "dumb"
	//heaps.  This is the fastest graph algorithm for complete graphs, though
	//we could do better geometrically - but with 10 terminals, why bother?
	public void mst(Wolk wolk) {
		n = wolk.size();
		
		int closest; 
		double minDist, d;
		
		edge = new boolean[n][n];
		// initialised to false
		
		double[] dist = new double[n];
		int[] neigh = new int[n];
		
		// initialize data structures
		for (int i = 0; i < n; i++) {
			dist[i] = wolk.get(0).centroid.distance(wolk.get(i).centroid);
			neigh[i] = 0;
			for (int j = 0; j < n; j++) {
				edge[i][j] = false;
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
			
			if (closest != -1) {
				// set an edge from it to its nearest neighbor
				edge[neigh[closest]][closest] = true;
				edge[closest][neigh[closest]] = true;
				
				// update nearest distances to current partial tree
				for (int j = 1; j < n; j++) {
					d = wolk.get(j).centroid.distance(wolk.get(closest).centroid);
					if (d < dist[j]) {
						dist[j] = d;
						neigh[j] = closest;
					}
				}
			} else {
				// gat????
			}
		}
	}
	
	// maak riemannian graph van de MST door de punten in k neighbourhood
	// toe te voegen
	public void enrich(Wolk wolk, int k) {
		for (int i=0; i<wolk.size(); i++) {
			Wolkpunt wolkpunt = wolk.get(i);
			Wolkpunt[] dichtstebijpunten = wolkpunt.vatDichstbijCentroids(wolk, k);
			for (int j=0; j<k; j++) {
				int jindex = vindPuntIndex(wolk, dichtstebijpunten[j]);
				edge[i][jindex] = true;
				edge[jindex][i] = true;
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
	
	// bereken de cost voor elke edge
	public void klusCosts(Wolk wolk) {
		cost = new double[wolk.size()][wolk.size()];
		for (int i=0; i<wolk.size(); i++) {
			for (int j=0; j<wolk.size(); j++) {
				if (edge[i][j]) {
					cost[i][j] = 1 - Math.abs(wolk.get(i).normalVector.dot(wolk.get(j).normalVector));
					cost[i][j] += 1 - Math.abs(wolk.get(j).minus(wolk.get(i)).normalize().dot(wolk.get(i).tangentVector));
				}
			}
		}
	}
	
	private boolean[] geweestePunkten;
	public boolean[][] geweesteEdges;
	
	// zorg dat alle normalen in de goede richting staan
	public void doeNormaals(Wolk wolk) {
		double maximumx = Double.NEGATIVE_INFINITY;
		int maximumi = -1;
		
		geweesteEdges = new boolean[wolk.size()][wolk.size()];
		
		geweestePunkten = new boolean[wolk.size()];
		for (int i=0; i<wolk.size(); i++) {
			geweestePunkten[i] = false;
		}
		
		// vind meest linkse punt
		for (int i=0; i<wolk.size(); i++) {
			if (wolk.get(i).x > maximumx) {
				maximumx = wolk.get(i).x;
				maximumi = i;
			}
		}
		
		// zorg dat normaal van meest linkse punt naar links wijst
		if (wolk.get(maximumi).normalVector.x < 0) {
			wolk.get(maximumi).normalVector = wolk.get(maximumi).normalVector.times(-1);
		}
		
		// traverse MST vanaf meest linkse punt
		geweestePunkten[maximumi] = true;
		
		while(true) {
			// vind edge met laagste cost
			double laagstecost = Double.POSITIVE_INFINITY;
			int laagstei = -1;
			int laagstej = -1;
			for (int i=0; i<wolk.size(); i++) {
				if (geweestePunkten[i]) {
					for (int j=0; j<wolk.size(); j++) {
						if (j != i && !geweestePunkten[j] && edge[i][j]) {
							if (cost[i][j] < laagstecost) {
								laagstecost = cost[i][j];
								laagstei = i;
								laagstej = j;
							}
						}
					}
				}
			}
			if (laagstei != -1) {
				// voor laagste edge, fix de normaal
				geweestePunkten[laagstej] = true;
				geweesteEdges[laagstei][laagstej] = true;
				if (wolk.get(laagstei).normalVector.dot(wolk.get(laagstej).normalVector) < 0) {
					wolk.get(laagstej).normalVector = wolk.get(laagstej).normalVector.times(-1);
				}
			} else {
				//klaar
				break;
			}
		};
		
	}
}
