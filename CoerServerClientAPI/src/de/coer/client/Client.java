package de.coer.client;

import de.coer.api.*;
import de.coer.api.exception.DatapackageException;

/**
 * Interface to use a Client and send datapackages between Client and Server
 * @author Corvin Zander, Eric Dupont
 */
public interface Client {

	/**
	 * Sends a datapackage to server and try to login
	 * Server response a clientID to identify the client
	 */
	public void login() throws DatapackageException;
	/**
	 * Sends a datapackage to server
	 * Server will logout the client
	 */
	public void logout();
	/**
	 * Sends a datapackage to connected server
	 * @param datapackage 
	 * @return the datapackage the server sends to response
	 */
	public void send(Datapackage datapackage);
	/**
	 * Method is used to register new methods the server can send
	 * @param identifier - method identifier
	 * @param executeable will be executed if the methods gets called
	 * @throws DatapackageException if something went wrong with the identifier
	 */
	public void registerMethod(String identifier, Executeable executeable) throws DatapackageException;
	
}
