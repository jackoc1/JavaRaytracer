package cs3318.group17.raytracer.math;

public class Point {
	public double x, y, z;

	/**
	 * Creates a point in the 3-dimensional cartesian plane.
	 *
	 * @param x x-coordinate of created point
	 * @param y y-coordinate of created point
	 * @param z z-coordinate of created point
	 */
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Converts a Vector coordinate into a Point coordinate.
	 *
	 * @param v a Vector
	 */
	public Point(Vector v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	/**
	 * @param p a 3-dimensional Cartesian coordinate
	 * @return the magnitude of the Vector from current Point to Point p
	 */
	public double distanceTo(Point p) {
		return Math.sqrt((p.x - x)*(p.x - x) + (p.y - y)*(p.y - y) + (p.z - z)*(p.z - z));
	}

	/**
	 * Returns a new point found by adding Vector v to the current Point. This new point's coordinates can be
	 * visualised as being the point at the tip of the Vector v if v's tail was placed over the current point.
	 *
	 * @param v a 3-dimensional vector
	 * @return the point found when vector v's coordinates are added to current point's coordinates
	 */
	public Point plus(Vector v) {
		return new Point(x + v.x, y + v.y, z + v.z);
	}

	/**
	 * @return a string of the coordinates of the current point in tuple form
	 */
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
