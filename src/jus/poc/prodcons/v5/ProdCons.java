package jus.poc.prodcons.v5;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	private Observateur ob;
	private int capacity;

	private ArrayBlockingQueue<Message> buffer; // FIFO
	private final Lock lock = new ReentrantLock();
	private final Condition nonPlein = lock.newCondition();
	private final Condition nonVide = lock.newCondition();

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

	@Override
	// Ajoute un message dans le tampon
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

	@Override
	// Capacité maximale du tampon
	public int taille() {
		return capacity;
	}

}
