package cs3318.group17.raytracer.pigments;

/**
 * Class that is used to check whether the color/pigment is reflective or transmittive
 * @author Idris Mokhtarzada
 */
public class Finish {
	public float amb, diff, spec, shiny, refl, trans, indexOfRefraction;

	/**
	 * Initialises the class with all the necessary parameters for the texture/pigment/colour.
	 *
	 * @param amb a float that represents the ambient light
	 * @param diff a float to represent the diffusion of the texture
	 * @param spec a float to represent the spectrum
	 * @param shiny a float to represent the shine
	 * @param refl a float to represent  reflection
	 * @param trans a float to represent transmitiveness
	 * @param ior a float to represent index of refraction
	 */
	public Finish(float amb, float diff, float spec, float shiny, float refl, float trans, float ior) {
		this.amb = amb;
		this.diff = diff;
		this.spec = spec;
		this.shiny = shiny;
		this.refl = refl;
		this.trans = trans;
		this.indexOfRefraction = ior;
	}

	/**
	 * Method that returns a True or False depending on if the colour/pigment is reflective or not.
	 *
	 * @return if the refl is above zero then the return is true, else it will return false
	 */
	public boolean isReflective() {
		return refl > 0;
	}

	/**
	 * Method that returns boolean representing whether the texture is transmittive or not.
	 *
	 * @return True if the trans parameter is above zero and False otherwise
	 */
	public boolean isTransmittive() {
		return trans > 0;
	}
}
