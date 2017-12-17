package jus.poc.prodcons.v5;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

/**
 * 
 * @author AUBERT Vincent et COURTIAL Julien
 *
 */
public class Producteur extends Acteur implements _Producteur {
	/**
	 * Le buffer associé au programme
	 */
	private ProdCons buffer;
	/**
	 * Le nombre de messages que le producteur doit produire
	 */
	private int nbmsg;
	/**
	 * Variable aléatoire de temps
	 */
	private Aleatoire time;
	/**
	 * L'observateur du professeur
	 */
	private Observateur ob;

	/**
	 * 
	 * Constructeur d'un producteur
	 * 
	 * @param observateur
	 *            L'observateur du professeur
	 * @param moyenneTempsDeTraitement
	 *            La moyenne de temps de production
	 * @param deviationTempsDeTraitement
	 *            La déviation de la moyenne de temps de production
	 * @param buffer2
	 *            Le buffer associé au programme
	 * @param nombreMoyenDeProduction
	 *            Le nombre moyen de messages à produire
	 * @param deviationNombreMoyenDeProduction
	 *            La déviation du nombre moyen de messages à produire
	 * @throws ControlException
	 */
	protected Producteur(Observateur observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
			ProdCons buffer, int nombreMoyenDeProduction, int deviationNombreMoyenDeProduction)
			throws ControlException {
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.buffer = buffer;

		nbmsg = Aleatoire.valeur(nombreMoyenDeProduction, deviationNombreMoyenDeProduction);

		time = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		ob = observateur;
	}

	/**
	 * Renvoie le nombre de message que doit produire le producteur
	 * 
	 * @return Le nombre de message à produire
	 */
	public int nombreDeMessages() {
		return nbmsg;
	}

	/**
	 * 
	 */
	public void run() {
		int t = 0;
		MessageX msg;
		for (int i = 0; i < nbmsg; i++) {

			// On attend un certain temps avant de produire un message
			try {
				t = time.next();
				sleep(t);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			try {
				msg = new MessageX("Bonjour " + i + " " + this.getId());
				ob.productionMessage(this, msg, t);
				buffer.put(this, msg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			Thread.yield();
		}

	}

}
