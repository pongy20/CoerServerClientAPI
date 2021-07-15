package de.coer.messenger.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import de.coer.server.CoerServer;
import de.coer.server.CoerServerClientThread;

public class MessengerServerClientThread extends CoerServerClientThread {

	private MessengerServer mServer;
	
	public MessengerServerClientThread(Socket client, MessengerServer server) {
		super(client, server);
		this.mServer = server;
	}
	@Override
	public void run() {
		try {
			out = new ObjectOutputStream(client.getOutputStream());
			out.flush();
			in = new ObjectInputStream(client.getInputStream());
			
			while(!isInterrupted()) {
				String msg;
				try {
					msg = in.readUTF();
					System.out.println("Nachricht erhalten, diese wird nun verteilt an alle!");
					mServer.sendMessageToAllClients(msg);
				} catch (SocketException e) {
					break;
				}
				if (msg != null) {
					//TODO: Nachricht an alle Spieler broadcasten
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void sendMessageToClient(String msg) {
		try {
			out.writeUTF(msg);
			out.flush();
		} catch (IOException e) {
			server.clients.remove(this);
		}
	}

}
