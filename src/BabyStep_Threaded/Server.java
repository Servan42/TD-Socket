package BabyStep_Threaded;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	int port;
	ServerSocket server;
	ServerSocket client;
	static int clientnumber;

	Server() {
		port = 1234;
		clientnumber = 0;
		try {
			// Create a server socket associated with port 1234
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void lancer() {
		System.out.println("Server starded (object)");
		while (true) {
			Socket client;
			try {
				// Server waits for a connection
				client = server.accept();
				clientnumber++;

				// Starting a thread thread with the new client
				new ClientThread(client).start();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.lancer();
	}

}
