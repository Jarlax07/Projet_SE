package jus.poc.prodcons.v1;

import java.util.concurrent.ArrayBlockingQueue;

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
	 * La capacité du buffer
	 */
	private int capacity;
	/**
	 * Le buffer
	 */
	private ArrayBlockingQueue<Message> buffer;

	/**
	 * Le constructeur du buffer
	 * 
	 * @param ob
	 *            L'observateur du professeur
	 * @param capacity
	 *            La capacité du buffer
	 */
	public ProdCons(Observateur ob, int capacity) {
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
	synchronized public Message get(_Consommateur arg0) throws Exception, InterruptedException {

		// Tant que le tampon est vide et que le programme n'est pas fini on
		// attend
		while (!(enAttente() > 0)) {
			try {
				wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Message msg = buffer.remove();
		notifyAll();

		return msg;
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
	synchronized public void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {

		// Tant que le buffer est complet, attendre
		while (!(enAttente() < taille())) {
			try {
				wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		buffer.add(arg1);

		notifyAll();

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
