package cs3318.group17.raytracer;

import cs3318.group17.raytracer.scene.Light;
import cs3318.group17.raytracer.scene.Scene;
import cs3318.group17.raytracer.math.Vector;
import cs3318.group17.raytracer.shapes.Shape;

import javax.imageio.ImageIO;
import java.awt.Desktop;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The RayTracer class takes an existing Scene and renders it while applying the correct shading to the objects in that
 * Scene.
 */
public final class RayTracer {
	private static final Color BACKGROUND_COLOR = Color.GRAY;
	private static final int MAX_RECURSION_LEVEL = 5;

	private boolean antiAliasing;
	private boolean multiThreading;

	private File drawnFile;
	private Scene scene;

	/**
	 * Creates an instance of RayTracer with the specified Scene for it to interact with.
	 *
	 * @param scene the scene instance to be used
	 */
	public RayTracer(Scene scene) {
		this.scene = scene;
		this.antiAliasing = false;
		this.multiThreading = false;
	}

	/**
	 * Creates an instance of RayTracer with the specified Scene while allowing for anti-aliasing and multi-thread functionality.
	 *
	 * @param scene the scene instance to be used
	 * @param antiAliasing boolean true if anti aliasing is to be used
	 * @param multiThreading boolean true if multi-threading it to be used
	 */
	public RayTracer(Scene scene, boolean antiAliasing, boolean multiThreading) {
		this.scene = scene;
		this.antiAliasing = antiAliasing;
		this.multiThreading = multiThreading;

	}

	/**
	 * Renders the contents of the Scene instance into an image File of the specified image file format, while
	 * implementing anti-aliasing and multi-threading capabilities. If there is already a File present, its contents
	 * are overwritten.
	 *
	 * @param fileName the File instance to be written to
	 * @param fileExtension the file extension as a String
	 * @param antiAliasing if boolean True, anti-aliasing is performed
	 * @param multiThreading if boolean True, multi-threading is performed
	 */
	public void drawTo(String fileName, String fileExtension, boolean antiAliasing, boolean multiThreading) {
		this.antiAliasing = antiAliasing;
		this.multiThreading = multiThreading;
		drawTo( fileName, fileExtension );
	}

	/**
	 * Renders the contents of the Scene instance into an image File of the specified image file format. If there
	 * is already a File present, its contents are overwritten.
	 *
	 * @param fileName the File instance to be written to
	 * @param fileExtension the file extension as a String
	 */
	public void drawTo(String fileName, String fileExtension) {
		File outFile = new File(fileName);
		int cols = scene.getNumberOfColumns();
		int rows = scene.getNumberOfRows();
		final BufferedImage image = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);

