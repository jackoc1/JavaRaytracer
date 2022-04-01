package cs3318.group17.raytracer.math;

public class Matrix {
	private final double[][] elements;
	private int numRows;
	private int numCols;

	/**
	 * Constructs a Matrix class object from the elements contained in a 2-dimensional double array
	 *
	 * The 4x4 stipulation is because this class is used for transformations involving World Space and Camera Space.
	 * Despite using a 3-dimensional coordinate system, we use an implicit 4-dimensional Vector for all transformations
	 * where the 4th coordinate is the magnitude of the corresponding 3-dimensional Vector. This is known as using a
	 * homogenous Vector since multiplying the homogenous Vector by a scalar does not affect the magnitude of the
	 * corresponding 3-dimensional Vector.
	 *
	 * Due to implementation details this Matrix class does not behave like a normal Matrix class and thus should not
	 * be used outside this Raytracing application (eg. {@link Matrix#times(Vector)}).
	 *
	 * @param matrix a 2-dimensional array with no more than 4 columns and 4 rows
	 */
	public Matrix(double[][] matrix) {
		if(matrix.length < 4 || matrix[0].length < 4) throw new IllegalArgumentException("Matrix must be a 4x4 array");
		this.elements = matrix;
		this.numRows = matrix.length;
		this.numCols = matrix[0].length;
	}

	/**
	 * Create an empty Matrix with a specified number of rows and columns.
	 *
	 * @param rows the number of rows the created Matrix will allow
	 * @param cols the number of columns the created Matrix will allow
	 */
	public Matrix(int rows, int cols) {
		this.elements = new double[rows][cols];
	}

	/**
	 * @param matrix2 the Matrix which is to be applied to the current Matrix
	 * @return The Matrix transformation which is equivalent to applying matrix2 and then current Matrix to a Vector
	 */
	public Matrix times(Matrix matrix2) {
		double[][] r = new double[this.numRows][matrix2.numCols];

		for(int row = 0; row < this.numRows; row++) {
			for(int col = 0; col < matrix2.numCols; col++) {
				r[row][col] = this.elements[row][0] * matrix2.elements[0][col]
						+ this.elements[row][1] * matrix2.elements[1][col]
						+ this.elements[row][2] * matrix2.elements[2][col]
						+ this.elements[row][3] * matrix2.elements[3][col];
			}
		}

		return new Matrix(r);
	}

	/**
	 * Implements Matrix multiplication with a Vector v. V is implicitly in homogenous form which is where the fourth
	 * coordinate magnitude comes from (needs to be in homogenous form for
	 * {@link cs3318.group17.raytracer.scene.Camera#convertCoords}.
	 * By dividing x, y and z by magnitude the returned Vector is converted from homogenous form to its 3-dimensional
	 * equivalent.
	 *
	 * @param v a 3-dimensional Vector
	 * @return the result of applying the current Matrix to v
	 */
	public Vector times(Vector v) {
		double x, y, z;

		x = elements[0][0] * v.x + elements[0][1] * v.y + elements[0][2] * v.z + elements[0][3];
		y = elements[1][0] * v.x + elements[1][1] * v.y + elements[1][2] * v.z + elements[1][3];
		z = elements[2][0] * v.x + elements[2][1] * v.y + elements[2][2] * v.z + elements[2][3];

		// fourth coordinate
		double magnitude = elements[3][0] * v.x + elements[3][1] * v.y + elements[3][2] * v.z + elements[3][3];

		x /= magnitude;
		y /= magnitude;
		z /= magnitude;

		return new Vector(x, y, z);
	}
}
