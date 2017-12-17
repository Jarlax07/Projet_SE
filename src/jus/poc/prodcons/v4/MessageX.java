package jus.poc.prodcons.v4;

import jus.poc.prodcons.Message;

/**
 * 
 * @author AUBERT Vincent et COURTIAL Julien
 *
 */
public class MessageX implements Message {
	/**
	 * Le message
	 */
	private String msg;
	/**
	 * Le nombre d'exemplaire du message
	 */
	private int nbExemplaire = 0;
	/**
	 * Le producteur du message
	 */
	private Producteur prod;

	/**
	 * Création d'un message
	 * 
	 * @param msg
	 *            Le message
	 * @param nbEx
	 *            Le nombre d'exemplaire du message
	 * @param prod
	 *            Le producteur du message
	 */
	public MessageX(String msg, int nbEx, Producteur prod) {
		this.msg = msg;
		this.nbExemplaire = nbEx;
		this.prod = prod;
	}

	/**
	 * 
	 */
	public String toString() {

		return msg;
	}

	/**
	 * Renvoie le nombre d'exemplaire du message
	 * 
	 * @return Le nombre d'exemplaire restant du message
	 */
	public int getNbEx() {
		return nbExemplaire;
	}

	/**
	 * Décrémente le nombre d'exemplaire de 1
	 */
	public void decrement() {
		this.nbExemplaire--;
	}

	/**
	 * Renvoie le producteur du message
	 * 
	 * @return Le producteur du message
	 */
	public Producteur getProd() {
		return prod;
	}

}
