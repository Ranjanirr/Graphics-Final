package edu.mtholyoke.cs.comsc331.raycaster;

import java.awt.Color;


import java.util.ArrayList;
import java.lang.Math;
public class AreaLight {
	/**
	 * 
	 * @param intersection = the intersection with the properties you want to light up
	 * @param lightLocation = the point where the light is coming from
	 * @param camera = eye
	 * @param objects = an array of objects mainly used to test for blocking shadows
	 * @return
	 */
public Color lightup(Intersection intersection,  Point3D lightLocation, PointVector camera, ArrayList<Object3D> objects) {
		Color ambient = ambient(intersection.getObject(), intersection);
	    if(intersection.getObject().getid() == 4 ) {
			//Tests whether it is the floor
	    	float intensity = testShadowGroup(lightLocation, intersection.intersectpoint(), objects);
	    	
	    	if(intensity<1) {
	    	//if there is, there is an intensity, and less the intensity, more the shadow	
	    		float red = (float)ambient.getRed()/255;
	    		float green = (float)ambient.getGreen()/255;
	    		float blue = (float)ambient.getBlue()/255;
	    		return new Color(intensity*red, intensity*green, intensity*blue);
	    	}
	    	else {
	    		return ambient;
	    	}
			
		}
	    else if (intersection.getObject().getid() == 5) {
	    	//Different light stuff
    		return PhongPoint(intersection, lightLocation, ((Cube) intersection.getObject()).getTnorm(camera, lightLocation), camera);
	    }
		else {
			
			if(testShadow(lightLocation, intersection.intersectpoint(), objects )) {
				return ambient;
			}
		   else {
			   //Based on the normal and lightLocation, you can calculate the Phong if there is no shadow
				Vector normal = subtract(intersection.intersectpoint(), ((Sphere) intersection.getObject()).getOrigin());
				normal = UnitVector(normal);
				return PhongPoint(intersection, lightLocation, normal, camera);
			}
			
			
		}
	}
	
	/**
	 * Returns ambient lighting - multiples object color by a constant light
	 * @param object 
	 * @param intersection
	 * @return
	 */
	public Color ambient( Object3D object, Intersection intersection) {
		float pr = object.getR()*(float)0.50;
		float pb = object.getB()*(float)0.50;
		float pg = object.getG()*(float)0.50;
	
		return new Color(pr,pg,pb);
}
	
	
	
