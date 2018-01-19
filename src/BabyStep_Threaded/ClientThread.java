package BabyStep_Threaded;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

public class ClientThread extends Thread {

	Socket client;

	ClientThread(Socket client) {
		this.client = client;
	}

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
