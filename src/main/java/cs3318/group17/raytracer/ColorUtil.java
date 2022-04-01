package cs3318.group17.raytracer;

import java.awt.Color;

/**
 * Manages the mixing of colours of light Rays as well as how their brightness changes as those Rays bounce around
 * the Scene.
 */
public class ColorUtil {
	/**
	 * The method blends two colours together, It starts by getting two arrays of floats using the getRGBColorComponents
	 * that will place a float representation of red green and blue into the array. Then to get the new red, green and
	 * blue values it adds the red float value from the base colour with the red value from the second colour. This
	 * is repeated for green and blue. Then returns the colour with the new RGB values.
	 *
	 * @param base the base colour that will be blended
	 * @param mixin the colour that will be mixed with the base colour
	 * @return the new colour after it has been created.
	 */
	public static Color blend(Color base, Color mixin) {
		float[] baseC = base.getRGBColorComponents(null);
		float[] mixinC = mixin.getRGBColorComponents(null);

		float red = clamp(baseC[0] + mixinC[0]);
		float green = clamp(baseC[1] + mixinC[1]);
		float blue = clamp(baseC[2] + mixinC[2]);
		return new Color(red, green, blue);
	}

	/**
	 * Method that limits the value of the float that gets put into it.
	 *
	 * @param x a float
	 * @return a float that sits inside the values of the clamp
	 */
	public static float clamp(float x) {
		return Math.max(0.0f, Math.min(1.0f, x));
	}

	/**
	 * Method that intensifies the input colour.
	 *
	 * @param color the color to be intensified
	 * @param intensity the amount the color is intensified
	 * @return  value of the other intensify method when called with color and a new Color as its parameters
	 */
	public static Color intensify(Color color, float intensity) {
		// TODO: clamp should not be necessary here
		return intensify(color, new Color(clamp(intensity), clamp(intensity), clamp(intensity)));
	}

	/**
	 * Method that intensifies the color by multiplying it by a float value that is contained within another Color
	 * object. Uses the getRGBColorComponents to extract the values that will be used to intensify the color and
	 * returns the new color.
	 *
	 * @param color the color that is to be intensified
	 * @param intensity the colour that contains the float values to intensify the main colour
	 * @return the new color after it has been intensified
	 */
	public static Color intensify(Color color, Color intensity) {
		float[] c = color.getRGBColorComponents(null);
		float[] i = intensity.getRGBColorComponents(null);

		return new Color(c[0] * i[0], c[1] * i[1], c[2] * i[2]);
	}

	/**
	 * Method that gets the average RGB values of the Colors in an array and goes through a for loop that combines
	 * all the RGB values of colors together into a float array called rgb and will return the new color that
	 * represents the average color of the array using the values in the rgb array.
	 *
	 * @param colors The array of colors that is used to find the average color
	 * @return the average color as a new Color object
	 */
	public static Color average(Color... colors) {
		float[] rgb = new float[3];
		float mult = 1.0f / colors.length;
		for(Color c: colors) {
			float[] cc = c.getRGBColorComponents(null);
			rgb[0] += cc[0] * mult;
			rgb[1] += cc[1] * mult;
			rgb[2] += cc[2] * mult;
		}

		return new Color(rgb[0], rgb[1], rgb[2]);
	}
}
