package cs3318.group17.raytracer.pigments;

import cs3318.group17.raytracer.math.Point;

import java.awt.Color;

/**
 * Class for checker pigments that implements the Pigment interface.
 * It creates a checker patern using two colors and returns them
 * @author Idris Mokhtarzada
 */
public class CheckerPigment implements Pigment {
	private Color color1;
	private Color color2;
	private double scale;

	/**
	 * Initializer for the CheckerPigment class that will take two colours and the scale of which they will be
	 * combined to create the checkered pigment.
	 *
	 * @param color1 The first colour for the checkered pigment
	 * @param color2 The second colour for the checkered pigment
	 * @param scale The scale for the combining of the two colours
	 */
	public CheckerPigment(Color color1, Color color2, double scale) {
		this.color1 = color1;
		this.color2 = color2;
		this.scale = scale;
	}

	/**
	 * Returns colour2 or return colour1 if "which" is equal to 0. It uses Co-ordinates and the scale to get the colour
	 * at different points.
	 *
	 * @param p The Co-ordinates that the color will be taken from
	 * @return either color1 or color2
	 */
	public Color getColor(Point p) {
		int which = (floor(p.x/scale) + floor(p.y/scale) + floor(p.z/scale)) % 2;
		if(which == 0) return color1;
		return color2;
	}

	private int floor(double d) {
		return (int)Math.abs(Math.floor(d));
	}

	/**
	 * Returns the type of pigment that the colour is.
	 *
	 * @return The type of pigment as a String
	 */
	public String toString() {
		return "checkered";
	}
}
