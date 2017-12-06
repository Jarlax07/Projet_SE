package jus.poc.prodcons.v1;

import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

import java.util.concurrent.ArrayBlockingQueue;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;

public class ProdCons implements Tampon {

	private Observateur ob;
	private int capacity;

	private ArrayBlockingQueue<Message> buffer;

	public ProdCons(Observateur ob, int capacity) {
		this.ob = ob;
		this.capacity =capacity;
		buffer = new ArrayBlockingQueue<Message>(this.capacity);
	}

	@Override
	public int enAttente() {
		return buffer.size();
	}

	@Override
	synchronized public Message get(_Consommateur arg0) throws Exception, InterruptedException {
		// TODO Auto-generated method stub
		
		
		while(!(enAttente()>0)){
			arg0.wait();
		}
		
		Message msg = buffer.remove();
		notifyAll();
		
		return msg;
	}

	@Override
	synchronized public void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		// TODO Auto-generated method stub
		
		while(!(enAttente() < taille())){
			arg0.wait();
		}
		
		buffer.add(arg1);
		
		notifyAll();

	}

	@Override
	public int taille() {
		return capacity;
	}

}
