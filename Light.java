package edu.mtholyoke.cs.comsc331.raycaster;

import java.awt.Color;


import java.util.ArrayList;
import java.lang.Math;
public class Light  {
	/**
	 * Lights the pixel given the pixel and the object properties
	 * @param intersection
	 * @param lightdirection
	 * @param camera
	 * @param objects
	 * @return
	 */
	public Color lightup(Intersection intersection,  Vector lightdirection, PointVector camera, ArrayList<Object3D> objects) {
		
	    if(intersection.getObject().getid() == 4 ) {
			
	    
	    		//Different light stuff
	    		return Diffuse(intersection, lightdirection, ((StripedTriangle) intersection.getObject()).getNormal(), camera);
	    	 
			
		}
	    else if (intersection.getObject().getid() == 5) {
	    	//Different light stuff
    		return Diffuse(intersection, lightdirection, ((Cube) intersection.getObject()).getTnorm(camera, lightdirection), camera);
	    }
	    else {
		
				Vector normal = subtract(intersection.intersectpoint(), ((Sphere) intersection.getObject()).getOrigin());
				normal = UnitVector(normal);
				return Diffuse(intersection, lightdirection, normal, camera);
		
			
			
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
	
	private float diffuse(PointVector lightdirection, PointVector normal) {
		return Math.max(dotProduct(lightdirection, normal), 0);
	}
	/**
	 * Does an overall direction diffuse without the Phong and shadows to give objects an extra glow
	 * based on angle from light, camera and the normal
	 * @param intersection
	 * @param lightdirection
	 * @param normal
	 * @param camera
	 * @return - a new color based on Phong Lighting
	 */
	
	public Color Diffuse(Intersection intersection, Vector lightdirection, PointVector normal, PointVector camera) {
		//This is the ambient light //light and normal already unit vectors
				float red = (float) (intersection.getObject().getR()*0.5);
				float green = (float) (intersection.getObject().getG()*0.5);
				float blue = (float) (intersection.getObject().getB()*0.5);
				
			
				lightdirection = UnitVector(lightdirection);
				
	
				 red= red*(diffuse(lightdirection, normal));
				 blue = blue*(diffuse(lightdirection, normal));
				 green = green*(diffuse(lightdirection, normal));
				 
			    
			
				 
				
				 return new Color(red,green,blue);
		 
	}
	//the pixel shall be done with both light sources, but I try with 1 to see how it works
	public float dotProduct(PointVector first, PointVector second) {
		float product = 0; 
		product += first.getX()*second.getX();
		product += first.getY()*second.getY();
		product += first.getZ()*second.getZ(); 
		return product;
	}
	public Vector UnitVector(Vector vector) {
		float magnitude = (float)( Math.sqrt(vector.getX()*vector.getX()+vector.getY()*vector.getY()+vector.getZ()*vector.getZ()));
		float unitX = vector.getX()/magnitude;
		float unitY = vector.getY()/magnitude;
		float unitZ = vector.getZ()/magnitude;
		return new Vector(unitX, unitY, unitZ);
	}
	public Vector subtract(PointVector one, PointVector two) {
		float newX = one.getX()-two.getX();
		float newY = one.getY() - two.getY();
		float newZ = one.getZ() - two.getZ();
		return new Vector(newX, newY, newZ);
	}
}