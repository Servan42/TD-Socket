package jus.aor.printing;

import static jus.aor.printing.Notification.QUERY_PRINT;
import static jus.aor.printing.Notification.REPLY_PRINT_OK;
import static jus.aor.printing.Notification.REPLY_UNKNOWN_ERROR;
import static jus.aor.printing.Notification.REPLY_UNKNOWN_NOTIFICATION;

import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

import jus.aor.printing.JobKey;
import jus.aor.printing.Level;
import jus.aor.printing.Notification;

public class Slave extends Thread {

	Socket client;
	Logger log = Logger.getLogger("Jus.Aor.Printing.Server", "jus.aor.printing.Server");

	public Slave(Socket client) {
		this.client = client;
	}

	public void run() {
		try {
			try {
				Notification protocole = null;
				JobKey jk = null;
				log.log(Level.INFO_1, "Client " + client.getInetAddress() + " connected");
				log.log(Level.INFO_1, "Lecture du protocole...");
				protocole = TCP.readProtocole(client);
				log.log(Level.INFO_1, "Protocole lu : " + protocole);
				log.log(Level.INFO_1, "Lecture de la JobKey...");
				jk = TCP.readJobKey(client);
				log.log(Level.INFO_1, "JobKey lue : " + jk);
				if (protocole == QUERY_PRINT) {
					TCP.writeProtocole(client, REPLY_PRINT_OK);
					TCP.writeJobKey(client, jk);
				} else {
					TCP.writeProtocole(client, REPLY_UNKNOWN_NOTIFICATION);
					TCP.writeJobKey(client, jk);
				}

			} catch (SocketException e) {
				log.log(Level.SEVERE, "Server.MasterSocket.Closed", e.getMessage());
			} catch (ArrayIndexOutOfBoundsException e) {
				TCP.writeProtocole(client, REPLY_UNKNOWN_NOTIFICATION);
			} catch (Exception e) {
				System.out.println(e.toString());
				TCP.writeProtocole(client, REPLY_UNKNOWN_ERROR);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
