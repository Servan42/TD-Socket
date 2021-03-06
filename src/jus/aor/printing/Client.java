package jus.aor.printing;

import static jus.aor.printing.Notification.REPLY_PRINT_OK;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Logger;

/**
 * Représentation du Client du serveur d'impression.
 * 
 * @author Morat Chanet Charlot
 */
public class Client {
	/** 1 second timeout for waiting replies */
	protected static final int TIMEOUT = 1000;
	/** la machine supportant le serveur d'impression */
	private String host = "localhost";
	/** le port d'installation du serveur d'impression */
	private int port = 3000;
	/** le listener UDP est-il vivant */
	private boolean alive = true;
	/** le logger du client */
	private Logger log = Logger.getLogger("Jus.Aor.Printing.Client", "jus.aor.printing.Client");
	/** l'interfaçage avec la console du client */
	private ClientGUI GUI;

	/**
	 * construction d'un client
	 */
	public Client() {
		GUI = new ClientGUI(this);
		log.setLevel(Level.INFO_2);
	}

	/**
	 * Choix d'une imprimante distante en indiquant l'@ du servive d'impression
	 * associé : se connecte à ce serveur d'impression, dès lors on peut envoyer
	 * des requêtes à celui-ci.
	 */
	public void selectPrinter() {
		// constuction du listener de spooler
		// -----------------------------------------------------------------------------
		// A COMPLETER
	}

	/**
	 * Se déconnecte d'un serveur d'impression, on ne peut plus envoyer de
	 * requête au serveur.
	 */
	public void deselectPrinter() {
		// arrêt du listener de spooler
		// -----------------------------------------------------------------------------
		// A COMPLETER
	}

	/**
	 * L'écoute des communications du spooler
	 */
	protected void runUDP() {
		// -----------------------------------------------------------------------------
		// A COMPLETER
	}

	/**
	 * @param key
	 *            la cle du du job
	 * @throws IOException
	 */
	private void jobTerminated(JobKey key) throws IOException {
		GUI.removePrintList(key);
	}

	/**
	 * Fixe le remote host
	 * 
	 * @param host
	 *            le remote host supportant le serveur d'impression
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Fixe le port du serveur d'impression
	 * 
	 * @param port
	 *            le port supportant le serveur d'impression
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * protocole du print d'un fichier
	 * 
	 * @param f
	 *            le fichier à imprimer
	 */
	private void onePrint(File f) {
		Socket soc = null;
		JobKey jk = new JobKey();
		try (InputStream fis = new FileInputStream(f)) {
			Notification ret;
			soc = new Socket("127.0.0.1", 3000);
			log.log(Level.INFO_2, "Connected to " + soc.getInetAddress());
			log.log(Level.INFO_2, "Ecriture de Notification " + Notification.QUERY_PRINT + " et JobKey " + jk.toString() + " ...");
			TCP.writeProtocole(soc, Notification.QUERY_PRINT);
			TCP.writeJobKey(soc, jk);
			log.log(Level.INFO_2, "Done");
			log.log(Level.INFO_2, "Lecture de notification...");
			ret = TCP.readProtocole(soc);
			log.log(Level.INFO_2, "Notification lue : " + ret);
			if (!TCP.readJobKey(soc).equals(jk))
				throw new Exception("JobKey incorrecte");

			if (ret == REPLY_PRINT_OK) {
				// Dans le cas où tout est correct on ajoute le job à la liste
				// des encours.
				 {log.log(Level.INFO_3,"Client.QueryPrint.Processing",jk);
				 GUI.addPrintList(jk);}
				 TCP.writeData(soc, fis, (int)f.length());
			} else
				log.log(Level.WARNING, "Client.QueryPrint.Failed", ret.toString());
		} catch (NumberFormatException e) {
			log.log(Level.SEVERE, "Client.QueryPrint.Port.Error", e.getMessage());
		} catch (UnknownHostException e) {
			log.log(Level.SEVERE, "Client.QueryPrint.Remote.Error", e.getMessage());
		} catch (IOException e) {
			log.log(Level.SEVERE, "Client.QueryPrint.IO.Error", e.getMessage());
		} catch (Exception e) {
			System.out.println(e.toString());
			log.log(Level.SEVERE, "Client.Jobkey.Incorrect", e.getMessage());
		}
	}

	/**
	 * Réalise l'émission quasi-simultanée de n requêtes d'impression à l'aide
	 * de onePrint
	 * 
	 * @param f
	 *            le fichier à imprimer
	 * @param n
	 *            nombre de requêtes d'impression à faire
	 */
	public void queryPrint(final File f, int n) {
		for (int i = 0; i < n; i++)
			new Thread() {
				public void run() {
					onePrint(f);
				}
			}.start();
	}

	/**
	 * protocole du server status
	 */
	public void queryStatus() {
		// --------------------------------------------------------------------------
		// A COMPLETER
	}

	/**
	 * protocole de délai d'impression
	 * 
	 * @param key
	 *            la clé du job
	 */
	public void queryJobs(JobKey key) {
		// --------------------------------------------------------------------------
		// A COMPLETER
	}

	/**
	 * @param args
	 */
	public static void main(String args[]) {
		new Client();
	}
}
