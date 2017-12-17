package jus.poc.prodcons.v4;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

public class TestProdCons extends Simulateur {
	protected int nbProd;
	protected int nbCons;
	protected int nbBuffer;
	protected int tempsMoyenProduction;
	protected int deviationTempsMoyenProduction;
	protected int tempsMoyenConsommation;
	protected int deviationTempsMoyenConsommation;
	protected int nombreMoyenDeProduction;
	protected int deviationNombreMoyenDeProduction;
	protected int nombreMoyenNbExemplaire;
	protected int deviationNombreMoyenNbExemplaire;

	protected Observateur ob;

	public TestProdCons(Observateur observateur) {
		super(observateur);
		ob = observateur;
	}

	
	/**
	 * 
	 * 
	 * @require
	 * 		nombreMoyenNbExemplaire + deviationNombreMoyenNbExemplaire < nbCons
	 */
	protected void run() throws Exception {
		// le corps de votre programme principal

		// On récupère les variables dans un fichier xml
		init("jus/poc/prodcons/options/options.xml");

		if (nombreMoyenNbExemplaire + deviationNombreMoyenNbExemplaire > nbCons) {
			System.out.println(
					"Risque de bloquage car le nombre d'exemplaires d'un message peut être supérieur au nombre de consommateur.");
			System.out
					.println("Veuillez modifier vos paramètres pour que le programme puisse fonctionner correctement");

		} else {

			Consommateur cons[] = new Consommateur[nbCons];

			Producteur prod[] = new Producteur[nbProd];

			ProdCons buffer = new ProdCons(ob, nbBuffer, cons);

			ob.init(nbProd, nbCons, nbBuffer);

			// On créer et on démarre les consommateurs
			for (int i = 0; i < nbCons; i++) {
				cons[i] = new Consommateur(ob, tempsMoyenConsommation, deviationTempsMoyenConsommation, buffer);
				cons[i].start();
				ob.newConsommateur(cons[i]);
			}

			// On créé et on démarre les producteurs
			for (int i = 0; i < nbProd; i++) {
				prod[i] = new Producteur(ob, tempsMoyenProduction, deviationTempsMoyenProduction, buffer,
						nombreMoyenDeProduction, deviationNombreMoyenDeProduction, nombreMoyenNbExemplaire,
						deviationNombreMoyenNbExemplaire);
				prod[i].start();
				ob.newProducteur(prod[i]);
			}

			// On boucle tant que tout ce qui doit être produit n'a pas été
			// consommé
			while (!(sum_prod(prod, nbProd) == sum_cons(cons, nbCons))) {
			}

			System.out.println("Fini");

		}
	}

	// Calcule la somme des messages à produire par les producteurs
	public int sum_prod(Producteur p[], int taille) {
		int somme = 0;
		for (int i = 0; i < taille; i++) {
			somme += p[i].nombreDeMessages();
		}
		return somme;
	}

	// Calcule la somme des messages déjà consommé par les consommateurs
	public int sum_cons(Consommateur c[], int taille) {
		int somme = 0;
		for (int i = 0; i < taille; i++) {
			somme += c[i].nombreDeMessages();
		}
		return somme;
	}

	public static void main(String[] args) {
		new TestProdCons(new Observateur()).start();
	}

	/**
	 * Retreave the parameters of the application.
	 * 
	 * @param file
	 *            the final name of the file containing the options.
	 * @throws IOException
	 * @throws InvalidPropertiesFormatException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	protected void init(String file) throws InvalidPropertiesFormatException, IOException, IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		Properties properties = new Properties();
		properties.loadFromXML(ClassLoader.getSystemResourceAsStream(file));
		String key;
		int value;
		Class<?> thisOne = getClass();
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			key = (String) entry.getKey();
			value = Integer.parseInt((String) entry.getValue());
			thisOne.getDeclaredField(key).set(this, value);
		}
	}
}