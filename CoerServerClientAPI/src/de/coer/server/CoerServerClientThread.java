package de.coer.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import de.coer.api.*;
import de.coer.api.exception.DatapackageException;

/**
 * Creates a Thread for each Client the server manages to do work with many clients at the same time
 * @author Eric Dupont, Corvin Zander
 */
public class CoerServerClientThread extends Thread {

	private long clientID;
	private Socket client;
	private CoerServer server;

	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public CoerServerClientThread(Socket client, CoerServer server) {
		this.client = client;
		this.server = server;
	}
	public CoerServerClientThread(long clientID, Socket client, CoerServer server) {
		this(client, server);
		this.client = client;
	}
	@Override
	public void run() {
		try {
			out = new ObjectOutputStream(client.getOutputStream());
			out.flush();
			in = new ObjectInputStream(client.getInputStream());
			
			login();
			
			while(!isInterrupted()) {
				Object obj;
				try {
					obj = in.readObject();
				} catch (SocketException e) {
					break;
				}
				
				if (obj instanceof Datapackage) {
					Datapackage dPackage = (Datapackage) obj;
					DebugMessage.instance().sendMessage("Datapackage erhalten (Client " + dPackage.getClientID() + "): " + dPackage.getIdentifier(), false);
					if (server.methods.containsKey(dPackage.getIdentifier())) {
						server.methods.get(dPackage.getIdentifier()).execute(dPackage);
					} else 
						throw new DatapackageException(DatapackageException.identifierNotDeclared);
				} else
					throw new DatapackageException(DatapackageException.noDatapackage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void login() {
		server.clients.add(this);
		server.latestClientID += 1;
		clientID = server.latestClientID;
		Datapackage pack = new Datapackage(BasicIdentifier.SEND_CLIENTID.getName(), clientID);
		send(pack);
		DebugMessage.instance().sendMessage("Client " + client.getInetAddress().getHostAddress() + ":" + client.getPort() + " wurde die ID " + clientID + " zugewiesen!", false);
	}
	@Override
	public void interrupt() {
		try {
			send(new Datapackage(BasicIdentifier.LOGOUT_ACCESSED.getName(), clientID));
			in.close();
			out.close();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.interrupt();
		
	}
	public void send(Datapackage pack) {
		try {
			out.writeObject(pack);
			out.flush();
			DebugMessage.instance().sendMessage("Das Datapackage '" + pack.getIdentifier() + "' wurde an den Client (ClientID: " + pack.getClientID() + ") gesendet.", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public long getClientId() {
		return clientID;
	}
	public void setClientID(long clientID) {
		this.clientID = clientID;
		//TODO: Doubled Code with above
		Datapackage pack = new Datapackage(BasicIdentifier.SEND_CLIENTID.getName(), clientID);
		send(pack);
	}
}
