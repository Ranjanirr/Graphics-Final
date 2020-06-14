package edu.mtholyoke.cs.comsc331.raycaster;
import java.util.ArrayList;

import java.awt.Color;
import java.lang.Math;

import edu.mtholyoke.cs.comsc331.math.Point;
import edu.mtholyoke.cs.comsc331.ppm.PPM;
public class RayCaster {
	private Point3D projectcenter;
	private Point3D eye;
	private float height;
	private float width;
	private Light light;
	private AreaLight area;
	private ArrayList<Object3D> objects;
	/**
	 *  The default constructor - brings new objects
	 */
	public  RayCaster() {
		objects = new ArrayList<Object3D>();
		light = new Light();
		area = new AreaLight();
		addObject3D(new Sphere(0,-25,2500,25, .25,.550,.650,3));
	    addObject3D(new Sphere(60,-25,2500,25, .250,.20,.250,3));
		addObject3D(new Sphere(-50,-25,2300,25, .500,.300,.10,3));
		//addObject3D(new Cube((float).40, (float).40,(float) 0.180, -150, -25, 2400, 35,  5));
		addObject3D(new StripedTriangle(new Point3D(-250,-51,5000),new Point3D(0,-50,2600), new Point3D(-250,-49,-200), .60, .200, .800, 4));
	    addObject3D(new StripedTriangle(new Point3D(-250,-49,-200),new Point3D(0,-50,2600), new Point3D(250,-49,-200), .60, .200, .600, 4));
	    addObject3D(new StripedTriangle(new Point3D(250,-49,-200),new Point3D(0,-50,2600), new Point3D(250,-51,5000), .60, .200, .800, 4));
	    addObject3D(new StripedTriangle(new Point3D(250,-51,5000),new Point3D(0,-50,2600), new Point3D(-250,-51,5000), .60, .200, .600, 4));
	   
	    setEyePosition(0, 0, 4000);
		
	}
	
	
	/**
	 * Sets the x,y,z position of the "eye" or "camera".
	 * This is the origin of the ray-caster's rays.
	 *
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setEyePosition(float x, float y, float z) {
		eye = new Point3D(x,y,z);
	}

	/**
	* Sets the x,y,z position and the width and height of the projection plane in world coordinates.
	*  The specified x,y,z position is the *center* of the projection plane.  And the projection plane is
	* always parallel with the XY plane (it has a constant z).
	 * @param x
	 * @param y
	 * @param z
	 * @param width
	 * @param height
	 */
	public void setProjectionPlane(float x, float y, float z, float width, float height) {
		projectcenter = new Point3D(x,y,z);
		
		this.width = width;
		this.height = height; 
		
 	}

	/**
	 * Adds 3D objects to the world.
	 * @param obj
	 */
	public void addObject3D(Object3D obj) {
		 objects.add(obj);
		
	}


