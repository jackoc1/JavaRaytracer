package cs3318.group17.raytracer.pigments;

import cs3318.group17.raytracer.math.Point;

import java.awt.Color;

/**
 * Class for solid pigments/colors that implements the Pigment interface
 * @author Idris Mokhtarzada
 */
public class SolidPigment implements Pigment {
	public Color color;

	/**
	 * Initialises the solidPigment class with a colour.
	 *
	 * @param color the colour of the solid pigment
	 */
	public SolidPigment(Color color) {
		this.color = color;
	}

	/**
	 * Method that returns the colour that was initialised with the class.
	 *
	 * @param p Not used but is needed because of the way the interface is
	 * @return the colour of the solid pigment that was initialised with the class
	 */
	public Color getColor(Point p) {
		return color;
	}

	/**
	 * Returns the type of pigment that the colour is.
	 *
	 * @return The type of pigment as a String
	 */
	public String toString() {
		return "solid";
	}
}
