package de.coer.messenger.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import de.coer.client.CoerClient;
import de.coer.messenger.gui.client.MessengerFrame;

public class MessengerClient extends CoerClient {

	private MessengerFrame frame;
	private String username;
	
	public MessengerClient(String host, int port, String username) throws Exception {
		super(host, port);
		this.username = username;
	}
	
	public void startListining() {
		if (isConnected() && listiningThread == null)  {
			
			try {
				
				in = new ObjectInputStream(getInputStream());
				out = new ObjectOutputStream(getOutputStream());
				
				listiningThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						while (!listiningThread.isInterrupted()) {
							try {
								String msg = in.readUTF();
								frame.msgPanel.addMessage(msg);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				});
				listiningThread.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	public void sendMessage(String msg) {
		try {
			out.writeUTF(username + ": " + msg);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setFrame(MessengerFrame frame) {
		this.frame = frame;
	}
}
