package de.coer.server;

import java.net.Socket;
import java.util.List;

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
	/**
	 * Broadcasts a message to all clients which are not in apart list
	 * @param pack
	 * @param apart Clients which should not receive the message
	 */
	public void broadcastMessage(Datapackage pack, List<CoerServerClientThread> apart);
	
}