	private float diffuse(Vector lightdirection, PointVector normal) {
		return Math.max(dotProduct(lightdirection, normal), 0);
	}
	/**
	 * Does the Phong lighting formula on every given intersection color
	 * based on angle from light, camera and the normal
	 * @param intersection
	 * @param lightdirection
	 * @param normal = the dot product of this and light an indicator of brightness
	 * @param camera 
	 * @return
	 */
	public Color PhongPoint(Intersection intersection, Point3D lightLocation, PointVector normal, PointVector camera) {
		//This is the ambient light
				float red = (float) (intersection.getObject().getR()*0.50);
				float green = (float) (intersection.getObject().getG()*0.50);
				float blue = (float) (intersection.getObject().getB()*0.50);
				
				//this is the formula for intensity
				Vector lightdirection = subtract(lightLocation, intersection.intersectpoint());
				float magd = (float)Math.sqrt(lightdirection.getX()*lightdirection.getX()+lightdirection.getY()*lightdirection.getY()+lightdirection.getZ()*lightdirection.getZ());
				double lightIntensity = (100*3.14)/(4*3.14*magd);
				
				
				//the entire formula for calculating Phong lighting
				 lightdirection = UnitVector(lightdirection);
				 float scalar= 2 * dotProduct(normal, lightdirection);
				 float nx = scalar*normal.getX();
				 float ny = scalar*normal.getY();
				 float nz = scalar*normal.getZ();
				 Vector N = new Vector(nx, ny, nz); //2(N*L)N
				 Vector R = subtract(N, lightdirection);
				 Vector V = UnitVector(subtract(camera, intersection.intersectpoint()));
				 float specular = (float)(0.20* Math.pow(dotProduct(V,R),20 ));
				 //tests if light in opposite direction so there are no negatives
				 if(dotProduct(normal,lightdirection)<0) {
					 specular = 0;
				 }
				 
				 
				 
				 red+= (red*diffuse(lightdirection, normal)+specular)*(lightIntensity*3);
				 blue += (blue*diffuse(lightdirection, normal)+specular)*(lightIntensity*2);
				 green += (green*diffuse(lightdirection, normal)+specular)*(lightIntensity);
				 
			    
			
				 
				
				 return new Color(red,green,blue);
		 
	}
	/**
	 * 
	 * @param first
	 * @param second
	 * @return a float representing the two vectors multiplied
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
	 * @param vector
	 * @return a vector which is the magnitude 1
	 */
	public Vector UnitVector(Vector vector) {
		float magnitude = (float) Math.sqrt(vector.getX()*vector.getX()+vector.getY()*vector.getY()+vector.getZ()*vector.getZ());
		float unitX = vector.getX()/magnitude;
		float unitY = vector.getY()/magnitude;
		float unitZ = vector.getZ()/magnitude;
		return new Vector(unitX, unitY, unitZ);
	}
	/**
	 * 
	 * @param one
	 * @param two
	 * @return a vector which is the subtraction of two vectors
	 */
	public Vector subtract(PointVector one, PointVector two) {
		float newX = one.getX()-two.getX();
		float newY = one.getY() - two.getY();
		float newZ = one.getZ() - two.getZ();
		return new Vector(newX, newY, newZ);
	}
	/**
	 * Intersects a ray from the origin to the light location and six points three pixels away. 
	 * However many times it does not detect a shadow to one of the rays, it adds to the falsecount
	 * and the larger the falsecount, the more intensity
	 * @param lightlocation
	 * @param intersectionPoint
	 * @param objects
	 * @return
	 */
	private float testShadowGroup(Point3D lightlocation, Point3D intersectionPoint, ArrayList<Object3D> objects) {
		ArrayList<Point3D> locations = new ArrayList<Point3D>();
		int Falsecount = 0; //how many times it did not produce a shadow
		float x = lightlocation.getX();
		float y = lightlocation.getY();
		float z = lightlocation.getZ();
		locations.add(new Point3D(x,y,z));
		locations.add(new Point3D(x+3,y,z));
		locations.add(new Point3D(x-3,y,z));
		locations.add(new Point3D(x,y+3,z));
		locations.add(new Point3D(x,y-3,z));
		locations.add(new Point3D(x,y,z+3));
		locations.add(new Point3D(x,y,z-3));
		for (int i = 0; i<locations.size(); i++) {//traverses through all the light locations
			if(testShadow(locations.get(i),intersectionPoint, objects )== false) {//means there is no shadow
				Falsecount = Falsecount+1;//counts how many spots there were no intersections
			}
		}
		return (float)Falsecount/7;//proportion of no intersections is the light intensity, higher number is less shadow
	}
	/**
	 * 
	 * @param lightlocation
	 * @param intersectionPoint
	 * @param objects = the objects to test on
	 * @return a boolean to say whether there are one or more objects blocking a point from being light up
	 */
	public boolean testShadow(Point3D lightlocation, Point3D intersectionPoint, ArrayList<Object3D> objects ) {
		ArrayList<Intersection> intersections = new ArrayList<Intersection>(); 
		Vector light = subtract(lightlocation,intersectionPoint);
		light = UnitVector(light);//Normalize light vector once created
		for (int l = 0; l<objects.size(); l++) {
			Object3D object =  objects.get(l);
			//tests whether the object is intersected when the light hits the intersection point
			object.intersect( light,  intersectionPoint, intersections);//You put the object in the intersections  
			
		}
		if(intersections.size() == 0) {
			return false; 
		}
		for (int i = 0; i<intersections.size(); i++) { 
			if (intersections.get(i).getDistance()>0.05) { //tests for any self intersections
				return true; 
			}
		}
		return false;
		
	}
}