	/**
	 * Sets the background color the RayCaster should use when rendering.
	 * The background color is the color the render method should set pixels
	 * for which there is no "hit" (intersection with an Object3D)
	 * Height, width and ppm are used for setting up the display
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setBackgroundColor(double r, double g, double b, int width, int height, PPM ppm) {
		for(int i = 0; i< width; i++) {
			for(int j = 0; j<height; j++) {
				ppm.setPixel(i, j, new Color((float)r,(float)g,(float)b));
			}
		}
	}

	/**
	 * Renders and returns a PPM with the Object3D that were added to the world rendered.
	 * If renderDistance is true then instead of using and Object3Ds color,
	 * pixels should a shade of gray scaled to the "hits" distance from the eye.
	 *       - If the distance less than or equal to minDistance, then the pixel should be white.
	 *       - If the distance is greater than or equal to maxDistance, then the pixel should be black
	 *       - else it should be scale proportionally from the range minDistance - maxDistance to the range 0 - 255
	 * @param width - PPM width in pixels
	 * @param height - PPM height in pixels
	 * @param renderDistance - if renderDistance is true pixel color should be a shade
	 *                         of gray scaled to the "hits" distance from the eye,
	 *                         else the pixel should be the Object3D's color
         * @param minDistance - distance (in world coordinates) used for scaling the renderDistance.
         * @param maxDistance - distance (in world coordinates) used for scaling the renderDistance.
	 * @return
	 */
	public PPM render(int width, int height) {
		
			return colorFunction(width, height);
			
		
	}
	/**
	 * 
	 * @param width
	 * @param height
	 * @return - a PPM with all the points with their respective colors
	 */
	private PPM colorFunction(int width, int height) {
		PPM myPPM = new PPM(width, height);
		setBackgroundColor(.800,.255,.255, width, height, myPPM);
		setProjectionPlane(250, 250, 100, width, height);
		for (int i = -width/2; i< width/2; i++) {//note: the value of center z is the value for all z
			for (int j = height/2; j>-height/2; j-- ) {//goes through every point  of column in each row
				ArrayList intersections = new ArrayList<Intersection>(1);
				float magnitude = (float) Math.sqrt((i-eye.getX())*(i-eye.getX())+(j-eye.getY())*(j-eye.getY())+(projectcenter.getZ() -eye.getZ())*(projectcenter.getZ() -eye.getZ()));
				//this is the direction from the point to the pixel on the PPM
				Vector Rd = new Vector((i - eye.getX())/magnitude,(j - eye.getY())/magnitude, (projectcenter.getZ() - eye.getZ())/magnitude);
				for (int l = 0; l<objects.size(); l++) {
					Object3D object =  objects.get(l);//Puts every object's intersection in the intersections array
					object.intersect( Rd,  eye, intersections);//You put the object in the intersections
					
				}
				Intersection minimumIntersection = findMinimum(intersections);//the intersection with the smallest distance
				if (minimumIntersection != null) {//means there is an intersection
					Object3D  intersected =  minimumIntersection.getObject();
					//returns the object that was intersected
					
					if(outOfBounds(minimumIntersection.intersectpoint().getX(),minimumIntersection.intersectpoint().getY(), height, width) == false) {
						    Point3D lightPoint = new Point3D(-10,20,2570);//point light
					    	Vector lightVector = new Vector(-30,5, 0);//directional light
					        
					    	Color areaColor = area.lightup(minimumIntersection,lightPoint ,eye, objects);
					        Color directional = light.lightup(minimumIntersection,lightVector ,eye, objects);
					    	//these are multiple lights
					    	myPPM.setPixel((int)translateX(i), (int)translateY(j), new Color(areaColor.getRed()+directional.getRed(),areaColor.getGreen()+directional.getGreen(), areaColor.getBlue()+directional.getBlue()));
					    	//myPPM.setPixel((int)translateX(i), (int)translateY(j),light.lightup(minimumIntersection,lightVector ,eye, objects));
					    	//Fill in the color at i,j.
					}
					
				}
				
			}
		}
		return myPPM;
	}
	/**
	 * 
	 * @param X
	 * @param Y
	 * @param height
	 * @param width
	 * @return whether the point on the PPM interescted is beyond the screen
	 */
	private boolean outOfBounds(float X, float Y, float height, float width) {
		boolean out = false;
		if(X<=-width/2||X>=width/2) {
			out = true;
		}
		else if(Y<=-height/2||Y>=height/2) {
			out = true;
		}
		return out;
	}
	/**
	 * Based on the distance of the pixel, it calculates the color of the pixel
	 * @param distance - distance to compare max and min
	 * @param maxDistance
	 * @param minDistance
	 * @return
	 */
	public Color findColor(float distance, float maxDistance, float minDistance) {
		float difference = maxDistance - minDistance;
		float colorfloat = ((distance-minDistance)/difference);
		int color = 255 - (int)(colorfloat*255);
		return new Color(color, color, color);
	}
	/**
	 * Translate methods translate from world to computer coordinates
	 * @param height - height in world coordinates
	 * @return
	 */
	public float translateY(float height) {
		return projectcenter.getY() - height;//subraction accounts for upside down
	}
	/**
	 * 
	 * @param width - width in world coordinates
	 * @return
	 */
	public float translateX(float width) {
		return projectcenter.getX() + width;
	}
	/**
	 * 
	 * @param intersections  - an array of intersection classes
	 * @return the minimum distance of intersections
	 */
	public Intersection findMinimum(ArrayList<Intersection> intersections) {
		if(intersections.size() == 0) {
			return null;
		}
		Intersection minimum = intersections.get(0);
		for (int i = 0; i<intersections.size(); i++) {
			if (minimum.getDistance()>intersections.get(i).getDistance() && intersections.get(i).getDistance()>=0) {
				minimum = intersections.get(i);
			}
		}
		if (minimum.getDistance()>= 0) {
			return minimum;
		}
		return null;
		
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
	 * Create a main that shows off your RayCaster by creating and displaying an interesting PPM.
	 * @throws InterruptedException 
	 *
	 */
	public static void main(String[] args) throws InterruptedException {
		RayCaster caster = new RayCaster();
		PPM displayer = caster.render(500, 500);
		displayer.display();
		
	}
	public Vector UnitVector(Vector vector) {
		float magnitude = (float) Math.sqrt(vector.getX()*vector.getX()+vector.getY()*vector.getY()+vector.getZ()*vector.getZ());
		float unitX = vector.getX()/magnitude;
		float unitY = vector.getY()/magnitude;
		float unitZ = vector.getZ()/magnitude;
		return new Vector(unitX, unitY, unitZ);
	}
	
}
