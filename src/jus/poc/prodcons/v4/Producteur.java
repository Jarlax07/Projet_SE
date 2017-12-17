package jus.poc.prodcons.v4;

import java.util.concurrent.Semaphore;

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
	 * Variable aléatoire du nombre d'exemplaire
	 */
	private Aleatoire nbex;
	/**
	 * Variable aléatoire de temps
	 */
	private Aleatoire time;
	/**
	 * L'observateur du professeur
	 */
	private Observateur ob;
	/**
	 * Le tableau du nombre d'exemplaire pour chaque message
	 */
	private int Exemplaires[];

	/**
	 * Semaphore lié à la contrainte : un producteur ne produit plus tant que
	 * tout les exemplaire du message ne sont pas consommé
	 */
	private Semaphore sem = new Semaphore(0);

	/**
	 * Constructeur d'un producteur
	 * 
	 * @param observateur
	 *            L'observateur du professeur
	 * @param moyenneTempsDeTraitement
	 *            La moyenne de temps de production
	 * @param deviationTempsDeTraitement
	 *            La déviation de la moyenne de temps de production
	 * @param buffer
	 *            Le buffer associé au programme
	 * @param nombreMoyenDeProduction
	 *            Le nombre moyen de messages à produire
	 * @param deviationNombreMoyenDeProduction
	 *            La déviation du nombre moyen de messages à produire
	 * @param nombreMoyenNbExemplaire
	 *            Le nombre moyen du nombre d'exemplaires
	 * @param deviationNombreMoyenNbExemplaire
	 *            La déviation du nombre moyen du nom d'exemplaires
	 * @throws ControlException
	 */
	protected Producteur(Observateur observateur, int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
			ProdCons buffer, int nombreMoyenDeProduction, int deviationNombreMoyenDeProduction,
			int nombreMoyenNbExemplaire, int deviationNombreMoyenNbExemplaire) throws ControlException {
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.buffer = buffer;

		nbmsg = Aleatoire.valeur(nombreMoyenDeProduction, deviationNombreMoyenDeProduction);

		time = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);

		nbex = new Aleatoire(nombreMoyenNbExemplaire, deviationNombreMoyenNbExemplaire);

		Exemplaires = new int[nbmsg];

		for (int i = 0; i < nbmsg; i++) {
			Exemplaires[i] = nbex.next();
		}

		ob = observateur;
	}

	/**
	 * Renvoie le nombre de message que doit produire le producteur
	 * 
	 * @return Le nombre de message à produire
	 */
	public int nombreDeMessages() {
		int somme = 0;
		for (int i = 0; i < nbmsg; i++) {
			somme += Exemplaires[i];
		}
		return somme;
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
				sleep(t * 100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			try {
				msg = new MessageX("Bonjour " + i + " " + this.getId(), Exemplaires[i], this);
				ob.productionMessage(this, msg, t);
				buffer.put(this, msg);
				sem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.yield();
		}

	}

	/**
	 * Renvoie le sémaphore lié au producteur
	 * 
	 * @return Le semaphore lié au producteur
	 */
	public Semaphore getSem() {
		return sem;
	}

}
