package jus.poc.prodcons.v6;

import jus.poc.prodcons.Message;

/**
 * 
 * @author AUBERT Vincent et COURTIAL Julien
 *
 */
public class ObservateurPerso {
	/**
	 * Le nombre de message déposé
	 */
	private int nbmsgdepot;
	/**
	 * Le nombre de message retiré
	 */
	private int nbmsgretir;
	/**
	 * Le nombre de message consommé
	 */
	private int nbmsgcons;
	
	/**
	 * Constructeur de notre observateur
	 */
	public ObservateurPerso() {
		nbmsgdepot=0;
		nbmsgretir=0;
		nbmsgcons=0;
	}

	/**
	 * Methode appelé quand un message est déposé
	 * @param m
	 * 		Le message déposé
	 */
	public void depotMessage( Message m) {
		nbmsgdepot++;
		System.out.println(nbmsgdepot + "eme message déposer : "+m);
	}
	
	/**
	 * Méthode appelé quand un message est retiré
	 * @param m
	 * 		Le message retiré
	 */
	public void retraitMessage(Message m) {
		nbmsgretir++;
		System.out.println(nbmsgretir+ "eme message retirer : "+m);
	}
	
	/**
	 * Méthode appelé quand un message est consommé
	 * @param m
	 * 		Le message consommé
	 */
	public void consommationMessage( Message m) {
		nbmsgcons++;
		System.out.println(nbmsgcons+ "eme message consommé : "+m);
		
	}

}
