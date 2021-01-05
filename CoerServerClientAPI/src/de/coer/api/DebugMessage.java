package de.coer.api;

/**
 * Useful Class to send debug Messages and disable debugmode if its not needed
 * @author Eric Dupont
 */
public class DebugMessage {

	/**
	 * Using Singleton in this case to secure async thread calls
	 */
	private static DebugMessage instance;
	
	/**
	 * Get the Debug Messenger
	 * @return instance of Debug Messenger
	 */
	public static DebugMessage instance() {
		if (instance == null)
			instance = new DebugMessage("[Debug] ");
		return instance;
	}
	
	private boolean debug = true;
	private final String prefix;
	
	private DebugMessage(final String prefix) {
		this.prefix = prefix;
	}
	
	public synchronized void sendMessage(final String msg, boolean error) {
		if (!debug) return;
		if (error)
			System.err.println(prefix + msg);
		else
			System.out.println(prefix + msg);
	}
	
	public void setDebugmode(boolean enabled) {
		debug = enabled;
	}
	public boolean isEnabled() {
		return debug;
	}
}
