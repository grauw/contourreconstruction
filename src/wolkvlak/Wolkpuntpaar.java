package wolkvlak;

public class Wolkpuntpaar implements Comparable<Wolkpuntpaar> {
	
	Wolkpunt één;
	Wolkpunt twee;
	double distance;
	
	public Wolkpuntpaar(Wolkpunt einz, Wolkpunt zwei) {
		één = einz;
		twee = zwei;
		distance = één.distance(twee);
	}
	
	public int compareTo(Wolkpuntpaar o) {
		Wolkpuntpaar anderpunt = (Wolkpuntpaar) o;
		if (distance < anderpunt.distance) return -1;
		if (distance > anderpunt.distance) return 1;
		return 0;
	}
	
}
