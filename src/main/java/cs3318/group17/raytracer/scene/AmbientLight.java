package cs3318.group17.raytracer.scene;

import cs3318.group17.raytracer.ColorUtil;
import cs3318.group17.raytracer.Ray;
import cs3318.group17.raytracer.RayHit;
import cs3318.group17.raytracer.math.Point;

import java.awt.Color;

public class AmbientLight extends Light {

	/**
	 * Creates a colored ambient light source in the 3-dimensional plane.
	 *
	 * @param location a location from which the light originates
	 * @param color the color associated with the light
	 * @param a a float attenuation constant
	 * @param b a float attenuation constant to be multiplied by the distance
	 * @param c a float attenuation constant to be multiplied by the distance^2
	 */
	public AmbientLight(Point location, Color color, float a, float b, float c) {
		super(location, color, a, b, c);
	}

	/**
	 * Returns an amplified version of the color seen on the surface hit by the ambient light ray.
	 *
	 * @param hit the hit surface by a ray
	 * @param lightRay the ambient light ray acting on the surface
	 *
	 * @return the color instance of the surface color after the ambient light hit the surface
	 */
	@Override
	public Color getColor(RayHit hit, Ray lightRay) {
		return ColorUtil.intensify(getColor(), hit.shape.finish.amb);
	}
}
