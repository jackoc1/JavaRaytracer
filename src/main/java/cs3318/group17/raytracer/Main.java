package cs3318.group17.raytracer;

import cs3318.group17.raytracer.gui.GUI;
import cs3318.group17.raytracer.scene.Scene;

import java.io.File;

public class Main {
	private final static int NUMBER_OF_REQUIRED_ARGS = 2;

	public static boolean DEBUG = false;
	public static boolean ANTI_ALIAS = false;
	public static boolean MULTI_THREAD = false;

	/**
	 * Main method when run without arguments launches gui while if correct arguments passed, outputs a raytraced image of the scene specified.
	 *
	 * @param args = A sequence of required and optional arguments.
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			GUI gui = new GUI();
			gui.setVisible(true);
		}
		else if (args.length < NUMBER_OF_REQUIRED_ARGS) {
			String errorMessage = """
					##################################
					Error: Invalid number of arguments
					##################################""";
			Log.error(errorMessage);
			Log.info(getUsage());
		}
		else {
			File inFile = new File(args[0]);
			String outFile = args[1];

			String fileExtension;
			if ( ! args[1].contains(".")) {
				Log.error( "image file must contain file extension" );
			}
			else {
				fileExtension = args[1].substring(args[1].lastIndexOf(".")+1);

				checkForOptionalArgs(args);

				Scene scene = new Scene(inFile);
				RayTracer rayTracer = new RayTracer(scene);

				rayTracer.drawTo(outFile, fileExtension, ANTI_ALIAS, MULTI_THREAD);
			}
		}
	}

	private static void checkForOptionalArgs(String[] args) {
		int numberOfArguments = args.length;
		int i = NUMBER_OF_REQUIRED_ARGS;  // first index after last required arg
		for (; i < numberOfArguments; i++) {
			String arg = args[i];
			switch (arg) {
				case "-test" -> DEBUG = true;
				case "-aa" -> ANTI_ALIAS = true;
				case "-multi" -> MULTI_THREAD = true;
				default -> Log.error("Unrecognized option: '" + arg + "' ignored.");
			}
		}
	}

	private static String getUsage() {
		return "Usage:\n" +
				"java -cp src cs3318.group17.raytracer.Main infile bmpfile width height [-options]\n"+
				"\n"+
				"    where:\n"+
				"        readfile    - input file name\n"+
				"        imagefile   - image format output file name\n"+
				"        width       - image width (in pixels)\n"+
				"        height      - image height (in pixels)\n"+
				"        -test     - run in test mode (see below)\n"+
				"        -aa         - use anti-aliasing (~4x slower)\n"+
				"        -multi      - use multi-threading (good for large, anti-aliased images)";
	}
}
