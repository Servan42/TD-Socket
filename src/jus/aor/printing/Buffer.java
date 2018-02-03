package jus.aor.printing;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Tableau de garde-action 
 *	Methode | Garde 					| Action
 *	--------|---------------------------|------------------
 *	get 	| buffer.length > 0 		| return message, vider cette case du buffer
 *	put 	| buffer.length < buffsize 	| remplir une case du buffer
 */

/**
 * Classe qui gère les stockages et destockages d'un tampon
 * 
 * @author CHANET CHARLOT
 *
 */
public class Buffer<Content> {

		private int buffSize;
		private ArrayList<Content> buffer;
		private int toGet;
		private int toPut;
		private final Lock lock = new ReentrantLock();
		private final Condition notFull = lock.newCondition();
		private final Condition notEmpty = lock.newCondition();
		
		/**
		 * Constructeur de ProdCons
		 * 
		 * @param size
		 *            Nombre de places à allouer dans le buffer.
		 */
		public Buffer(int size) {
			buffSize = size;
			buffer = new ArrayList<Content>();
			for(int i=0; i<buffSize; i++)
				buffer.add(null);
			toGet = 0;
			toPut = 0;
		}

		/**
		 * @return Nombre de message en attente de consommation (nombre de cases
		 *         utilisées dans le tableau)
		 */
		public int enAttente() {
			int ret;
			if(toGet < toPut)
				ret = toPut-toGet;
			else
				ret = buffSize-toGet+toPut;
			return ret;
		}

		/**
		 * Methode permettant au Consommateur de recuperer un message dans le
		 * tampon. Recupère le message le plus ancien du tampon, et met la case du
		 * tampon à <code>null</code> après recuperation
		 * 
		 * @param arg0
		 *            Consommateur qui demande le message
		 * @return Message sorti du tampon.
		 */
		public Content get() throws Exception, InterruptedException {
			Content retour;
			lock.lock();
			try {
				while (buffer.get(toGet) == null) {
					try {
						notEmpty.await();
					} catch (Exception e) {
						System.out.println("Fonction get " + e.toString());
					}
				}

				retour = buffer.get(toGet);

				buffer.set(toGet, null);
				toGet = (toGet+1)%buffSize;
				notFull.signal();
			} finally {
				lock.unlock();
			}
			return retour;
		}

		/**
		 * Methode permettant au Producteur de poser un message dans le tampon, à un
		 * emplacement vide (=null), et seulement si le tampon n'est pas plein.
		 * 
		 * @param arg0
		 *            Producteur qui pose le message
		 * @param arg1
		 *            Message à deposer dans le tampon
		 */
		public void put(Content arg1) throws Exception, InterruptedException {
			lock.lock();
			try {
				while (buffer.get(toPut) != null)
					try {
						notFull.await();
					} catch (Exception e) {
						System.out.println("Fonction put " + e.toString());
					}

				buffer.set(toPut,arg1);
				toPut = (toPut+1)%buffSize;
				notEmpty.signal();
			} finally {
				lock.unlock();
			}
		}

		/**
		 * Getter de buffSize
		 * 
		 * @return Taille du buffer
		 */
		public int taille() {
			return buffSize;
		}
}
