package jus.poc.prodcons.v5;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
	 * Le lock pour la protection du partage de données pour les méthodes put et
	 * get
	 */
	private final Lock lock = new ReentrantLock();
	/**
	 * La condition de production
	 */
	private final Condition nonPlein = lock.newCondition();
	/**
	 * La condition de consommation
	 */
	private final Condition nonVide = lock.newCondition();

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
		lock.lock();

		try {
			while (!(enAttente() > 0)) {
				nonVide.await();
			}
			Message message;

			message = buffer.remove();
			ob.retraitMessage(arg0, message);

			nonPlein.signal();

			return message;
		} finally {
			lock.unlock();
		}

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
		lock.lock();

		try {
			while (!(enAttente() < taille())) {
				nonPlein.await();
			}

			buffer.add(arg1);
			ob.depotMessage(arg0, arg1);

			nonVide.signal();
		} finally {
			lock.unlock();
		}

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
