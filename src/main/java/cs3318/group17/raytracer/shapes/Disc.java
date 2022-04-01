package cs3318.group17.raytracer.shapes;

import cs3318.group17.raytracer.*;
import cs3318.group17.raytracer.math.Point;
import cs3318.group17.raytracer.math.Vector;
import cs3318.group17.raytracer.pigments.Finish;
import cs3318.group17.raytracer.pigments.Pigment;

/**
 * Fully describes a Disc's placement and orientation in a Scene as well as whether a Ray has intersected the
 * Disc or not.
 */
public class Disc extends Shape {
	private final Point center;
	private final double radius;
	private final Plane plane;

	/**
	 * Creates a 2-dimensional Disc in 3-dimensional space. A Disc is a subset of a Plane which is the set of points
	 * within one Disc radius length away from a point on that Plane, with that point known as the center.
	 *
	 * @param center Point at centre of the disc surface
	 * @param normal Normal Vector to the disc surface with magnitude 1
	 * @param radius radius of the disc surface
	 *
	 * TODO: rendered image does not fully align with specified coordinates at all times, often disappears (intersect?)
	 */
	public Disc(Pigment pigment, Finish finish, Point center, Vector normal, double radius) {
		super(pigment, finish);
		this.radius = radius;
		this.center = center;
		this.plane = new Plane(pigment, finish, center, normal);
	}

	/**
	 * A Disc is a subsection of a plane. To calculate the RayHit of a Ray on a Disc it is sufficient to calculate the
	 * RayHit of the incoming Ray on the Plane that contains the Disc and then check if that Point is within one radius
	 * of the Disc's center which is also on the Plane.
	 *
	 * @param ray the incident Ray on the current Disc
	 * @return the RayHit of a ray incident on the current Disc or null if no intersection occurs
	 */
	public RayHit intersect(Ray ray) {
		RayHit rayHit = plane.intersect(ray);
		if (rayHit == null) {
			return null;
		}
		else if (rayHit.point.distanceTo(this.center) > this.radius) {
			// if Point of intersection further from center than a radius length it can't have intersected Disc
			return null;
		}
		return new RayHit(ray, this, rayHit.normal, rayHit.t, rayHit.getIncoming());
	}

	Point getCenter() { return center; }

	Vector getNormal() {return plane.getNormal(); }

	double getRadius() { return radius; }
}
