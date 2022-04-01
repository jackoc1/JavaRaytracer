package cs3318.group17.raytracer.scene;

import cs3318.group17.raytracer.Log;
import cs3318.group17.raytracer.Ray;
import cs3318.group17.raytracer.math.Matrix;
import cs3318.group17.raytracer.math.Point;
import cs3318.group17.raytracer.math.Vector;

public class Camera {
	private final Point eye;
	private final Vector vx;
	private final Vector vy;
	private final Vector vz;

	private final double windowDistance;
	private final double windowWidth;
	private final double windowHeight;

	private int cols, rows;

	/**
	 * The Camera is the lens through which we look at objects in the Scene. To fully define the Camera's view of the
	 * scene we need to:
	 *
	 * 1) choose a location in World Space for the eye to be located at
	 * 2) choose a location in World Space for the eye to be centred on (looking at)
	 * 3) rotate the Camera's view clockwise/anti-clockwise until the specified up vector points to the top of the
	 * 		Camera's window screen
	 * 4) determine how tall the window screen on which our image is generated using a combination of the vertical field
	 * 		of vision (radians) of the eye and how far the window screen is from the eye
	 * 5) decide the resolution of the screen by specifying the number of cols and rows it is to be divided into
	 *
	 * There is a one to one correspondence between a square on the window screen grid and its respective pixel in the
	 * final image.
	 *
	 * @param eye coordinates for the eye in World Space
	 * @param center coordinates for where the eye is focused
	 * @param up vector which decides which direction is "up" when the eye focuses on center
	 * @param fovy the angle of elevation from the eye to the top of the window screen
	 * @param cols number of columns to be drawn on the window screen
	 * @param rows number of rows to be drawn on the window screen
	 */
	public Camera(Point eye, Point center, Vector up, double fovy, int cols, int rows) {
		final double fovx;
		fovy = Math.toRadians(fovy);
		fovx = fovy * cols / rows;

		Vector focus = new Vector(eye, center);
		this.vz = focus.negate().normalize();  // 1st axis direction down which the camera looks
		this.vx = up.cross(vz).normalize();  // 2nd axis perpendicular to up and vz
		this.vy = vz.cross(vx);  // 3rd axis perpendicular to vx and vz

		this.eye = eye;
		this.cols = cols;
		this.rows = rows;

		windowDistance = 1.0;  // determines how far the window is from the eye in World Space units
		windowHeight = Math.tan(fovy / 2.0) * windowDistance * 2.0;
		windowWidth = Math.tan(fovx / 2.0) * windowDistance * 2.0;

		Log.debug("  View frame:");
		Log.debug("    Org: " + eye);
		Log.debug("    X:   " + vx);
		Log.debug("    Y:   " + vy);
		Log.debug("    Z:   " + vz);

		Log.debug("    Window width: " + windowWidth);
		Log.debug("          height: " + windowHeight);
	}

	/**
	 * Returns the Ray which originates from the eye and passes through the centre of the square located at the col-th
	 * column of the row-th row of the window screen grid.
	 *
	 * @param col the index of the column on the window screen grid through which the Ray passes
	 * @param row the index of the row on the window screen grid through which the Ray passes
	 * @return a ray originating from the eye passing through a particular square of the window screen grid
	 */
	public Ray getRay(int col, int row) {
		return getRay(col, row, 0.5, 0.5);
	}

	/**
	 * Returns the ray which passes through the center of the square on the window screen grid located in the col-th
	 * column of the row-th row of the window screen grid. Center of the square is achieved by setting the pixel offset
	 * in the x and y directions to be 0.5 of a pixel as there is a one to one correspondence between pixels in the
	 * generated image and squares on the window screen grid.
	 *
	 * x and y are in Camera Space coordinates since they locate a point on the window screen from the eye's
	 * perspective. {@link #convertCoords(Point)} gives the 3-dimensional coordinates of these points in World Space and
	 * allows all subsequent computations to be done exclusively in World Space.
	 *
	 * @param col the index of the column on the window screen through which the Ray passes
	 * @param row the index of the row on the window screen through which the Ray passes
	 * @param pixelAdjustmentX horizontal offset of rays on the window screen grid in fractions of a pixel
	 * @param pixelAdjustmentY vertical offset of rays on the window screen grid in fractions of a pixel
	 * @return a ray originating from the eye passing through a particular square on the window screen grid
	 */
	public Ray getRay(int col, int row, double pixelAdjustmentX, double pixelAdjustmentY) {
		double x = (((double)col + pixelAdjustmentX) / cols) * windowWidth - (windowWidth / 2.0);
		double y = (((double)row + pixelAdjustmentY) / rows) * windowHeight - (windowHeight / 2.0);

		Vector v = new Vector(eye, convertCoords(new Point(x, y, -windowDistance)));

		Log.debug("  Generating ray:");
		Log.debug("    Window coordinates: (" + x + ", " + y + ")");
		Log.debug("    Passes through window point: " + v);

		Ray ray = new Ray(eye, v);
		Log.debug("    Final ray: " + ray);

		return ray;
	}

	/**
	 * Converts a Point to a Vector with the same coordinates and then see {@link #convertCoords(Vector)}.
	 *
	 * @param p a 3-dimensional point in Camera Space
	 * @return a 3-dimensional point in World Space
	 */
	public Point convertCoords(Point p) {
		Vector v = convertCoords(new Vector(p));
		return new Point(v.x, v.y, v.z);
	}

	/**
	 * Converts coordinates from Camera Space to World Space. The below matrices are the inverse matrices of the more
	 * common matrices found online which go from World Space to Camera Space such as in the slides found at
	 * https://www.cs.colostate.edu/~cs410/yr2013fa/more_progress/L12_CamerasRaytracing.pdf.
	 * It is not standard matrix-vector multiplication happening in the Matrix.times() method. The vector v is
	 * implicitly converted to homogenous coordinate form where the fourth element is the vector's magnitude (in this
	 * case magnitude 1).
	 *
	 * @param v vector coordinate in Camera Space
	 * @return v's equivalent coordinate when translated to World Space
	 */
	public Vector convertCoords(Vector v) {
		Matrix rT = new Matrix(new double[][]{
				{vx.x, vy.x, vz.x, 0},
				{vx.y, vy.y, vz.y, 0},
				{vx.z, vy.z, vz.z, 0},
				{0, 0, 0, 1}
		});
		Matrix tInv = new Matrix(new double[][]{
				{1, 0, 0, eye.x},
				{0, 1, 0, eye.y},
				{0, 0, 1, eye.z},
				{0, 0, 0, 1}
		});

		Matrix matrix = tInv.times(rT);
		return matrix.times(v);
	}

	public int getNumberOfCols() { return this.cols; }

	public int getNumberOfRows() { return this.rows; }
}
