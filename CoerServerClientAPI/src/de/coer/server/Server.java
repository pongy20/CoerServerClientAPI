package de.coer.server;

import java.net.Socket;

import de.coer.api.*;
/**
 * Interface to use a Server in Server Client model
 * @author Corvin Zander, Eric Dupont
 */
public interface Server {

	/**
	 * Registers a method which can be triggered by identifier and should execute the Executeable
	 * @param identifier
	 * @param executeable
	 */
	public void registerMethod(String identifier, Executeable executeable);
	public void clientAdded(Socket client);
	/**
	 * Disconnects a client from server
	 * @param thread
	 */
	public void removeClient(CoerServerClientThread thread);
	/**
	 * Starts the server
	 */
	public void startServer();
	
}
