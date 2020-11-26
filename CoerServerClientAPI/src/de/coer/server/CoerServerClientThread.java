package de.coer.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import de.coer.api.*;

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
			
			while(true) {
				
				if (isInterrupted())
					break;
				
				Object obj = in.readObject();
				if (obj instanceof Datapackage) {
					Datapackage dPackage = (Datapackage) obj;
					DebugMessage.sendMessage("Datapackage erhalten (Client " + dPackage.getClientID() + "): " + dPackage.getIdentifier(), false);
					if (server.methods.containsKey(dPackage.getIdentifier())) {
						server.methods.get(dPackage.getIdentifier()).execute(dPackage);
					} else {
						System.err.println("Identifier ist nicht registriert!");
					}
				} else
					System.err.println("Kein Datapackage");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void login() {
		server.clients.add(this);
		clientID = server.clients.size();
		Datapackage pack = new Datapackage(BasicIdentifier.SEND_CLIENTID.getName(), clientID);
		send(pack);
		DebugMessage.sendMessage("Client " + client.getInetAddress().getHostAddress() + ":" + client.getPort() + " wurde die ID " + clientID + " zugewiesen!", false);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public long getClientId() {
		return clientID;
	}
}
