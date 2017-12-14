package jus.poc.prodcons.v4;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	private String msg;
	private int nbExemplaire=0;
	private Producteur prod; // Producteur emetteur du message

	public MessageX(String msg,int nbEx, Producteur prod) {
		this.msg = msg;
		this.nbExemplaire = nbEx;
		this.prod=prod;
	}

	public String toString() {

		return msg;
	}
	
	public int getNbEx(){
		return nbExemplaire;
	}
	
	public void decrement(){
		this.nbExemplaire --;
	}
	
	public Producteur getProd(){
		return prod;
	}
	
}
