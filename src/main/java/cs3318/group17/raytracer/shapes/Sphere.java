package cs3318.group17.raytracer.shapes;

import cs3318.group17.raytracer.Ray;
import cs3318.group17.raytracer.RayHit;
import cs3318.group17.raytracer.math.Point;
import cs3318.group17.raytracer.math.Vector;
import cs3318.group17.raytracer.pigments.Finish;
import cs3318.group17.raytracer.pigments.Pigment;

/**
 * Fully describes a Sphere's placement and size in a Scene as well as whether a Ray has intersected the
 * Sphere or not.
 */
public class Sphere extends Shape {
	private final Point center;
	private final double radius;

	/**
	 * A Sphere is fully defined by the Point location of its center and its radius.
	 *
	 * @param center the Point location of the Sphere's center
	 * @param radius the radius of the Sphere
	 */
	public Sphere(Pigment pigment, Finish finish, Point center, double radius) {
		super( pigment, finish );
		this.center = center;
		this.radius = radius;
	}

	/**
	 * Computes the RayHit of a Ray with the Sphere's surface.
	 *
	 * A sphere is a quadratic surface which is described by a quadratic equation. By subbing the x, y and z
	 * coordinates of a Ray in parametric form into the quadratic which describes the Sphere's surface one can compute
	 * the value of t (length along the Ray) at which the intersection occurs, if such an intersection does occur.
	 *
	 * If the discriminant b^2 - 4ac is < 0 then the square root of the discriminant does not exist in the set of real
	 * numbers. This can be taken to mean the intersection does not occur.
	 *
	 * There are two values of t for which the Ray intersects the Sphere, once from the front and once from the back.
	 * There is only one value of t in the niche case the Ray is tangential to the Sphere. The lesser value of t is
	 * to be used except when the Ray is inside the Sphere as then the smaller value of t lies behind the Ray's origin.
	 *
	 * If both values of t are < 0 then the Sphere lies behind the Ray's origin and even though the Ray is in line
	 * with the Sphere, the Ray does not travel backwards and thus never intersects the Sphere.
	 *
	 * @param ray the incident Ray on the current Sphere
	 * @return the RayHit of ray incident of the Sphere's surface or null if no intersection occurs
	 */
	public RayHit intersect(Ray ray) {
		Point p = ray.origin;
		Vector u = ray.direction;
		Vector v = new Vector(center, p);
		double b = 2 * (v.dot(u));
		double c = v.dot(v) - radius*radius;
		double discriminant = b*b - 4*c;

		if(discriminant < 0) { return null; }

		double firstRoot = (-b - Math.sqrt(discriminant)) / 2;
		double secondRoot = (-b + Math.sqrt(discriminant)) / 2;

		if(firstRoot < 0 && secondRoot < 0) {
			// sphere is behind the ray
			return null;
		}

		double tValue;
		Vector normal;
		Point intersection;
		boolean incoming;
		if(firstRoot < 0 && secondRoot > 0) {
			// ray origin lies inside the sphere. take secondRoot
			tValue = secondRoot;
			intersection = ray.getPointOnRay(tValue);
			normal = new Vector(intersection, center);
			incoming = false;
		} else {
			// both roots positive. take firstRoot
			tValue = firstRoot;
			intersection = ray.getPointOnRay(tValue);
			normal = new Vector(center, intersection);
			incoming = true;
		}

		return new RayHit(ray, this, normal, intersection, incoming);
//*/
	}

	/**
	 * Returns true if a Point is contained within the boundary of the Sphere's surface.
	 *
	 * @param p a Point in 3-dimensional space
	 * @return true if p is contained by the current Sphere's boundary or false otherwise
	 */
	@Override
	public boolean contains(Point p) {
		return new Vector(center, p).magnitude() < radius;
	}

	/**
	 * Returns the current Sphere's pigment type plus the word sphere
	 *
	 * @return a string describing the current Sphere's pigment
	 */
	public String toString() {
		return pigment + " sphere";
	}
}
