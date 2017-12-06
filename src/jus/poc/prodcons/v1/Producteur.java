package jus.poc.prodcons.v1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {
	private ProdCons buffer;
	private int nbmsg;

	protected Producteur(Observateur observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
			ProdCons buffer, int nombreMoyenDeProduction, int deviationNombreMoyenDeProduction)
			throws ControlException {
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		// TODO Auto-generated constructor stub
		this.buffer = buffer;

		nbmsg = Aleatoire.valeur(nombreMoyenDeProduction, deviationNombreMoyenDeProduction);
	}

	@Override
	public int nombreDeMessages() {
		return nbmsg;
	}

	public void run() {
		
		for(int i=0; i<nbmsg;i++){
			try {
				buffer.put(this, new MessageX("Bonjour"));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	}

}
