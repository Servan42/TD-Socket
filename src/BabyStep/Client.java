package BabyStep;
import java.io.DataInputStream;
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
	
	Client(){
		serverPort = 1234;
		serverHost = "127.0.0.1";
	}
	
	public void lancer(){
		try {
			// Client connects to the server
			server = new Socket(serverHost,serverPort);
			System.out.println("Connected to " + server.getInetAddress());
			
			// Get the client's input stream
			InputStream is = server.getInputStream();
			DataInputStream dis = new DataInputStream(is);
			int length = dis.readInt();
			
			// Read the data from input stream
			byte[] b = new byte[length];
			int num = 0;
			while(num < length){
				num = dis.read(b,0,length);
			}
			
			//Transform data from bytes to String
			String date = new String(b);
			System.out.println("Server said: " + date);
			
			// Client sends bytes to server
			
			
			is.close();
			dis.close();
			
			is = server.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			Date dateObject = (Date) ois.readObject();
			System.out.println("Server said (object) : " + dateObject);
			ois.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void lancerObject() {
		try {
			// Client connects to the server
			server = new Socket(serverHost,serverPort);
			System.out.println("Connected to " + server.getInetAddress());
			
			// Get the client's input stream
			InputStream is = server.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			
			// Read the data from input stream
			Date dateObject = (Date) ois.readObject();
			
			//Transform data from bytes to String
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
	
	public static void main(String[] args){
		Client client = new Client();
		// Uncomment one of the following lines at a time.
		//client.lancer();
		client.lancerObject();
	}
}