        try {
			if ( ! ImageIO.write(image, fileExtension, outFile) ) {
				throw new IOException("Make sure file/directory permissions, file extension, and/or path to directory are valid.");
			}
			else {
				long startTime = System.currentTimeMillis();

				if (multiThreading) {
					final ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2,
							1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

					for (int r = 0; r < rows; r++) {
						for (int c = 0; c < cols; c++) {
							final int cc = c;
							final int rr = r;
							executor.execute(() -> image.setRGB(cc, rr, getPixelColor(cc, rr).getRGB()));
						}
					}

					executor.shutdown();
					try {
						if (!executor.awaitTermination(5, TimeUnit.MINUTES)) {
							Log.warn("Not all threads finish in 5 minutes!");
						}
					} catch (InterruptedException e) {
						Log.error("awaitTermination was interrupted while waiting.");
					}
				} else {
					for (int r = 0; r < rows; r++) {
						for (int c = 0; c < cols; c++) {
							image.setRGB(c, r, getPixelColor(c, r).getRGB());
						}
						if (r % 5 == 0) {
							Log.info((rows - r) + " rows left to trace.");
						}
					}
				}

				long finishTime = System.currentTimeMillis();
				Log.info("Finished in: " + (finishTime - startTime) + "ms");
				ImageIO.write(image, fileExtension, outFile);
				drawnFile = outFile;
			}
		}
		catch ( IOException e ) {
			Log.error( e.getMessage() );
		}
	}

	/**
	 * Calculates the Color of a Ray after it has intersected a Shape with RayHit hit. Recursively follows this
	 * reflected/refracted ray to a specified depth number of reflections/refractions and updates the Color of Ray after
	 * each collision.
	 *
	 * @param hit the RayHit of a Ray whose Color needs to be determined after intersecting a shape
	 * @param depth the number of reflections/refractions allowed for a single Ray before it is no longer tracked
	 * @return the Color of a Ray after RayHit hit
	 */
	private Color shade(RayHit hit, int depth) {
		Color color = Color.BLACK;

		// ambient light source
		if( hit.shape.finish.amb > 0 ) {
			color = ColorUtil.blend( color,
					                 ColorUtil.intensify( hit.shape.getColor( hit.point ),
											 scene.getAmbientLight().getColor( hit, null ) ) );
		}

		// light source(s)
		for(Light light : scene.getLightsArray()) {
			Vector lightRayVec = new Vector(hit.point, light.getLocation());
			Ray lightRay = new Ray(hit.point, lightRayVec);
			lightRay.length = lightRayVec.magnitude();

			RayHit obstruction = findHit(lightRay);
			if(obstruction == null) {
				Color c = light.getColor(hit, lightRay);
				color = ColorUtil.blend(color, c);
			}
		}

		if(depth <= MAX_RECURSION_LEVEL) {
			if(hit.shape.finish.isReflective()) {
				color = ColorUtil.blend( color,
						                 ColorUtil.intensify( trace( hit.getReflectionRay( ), depth+1 ),
                                         hit.shape.finish.refl ) );
			}

			if(hit.shape.finish.isTransmittive()) {
				color = ColorUtil.blend(color, ColorUtil.intensify(trace(hit.getTransmissionRay(), depth+1), hit.shape.finish.trans));
			}
		}

		return color;
	}

	/**
	 * Checks a Ray against each Shape in the Scene for intersections and returns the RayHit of the closest Shape to
	 * Ray's origin, since once it hits this Shape its path has changed and any other RayHits are no longer valid.
	 * Closest Shape is determined by the RayHit with the smallest value of t > 0.
	 *
	 * @param ray a Ray
	 * @return the RayHit of ray with the first Shape it encounters in the Scene or null if no intersections occur
	 */
	private RayHit findHit(Ray ray) {
		RayHit hit = null;

		for(Shape shape: scene.getShapesArray()) {
			RayHit h = shape.intersect(ray);
			if(h != null && h.t < ray.length) {
				hit = h;
				ray.length = h.t;
			}
		}

		return hit;
	}

	/**
	 * Traces a Ray around the Scene checking for if it has intersected any Shapes or Light sources or if it has
	 * disappeared off into the background and colours it appropriately.
	 *
	 * @param ray a Ray to be checked for any intersections
	 * @param depth how many times ray has been reflected/refracted already
	 * @return the Color of ray
	 */
	private Color trace(Ray ray, int depth) {
		RayHit hit = findHit(ray);

		if(hit != null) {
			return shade(hit, depth);
		}

		// missed everything. return background color
		return BACKGROUND_COLOR;
	}

	/**
	 * Returns the Color of a pixel in the rendered image after the ray tracing algorithm has been applied to the Ray
	 * which passes through the pixel.
	 *
	 * @param col the column of the rendered image in which the coloured pixel lies
	 * @param row the row of the rendered image in which the coloured pixel lies
	 * @return the Color of the pixel in the col-th column and row-th row of the rendered image
	 */
	private Color getPixelColor(int col, int row) {
		int rows = scene.getNumberOfRows();
		int bmpRow = rows-1 - row;
		if(antiAliasing) {
			Ray ray = scene.getCamera().getRay(col, bmpRow, 0, 0);
			Color c1 = trace(ray, 0);
			ray = scene.getCamera().getRay(col, bmpRow, .5, 0);
			Color c2 = trace(ray, 0);
			ray = scene.getCamera().getRay(col, bmpRow, 0, .5);
			Color c3 = trace(ray, 0);
			ray = scene.getCamera().getRay(col, bmpRow, .5, .5);
			Color c4 = trace(ray, 0);

			return ColorUtil.average(c1, c2, c3, c4);
		}
		else {
			Ray ray = scene.getCamera().getRay(col, bmpRow);
			return trace(ray, 0);
		}
	}

	/**
	 * Opens the most recent drawn file if it exists.
	 */
	public void openDrawnFile() {
		Desktop dt = Desktop.getDesktop();
		try {
			if ( drawnFile != null ) {
				dt.open(drawnFile);
			}
		}
		catch (IOException e) {
			Log.error("Couldn't open file");
			System.exit(1);
		}
	}

	/**
	 * Returns the Scene instance using in the ray tracer.
	 *
	 * @return the Scene instance
	 */
	public Scene getScene() { return this.scene; }

	/**
	 * Sets the Scene instance used in the scene with the one specified.
	 *
	 * @param scene a scene instance
	 */
	public void setScene(Scene scene) {
		this.scene = scene;
	}

	/**
	 * Returns boolean value for if anti-aliasing is being used in the scene.
	 *
	 * @return the boolean value for if anti-aliasing is being used
	 */
	public boolean getAntiAliasing() { return this.antiAliasing; }

	/**
	 * Returns boolean value for if multi-threading is being used in the scene.
	 *
	 * @return the boolean value for if multi-threading is being used
	 */
	public boolean getMultiThreading() { return this.multiThreading; }

	/**
	 * Sets boolean true/false for anti-aliasing to be used while drawing the scene.
	 *
	 * @param antiAliasing a boolean value to specify anti-aliasing
	 */
	public void setAntiAliasing(boolean antiAliasing) {
		this.antiAliasing = antiAliasing;
	}

	/**
	 * Sets boolean true/false for multi-threading to be used while drawing the scene.
	 *
	 * @param multiThreading a boolean value to specify multi-threading
	 */
	public void setMultiThreading(boolean multiThreading) {
		this.multiThreading = multiThreading;
	}
}
