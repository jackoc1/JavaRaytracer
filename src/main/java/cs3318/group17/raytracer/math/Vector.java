package cs3318.group17.raytracer.math;

import cs3318.group17.raytracer.Log;

public class Vector {
	public double x, y, z;

	/**
	 * Creates a 3-dimensional Vector which has base at the origin and head at the coordinates (x, y, x)
	 *
	 * @param x the x-coordinate of the created Vector
	 * @param y the y-coordinate of the created Vector
	 * @param z the z-coordinate of the created Vector
	 */
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Creates a 3-dimensional Vector which has base at the origin (0, 0, 0) and head at the coordinates of the Point p.
	 *
	 * @param p a 3-dimensional Cartesian coordinate
	 */
	public Vector(Point p) {
		this(p.x, p.y, p.z);
	}

	/**
	 * Create a new vector from point1 to point2.
	 *
	 * @param from the point which will serve as the base of the created Vector
	 * @param to the point which will serve as the head of the created Vector
	 */
	public Vector(Point from, Point to) {
		this(to.x - from.x, to.y - from.y, to.z - from.z);
	}

	/**
	 * The magnitude of a Vector is equivalent to the length of a line drawn from the base of that Vector to its tip.
	 * By dividing each of a Vector's coordinates by its magnitude, this method returns a vector which points in the
	 * same direction as the original vector, but the new vector's magnitude is normalised to equal 1.
	 *
	 * @return the current vector normalised to length 1
	 */
	public Vector normalize() {
		double magnitude = magnitude();
		double divisor;
		if(magnitude == 0) {
			Log.error("Trying to normalize a Vector with magnitude 0.");
			divisor = Double.POSITIVE_INFINITY;
		}
		else divisor = 1 / magnitude;

		return this.times(divisor);
	}

	/**
	 * The magnitude of a Vector is equivalent to the length of a line from the base of that Vector to its tip.
	 *
	 * @return the magnitude of the current vector
	 */
	public double magnitude() {
		return Math.sqrt(this.dot(this));
	}

	/**
	 * Returns a Vector found by adding the corresponding elements of the current Vector and the Vector v. This can
	 * be thought of as the vector found by placing the base of the current Vector at the origin, placing the base of
	 * Vector v at the head of the current Vector, and then the coordinates at head of the Vector v are the coordinates of
	 * the new Vector.
	 *
	 * @param v a Vector to be added to current Vector
	 * @return the Vector found by adding current Vector and v
	 */
	public Vector plus(Vector v) {
		return new Vector(x + v.x, y + v.y, z + v.z);
	}

	/**
	 * Returns a Vector found by subtracting the corresponding elements of v from the current Vector. This resulting
	 * Vector can be thought of as the Vector found by placing both the current Vector and v with their bases at the
	 * origin and the Vector with base at the head of v and head at the head of the current Vector.
	 *
	 * @param v a Vector to be subtracted from the current Vector
	 * @return the Vector found by subtracting v from the current Vector
	 */
	public Vector minus(Vector v) {
		return new Vector(x - v.x, y - v.y, z - v.z);
	}

	/**
	 * Flips the direction of the current Vector.
	 *
	 * @return Vector pointing in opposite direction to current Vector having the same magnitude
	 */
	public Vector negate() {
		return times(-1);
	}

	/**
	 * A Vector v times a scalar c results in a new Vector pointing in the same direction as the original Vector but has
	 * magnitude equal to c multiplied by the magnitude of v. It essentially scales up or down the current Vector.
	 * Scaling by a negative scalar flips direction but is otherwise the same.
	 *
	 * @param scalar a real number
	 * @return scaled up/down version of the current Vector
	 */
	public Vector times(double scalar) {
		return new Vector(x * scalar, y * scalar, z * scalar);
	}

	/**
	 * Computes the vector cross product between the current Vector and v. The resulting Vector is perpendicular to the
	 * plane traced out between the current Vector and v.
	 *
	 * @param v the Vector to be crossed with the current Vector
	 * @return perpendicular Vector to plane traced out by v and current Vector
	 */
	public Vector cross(Vector v) {
		return new Vector(((y * v.z) - (z * v.y)),
						  ((z * v.x) - (x * v.z)),
						  ((x * v.y) - (y * v.x)));
	}

	/**
	 * Returns the dot product of the current Vector with v. Dot product is equal to the sum of the product of the two
	 * vectors respective elements.
	 *
	 * @param v the Vector to be dotted with the current Vector
	 * @return the dot product of the current Vector with v
	 */
	public double dot(Vector v) {
		return (x * v.x) + (y * v.y) + (z * v.z);
	}

	/**
	 * Computes the halfway vector between the incident and reflected Ray used in the Blinn-Phong reflection
	 * model.
	 *
	 * @param v1 normalised incident Ray to a surface
	 * @param v2 normalised reflected Ray from a surface
	 * @return the normalised halfway Vector between these two lines
	 */
	public static Vector halfway(Vector v1, Vector v2) {
		return v1.plus(v2).normalize();
	}

	/**
	 * @return a string of the coordinates/elements of the current Vector
	 */
	public String toString() {
		return "[" + x + ", " + y + ", " + z + "]";
	}
}
