package cs3318.group17.raytracer.shapes;

import cs3318.group17.raytracer.*;
import cs3318.group17.raytracer.math.Vector;
import cs3318.group17.raytracer.math.Point;
import cs3318.group17.raytracer.pigments.Finish;
import cs3318.group17.raytracer.pigments.Pigment;

/**
 * Fully describes a Plane's placement and orientation in a Scene as well as whether a Ray has intersected the
 * Plane or not.
 */
public class Plane extends Shape {
	private final double a, b, c, d;
	private final Vector normal;

	/**
	 * The equation of a Plane is given by ax + by + cz + d = 0 and Vector(a, b, c) is normal to this plane.
	 *
	 * @param a x-coordinate of normal vector
	 * @param b y-coordinate of normal vector
	 * @param c z-coordinate of normal vector
	 * @param d constant required to satisfy above equation
	 */
	public Plane(Pigment pigment, Finish finish, double a, double b, double c, double d) {
		super(pigment, finish);
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.normal = new Vector(a, b, c).normalize();
	}

	/**
	 * Construct a plane of the form ax + by + cz - d = 0 from a Point on the Plane and the normal Vector to the Plane.
	 *
	 * @param p a Point on the Plane
	 * @param normal the normal to the Plane
	 */
	public Plane(Pigment pigment, Finish finish, Point p, Vector normal) {
		super(pigment, finish);
		this.a = normal.x;
		this.b = normal.y;
		this.c = normal.z;
		this.d = normal.dot(new Vector(p.x, p.y, p.z));
		this.normal = normal.normalize();
	}

	/**
	 * Calculates the RayHit of a Ray with the current Plane. The equation being solved is
	 * 		Ray(a0, b0, c0, origin) = Plane(a1, b1, c1, d)
	 *
	 * where Ray(a0, b0, c0) is parameterized as
	 * 		x = x0 + t*a0,
	 * 		y = y0 + t*b0,
	 * 		z = z0 + t*c0	with (x0, y0, z0) = Ray.origin and (a0, b0, c0) = Ray.direction.
	 *
	 * and Plane(a1, b1, c1, d) is defined by the equation
	 * 		a1*x + b1*y + c1*z + d = 0
	 * To solve for unknown t sub parameterized (x, y, z) from Ray in for (x, y, z) in Plane.
	 *
	 *
	 * The variable t is the length from the origin of the incoming Ray to the intersection Point. t is the length of
	 * the Ray because the direction Vector of the Ray is normalized to length 1.
	 * The variable name t comes from the mathematical convention for writing the parametric equation of a line in
	 * 3-dimensional space.
	 *
	 * Denominator equals zero when dot product of the plane's normal and the incoming ray is equal to zero. t is
	 * undefined when division by a zero denominator is attempted, meaning the intersection point does not exist.
	 *
	 *
	 * @param ray the incident Ray on the current Plane
	 * @return the RayHit of a Ray incident on the current Plane or null if no intersection occurs
	 */
	public RayHit intersect(Ray ray) {
		double denominator = this.normal.dot(ray.direction);
		if(denominator == 0.0) { return null; }

		double t = - (a * ray.origin.x + b * ray.origin.y + c * ray.origin.z + d) / denominator;

		if(t < 0) return null;

		return new RayHit(ray, this, normal, t, true);
	}

	Vector getNormal() { return this.normal; }
}
