package jus.poc.prodcons.v6;

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
	private ObservateurPerso ob2;

	protected Consommateur(Observateur observateur,ObservateurPerso ob2, int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
			ProdCons buffer) throws ControlException {
		super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);

		this.buffer = buffer;
		this.nbmsg = 0;

		time = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		ob = observateur;
		this.ob2 =ob2;
		this.setDaemon(true);
	}

	@Override
	// Renvoi le nombre de message deja consommé
	public int nombreDeMessages() {
		return nbmsg;
	}

	public void run() {
		int t = 0;

		Message msg;
		while (true) {
			// On attend un certain temps avant de consommé
			try {
				t = time.next();
				sleep(t);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			try {
				msg = buffer.get(this);
				System.out.println(msg);
				ob.consommationMessage(this, msg, t);
				ob2.consommationMessage(msg);
				nbmsg++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {

				e.printStackTrace();

			}
			Thread.yield();
		}

	}

}
