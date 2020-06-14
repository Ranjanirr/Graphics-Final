
package edu.mtholyoke.cs.comsc331.raycaster;

import java.util.ArrayList;

public interface Object3D {
	
	
	public float getR();
	public float getB();
	public float getG();
	public int getid();
	/**
	 * This is the method for when a ray hits a point on the object. The method
	 * is different for different object types it hits
	 * @param Rd
	 * @param eye
	 * @param intersections
	 */
	public void intersect(Vector Rd, Point3D eye, ArrayList<Intersection> intersections );
}
