package de.coer.api;

/**
 * Basic Identifier which are used to execute Methods between Client and Server
 * @author Eric Dupont, Corvin Zander
 */
public enum BasicIdentifier {

	SEND_CLIENTID("send_clientID"),
	DISCONNECT_SERVER("disconnect_server"),
	DISCONNECT_CLIENT("disconnect_client"),
	LOGOUT_ACCESSED("logout_accessed");
	
	private final String name;
	
	private BasicIdentifier(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
