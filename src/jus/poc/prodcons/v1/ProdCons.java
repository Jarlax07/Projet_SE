package jus.poc.prodcons.v1;

import java.util.concurrent.ArrayBlockingQueue;

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

	public ProdCons(Observateur ob, int capacity) {
		this.ob = ob;
		this.capacity = capacity;
		buffer = new ArrayBlockingQueue<Message>(this.capacity);
	}

	@Override
	// Retourne le nombre de message déjà ajouté dans le tampon
	public int enAttente() {
		return buffer.size();
	}

	@Override
	// Recupère un message dans le tampon
	synchronized public Message get(_Consommateur arg0) throws Exception, InterruptedException {

		// Tant que le tampon est vide et que le programme n'est pas fini on
		// attend
		while (!(enAttente() > 0) && !fini()) {
			try {
				wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Si le programme est fini on sort de la methode en envoyant une
		// Exception
		if (fini()) {
			throw new Exception("Fin");
		}

		Message msg = buffer.remove();
		notifyAll();

		return msg;
	}

	@Override
	// Ajoute un message dans le tampon
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

	@Override
	// Capacité maximale du tampon
	public int taille() {
		return capacity;
	}

	// Reveille tout les threads en attente dans get
	synchronized public void reveiller() {
		fini = true;
		notifyAll();
	}

	// Accesseur de la variable fini
	public boolean fini() {
		return fini;
	}

}
