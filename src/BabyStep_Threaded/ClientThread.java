package BabyStep_Threaded;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * Thread qui traite les requetes pour le serveur.
 * 
 * @author servan
 */
public class ClientThread extends Thread {

	Socket client;

	/**
	 * Constructeur de ClientThread.
	 * @param client La socket donnée par le serveur après acceptation de la connection.
	 */
	public ClientThread(Socket client) {
		this.client = client;
	}

	/**
	 * Envoie un objet au client.
	 */
	public void run() {

		// A client connected
		System.out.println("Client " + Server.clientnumber + " -> "+ client.getInetAddress() + " connected");
		
		try {
			// Server receives bytes from client
			// Get the server's output stream
			OutputStream os;
			os = client.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			os = client.getOutputStream();

			// Build data
			Date date = new Date();

			// Send the data to the client (Write in output stream)
			oos.writeObject(date);

			// Closing the streams
			os.close();
			oos.close();
			(Server.clientnumber)--;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
