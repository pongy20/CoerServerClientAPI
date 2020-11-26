package de.coer.api;

/**
 * Usefull Class to send debug Messages and disable debugmode if its not needed
 * @author Eric Dupont
 */
public class DebugMessage {

	private static boolean debug = true;
	
	public static void sendMessage(String msg, boolean error) {
		if (!debug) return;
		if (error)
			System.err.println(msg);
		else
			System.out.println(msg);
	}
	
	public static void setDebugmode(boolean enabled) {
		debug = enabled;
	}
	
}
