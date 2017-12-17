package jus.poc.prodcons.v6;

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
	 * Création d'un message
	 * 
	 * @param msg
	 *            Le message
	 */
	public MessageX(String msg) {
		this.msg = msg;

	}

	/**
	 * 
	 */
	public String toString() {

		return msg;
	}
}
