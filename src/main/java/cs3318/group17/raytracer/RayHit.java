package cs3318.group17.raytracer;

import cs3318.group17.raytracer.math.Point;
import cs3318.group17.raytracer.math.Vector;
import cs3318.group17.raytracer.shapes.Shape;

/**
 * RayHit is a class which computes and then stores all the information produced when a Ray is incident upon a Shape
 * {@link RayHit#RayHit(Ray, Shape, Vector, double, boolean)}.
 */
public class RayHit {
	public final Ray ray;
	public final Shape shape;
	public final double t;
	public final Vector normal;
	public final Point point;
	private final boolean incoming;

	/**
	 * RayHit is a class which computes and then stores all the information produced when a Ray is incident upon a
	 * Shape including: the references to which Ray and Shape are involved in the intersection, the Point of incidence,
	 * the normal to the surface of Shape at the Point of incidence, the length along the Ray at which the Point of
	 * incidence occurs and whether the Ray is entering or exiting the Shape if that shape is transparent.
	 *
	 * @param ray the Ray which intersected shape
	 * @param shape the Shape which was intersected by ray
	 * @param normal the normal Vector to shape's surface at the Point of intersection
	 * @param t the length along ray at which the intersection occurred
	 * @param entering true if ray's origin is outside shape, false if Ray is inside shape (must be a solid to be false)
	 */
	public RayHit(Ray ray, Shape shape, Vector normal, double t, boolean entering) {
		this.ray = ray;
		this.shape = shape;
		this.t = t;
		this.normal = normal.normalize();
		this.point = ray.getPointOnRay(t);
		this.incoming = entering;
	}

	/**
	 * Same as {@link RayHit#RayHit(Ray, Shape, Vector, double, boolean)} but Point of intersection is given and length
	 * t along ray is computed instead.
	 *
	 * @param ray the Ray which intersected shape
	 * @param shape the Shape which was intersected by ray
	 * @param normal the normal Vector to shape's surface at the Point of intersection
	 * @param intersection the Point of intersection between ray and shape
	 * @param entering true if ray's origin is outside shape, false if Ray is inside shape (must be a solid to be false)
	 */
	public RayHit(Ray ray, Shape shape, Vector normal, Point intersection, boolean entering) {
		this.ray = ray;
		this.shape = shape;
		this.t = new Vector(ray.origin, intersection).magnitude();
		this.normal = normal.normalize();
		this.point = intersection;
		this.incoming = entering;
	}

	/**
	 * If a Shape is reflective then light Rays incident upon it are reflected such that the angle of reflection between
	 * the reflected Ray and the normal to the surface at the Point of reflection is equal to the angle of incidence
	 * between the incident Ray and the normal to the surface at the Point of reflection.
	 *
	 * @return the reflected Ray produced when ray is incident upon a reflective shape's surface
	 */
	public Ray getReflectionRay() {
		return new Ray(point, ray.direction.minus(normal.times(2.0*ray.direction.dot(normal))));
	}

	/**
	 * If a Shape is transparent then light Rays incident upon it are not reflected but are instead refracted. This
	 * method calculates the new direction of the refracted Ray which can be computed using the angle between the
	 * incident Ray to the normal at the Point of intersection and the refractive index of the Shape.
	 *
	 * @return the refracted Ray produced when ray is incident upon a transparent shape's surface
	 */
	public Ray getTransmissionRay() {
		Vector v = ray.direction.negate();
		Vector n = normal;
		double cosi = v.dot(n);
		double nint;
		if(incoming) nint = 1.0 / shape.finish.indexOfRefraction;
		else nint = shape.finish.indexOfRefraction;
		double cost = Math.sqrt(1.0 - nint*nint * (1 - cosi*cosi));

		return new Ray(point, n.times(nint * cosi - cost).minus(v.times(nint)));
	}

	public boolean getIncoming() { return this.incoming; }
}
