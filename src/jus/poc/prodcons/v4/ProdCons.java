package jus.poc.prodcons.v4;

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

	private ArrayBlockingQueue<MessageX> buffer; // FIFO
	private Semaphore nonVide = new Semaphore(0); // Condition de consommation

	private Semaphore nonPlein; // Condition de production
	private Semaphore mutexIn = new Semaphore(1); // Protection pour le partage
	private Semaphore mutexOut = new Semaphore(1); // des données
	
	private Consommateur cons[];

	public ProdCons(Observateur ob, int capacity, Consommateur c[]) {
		this.ob = ob;
		this.capacity = capacity;
		buffer = new ArrayBlockingQueue<MessageX>(this.capacity);

		nonPlein = new Semaphore(this.capacity);
		cons=c;
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

			e.printStackTrace();
		}
		MessageX message;
		int nbex;
		synchronized (this) {
			message = buffer.iterator().next();
			nbex=message.getNbEx();
			if (nbex > 1) {
				message.decrement();
				ob.retraitMessage(arg0, message);
			} else {
				message = buffer.remove();
				ob.retraitMessage(arg0, message);
				this.debloquer(cons);
			}
		}
		
		mutexOut.release();
		if (nbex > 1) {
			nonVide.release();
		}else{
			nonPlein.release();
		}

		
		message.getProd().getSem().release();
		
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
		
		synchronized(this){

			buffer.add((MessageX) arg1);
			ob.depotMessage(arg0, arg1);
		}
		mutexIn.release();
		nonVide.release();

	}

	@Override
	// Capacité maximale du tampon
	public int taille() {
		return capacity;
	}
	
	public void debloquer(Consommateur c[]){
		for(int i=0; i< c.length;i++){
			if(c[i].getSem().hasQueuedThreads()){
				c[i].getSem().release();
			}
		}
	}

}
