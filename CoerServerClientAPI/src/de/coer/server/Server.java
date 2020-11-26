package de.coer.server;

import java.net.Socket;

import de.coer.api.*;

public interface Server {

	public void registerMethod(String identifier, Executeable executeable);
	public void clientAdded(Socket client);
	public void removeClient(CoerServerClientThread thread);
	public void startServer();
	
}
