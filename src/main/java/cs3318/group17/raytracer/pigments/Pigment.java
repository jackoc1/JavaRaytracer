package cs3318.group17.raytracer.pigments;

import cs3318.group17.raytracer.math.Point;

import java.awt.Color;

/**
 * An interface for pigments that is to be implemented in other classes.
 */
public interface Pigment {
	Color getColor(Point p);
}
