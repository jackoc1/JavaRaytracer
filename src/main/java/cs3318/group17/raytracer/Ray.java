package cs3318.group17.raytracer;

import cs3318.group17.raytracer.math.Point;
import cs3318.group17.raytracer.math.Vector;

/**
 * Fully describes the state of a Ray of light active in the Scene.
 */
public class Ray {
	public final Point origin;
	public final Vector direction;
	public double length;

	/**
	 * Constructs a Ray which moves from a Point of origin along a given direction Vector. All Rays when first created
	 * are of infinite length and only have finite length when a RayHit occurs between the Ray and a Shape.
	 *
	 * @param origin the Point of origin of the Ray
	 * @param direction the direction along which the Ray moves in Vector form
	 */
	public Ray(Point origin, Vector direction) {
		this(origin, direction, true);
	}

	/**
	 * Constructs a Ray which moves from a Point of origin along a given direction Vector. All Rays when first created
	 * are of infinite length and only have finite length when a RayHit occurs between the Ray and a Shape.
	 *
	 * Not entirely sure what adjustForError's purpose is but possibly to avoid zero magnitude Vector issues?
	 *
	 * @param origin the Point of origin of the Ray
	 * @param direction the direction along which the Ray moves in Vector form
	 * @param adjustForError if true moves the origin along the direction Vector by a minuscule amount
	 */
	public Ray(Point origin, Vector direction, boolean adjustForError) {
		this.length = Double.POSITIVE_INFINITY;

		this.direction = direction.normalize();

		if(adjustForError) origin = origin.plus(this.direction.times(0.001));

		this.origin = origin;
	}

	/**
	 * A Point on the Ray is calculated by adding the normalized direction Vector of the Ray multiplied by the input
	 * length to the Point of origin of the Ray.
	 *
	 * @param length length along the Ray from the Ray origin to the desired Point
	 * @return a Point on the Ray
	 */
	public Point getPointOnRay(double length) {
		return origin.plus(direction.times(length));
	}

	/**
	 * The end of a Ray is calculated by adding the normalized direction Vector of the Ray multiplied by the Ray's
	 * length to the Point of origin of the Ray.
	 *
	 * @return the Point coordinates of the end of the Ray
	 */
	public Point getEnd() {
		return getPointOnRay(this.length);
	}

	/**
	 * Returns a String which contains the Point of Origin of the current Ray, the direction of the current Ray in
	 * Vector form and the length of the Ray from its base to its end.
	 *
	 * @return a String containing the current Ray's description
	 */
	public String toString() {
		return "Origin:" + origin + "\tDirection:" + direction + "\tlength:" + length;
	}
}
