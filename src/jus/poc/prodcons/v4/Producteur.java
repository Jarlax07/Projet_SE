package jus.poc.prodcons.v4;

import java.util.concurrent.Semaphore;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {
	private ProdCons buffer;
	private int nbmsg;
	private Aleatoire nbex;
	private Aleatoire time;
	private Observateur ob;
	private int Exemplaires[];
	
	// Semaphore lié à la contrainte producteur ne produit plus tant que tout
	// les exemplaire du message ne sont pas consommé
	private Semaphore sem = new Semaphore(0);

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

	@Override
	// Renvoie le nombre de message que doit produire le producteur
	public int nombreDeMessages() {
		int somme = 0;
		for (int i = 0; i < nbmsg; i++) {
			somme += Exemplaires[i];
		}
		return somme;
	}

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
				msg = new MessageX("Bonjour" + i, Exemplaires[i], this);
				ob.productionMessage(this, msg, t);
				buffer.put(this, msg);
				sem.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public Semaphore getSem() {
		return sem;
	}

}
