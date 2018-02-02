package jus.aor.printing;

import java.io.IOException;
import java.lang.Thread.State;
import java.net.*;
import java.util.*;
import java.util.logging.Logger;

import jus.aor.printing.Esclave.Slave;
import jus.util.Formule;

import static jus.aor.printing.Notification.*;

/**
 * Représentation du serveur d'impression.
 * 
 * @author Morat
 */
public class Server {
	/** 1 second timeout for waiting replies */
	protected static final int TIMEOUT = 1000000000;
	protected static final int MAX_REPONSE_LEN = 1024;
	/** la taille de la temporisation */
	protected int backlog = 10;
	/** le port de mise en oeuvre du service */
	protected int port = 3000;
	/** le nombre d'esclaves maximum du pool */
	protected int poolSize = 10;
	/** le contrôle d'arret du serveur */
	protected boolean alive = false;
	/** le master server TCP socket */
	protected ServerSocket serverTCPSoc;
	/** le logger du server */
	Logger log = Logger.getLogger("Jus.Aor.Printing.Server", "jus.aor.printing.Server");
	protected Spooler spooler;

	/**
	 * Construction du server d'impression
	 */
	public Server() {
		log.setLevel(Level.INFO_1);
		try {
			serverTCPSoc = new ServerSocket(port, backlog);
		} catch (IOException e) {
			log.log(Level.SEVERE, "Server.Creation.Error", e.getMessage());
		}
	}

	/**
	 * le master thread TCP.
	 */
	private void runTCP() {
		try {
			Socket soc = null;
			Notification protocole = null;
			JobKey jk = null;
			log.log(Level.INFO_1, "Server.TCP.Started", new Object[] { port, backlog });
			log.log(Level.INFO_1,"Server started.");
			while (alive) {
				log.log(Level.INFO, "Server.TCP.Waiting");
				try {
					soc = serverTCPSoc.accept();
					log.log(Level.INFO_1,"Client " + soc.getInetAddress() + " connected");
					log.log(Level.INFO_1,"Lecture du protocole...");
					protocole = TCP.readProtocole(soc);
					log.log(Level.INFO_1,"Protocole lu : " + protocole);
					log.log(Level.INFO_1,"Lecture de la JobKey...");
					jk = TCP.readJobKey(soc);
					log.log(Level.INFO_1,"JobKey lue : " + jk);
					if(protocole == QUERY_PRINT) {
						TCP.writeProtocole(soc, REPLY_PRINT_OK);
						TCP.writeJobKey(soc, jk);
					} else {
						TCP.writeProtocole(soc, REPLY_UNKNOWN_NOTIFICATION);
						TCP.writeJobKey(soc, jk);
					}
				} catch (SocketException e) {
					log.log(Level.SEVERE, "Server.MasterSocket.Closed", e.getMessage());
				} catch (ArrayIndexOutOfBoundsException e) {
					TCP.writeProtocole(soc, REPLY_UNKNOWN_NOTIFICATION);
				} catch (Exception e) {
					System.out.println(e.toString());
					TCP.writeProtocole(soc, REPLY_UNKNOWN_ERROR);
				}
			}
			log.log(Level.INFO_1, "Server.TCP.Stopped");
			serverTCPSoc.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	protected void setBacklog(int backlog) {
		this.backlog = backlog;
	}

	protected void setport(int port) {
		this.port = port;
	}

	protected void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	/**
	 * @param f
	 * @see jus.aor.printing.Spooler#impressionTimeOfSize(jus.util.Formule)
	 */
	public void impressionTimeOfSize(Formule f) {
		spooler.impressionTimeOfSize(f);
	}

	/**
	 * 
	 */
	void start() {
		alive = true;
		new Thread() {
			public void run() {
				runTCP();
			}
		}.start();
	}

	/**
	 * 
	 */
	public void stop() {
		alive = false;
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		new ServerGUI(new Server());
	}
}