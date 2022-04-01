package cs3318.group17.raytracer.scene;

import cs3318.group17.raytracer.ColorUtil;
import cs3318.group17.raytracer.Ray;
import cs3318.group17.raytracer.RayHit;
import cs3318.group17.raytracer.math.Point;
import cs3318.group17.raytracer.math.Vector;

import java.awt.Color;

public class Light {
	private final Point location;
	private final Color color;
	private final float a, b, c;

	/**
	 * Creates a colored light source in the 3-dimensional plane.
	 *
	 * @param location a location from which the light originates
	 * @param color the color associated with the light
	 * @param a attenuation constant
	 * @param b attenuation constant to be multiplied by the distance
	 * @param c attenuation constant to be multiplied by the distance^2
	 */
	public Light(Point location, Color color, float a, float b, float c) {
		this.location = location;
		this.color = color;
		this.a = a;
		this.b = b;
		this.c = c;
	}

	/**
	 * Returns the measure of light intensity the beam has at a particular distance from its source
	 * while taking into account the combined action of the absorption and scattering of light through
	 * the formula '1/(a + (b * distance) + (c * (distance * distance)))'.
	 *
	 * @param distance the distance from the source of the light
	 *
	 * @return light attenuation factor at distance d
	 */
	public float getAttenuationFactor(double distance) {
		return 1.0f / ( float )(a + b*distance + c*(distance*distance));
	}


	/**
	 * Returns the location of the light source from which it originates.
	 *
	 * @return the location instance of the light source
	 */
	public final Point getLocation() {
		return location;
	}

	/**
	 * Returns the color of the light originating from the source.
	 *
	 * @return the color instance of the light source
	 */
	public final Color getColor() {
		return color;
	}

	/**
	 * Returns the diffused and specular color seen on the surface hit by the light ray.
	 *
	 * @param hit the hit surface instant by a ray
	 * @param lightRay the light ray instance acting on the surface
	 *
	 * @return the color instance of the surface color after the light hit/couldn't hit the surface
	 */
	public Color getColor(RayHit hit, Ray lightRay) {
		double distance = lightRay.origin.distanceTo(location);
		float attenuationFactor = getAttenuationFactor(distance);

		// diffuse
		float diffuseStrength;
		if ( hit.shape.finish.diff > 0 ) {
			diffuseStrength = hit.shape.finish.diff * (float)Math.max(0.0, hit.normal.dot(lightRay.direction));
		} else {
			diffuseStrength = 0.0f;
		}

		// specular
		float specularStrength;
		if(hit.shape.finish.spec > 0) {
			Vector halfway = Vector.halfway(lightRay.direction, hit.ray.direction.negate());
			specularStrength = hit.shape.finish.spec * (float)Math.pow(Math.max(0.0, hit.normal.dot(halfway)), hit.shape.finish.shiny);
		}
		else {
			specularStrength = 0.0f;
		}

		float[] shapeColor = hit.shape.getColor(hit.point).getRGBColorComponents(null);
		float[] intensity = color.getRGBColorComponents(null);
		float red = intensity[0] * attenuationFactor * (shapeColor[0] * diffuseStrength + specularStrength);
		float green = intensity[1] * attenuationFactor * (shapeColor[1] * diffuseStrength + specularStrength);
		float blue = intensity[2] * attenuationFactor * (shapeColor[2] * diffuseStrength + specularStrength);

		return new Color(ColorUtil.clamp(red), ColorUtil.clamp(green), ColorUtil.clamp(blue));
	}
}
