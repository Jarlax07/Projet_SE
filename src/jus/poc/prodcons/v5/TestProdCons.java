package jus.poc.prodcons.v5;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

/**
 * 
 * @author AUBERT Vincent et COURTIAL Julien
 *
 */
public class TestProdCons extends Simulateur {
	/**
	 * Le nombre de producteurs
	 */
	protected int nbProd;
	/**
	 * Le nombre de consommateurs
	 */
	protected int nbCons;
	/**
	 * La capacité du buffer
	 */
	protected int nbBuffer;
	/**
	 * Le temps moyen entre chaque production
	 */
	protected int tempsMoyenProduction;
	/**
	 * La déviation du temps moyen entre chaque production
	 */
	protected int deviationTempsMoyenProduction;
	/**
	 * Le temps moyen entre chaque consommation
	 */
	protected int tempsMoyenConsommation;
	/**
	 * La déviation du temps moyen entre chaque consommation
	 */
	protected int deviationTempsMoyenConsommation;
	/**
	 * Le nombre moyen de production
	 */
	protected int nombreMoyenDeProduction;
	/**
	 * La déviation du nombre moyen de production
	 */
	protected int deviationNombreMoyenDeProduction;
	/**
	 * Le nombre moyen du nombre d'exemplaires de messages
	 */
	protected int nombreMoyenNbExemplaire;
	/**
	 * La déviation du nombre moyen du nombre d'exemplaires de messages
	 */
	protected int deviationNombreMoyenNbExemplaire;

	/**
	 * L'observateur du professeur
	 */
	protected Observateur ob;

	/**
	 * 
	 * 
	 * @param observateur
	 *            L'observateur du professeur
	 */
	public TestProdCons(Observateur observateur) {
		super(observateur);
		ob = observateur;
	}

	/**
	 * Programme principal
	 */
	protected void run() throws Exception {

		// Récupère les variables à partir d'un fichier xml
		init("jus/poc/prodcons/options/options.xml");

		Consommateur cons[] = new Consommateur[nbCons];

		Producteur prod[] = new Producteur[nbProd];

		ProdCons buffer = new ProdCons(ob, nbBuffer);

		ob.init(nbProd, nbCons, nbBuffer);

		// Créer et démarre les consommateurs
		for (int i = 0; i < nbCons; i++) {
			cons[i] = new Consommateur(ob, tempsMoyenConsommation, deviationTempsMoyenConsommation, buffer);
			cons[i].start();
			ob.newConsommateur(cons[i]);
		}

		// Créer et démarre les producteurs
		for (int i = 0; i < nbProd; i++) {
			prod[i] = new Producteur(ob, tempsMoyenProduction, deviationTempsMoyenProduction, buffer,
					nombreMoyenDeProduction, deviationNombreMoyenDeProduction);
			prod[i].start();
			ob.newProducteur(prod[i]);
		}

		// Boucle tant que tout les messages n'ont pas été produit et consommé
		while (!(sum_prod(prod, nbProd) == sum_cons(cons, nbCons))) {

		}

		System.out.println("Fini");
	}

	/**
	 * Calcule la somme du nombre de messages des producteurs
	 * 
	 * @param p
	 *            Le tableau contenant tout les producteurs.
	 * @param taille
	 *            La taille du tableau.
	 * @return Le nombre de messages que les producteurs doivent produire
	 */
	public int sum_prod(Producteur p[], int taille) {
		int somme = 0;
		for (int i = 0; i < taille; i++) {
			somme += p[i].nombreDeMessages();
		}
		return somme;
	}

	/**
	 * Calcule la somme du nombre de message consommé par les producteurs
	 * 
	 * @param c
	 *            Le tableau contenant tout les consommateurs.
	 * @param taille
	 *            La taille du tableau.
	 * @return Le nombre de messages que les consommateurs ont déjà consommé.
	 */
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