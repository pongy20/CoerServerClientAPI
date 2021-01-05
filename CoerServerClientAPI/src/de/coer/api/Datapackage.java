package de.coer.api;

import java.io.Serializable;

/**
 * Datapackages are used to send Objects between Server and Client in an default protocol
 * @author Corvin Zander, Eric Dupont
 *
 */
public class Datapackage implements Serializable {

	private static final long serialVersionUID = 8119028858658120488L;
	/**
	 * To identify wich method should be executed
	 */
	private final String identifier;
	private long clientID;
	/**
	 * parameters
	 */
	private final Object[] value;
	
	public Datapackage(final String identifier, long clientID, final Object... value) {
		this.identifier = identifier;
		this.clientID = clientID;
		this.value = value;
	}

	public String getIdentifier() {
		return identifier;
	}

	public long getClientID() {
		return clientID;
	}

	public Object[] getValues() {
		return value;
	}
	public Object getValue() {
		if (value != null || value[0] != null) {
			return value[0];
		}
		return null;
	}
	
}
