package cs3318.group17.raytracer.shapes;

import cs3318.group17.raytracer.*;
import cs3318.group17.raytracer.math.Point;
import cs3318.group17.raytracer.math.Vector;
import cs3318.group17.raytracer.pigments.Finish;
import cs3318.group17.raytracer.pigments.Pigment;

/**
 * Fully describes a Triangle's placement and orientation in a Scene as well as whether a Ray has intersected the
 * Triangle or not.
 */
public class Triangle extends Shape {
	private final Point p1, p2, p3;
	private final Vector u, v;
	private final Plane plane;
	private final Vector normal;

	/**
	 * Constructs the 3-dimensional triangle which is a subset of the Plane defined by Points p1, p2 and p3.
	 *
	 * @param p1 first corner of the Triangle
	 * @param p2 second corner of the Triangle
	 * @param p3 third corner of the Triangle
	 */
	public Triangle(Pigment pigment, Finish finish, Point p1, Point p2, Point p3) {
		super(pigment, finish);
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;

		this.u = new Vector(p1, p2);
		this.v = new Vector(p1, p3);
		this.normal = u.cross(v).normalize();

		double a = normal.x;
		double b = normal.y;
		double c = normal.z;
		double d = p1.x * normal.x + p1.y * normal.y + p1.z * normal.z;

		this.plane = new Plane(pigment, finish, a, b, c, -d);  // shouldn't this be -d only if angle between Vector(p1) and normal > 90?
	}

	/**
	 * Computes the RayHit of a Ray with the current Triangle. Does so by first computing the RayHit of the Ray with
	 * the Plane defined by the current Triangle's 3 points p1, p2, p3 and then checks to see if that intersection
	 * point is a part of the set of points contained by the current Triangle.
	 *
	 * @param ray the incident Ray on the current Triangle
	 * @return the RayHit of ray on the current Triangle or null if there is no intersection
	 */
	@Override
	public RayHit intersect(Ray ray) {
		RayHit planeHit = plane.intersect(ray);
		if(planeHit == null) return null;

		double uu, uv, vv, wu, wv, D;
		uu = u.dot(u);
		uv = u.dot(v);
		vv = v.dot(v);
		Vector w = new Vector(planeHit.point.plus(new Vector(p1).negate()));

		wu = w.dot(u);
		wv = w.dot(v);
		D = uv * uv  - uu * vv;

		double s, t;
		s = (uv * wv - vv * wu) / D;
		if(s < 0 || s > 1) return null;
		t = (uv * wu - uu * wv) / D;
		if(t < 0 || (s + t) > 1) return null;

		return new RayHit(planeHit.ray, this, planeHit.normal, planeHit.point, true);
	}
}
