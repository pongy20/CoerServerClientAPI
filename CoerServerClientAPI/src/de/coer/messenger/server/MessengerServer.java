package de.coer.messenger.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import de.coer.api.DebugMessage;
import de.coer.server.CoerServer;
import de.coer.server.CoerServerClientThread;

public class MessengerServer extends CoerServer {

	private List<MessengerServerClientThread> clients;
	
	public MessengerServer(int port) {
		super(port);
		clients = new ArrayList<>();
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
					DebugMessage.instance().sendMessage("Client wurde verbunden", false);
				} catch (Exception e) {
					break;		// Server is disconnected (server.close())
				}
				MessengerServerClientThread scThread = new MessengerServerClientThread(client, this);
				scThread.start();
				clients.add(scThread);
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
	
	public void sendMessageToAllClients(String message) {
		for (MessengerServerClientThread thread : clients) {
			thread.sendMessageToClient(message);
		}
	}
	
}
