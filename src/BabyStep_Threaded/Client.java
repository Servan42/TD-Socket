package BabyStep_Threaded;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

public class Client {

	int serverPort;
	String serverHost;
	Socket server;

	Client() {
		serverPort = 1234;
		serverHost = "127.0.0.1";
	}

	public void lancer() {
		try {
			// Client connects to the server
			server = new Socket(serverHost, serverPort);
			System.out.println("Connected to " + server.getInetAddress());

			// Get the client's input stream
			InputStream is = server.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);

			// Read the data from input stream
			Date dateObject = (Date) ois.readObject();

			// Transform data from bytes to String
			System.out.println("Server said (object) : " + dateObject);

			// Client sends bytes to server

			is.close();
			ois.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.lancer();
	}
}
