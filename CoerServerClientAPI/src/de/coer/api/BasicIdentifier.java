package de.coer.api;

public enum BasicIdentifier {

	SEND_CLIENTID("send_clientID"),
	DISCONNECT_SERVER("disconnect_server"),
	DISCONNECT_CLIENT("disconnect_client"),
	LOGOUT_ACCESSED("logout_accessed");
	
	String name;
	
	private BasicIdentifier(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
