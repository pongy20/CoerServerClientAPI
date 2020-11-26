package de.coer.api;

import java.io.Serializable;

public class Datapackage implements Serializable {

	private static final long serialVersionUID = 8119028858658120488L;
	private String identifier;
	private long clientID;
	private Object[] value;
	
	public Datapackage(String identifier, long clientID, Object... value) {
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
