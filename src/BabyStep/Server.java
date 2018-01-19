package BabyStep;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {

	int port;
	ServerSocket server;
	ServerSocket client;
	
	Server(){
		port = 1234;
		try {
			// Create a server socket associated with port 1234
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void lancer() {
		System.out.println("Server starded");
		while(true){
			Socket client;
			try {
				// Server waits for a connection
				client = server.accept();
				
				// A client connected
				System.out.println("Client " + client.getInetAddress() + " connected");
				
				//Server receives bytes from client
				//Get the server's output stream
				OutputStream os = client.getOutputStream();
				DataOutputStream dos = new DataOutputStream(os);
				
				//Build data and transform it to bytes
				Date date = new Date();
				
				byte[] b = date.toString().getBytes();
				dos.writeInt(b.length); // We put le number of bytes sent in the head of the message. 
				
				//Send the data to the client (Write in output stream)
				dos.write(b);
				
				
				// Closing the streams
				os.close();
				dos.close();
				 
				//Creer une methode lancer objet et recommencer avec les trucs ci dessous
				os = client.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeObject(date);
				oos.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void lancerObject() {
		System.out.println("Server starded (object)");
		while(true){
			Socket client;
			try {
				// Server waits for a connection
				client = server.accept();
				
				// A client connected
				System.out.println("Client " + client.getInetAddress() + " connected");
				
				//Server receives bytes from client
				//Get the server's output stream
				OutputStream os = client.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(os);
				os = client.getOutputStream();
				//Build data
				Date date = new Date();
				
				//Send the data to the client (Write in output stream)
				oos.writeObject(date);
				
				// Closing the streams
				os.close();
				oos.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args){
		Server server = new Server();
		// Uncomment one of the following lines at a time.
		//server.lancer();
		server.lancerObject();
	}
	
	
}
