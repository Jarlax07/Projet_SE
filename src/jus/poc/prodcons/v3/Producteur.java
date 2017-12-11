package jus.poc.prodcons.v3;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {
	private ProdCons buffer;
	private int nbmsg;
	private Aleatoire time;

	protected Producteur(Observateur observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
			ProdCons buffer, int nombreMoyenDeProduction, int deviationNombreMoyenDeProduction)
			throws ControlException {
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.buffer = buffer;

		nbmsg = Aleatoire.valeur(nombreMoyenDeProduction, deviationNombreMoyenDeProduction);

		time = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);

	}

	@Override
	// Renvoi le nombre de message que doit produire le producteur
	public int nombreDeMessages() {
		return nbmsg;
	}

	public void run() {

		for (int i = 0; i < nbmsg; i++) {

			// On attend un certain temps avant de produire un message
			try {
				sleep(time.next() * 100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			try {
				buffer.put(this, new MessageX("Bonjour"+i));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
