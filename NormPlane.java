package edu.mtholyoke.cs.comsc331.raycaster;

public class NormPlane{
	
	protected float red;
	protected float green;
	protected float blue;
	
	protected float a;
	protected float b;
	protected float c;
	protected float d;
	
	
	public NormPlane(float r, float g, float bl, float a, float b, float c, float d) {
		red = r;
		green = g;
		blue = bl;
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		
	}
	
	/*public NormPlane(int r, int g, int bl, Point3D p1, Point3D p2, Point3D p3, Material mat) {
		red = r;
		green = g;
		blue = bl;
		Point3D d1 = p1.getDir(p2);
		Point3D d2 = p3.getDir(p2);
		Point3D normal = d1.cross(d2);
		a = normal.x;
		b = normal.y;
		c = normal.z;
		d = -1 * (normal.x * p1.x) - (normal.y * p1.z) - (normal.z * p1.z);
		material = mat;
	}*/
	
	public NormPlane(float r, float g, float bl, Point3D normal, float x, float y, float z) {
		red = r;
		green = g;
		blue = bl;
		a = normal.getX();
		b = normal.getY();
		c = normal.getZ();
		d = -1 * (normal.getX() * x) - (normal.getY() * y) - (normal.getZ() * z);
		
	}

	public float getT(PointVector direction, PointVector origin) {
		Vector normal = UnitVector(new Point3D(a, b, c));
		float vd = dotProduct(normal, direction);
		float vo = -1 * (dotProduct(normal, direction) + d);
		float t = vo/vd;
		if (t < 0) {
			return -1;
		}
		return t;
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

	/*@Override
	public Point3D getTnorm(Ray ray) {
		Point3D norm = new Point3D(a, b, c).normalize();
		return norm;
	}*/



	public Vector UnitVector(PointVector vector) {
		float magnitude = vector.getX()*vector.getX()+vector.getY()*vector.getY()+vector.getZ()*vector.getZ();
		float unitX = vector.getX()/magnitude;
		float unitY = vector.getY()/magnitude;
		float unitZ = vector.getZ()/magnitude;
		return new Vector(unitX, unitY, unitZ);
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
}