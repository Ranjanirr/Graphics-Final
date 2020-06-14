package edu.mtholyoke.cs.comsc331.raycaster;
import edu.mtholyoke.cs.comsc331.math.Point;

/**
 * Represents a 3D point or vector, methods such as dot producting and ray
 * casting can be applicable to both
 * @author eitan
 *
 */
public class PointVector {
	
	private float x;
	private float y;
	private float z;

	/**
	 * Creates a 3D point or vector
	 * @param x
	 * @param y
	 */
	public PointVector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z; 
	}
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	public void set(float x, float y, float z) {
		this.x =x;
		this.y = y;
		this.z = z;
	}

	
	
	

}