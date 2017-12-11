package jus.poc.prodcons.v3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	private Observateur ob;
	private int capacity;
	private boolean fini = false;

	private ArrayBlockingQueue<Message> buffer; // FIFO
	private Semaphore nonVide = new Semaphore(0); // Condition de consommation

	private Semaphore nonPlein; // Condition de production
	private Semaphore mutexIn = new Semaphore(1); // Protection pour le partage
	private Semaphore mutexOut = new Semaphore(1); // des données

	public ProdCons(Observateur ob, int capacity) {
		this.ob = ob;
		this.capacity = capacity;
		buffer = new ArrayBlockingQueue<Message>(this.capacity);

		nonPlein = new Semaphore(this.capacity);
	}

	@Override
	// Retourne le nombre de message déjà ajouté dans le tampon
	public int enAttente() {
		return buffer.size();
	}

	@Override
	// Recupère un message dans le tampon
	public Message get(_Consommateur arg0) throws Exception, InterruptedException {
		try {
			nonVide.acquire();
			mutexOut.acquire();

		} catch (InterruptedException e) {

			if (fini()) {
				throw new Exception("Fin");
			}
			e.printStackTrace();
		}

		Message message = buffer.remove();
		ob.retraitMessage(arg0, message);
		mutexOut.release();
		nonPlein.release();
		return message;
	}

	@Override
	// Ajoute un message dans le tampon
	public void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {

		try {
			nonPlein.acquire();
			mutexIn.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		buffer.add(arg1);
		ob.depotMessage(arg0, arg1);
		mutexIn.release();
		nonVide.release();

	}

	@Override
	// Capacité maximale du tampon
	public int taille() {
		return capacity;
	}

	// Reveille tout les threads en attente dans get
	synchronized public void reveiller() {
		fini = true;
	}

	// Accesseur de la variable fini
	public boolean fini() {
		return fini;
	}

}
