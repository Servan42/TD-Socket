/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.printing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Classe de service fournissant toutes les interactions (read, write) en mode
 * TCP.
 * 
 * @author Morat
 */
class TCP {
	private static final int MAX_LEN_BUFFER = 1024;
	// private static String buffer = new String();

	/**
	 * 
	 * @param soc
	 *            the socket
	 * @param not
	 *            the notification
	 * @throws IOException
	 */
	static void writeProtocole(Socket soc, Notification not) throws IOException {
		OutputStream os = soc.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeInt(not.ordinal());
	}

	/**
	 * 
	 * @param soc
	 *            the socket
	 * @return the notification
	 * @throws IOException
	 */
	static Notification readProtocole(Socket soc) throws IOException {
		int ordinal;
		InputStream is = soc.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		ordinal = dis.readInt();
		return Notification.values()[ordinal];
	}

	/**
	 * 
	 * @param soc
	 *            the socket
	 * @param key
	 *            the JobKey to write
	 * @throws IOException
	 */
	static void writeJobKey(Socket soc, JobKey key) throws IOException {
		OutputStream os = soc.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		byte[] b = key.marshal();
		dos.writeInt(b.length);
		dos.write(b);
	}

	/**
	 * 
	 * @param soc
	 *            the socket
	 * @return the JobKey
	 * @throws IOException
	 */
	static JobKey readJobKey(Socket soc) throws IOException {
		InputStream is = soc.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		int length = dis.readInt();
		byte[] b = new byte[length];
		int size = 0;
		while (size < length) {
			size = size + dis.read(b, size, length - size);
		}
		return new JobKey(b);
	}

	/**
	 * 
	 * @param soc
	 *            the socket
	 * @param fis
	 *            the input stream ti transfert
	 * @param len
	 *            the len of the input stream
	 * @throws IOException
	 */
	static void writeData(Socket soc, InputStream fis, int len) throws IOException {
		// Buffer limit not implemented yet
		DataInputStream dfis = new DataInputStream(fis);
		OutputStream os = soc.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		byte[] b = new byte[MAX_LEN_BUFFER];
		/* Sending the file kbyte per kbyte */
		int offset = 0;
		dos.writeInt(len);
		System.out.println("Taille du fichier à envoyer : " + len);
		for (offset = 0; len > MAX_LEN_BUFFER; offset += MAX_LEN_BUFFER, len -= MAX_LEN_BUFFER) {
//		while(len <= MAX_LEN_BUFFER) {
//			if(fis.available() >= MAX_LEN_BUFFER) {
				System.out.println("Lecture du paquet " + offset/MAX_LEN_BUFFER);
				dfis.readFully(b, offset, MAX_LEN_BUFFER);
				System.out.println("Paquet " + offset/MAX_LEN_BUFFER + " lu : ");
				System.out.println(new String(b));
				// dos.writeInt(b.length);
				dos.write(b);
//				offset += MAX_LEN_BUFFER;
//				len -= MAX_LEN_BUFFER;
//			}
		}

//		}
		/* Sending the end of the file */
		byte[] b2 = new byte[len];
		dfis.readFully(b2, offset, len);
		// dos.writeInt(b.length);
		dos.write(b2);
	}

	/**
	 * 
	 * @param soc
	 *            the socket
	 * @return string data
	 * @throws IOException
	 */
	static String readData(Socket soc) throws IOException {
		// Buffer limit not implemented yet
		DataInputStream dis = new DataInputStream(soc.getInputStream());
		int len = dis.readInt();
		System.out.println("YOPOOOOOOOOOOOOOOOOOOOOOO" + len);
		byte[] b = new byte[len];
		int offset = 0;
		for(offset=0; len > MAX_LEN_BUFFER; offset+=1024, len-=1024) {
			System.out.println("Lecture du paquet " + offset/MAX_LEN_BUFFER);
			dis.readFully(b, offset, MAX_LEN_BUFFER);
			System.out.println("Paquet " + offset/MAX_LEN_BUFFER + " lu : ");
			System.out.println(new String(b));
		}
		dis.readFully(b, offset, len);
		String recieved = new String(b);
		return recieved;
	}

	/**
	 * 
	 * @param soc
	 *            the socket
	 * @param jobs
	 *            the JobState
	 * @throws IOException
	 */
	static void writeJobState(Socket soc, JobState jobs) throws IOException {
		// -----------------------------------------------------------------------------
		// A COMPLETER
		System.out.println("NOT IMPLEMENTED YET");
	}

	/**
	 * 
	 * @param soc
	 *            the socket
	 * @return the JobState
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	static JobState readJobState(Socket soc) throws IOException, ClassNotFoundException {
		// -----------------------------------------------------------------------------
		// A COMPLETER
		System.out.println("NOT IMPLEMENTED YET");
		return null;
	}
}
