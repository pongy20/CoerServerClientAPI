package de.coer.api;

public class DebugMessage {

	private static boolean debug = true;
	
	public static void sendMessage(String msg, boolean error) {
		if (!debug) return;
		if (error)
			System.err.println(msg);
		else
			System.out.println(msg);
	}
	
}
