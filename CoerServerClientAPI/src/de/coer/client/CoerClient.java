package de.coer.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import de.coer.api.*;
import de.coer.api.exception.DatapackageException;

/**
 * Implementation of Client Interface
 * @author Eric Dupont, Corvin Zander
 */
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
					closeChannels();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		registerMethod(BasicIdentifier.SEND_CLIENTID.getName(), new Executeable() {
			
			@Override
			public void execute(Datapackage datapackage) {
				CoerClient.this.clientID = datapackage.getClientID();
				DebugMessage.instance().sendMessage("ClientID vom Server erhalten: " + CoerClient.this.clientID, false);
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
			DebugMessage.instance().sendMessage("Verbindung zum Server wurde getrennt!", false);
		}
		
	}
	@Override
	public void login() throws DatapackageException {
		if (isConnected()) {
			loggedIn = true;
			DebugMessage.instance().sendMessage("Verbindung zum Server wurde hergestellt! Versuche beim Server anzumelden...", false);
			try {
				in = new ObjectInputStream(getInputStream());
				out = new ObjectOutputStream(getOutputStream());
				
				// start listening on Thread
				
				listiningThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						while(loggedIn) {
							try {
								Object obj = in.readObject();
								DebugMessage.instance().sendMessage("Eine Nachricht vom Server...", false);
								if (obj instanceof Datapackage) {
									Datapackage dPackage = (Datapackage) obj;
									DebugMessage.instance().sendMessage("Datapackage vom Server erhalten: " + "'" + dPackage.getIdentifier()+ "'", false);
									if (methods.containsKey(dPackage.getIdentifier())) {
										methods.get(dPackage.getIdentifier()).execute(dPackage);
									} else
										throw new DatapackageException(DatapackageException.identifierNotDeclared);
								} else
									throw new DatapackageException(DatapackageException.noDatapackage);
							} catch (Exception e) {
								DebugMessage.instance().sendMessage("Verbindung zum Server verloren. Client wurde gestoppt.", true);
								closeChannels();
								break;
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
	public void send(Datapackage datapackage) {
		try {
			out.writeObject(datapackage);
			out.flush();
			DebugMessage.instance().sendMessage("Das Datapackage '" + datapackage.getIdentifier() + "' wurde an den Server gesendet.", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public long getClientID() {
		return clientID;
	}
	public boolean isLoggedIn() {
		return loggedIn;
	}
	public void closeChannels() {
		try {
			out.close();
			in.close();
			close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
}
