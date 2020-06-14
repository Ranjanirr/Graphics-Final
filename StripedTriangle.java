package edu.mtholyoke.cs.comsc331.raycaster;

import java.util.ArrayList;

import edu.mtholyoke.cs.comsc331.math.Point2D;

public class StripedTriangle implements Object3D {
	//Have three points
	//Calculate a vector normal = A,B,C by doing the cross product
	//Unit vector A,B,C
	//Calculate D - muliply each vector component by individual point
	//Intersect = takes in a ray, and an array of intersections
	private Point3D one;
	private Point3D two;
	private Point3D three;
	private float Red;
	private float Blue;
	private float Green;
	private float Stripe1;
	private float Stripe2;
	private int id;
	private Vector normal;
	//Create an intersect method
	/**
	 * Makes a new triangle with stripes
	 * @param one
	 * @param two
	 * @param three - they are the three points
	 * @param R
	 * @param G
	 * @param B
	 * @param id - tells if it is a triangle
	 */
	public StripedTriangle(Point3D one, Point3D two, Point3D three, double R, double G, double B, int id) {
		this.one = one;
		this.two = two;
		this.three = three;
		Red = (float)R;
		Blue = (float)B;
	    Stripe1 = (float)G;
	    Stripe2 = (float)(G-0.2);
	    this.id = id;
	    this.normal = Normal(subtract(one,two),subtract(three,two));
		
	}
	/**
	 * Can return the stripe color or non stripe - stripes are 5 pixels long
	 * @param XCoord - position that tells whether it should be a stripe
	 */
	public void setGreen(float XCoord) {
		double remainder = XCoord%5;
		double xtransfer = XCoord-remainder;
		if(xtransfer%2 == 0) {
			Green = (float) (Stripe1);
		}
		else {
			Green = (float)(Stripe2);
		}
	}
	
	public Vector getNormal() {
		return normal;
	}
	@Override
	public float getR() {
		// TODO Auto-generated method stub
		return Red;
	}

	@Override
	public float getB() {
		// TODO Auto-generated method stub
		return Blue;
	}

	@Override
	public float getG() {
		// TODO Auto-generated method stub
		return Green;
	}
	/**
	 *Method to intersect the triangle with a vector from the origin
	 */
	@Override
	public void intersect(Vector Rd, Point3D eye, ArrayList<Intersection> intersections) {
		// TODO Auto-generated method stub
		//Point of projection is not the same point of the plane itself!
		/**/
		Vector vector1 = subtract(one, two);
		Vector vector2 = subtract(three, two);
		
		
		float Vd = dotProduct(Normal(vector1, vector2), Rd);
		if (Vd != 0) {//means there is an intersection and ray is not parallel to triangle
			float t = dotProduct(subtract(one, eye), Normal(vector1, vector2))/Vd;
			Point3D intersected = RayPoint((float)t, Rd, eye);//point of intersection
			Point2D one2D = new Point2D(one.getX(), one.getY());//converts triangle to two dimensions
			Point2D two2D = new Point2D(two.getX(), two.getY());
			Point2D three2D = new Point2D(three.getX(), three.getY());
			Point2D intersected2D = new Point2D(intersected.getX(), intersected.getY());
			 boolean firstTrue = WithinLine(one2D, two2D, intersected2D);//tests whether the point is within triangle
			 boolean secondTrue = WithinLine(two2D, three2D, intersected2D);
			 boolean thirdTrue = WithinLine(three2D, one2D, intersected2D);
			 if (firstTrue && secondTrue && thirdTrue) {
				 //System.out.println("true");
				 intersections.add(new Intersection(t, this, RayPoint((float)t, Rd, eye)));
			 }
			 
			
		}
		
		

	}
	/**
	 * Normal vector to traingle 
	 * @param one - first vector
	 * @param two - second vector
	 * @return
	 */
	public Vector Normal(Vector one, Vector two) {
		/*cx = aybz − azby
		cy = azbx − axbz
		cz = axby − aybx*/
		float crossX = one.getY()*two.getZ()-one.getZ()*two.getY();
		float crossY = one.getZ()*two.getX()-one.getX()*two.getZ();
		float crossZ = one.getX()*two.getY()-one.getY()*two.getX();
		Vector unit =  UnitVector(new Vector(crossX,crossY,crossZ));
		return unit;
	}
	/**
	 * 
	 * @param vector = the vector to be normalized
	 * @return - vector the size of 1
	 */
	public Vector UnitVector(Vector vector) {
		float magnitude = vector.getX()*vector.getX()+vector.getY()*vector.getY()+vector.getZ()*vector.getZ();
		float unitX = vector.getX()/magnitude;
		float unitY = vector.getY()/magnitude;
		float unitZ = vector.getZ()/magnitude;
		return new Vector(unitX, unitY, unitZ);
	}
	
	public Vector subtract(Point3D one, Point3D two) {
		float newX = one.getX()-two.getX();
		float newY = one.getY() - two.getY();
		float newZ = one.getZ() - two.getZ();
		return new Vector(newX, newY, newZ);
	}
	/**
	 * 
	 * @param first
	 * @param second
	 * @return the dot product of points/vectors
	 */
	public float dotProduct(PointVector first, PointVector second) {
		float product = 0; 
		product += first.getX()*second.getX();
		product += first.getY()*second.getY();
		product += first.getZ()*second.getZ(); 
		return product;
	}
	@Override
	/**
	 * Returns if this is a triangle
	 */
	public int getid() {
		// TODO Auto-generated method stub
		return id;
	}
	/**
	 * Code for the formula of a ray to get a point of intersection
	 * @param scalar - t
	 * @param vector - Rd
	 * @param origin - Ro
	 * @return
	 */
	public Point3D RayPoint(float scalar, Vector vector, Point3D origin) {
		Point3D point = new Point3D(1,1,1);
		float intersectx = (float) (origin.getX()+scalar*vector.getX());
		float intersecty = (float)(origin.getY()+scalar*vector.getY());
		float intersectz = (float)(origin.getZ()+scalar*vector.getZ());
		setGreen(intersectx);
		point.set(intersectx, intersecty, intersectz);
		return point;
	}
	/**
	 * 
	 * @param first - the first point of line
	 * @param second - the second point of line
	 * @param intersected - the intersected point
	 * @return
	 */
	private boolean WithinLine(Point2D first, Point2D second, Point2D intersected) {
		double dy = second.getY() - first.getY();
		double dx = second.getX() - first.getX();
		if(dy == 0) {// it is a flat line
			if (dx>0) {
				if(intersected.getY()>second.getY()) {
					return true;
				}
				else {
					return false;
				}
			}
			else {//dx cannot be equal to zero because then it would be a point
				if(intersected.getY()<second.getY()) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		if((-dy*intersected.getX())+(dx*intersected.getY())-(dx*second.getY())+(dy*second.getX())> 0) {
			return true;//means it is within line
		}
		else  {
			return false; 
		}
		
		
	}
}
