package cs3318.group17.raytracer;

/**
 * Logs the state of the application including whether the current state has encountered an error or warning.
 */
public class Log {
	/**
	 * For printing messages which tell the client when a breaking issue has occurred in the code.
	 *
	 * @param msg the String to be printed
	 */
	public static void error(String msg) {
		System.err.println("ERROR: " + msg);
	}

	/**
	 * For printing messages which tell the client when a non-breaking issue has occurred in the code.
	 *
	 * @param msg the String to be printed
	 */
	public static void warn(String msg) {
		System.out.println("Warning: " + msg);
	}

	/**
	 * For printing messages which tell the client relevant information about the code's operational state at runtime.
	 *
	 * @param msg the String to be printed
	 */
	public static void info(String msg) {
		System.out.println(msg);
	}

	/**
	 * For printing messages which tell the client relevant information about the code's operational state at runtime
	 * in greater detail than {@link Log#info(String)}. These messages are only printed if the DEBUG argument in Main
	 * is set to true.
	 *
	 * @param msg the String to be printed
	 */
	public static void debug(String msg) {
		if(Main.DEBUG) System.out.println(msg);
	}
}
