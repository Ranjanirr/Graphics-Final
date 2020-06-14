package edu.mtholyoke.cs.comsc331.raycaster;

import java.util.ArrayList;

public class Cube implements Object3D{
	
	protected float red;
	protected float green;
	protected float blue;
	
	// center of cube
	protected float x;
	protected float y;
	protected float z;
	protected int id;
	// shortest distance from the center to any of the faces
	protected float rad;
	
	
	
	// planes that make up the cube
	protected NormPlane rightP;
	protected NormPlane leftP;
	protected NormPlane topP;
	protected NormPlane bottomP;
	protected NormPlane backP;
	protected NormPlane frontP;
	
	Point3D rightNorm = new Point3D (1, 0, 0);
	Point3D leftNorm = new Point3D (-1, 0, 0);
	Point3D topNorm = new Point3D (0, 1, 0);
	Point3D bottomNorm = new Point3D (0, -1, 0);
	Point3D backNorm = new Point3D (0, 0, 1);
	Point3D frontNorm = new Point3D (0, 0, -1);
	
	public Cube(float r, float g, float bl, float xCent, float yCent, float zCent, float radius, int identify) {
		red = r;
		green = g;
		blue = bl;
		x = xCent;
		y = yCent;
		z = zCent;
		rad = radius;
		id = identify;
		Point3D rightNorm = new Point3D (1, 0, 0);
		Point3D leftNorm = new Point3D (-1, 0, 0);
		Point3D topNorm = new Point3D (0, 1, 0);
		Point3D bottomNorm = new Point3D (0, -1, 0);
		Point3D backNorm = new Point3D (0, 0, 1);
		Point3D frontNorm = new Point3D (0, 0, -1);
		rightP = new NormPlane(0, 255, 255, leftNorm, x+rad, y, z);
		leftP = new NormPlane(0, 255, 255, leftNorm, x-rad, y, z);
		topP = new NormPlane(255, 0, 255, topNorm, x, y+rad, z);
		bottomP = new NormPlane(255, 0, 0, bottomNorm, x, y-rad, z);
		backP = new NormPlane(255, 255, 0, backNorm, x, y, z+rad);
		frontP = new NormPlane(0, 0, 255, frontNorm, x, y, z-rad);

	}
	public void intersect(Vector Rd, Point3D eye, ArrayList<Intersection> intersections) {
		// TODO Auto-generated method stub
		//Point of projection is not the same point of the plane itself!
		/**/
		float t = getT(Rd, eye);
		if (t!= -1) {
			intersections.add(new Intersection(t, this, RayPoint((float)t, Rd, eye)));
				
			
		}
		else {
			//System.out.println("True");	
			//Means that every vector is parallel
		}
		

	}
	public float getT(PointVector direction, PointVector origin) {
		// tnear = max of mins, tfar = min of max
		float tnear = Float.NEGATIVE_INFINITY;
		float tfar = Float.POSITIVE_INFINITY;
		float var;
		// gets intersection
		float rightT =rightP.getT( direction,  origin);
		float leftT =leftP.getT( direction,  origin);
		//checks if valid
		if (rightT > 0 || leftT > 0) {
			// leftT = min, rightT = max
			if (leftT > rightT) {
				var = leftT;
				leftT = rightT;
				rightT = var;
			}
			if (rightT < Float.POSITIVE_INFINITY) {
				// checks for min
				if (rightT < tfar) tfar = rightT;
			} else return -1;
			if (leftT > Float.NEGATIVE_INFINITY) {
				if (leftT > tnear) tnear = leftT;
			} else return -1;
		} else return -1;
		
		float topT =topP.getT(direction,  origin);
		float bottomT =bottomP.getT(direction,  origin);
		//checks if valid
		if (topT > 0 || bottomT > 0) {
			// bottomT = min, topT = max
			if (bottomT > topT) {
				var = bottomT;
				bottomT = topT;
				topT = var;
			}
			if (topT < Float.POSITIVE_INFINITY) {
				if (topT < tfar) tfar = topT;
			} else return -1;
			if (bottomT > Float.NEGATIVE_INFINITY) {
				if (bottomT > tnear) tnear = bottomT;
			} else return -1;
		} else return -1;
		
		float backT =backP.getT(direction,  origin);
		float frontT =frontP.getT(direction,  origin);
		//checks if valid
		if (backT > 0 || frontT > 0) {
			// frontT = min, backT = max
			if (frontT > backT) {
				var = frontT;
				frontT = backT;
				backT = var;
			}
			if (backT < Float.POSITIVE_INFINITY) {
				if (backT < tfar) tfar = backT;
			} 
			if (frontT > Float.NEGATIVE_INFINITY) {
				if (frontT > tnear) tnear = frontT;
			} 
		} 
		
		// if all max of min is behind eye, or all min of max is behind eye
		if (tnear < 0 || tfar < 0) {
			//System.out.println("a");
			return -1;
		}
		// if min of max < max of min, no intersection
		if (tfar < tnear) {
			//System.out.println("b");
			return -1;
		}
		//System.out.println("("+tnear+","+tfar+")");
		//System.out.println("("+ray.getDirection().x+","+ray.getDirection().y+","+ray.getDirection().z+")");
		//System.out.println("("+ray.getOrigin().x+","+ray.getOrigin().y+","+ray.getOrigin().z+")");
		return tnear;
	}

	
	public Point3D getTnorm(PointVector camera, PointVector direction) {
		Point3D norm;
		float t = getT(direction, camera);
		if (t == rightP.getT(direction,  camera)) {
			norm = new Point3D(1, 0, 0);
		} else if (t == leftP.getT(direction,  camera)) {
			norm = new Point3D(-1, 0, 0);
		} else if (t == topP.getT(direction,  camera)) {
			norm = new Point3D(0, 1, 0);
		} else if (t == bottomP.getT(direction,  camera)) {
			norm = new Point3D(0, -1, 0);
		} else if (t == backP.getT(direction,  camera)) {
			norm = new Point3D(0, 0, 1);
		} else norm = new Point3D(0, 0, -1);
		return norm;
	}

	
	public float getR() {
		return red;
	}

	
	public float getG() {
		return green;
	}

	
	public float getB() {
		return blue;
	}
	public Point3D RayPoint(float scalar, Vector vector, Point3D origin) {
		Point3D point = new Point3D(1,1,1);
		float intersectx = (float) (origin.getX()+scalar*vector.getX());
		float intersecty = (float)(origin.getY()+scalar*vector.getY());
		float intersectz = (float)(origin.getZ()+scalar*vector.getZ());
		point.set(intersectx, intersecty, intersectz);
		return point;
	}
	@Override
	public int getid() {
		// TODO Auto-generated method stub
		return id;
	}
}
	