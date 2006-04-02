package wolkvlak;

public class Wolkpuntcentroidpaar implements Comparable<Wolkpuntpaar> {
	
	Wolkpunt één;
	Wolkpunt twee;
	double distance;
	
	public Wolkpuntcentroidpaar(Wolkpunt einz, Wolkpunt zwei) {
		één = einz;
		twee = zwei;
		distance = één.centroid.distance(twee.centroid);
	}
	
	public int compareTo(Wolkpuntpaar o) {
		Wolkpuntpaar anderpunt = (Wolkpuntpaar) o;
		if (distance < anderpunt.distance) return -1;
		if (distance > anderpunt.distance) return 1;
		return 0;
	}
	
}
