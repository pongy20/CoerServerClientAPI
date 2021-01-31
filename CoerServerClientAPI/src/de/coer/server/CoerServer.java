package de.coer.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.coer.api.*;
import de.coer.api.exception.DatapackageException;

/**
 * Implementation of Server interface in a Thread
 * @author Corvin Zander, Eric Dupont
 *
 */
public class CoerServer extends Thread implements Server {
	
	private final int port;
	private ServerSocket serverSocket;
	public List<CoerServerClientThread> clients;
	
	public Map<String, Executeable> methods;
	
	
	public CoerServer(final int port) {
		this.port = port;
		this.clients = new ArrayList<CoerServerClientThread>();
		this.methods = new HashMap<String, Executeable>();
		// register default disconnect method for clients
		try {
			registerMethod(BasicIdentifier.DISCONNECT_CLIENT.getName(), new Executeable() {
				
				@Override
				public void execute(Datapackage datapackage) {
					for (CoerServerClientThread temp : clients) {
						if (datapackage.getClientID() == temp.getClientId()) {
							removeClient(temp);
							clients.remove(temp);
							break;
						}
					}
					
				}
			});
		} catch (DatapackageException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			
			DebugMessage.instance().sendMessage("Server wurde erfolgreich gestartet! Warten auf Clients ...", false);
			while (!isInterrupted()) {
				Socket client;
				try {
					client = serverSocket.accept();
				} catch (Exception e) {
					break;		// Server is disconnected (server.close())
				}
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
		if (DebugMessage.instance().isEnabled()) {
			String clientAddr = client.getInetAddress().getHostAddress();
			int clientPort = client.getPort();
			DebugMessage.instance().sendMessage("Verbindung zu " + clientAddr + ":" + clientPort + " aufgebaut.", false);
		}
	}
	@Override
	public void removeClient(CoerServerClientThread thread) {
		thread.interrupt();
		DebugMessage.instance().sendMessage("Verbindung zu Client " + thread.getClientId() + " wurde beendet!", false);
		DebugMessage.instance().sendMessage("Clients online: " + clients.size(), false);
	}
	@Override
	public void registerMethod(String identifier, Executeable executeable) throws DatapackageException {
		if (identifier == null) {
			throw new DatapackageException(DatapackageException.noIdentifier);
		}
		if (methods.containsKey(identifier)) {
			throw new DatapackageException(DatapackageException.identifierAlreadyExists);
		}
		methods.put(identifier, executeable);
	}

	@Override
	public void start() {
		super.start();
	}

	@Override
	public void broadcastMessage(Datapackage pack, List<CoerServerClientThread> apart) {
		for (CoerServerClientThread thread : clients) {
			if (apart != null) {
				if (apart.contains(thread))
					continue;
			}
			thread.send(pack);
				
		}
	}
	@Override
	public void broadcastMessage(Datapackage pack, CoerServerClientThread... apart) {
		for (CoerServerClientThread thread : clients) {
			if (apart != null) {
				for (CoerServerClientThread temp : apart) {
					if (temp.getClientId() == thread.getClientId())
						continue;
				}
					
			}
			thread.send(pack);	
		}
	}
	@Override
	public CoerServerClientThread getThreadByID(long clientID) {
		for (CoerServerClientThread thread : clients) {
			if (thread.getClientId() == clientID)
				return thread;
		}
		return null;
	}
	@Override
	public void close() {
		for (CoerServerClientThread client : clients) {
			removeClient(client);
		}
		clients.clear();
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.interrupt();
		DebugMessage.instance().sendMessage("Server wurde erfolgreich gestoppt!", false);
	}
}
