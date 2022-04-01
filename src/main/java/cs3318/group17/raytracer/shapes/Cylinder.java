package cs3318.group17.raytracer.shapes;

import java.lang.Math;

import cs3318.group17.raytracer.Ray;
import cs3318.group17.raytracer.RayHit;
import cs3318.group17.raytracer.math.Point;
import cs3318.group17.raytracer.math.Vector;
import cs3318.group17.raytracer.pigments.Finish;
import cs3318.group17.raytracer.pigments.Pigment;

/**
 * Fully describes a Cylinder's placement, orientation and size in a Scene as well as whether a Ray has intersected the
 * Cylinder or not.
 */
public class Cylinder extends Shape {
	private final Disc base;
	private final Disc top;
	private final double height;

	/**
	 * Constructs a right Cylinder from a point on the Cylinder's base, an orientation for the Cylinder's axis and a
	 * radius and height to determine the Cylinder's size.
	 *
	 * @param centerOfBase a Point in World Space which will serve as the center of the Cylinder's Disc base
	 * @param axis a Vector which is parallel to the Cylinder's intended axis
	 * @param radius the length of the Cylinder's radius in World Units
	 * @param height the height of the Cylinder from its base to its top in World Units
	 */
	public Cylinder(Pigment pigment, Finish finish, Point centerOfBase, Vector axis, double radius, double height) {
		super(pigment, finish);
		this.base =  new Disc(pigment, finish, centerOfBase, axis, radius);
		this.height = height;
		Point centerOfTop = centerOfBase.plus(base.getNormal().times(height));
		this.top = new Disc(pigment, finish, centerOfTop, axis, radius);
	}

	/**
	 * Construct a right Cylinder by extruding a given Disc by a length equal to height.
	 *
	 * @param base a Disc object which will serve as the extruded Cylinder's base
	 * @param height the height of the Cylinder in World Space units
	 */
	public Cylinder(Pigment pigment, Finish finish, Disc base, double height) {
		super(pigment, finish);
		this.base = base;
		this.height = height;
		Point centerOfTop = base.getCenter().plus(base.getNormal().times(height));
		this.top = new Disc(pigment, finish, centerOfTop, base.getNormal(), height);
	}

	/**
	 * Computes the RayHit of a Ray with the current Cylinder.
	 *
	 * TODO: Fix Disc rendering so top and bottom of Cylinder can be added to complete the solid.
	 *
	 * @param ray the incident Ray on the Cylinder's surface
	 * @return the RayHit of ray with current Cylinder or null if no intersection occurs
	 */
	public RayHit intersect(Ray ray) {
		return curvedSurfaceIntersect(ray);
	}

