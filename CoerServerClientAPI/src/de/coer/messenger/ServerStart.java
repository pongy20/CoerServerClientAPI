package de.coer.messenger;

import de.coer.messenger.server.MessengerServer;

public class ServerStart {

	public static void main(String[] args) {
		MessengerServer server = new MessengerServer(8088);
		server.start();
	}
	
}
