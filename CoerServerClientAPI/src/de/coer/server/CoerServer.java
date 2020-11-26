package de.coer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.coer.api.*;

public class CoerServer extends Thread implements Server {

	public static boolean debug = true;
	
	private int port;
	private ServerSocket serverSocket;
	public List<CoerServerClientThread> clients;
	
	public Map<String, Executeable> methods;
	
	
	public CoerServer(int port) {
		this.port = port;
		this.clients = new ArrayList<CoerServerClientThread>();
		this.methods = new HashMap<String, Executeable>();
		registerMethod(BasicIdentifier.DISCONNECT_CLIENT.getName(), new Executeable() {
			
			@Override
			public void execute(Datapackage datapackage) {
				for (CoerServerClientThread temp : clients) {
					if (datapackage.getClientID() == temp.getClientId()) {
						removeClient(temp);
						break;
					}
				}
				
			}
		});
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			
			System.out.println("Server gestartet, warten auf Clients ...");
			
			while (true) {
				Socket client = serverSocket.accept();
				CoerServerClientThread scThread = new CoerServerClientThread(client, this);
				scThread.start();
				clientAdded(client);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSocket != null) 
					serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void clientAdded(Socket client) {
		if (debug) {
			String clientAddr = client.getInetAddress().getHostAddress();
			int clientPort = client.getPort();
			DebugMessage.sendMessage("Verbindung zu " + clientAddr + ":" + clientPort + " aufgebaut.", false);
			DebugMessage.sendMessage("Clients online: " + clients.size(), false);
		}
	}
	@Override
	public void removeClient(CoerServerClientThread thread) {
		clients.remove(thread);
		thread.interrupt();
		DebugMessage.sendMessage("Verbindung zu Client " + thread.getClientId() + " wurde beendet!", false);
		DebugMessage.sendMessage("Clients online: " + clients.size(), false);
	}
	@Override
	public void registerMethod(String identifier, Executeable executeable) {
		if (identifier == null) {
			System.err.println("Can't register a method with null identifier");
		}
		if (methods.containsKey(identifier)) {
			System.err.println("There is already an method registered using identifiert: " + identifier);
		}
		methods.put(identifier, executeable);
	}

	@Override
	public void startServer() {
		start();
	}
}
