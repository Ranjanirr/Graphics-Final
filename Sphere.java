package edu.mtholyoke.cs.comsc331.raycaster;

import java.util.ArrayList;

public class Sphere implements Object3D{
	private float x;
	private float y;
	private float z;
	private float r;
	private float g;
	private float b;
	private float rad;
	private int id;
	//
	/**
	 * Just a class for storing all the values in a sphere
	 * @param x
	 * @param y
	 * @param z
	 * @param rad
	 * @param r
	 * @param g
	 * @param b
	 * @param id - tells if it is a sphere
	 */
	public Sphere(float x, float y, float z, float rad, double r, double g, double b, int id) {
		this.x = x ;
		this.y = y;
		this.z = z;
		this.r = (float)r;
		this.rad = rad; 
		this.g = (float)g;
		this.b = (float)b;
		this.id = id;
		
	}
	public Point3D getOrigin () {
		return new Point3D(x,y,z);
	}
	public int getid() {
		return id;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	
	public float getZ() {
		return z;
	}
	public float getRad() {
		return rad;
	}
	public float getR() {
		return r;
	}
	public float getG() {
		return g;
	}
	public float getB() {
		return b;
	}
	/**
	 * This method given a direction and a starting point puts all the times the
	 * light from the starting point 
	 */
	public void intersect(Vector Rd, Point3D eye, ArrayList<Intersection> intersections ) {
		
		//This is the geomentric method
		Point3D So = new Point3D(getX(), getY(), getZ());
		Vector RS = new Vector((So.getX()-eye.getX()),(So.getY()-eye.getY()),(So.getZ()-eye.getZ()));
		float tp = dotProduct(RS, Rd);
		float d2 = dotProduct(RS, RS) - (tp*tp);
		float r2 = getRad()*getRad();
		if(d2<=r2) {
			//If it is not, no intersection
			double u = Math.sqrt(getRad()*getRad()- d2);
			double t1 = tp + u;
			double t2 = tp -u;
			//adds intersections for the front and back - will take one out when finding minimum
			intersections.add(new Intersection(t1, this, RayPoint((float)t1, Rd, eye)));
			intersections.add(new Intersection(t2, this, RayPoint((float)t2, Rd, eye)));
		}	
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
		/**
		 * 
		 * @param one
		 * @param two
		 * @return = a new vector which is the difference of the two given
		 */
		public Vector subtract(Vector one, Vector two) {
			float newX = one.getX()-two.getX();
			float newY = one.getY() - two.getY();
			float newZ = one.getZ() - two.getZ();
			return new Vector(newX, newY, newZ);
		}
		/**
		 * Finds a point using the ray equation - Rt = Ro + tRd
		 * @param scalar - the equilivant of t
		 * @param vector - equivilant of rd
		 * @param origin - equivalent of Ro
		 * @return
		 */
		public Point3D RayPoint(float scalar, Vector vector, Point3D origin) {
			Point3D point = new Point3D(1,1,1);
			float intersectx = (float) (origin.getX()+scalar*vector.getX());
			float intersecty = (float)(origin.getY()+scalar*vector.getY());
			float intersectz = (float)(origin.getZ()+scalar*vector.getZ());
			point.set(intersectx, intersecty, intersectz);
			return point;
		}
		
		
}