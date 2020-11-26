package de.coer.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import de.coer.api.*;

public class CoerClient extends Socket implements Client {

	private long clientID;
	
	private boolean loggedIn;
	
	public Map<String, Executeable> methods;
	
	private Thread listiningThread;
	
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public CoerClient(String host, int port) throws Exception {
		this(host, port, -1);
	}
	public CoerClient(String host, int port, long clientID) throws Exception {
		super(host, port);
		this.clientID = clientID;
		this.methods = new HashMap<String, Executeable>();
		registerMethod(BasicIdentifier.LOGOUT_ACCESSED.getName(), new Executeable() {
			
			@Override
			public void execute(Datapackage datapackage) {
				try {
					out.close();
					in.close();
					close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void logout() {
		if (isConnected()) {
			try {
				send(new Datapackage(BasicIdentifier.DISCONNECT_CLIENT.getName(), clientID));
				loggedIn = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
			DebugMessage.sendMessage("Verbindung zum Server wurde getrennt!", false);
		}
		
	}
	@Override
	public void login() {
		if (isConnected()) {
			loggedIn = true;
			DebugMessage.sendMessage("Verbindung zum Server wurde hergestellt! Versuche beim Server anzumelden...", false);
			try {
				in = new ObjectInputStream(getInputStream());
				out = new ObjectOutputStream(getOutputStream());
				
				// init clientID
				
				Object loginObj = in.readObject();
				if (loginObj instanceof Datapackage) {
					Datapackage pack = (Datapackage) loginObj;
					clientID = pack.getClientID();
					DebugMessage.sendMessage("ClientID vom Server erhalten: " + clientID, false);
				} else
					DebugMessage.sendMessage("Es wurde keine Datapackage gesendet!", true);
				
				// start listining
				
				listiningThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						while(loggedIn) {
							
							try {
								Object obj = in.readObject();
								if (obj instanceof Datapackage) {
									Datapackage dPackage = (Datapackage) obj;
									DebugMessage.sendMessage("Datapackage vom Server erhalten: " + "'" + dPackage.getIdentifier()+ "'", false);
									if (methods.containsKey(dPackage.getIdentifier())) {
										methods.get(dPackage.getIdentifier()).execute(dPackage);
									} else {
										System.err.println("Identifier ist nicht registriert!");
									}
								} else
									System.err.println("Kein Datapackage");
							} catch (Exception e) {
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
	@Override
	public void send(Datapackage datapackage) throws Exception {
		// identifier --> <clientID> --> <object[]>
		out.writeObject(datapackage);
		out.flush();
	}
	public long getClientID() {
		return clientID;
	}
	public boolean isLoggedIn() {
		return loggedIn;
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
}
