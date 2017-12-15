package jus.poc.prodcons.v6;

import jus.poc.prodcons.Message;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ObservateurPerso {
	
	private int nbmsgdepot;
	private int nbmsgretir;
	private int nbmsgcons;
	
	public ObservateurPerso() {
		nbmsgdepot=0;
		nbmsgretir=0;
		nbmsgcons=0;
	}


	public void depotMessage( Message m) {
		nbmsgdepot++;
		System.out.println(nbmsgdepot + "eme message déposer : "+m);
	}

	public void retraitMessage(Message m) {
		nbmsgretir++;
		System.out.println(nbmsgretir+ "eme message retirer : "+m);
	}

	public void consommationMessage( Message m) {
		nbmsgcons++;
		System.out.println(nbmsgcons+ "eme message consommé : "+m);
		
	}

}
