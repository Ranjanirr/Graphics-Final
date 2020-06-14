
package edu.mtholyoke.cs.comsc331.raycaster;

import java.awt.Color;

public class Intersection{
	private double distance;
	private Object3D object;
	private Point3D intersectpoint;
	/**
	 * An intersection has two data, distance and the object-
	 * object is mainly used to find color
	 * @param distance
	 * @param object
	 */
	public Intersection(double distance, Object3D object, Point3D intersectpoint) {
		this.distance = distance;
		this.object = object;
		this.intersectpoint = intersectpoint;
	}
	public double getDistance() {
		return distance;
	}
	public Object3D getObject() {
		return object; 
	}
	public Point3D intersectpoint() {
		return intersectpoint;
	}
	
}
