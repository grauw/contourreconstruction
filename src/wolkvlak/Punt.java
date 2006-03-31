package wolkvlak;

import java.awt.geom.Point2D;

public class Punt extends Point2D.Double {

	public Punt()
	{
		x = 0;
		y = 0;
	}
	
	public Punt(double nx, double ny)
	{
		x = nx;
		y = ny;
	}
	
	
	//Inproduct
	public double dot(Punt punt)
	{			
		return (this.x * punt.x) + (this.y * punt.y);		
	}
	
	//Uitproduct
	public double cross(Punt punt)
	{
		return (this.x * punt.y) - (this.y * punt.x);
	}
	
	//Vectorlengte
	public double length()
	{
		return (double)Math.sqrt(this.dot(this));
	}
	
	public Punt normalize()
	{
		return this.div(this.length());
	}
	
	// Vector / Scalar
	public Punt div(double scalar) 
	{	
		return new Punt(
				this.x / scalar,
				this.y / scalar
			);
	}
	
}
