package wolkvlak;

import java.awt.geom.Point2D;

public class Punt extends Point2D.Double {
	
	public Punt() {
		super();
	}
	
	public Punt(double x, double y) {
		super(x, y);
	}
	
	// Punt - Punt
	public Punt minus(Punt punt) {	
		return new Punt(
				x - punt.x,
				y - punt.y
			);
	}
	
	//Inproduct
	public double dot(Punt punt) {			
		return (x * punt.x) + (y * punt.y);		
	}
	
	//Uitproduct
	public double cross(Punt punt) {
		return (x * punt.y) - (y * punt.x);
	}
	
	//Vectorlengte
	public double length() {
		return (double) Math.sqrt(dot(this));
	}
	
	public Punt normalize() {
		return this.times(1.0 / length());
	}
	
	// Vector * scalar
	public Punt times(double scalar) {	
		return new Punt(
				x * scalar,
				y * scalar
			);
	}
	
}
