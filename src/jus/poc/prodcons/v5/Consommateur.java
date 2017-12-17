package jus.poc.prodcons.v5;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;


/**
 * 
 * @author AUBERT Vincent et COURTIAL Julien
 *
 */
public class Consommateur extends Acteur implements _Consommateur {

	/**
	 * Le buffer associé au programme
	 */
	private ProdCons buffer;
	/**
	 * Le nombre de messages consommé par le consommateur
	 */
	private int nbmsg;
	/**
	 * La variable aléatoire de temps
	 */
	private Aleatoire time;
	/**
	 * L'observateur du professeur
	 */
	private Observateur ob;
	
	/**
	 * 
	 * Le constructeur d'un consommateur
	 * 
	 * @param observateur
	 * 		L'observateur du professeur
	 * @param moyenneTempsDeTraitement
	 * 		Le temps moyen de consommation
	 * @param deviationTempsDeTraitement
	 * 		La déviation du temps moyen de consommation
	 * @param buffer
	 * 		Le buffer associé au programme
	 * @throws ControlException
	 */
	protected Consommateur(Observateur observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
			ProdCons buffer) throws ControlException {
		super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);

		this.buffer = buffer;
		this.nbmsg = 0;

		time = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		ob = observateur;
		this.setDaemon(true);
	}

	/**
	 * Renvoie le nombre de message consommé
	 * @return
	 * 		Le nombre de message déjà consommé
	 */
	public int nombreDeMessages() {
		return nbmsg;
	}
	
	/**
	 * 
	 */
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