	/**
	 * Computes the RayHit with just the curved surface portion of the current Cylinder.
	 *
	 * The equation which needs to be solved works similarly to the intersection method for the Sphere class
	 * {@link cs3318.group17.raytracer.shapes.Sphere#intersect} in that the solutions for t are the two roots of a
	 * quadratic equation. However, the system of equations of a cylinder with arbitrary axis with a
	 * parametric line is far more complex than for a sphere. Sympy was used to derive the general solution to this
	 * system of equations and the very long results can be seen in this method's code. See assignment report for the
	 * relevant equations and Sympy code.
	 *
	 * This method works by calculating the intersection of a ray with the infinitely long cylinder sharing the same
	 * axis as the current cylinder, and then checking if those intersection points coincide with the current finite
	 * cylinder. The closest root is to be used unless the closest root lies outside the finite cylinder and the
	 * second root does lie on the finite cylinder (ie. the case of a ray passing through the base/top of the cylinder).
	 *
	 * There are a menagerie of warnings in this method to do with repeated computations. It is true that this method
	 * is inefficient, but I am not willing to replace all the repeated computations with variables by hand :).
	 *
	 * @param ray the incoming Ray incident on the Cylinder's curved surface
	 * @return the RayHit of ray with the current Cylinder or null if no intersection occurs
	 */
	private RayHit curvedSurfaceIntersect(Ray ray) {
		Point p1 = base.getCenter();
		Point p2 = top.getCenter();

		double t = 0, x0, y0, z0, a, b, c, r, x1, y1, z1, x2, y2, z2;
		x0 = ray.origin.x;
		y0 = ray.origin.y;
		z0 = ray.origin.z;
		a = ray.direction.x;
		b = ray.direction.y;
		c = ray.direction.z;
		r = this.base.getRadius();
		x1 = p1.x;
		y1 = p1.y;
		z1 = p1.z;
		x2 = p2.x;
		y2 = p2.y;
		z2 = p2.z;

		double t1, t2;
		double discriminant = (x1*x1 - 2*x1*x2 + x2*x2 + y1*y1 - 2*y1*y2 + y2*y2 + z1*z1 -
				2*z1*z2 + z2*z2)*(a*a*r*r*y1*y1 - 2*a*a*r*r*y1*y2 + a*a*r*r*y2*y2 + a*a*r*r*z1*z1 - 2*a*a*r*r*z1*z2 +
				a*a*r*r*z2*z2 - a*a*y0*y0*z1*z1 + 2*a*a*y0*y0*z1*z2 - a*a*y0*y0*z2*z2 + 2*a*a*y0*y1*z0*z1 -
				2*a*a*y0*y1*z0*z2 - 2*a*a*y0*y1*z1*z2 + 2*a*a*y0*y1*z2*z2 - 2*a*a*y0*y2*z0*z1 + 2*a*a*y0*y2*z0*z2 +
				2*a*a*y0*y2*z1*z1 - 2*a*a*y0*y2*z1*z2 - a*a*y1*y1*z0*z0 + 2*a*a*y1*y1*z0*z2 - a*a*y1*y1*z2*z2 +
				2*a*a*y1*y2*z0*z0 - 2*a*a*y1*y2*z0*z1 - 2*a*a*y1*y2*z0*z2 + 2*a*a*y1*y2*z1*z2 - a*a*y2*y2*z0*z0 +
				2*a*a*y2*y2*z0*z1 - a*a*y2*y2*z1*z1 - 2*a*b*r*r*x1*y1 + 2*a*b*r*r*x1*y2 + 2*a*b*r*r*x2*y1 -
				2*a*b*r*r*x2*y2 + 2*a*b*x0*y0*z1*z1 - 4*a*b*x0*y0*z1*z2 + 2*a*b*x0*y0*z2*z2 - 2*a*b*x0*y1*z0*z1 +
				2*a*b*x0*y1*z0*z2 + 2*a*b*x0*y1*z1*z2 - 2*a*b*x0*y1*z2*z2 + 2*a*b*x0*y2*z0*z1 - 2*a*b*x0*y2*z0*z2 -
				2*a*b*x0*y2*z1*z1 + 2*a*b*x0*y2*z1*z2 - 2*a*b*x1*y0*z0*z1 + 2*a*b*x1*y0*z0*z2 + 2*a*b*x1*y0*z1*z2 -
				2*a*b*x1*y0*z2*z2 + 2*a*b*x1*y1*z0*z0 - 4*a*b*x1*y1*z0*z2 + 2*a*b*x1*y1*z2*z2 - 2*a*b*x1*y2*z0*z0 +
				2*a*b*x1*y2*z0*z1 + 2*a*b*x1*y2*z0*z2 - 2*a*b*x1*y2*z1*z2 + 2*a*b*x2*y0*z0*z1 - 2*a*b*x2*y0*z0*z2 -
				2*a*b*x2*y0*z1*z1 + 2*a*b*x2*y0*z1*z2 - 2*a*b*x2*y1*z0*z0 + 2*a*b*x2*y1*z0*z1 + 2*a*b*x2*y1*z0*z2 -
				2*a*b*x2*y1*z1*z2 + 2*a*b*x2*y2*z0*z0 - 4*a*b*x2*y2*z0*z1 + 2*a*b*x2*y2*z1*z1 - 2*a*c*r*r*x1*z1 +
				2*a*c*r*r*x1*z2 + 2*a*c*r*r*x2*z1 - 2*a*c*r*r*x2*z2 - 2*a*c*x0*y0*y1*z1 + 2*a*c*x0*y0*y1*z2 +
				2*a*c*x0*y0*y2*z1 - 2*a*c*x0*y0*y2*z2 + 2*a*c*x0*y1*y1*z0 - 2*a*c*x0*y1*y1*z2 - 4*a*c*x0*y1*y2*z0 +
				2*a*c*x0*y1*y2*z1 + 2*a*c*x0*y1*y2*z2 + 2*a*c*x0*y2*y2*z0 - 2*a*c*x0*y2*y2*z1 + 2*a*c*x1*y0*y0*z1 -
				2*a*c*x1*y0*y0*z2 - 2*a*c*x1*y0*y1*z0 + 2*a*c*x1*y0*y1*z2 + 2*a*c*x1*y0*y2*z0 - 4*a*c*x1*y0*y2*z1 +
				2*a*c*x1*y0*y2*z2 + 2*a*c*x1*y1*y2*z0 - 2*a*c*x1*y1*y2*z2 - 2*a*c*x1*y2*y2*z0 + 2*a*c*x1*y2*y2*z1 -
				2*a*c*x2*y0*y0*z1 + 2*a*c*x2*y0*y0*z2 + 2*a*c*x2*y0*y1*z0 + 2*a*c*x2*y0*y1*z1 - 4*a*c*x2*y0*y1*z2 -
				2*a*c*x2*y0*y2*z0 + 2*a*c*x2*y0*y2*z1 - 2*a*c*x2*y1*y1*z0 + 2*a*c*x2*y1*y1*z2 + 2*a*c*x2*y1*y2*z0 -
				2*a*c*x2*y1*y2*z1 + b*b*r*r*x1*x1 - 2*b*b*r*r*x1*x2 + b*b*r*r*x2*x2 + b*b*r*r*z1*z1 - 2*b*b*r*r*z1*z2 +
				b*b*r*r*z2*z2 - b*b*x0*x0*z1*z1 + 2*b*b*x0*x0*z1*z2 - b*b*x0*x0*z2*z2 + 2*b*b*x0*x1*z0*z1 -
				2*b*b*x0*x1*z0*z2 - 2*b*b*x0*x1*z1*z2 + 2*b*b*x0*x1*z2*z2 - 2*b*b*x0*x2*z0*z1 + 2*b*b*x0*x2*z0*z2 +
				2*b*b*x0*x2*z1*z1 - 2*b*b*x0*x2*z1*z2 - b*b*x1*x1*z0*z0 + 2*b*b*x1*x1*z0*z2 - b*b*x1*x1*z2*z2 +
				2*b*b*x1*x2*z0*z0 - 2*b*b*x1*x2*z0*z1 - 2*b*b*x1*x2*z0*z2 + 2*b*b*x1*x2*z1*z2 - b*b*x2*x2*z0*z0 +
				2*b*b*x2*x2*z0*z1 - b*b*x2*x2*z1*z1 - 2*b*c*r*r*y1*z1 + 2*b*c*r*r*y1*z2 + 2*b*c*r*r*y2*z1 -
				2*b*c*r*r*y2*z2 + 2*b*c*x0*x0*y1*z1 - 2*b*c*x0*x0*y1*z2 - 2*b*c*x0*x0*y2*z1 + 2*b*c*x0*x0*y2*z2 -
				2*b*c*x0*x1*y0*z1 + 2*b*c*x0*x1*y0*z2 - 2*b*c*x0*x1*y1*z0 + 2*b*c*x0*x1*y1*z2 + 2*b*c*x0*x1*y2*z0 +
				2*b*c*x0*x1*y2*z1 - 4*b*c*x0*x1*y2*z2 + 2*b*c*x0*x2*y0*z1 - 2*b*c*x0*x2*y0*z2 + 2*b*c*x0*x2*y1*z0 -
				4*b*c*x0*x2*y1*z1 + 2*b*c*x0*x2*y1*z2 - 2*b*c*x0*x2*y2*z0 + 2*b*c*x0*x2*y2*z1 + 2*b*c*x1*x1*y0*z0 -
				2*b*c*x1*x1*y0*z2 - 2*b*c*x1*x1*y2*z0 + 2*b*c*x1*x1*y2*z2 - 4*b*c*x1*x2*y0*z0 + 2*b*c*x1*x2*y0*z1 +
				2*b*c*x1*x2*y0*z2 + 2*b*c*x1*x2*y1*z0 - 2*b*c*x1*x2*y1*z2 + 2*b*c*x1*x2*y2*z0 - 2*b*c*x1*x2*y2*z1 +
				2*b*c*x2*x2*y0*z0 - 2*b*c*x2*x2*y0*z1 - 2*b*c*x2*x2*y1*z0 + 2*b*c*x2*x2*y1*z1 + c*c*r*r*x1*x1 -
				2*c*c*r*r*x1*x2 + c*c*r*r*x2*x2 + c*c*r*r*y1*y1 - 2*c*c*r*r*y1*y2 + c*c*r*r*y2*y2 - c*c*x0*x0*y1*y1 +
				2*c*c*x0*x0*y1*y2 - c*c*x0*x0*y2*y2 + 2*c*c*x0*x1*y0*y1 - 2*c*c*x0*x1*y0*y2 - 2*c*c*x0*x1*y1*y2 +
				2*c*c*x0*x1*y2*y2 - 2*c*c*x0*x2*y0*y1 + 2*c*c*x0*x2*y0*y2 + 2*c*c*x0*x2*y1*y1 - 2*c*c*x0*x2*y1*y2 -
				c*c*x1*x1*y0*y0 + 2*c*c*x1*x1*y0*y2 - c*c*x1*x1*y2*y2 + 2*c*c*x1*x2*y0*y0 - 2*c*c*x1*x2*y0*y1 -
				2*c*c*x1*x2*y0*y2 + 2*c*c*x1*x2*y1*y2 - c*c*x2*x2*y0*y0 + 2*c*c*x2*x2*y0*y1 - c*c*x2*x2*y1*y1);

		if (discriminant < 0) { return null; }  // no intersection occurred

		// t1 and t2 roots are the lengths along ray which intersect the front and back of the infinite cylinder
		t1 = (-a*x0*y1*y1 + 2*a*x0*y1*y2 - a*x0*y2*y2 - a*x0*z1*z1 + 2*a*x0*z1*z2 - a*x0*z2*z2 + a*x1*y0*y1 -
				a*x1*y0*y2 - a*x1*y1*y2 + a*x1*y2*y2 + a*x1*z0*z1 - a*x1*z0*z2 - a*x1*z1*z2 + a*x1*z2*z2 - a*x2*y0*y1 +
				a*x2*y0*y2 + a*x2*y1*y1 - a*x2*y1*y2 - a*x2*z0*z1 + a*x2*z0*z2 + a*x2*z1*z1 - a*x2*z1*z2 + b*x0*x1*y1 -
				b*x0*x1*y2 - b*x0*x2*y1 + b*x0*x2*y2 - b*x1*x1*y0 + b*x1*x1*y2 + 2*b*x1*x2*y0 - b*x1*x2*y1 -
				b*x1*x2*y2 - b*x2*x2*y0 + b*x2*x2*y1 - b*y0*z1*z1 + 2*b*y0*z1*z2 - b*y0*z2*z2 + b*y1*z0*z1 -
				b*y1*z0*z2 - b*y1*z1*z2 + b*y1*z2*z2 - b*y2*z0*z1 + b*y2*z0*z2 + b*y2*z1*z1 - b*y2*z1*z2 + c*x0*x1*z1 -
				c*x0*x1*z2 - c*x0*x2*z1 + c*x0*x2*z2 - c*x1*x1*z0 + c*x1*x1*z2 + 2*c*x1*x2*z0 - c*x1*x2*z1 -
				c*x1*x2*z2 - c*x2*x2*z0 + c*x2*x2*z1 + c*y0*y1*z1 - c*y0*y1*z2 - c*y0*y2*z1 + c*y0*y2*z2 - c*y1*y1*z0 +
				c*y1*y1*z2 + 2*c*y1*y2*z0 - c*y1*y2*z1 - c*y1*y2*z2 - c*y2*y2*z0 + c*y2*y2*z1
				- Math.sqrt(discriminant)) /
				(a*a*y1*y1 - 2*a*a*y1*y2 + a*a*y2*y2 + a*a*z1*z1 - 2*a*a*z1*z2 + a*a*z2*z2 - 2*a*b*x1*y1 +
						2*a*b*x1*y2 + 2*a*b*x2*y1 - 2*a*b*x2*y2 - 2*a*c*x1*z1 + 2*a*c*x1*z2 + 2*a*c*x2*z1 -
						2*a*c*x2*z2 + b*b*x1*x1 - 2*b*b*x1*x2 + b*b*x2*x2 + b*b*z1*z1 - 2*b*b*z1*z2 + b*b*z2*z2 -
						2*b*c*y1*z1 + 2*b*c*y1*z2 + 2*b*c*y2*z1 - 2*b*c*y2*z2 + c*c*x1*x1 - 2*c*c*x1*x2 + c*c*x2*x2 +
						c*c*y1*y1 - 2*c*c*y1*y2 + c*c*y2*y2);

		t2 = (-a*x0*y1*y1 + 2*a*x0*y1*y2 - a*x0*y2*y2 - a*x0*z1*z1 + 2*a*x0*z1*z2 - a*x0*z2*z2 + a*x1*y0*y1 -
				a*x1*y0*y2 - a*x1*y1*y2 + a*x1*y2*y2 + a*x1*z0*z1 - a*x1*z0*z2 - a*x1*z1*z2 + a*x1*z2*z2 - a*x2*y0*y1 +
				a*x2*y0*y2 + a*x2*y1*y1 - a*x2*y1*y2 - a*x2*z0*z1 + a*x2*z0*z2 + a*x2*z1*z1 - a*x2*z1*z2 + b*x0*x1*y1 -
				b*x0*x1*y2 - b*x0*x2*y1 + b*x0*x2*y2 - b*x1*x1*y0 + b*x1*x1*y2 + 2*b*x1*x2*y0 - b*x1*x2*y1 -
				b*x1*x2*y2 - b*x2*x2*y0 + b*x2*x2*y1 - b*y0*z1*z1 + 2*b*y0*z1*z2 - b*y0*z2*z2 + b*y1*z0*z1 -
				b*y1*z0*z2 - b*y1*z1*z2 + b*y1*z2*z2 - b*y2*z0*z1 + b*y2*z0*z2 + b*y2*z1*z1 - b*y2*z1*z2 + c*x0*x1*z1 -
				c*x0*x1*z2 - c*x0*x2*z1 + c*x0*x2*z2 - c*x1*x1*z0 + c*x1*x1*z2 + 2*c*x1*x2*z0 - c*x1*x2*z1 -
				c*x1*x2*z2 - c*x2*x2*z0 + c*x2*x2*z1 + c*y0*y1*z1 - c*y0*y1*z2 - c*y0*y2*z1 + c*y0*y2*z2 - c*y1*y1*z0 +
				c*y1*y1*z2 + 2*c*y1*y2*z0 - c*y1*y2*z1 - c*y1*y2*z2 - c*y2*y2*z0 + c*y2*y2*z1
				+ Math.sqrt(discriminant)) /
				(a*a*y1*y1 - 2*a*a*y1*y2 + a*a*y2*y2 + a*a*z1*z1 - 2*a*a*z1*z2 + a*a*z2*z2 - 2*a*b*x1*y1 +
						2*a*b*x1*y2 + 2*a*b*x2*y1 - 2*a*b*x2*y2 - 2*a*c*x1*z1 + 2*a*c*x1*z2 + 2*a*c*x2*z1 -
						2*a*c*x2*z2 + b*b*x1*x1 - 2*b*b*x1*x2 + b*b*x2*x2 + b*b*z1*z1 - 2*b*b*z1*z2 + b*b*z2*z2 -
						2*b*c*y1*z1 + 2*b*c*y1*z2 + 2*b*c*y2*z1 - 2*b*c*y2*z2 + c*c*x1*x1 - 2*c*c*x1*x2 + c*c*x2*x2 +
						c*c*y1*y1 - 2*c*c*y1*y2 + c*c*y2*y2);

		boolean incoming = true;
		if (t1 >= 0 && t2 >= 0) { t = t1;}
		if (t1 < 0 && t2 >= 0) { t = t2; incoming = false; }  // inside cylinder
		if (t1 < 0 && t2 < 0) { return null; }  // cylinder intersection behind ray origin

		// check if closest intersection point of infinite cylinder is an element of the finite sphere's surface
		Vector intersection = new Vector(ray.getPointOnRay(t));
		Vector baseToIntersection = intersection.minus(new Vector(base.getCenter()));
		double lengthAlongAxisToPointClosestToIntersection = baseToIntersection.dot(base.getNormal());
		Vector pointOnAxisClosestToIntersection = base.getNormal().times(lengthAlongAxisToPointClosestToIntersection);
		Vector normal = intersection.minus(pointOnAxisClosestToIntersection).normalize();

		// if closest intersection point not on finite cylinder check if further intersection point is
		if (0 >= lengthAlongAxisToPointClosestToIntersection || height <= lengthAlongAxisToPointClosestToIntersection) {
			intersection = new Vector(ray.getPointOnRay(t2));
			baseToIntersection = intersection.minus(new Vector(base.getCenter()));
			lengthAlongAxisToPointClosestToIntersection = baseToIntersection.dot(base.getNormal());
			pointOnAxisClosestToIntersection = base.getNormal().times(lengthAlongAxisToPointClosestToIntersection);
			normal = intersection.minus(pointOnAxisClosestToIntersection).normalize();

			// neither closest nor furthest intersection points lie on the finite cylinder's surface
			if (0 >= lengthAlongAxisToPointClosestToIntersection || height <= lengthAlongAxisToPointClosestToIntersection) {
				return null;
			} else {
				return new RayHit(ray, this, normal, t2, incoming);
			}
		}

		return new RayHit(ray, this, normal, t, incoming);
	}
}
