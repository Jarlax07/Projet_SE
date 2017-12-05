package jus.poc.prodcons.v1;

import java.util.Properties;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

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

	public TestProdCons(Observateur observateur) {
		super(observateur);
	}

	protected void run() throws Exception {
		// le corps de votre programme principal
		init("jus/poc/prodcons/options/options.xml");
		
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