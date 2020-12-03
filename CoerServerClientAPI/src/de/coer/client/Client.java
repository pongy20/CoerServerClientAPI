package de.coer.client;

import de.coer.api.*;

/**
 * Interface to use a client and send datapackages between client and server
 * @author Corvin Zander, Eric Dupont
 */
public interface Client {

	/**
	 * Sends a datapackage to server and try to login
	 * Server response a clientID to identify the client
	 */
	public void login();
	/**
	 * Sends a datapackage to server
	 * Server will logout the client
	 */
	public void logout();
	/**
	 * Sends a datapackage to connected server
	 * @param datapackage 
	 * @return the datapackage the server sends to response
	 * @throws Exception
	 */
	public void send(Datapackage datapackage);
	/**
	 * Method is used to register new methods the server can send
	 * @param identifier - method identifier
	 * @param executeable will be executed if the methods gets called
	 */
	public void registerMethod(String identifier, Executeable executeable);
	
}
