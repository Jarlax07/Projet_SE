package jus.poc.prodcons.v3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

/**
 * 
 * @author AUBERT Vincent et COURTIAL Julien
 *
 */
public class ProdCons implements Tampon {

	/**
	 * L'observateur du professeur
	 */
	private Observateur ob;
	/**
	 * La capacité du buffer
	 */
	private int capacity;
	/**
	 * Le buffer
	 */
	private ArrayBlockingQueue<Message> buffer;
	/**
	 * Le semaphore représentant la condition de consommation.
	 */
	private Semaphore nonVide = new Semaphore(0);
	/**
	 * Le semaphore représentant la condition de production.
	 */
	private Semaphore nonPlein;
	/**
	 * Le semaphore de protection du partage de données pour la methode put
	 */
	private Semaphore mutexIn = new Semaphore(1);
	/**
	 * Le semaphore de protection du partage de données pour la méthode get.
	 */
	private Semaphore mutexOut = new Semaphore(1);

	/**
	 * Le constructeur du buffer
	 * 
	 * @param ob
	 *            L'observateur du professeur
	 * @param capacity
	 *            La capacité du buffer
	 */
	public ProdCons(Observateur ob, int capacity) {
		this.ob = ob;
		this.capacity = capacity;
		buffer = new ArrayBlockingQueue<Message>(this.capacity);

		nonPlein = new Semaphore(this.capacity);
	}

	/**
	 * Le nombre de message dans le buffer
	 * 
	 * @return le nombre de message déjà ajouté dans le buffer
	 */
	public int enAttente() {
		return buffer.size();
	}

	/**
	 * Bloque tant qu'il n'est pas possible de retirer puis retire un message de
	 * tête du buffer
	 * 
	 * @param arg0
	 *            Le consommateur du message
	 * @return Le message en tête du buffer
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public Message get(_Consommateur arg0) throws Exception, InterruptedException {
		try {
			nonVide.acquire();
			mutexOut.acquire();

		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		Message message;

		synchronized (this) {

			message = buffer.remove();
			ob.retraitMessage(arg0, message);
		}
		mutexOut.release();
		nonPlein.release();
		return message;
	}

	/**
	 * Bloque tant qu'il n'est pas possible d'ajouter et ajoute un message en
	 * fin de buffer
	 * 
	 * @param arg0
	 *            Le producteur du message
	 * @param arg1
	 *            Le message produit
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {

		try {
			nonPlein.acquire();
			mutexIn.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (this) {
			buffer.add(arg1);
			ob.depotMessage(arg0, arg1);

		}
		mutexIn.release();
		nonVide.release();

	}

	/**
	 * La taille du buffer
	 * 
	 * @return La capacité du buffer
	 */
	public int taille() {
		return capacity;
	}

}
