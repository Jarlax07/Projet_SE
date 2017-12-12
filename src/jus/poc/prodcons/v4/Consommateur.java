package jus.poc.prodcons.v4;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur {

	private ProdCons buffer;
	private int nbmsg;
	private Aleatoire time;
	private Observateur ob;

	protected Consommateur(Observateur observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
			ProdCons buffer) throws ControlException {
		super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);

		this.buffer = buffer;
		this.nbmsg = 0;

		time = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		ob=observateur;
	}

	@Override
	// Renvoi le nombre de message deja consommé
	public int nombreDeMessages() {
		return nbmsg;
	}

	public void run() {
		int t=0;
		// On attend un certain temps avant de consommé
		try {
			t=time.next();
			sleep(t * 100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		Message msg;
		while (!buffer.fini()) {
			try {
				msg = buffer.get(this);
				System.out.println(msg);
				ob.consommationMessage(this, msg, t);
				nbmsg++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {

				// Si l'exception récupéré n'est pas l'exception de fin du
				// consommateur on affiche la trace
				if (!e.getMessage().equals("Fin"))
					e.printStackTrace();

			}
		}

	}

}
