package jus.poc.prodcons.v1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur {
	
	private ProdCons buffer;
	private int nbmsg;
	
	protected Consommateur(Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement,ProdCons buffer) throws ControlException {
			super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		// TODO Auto-generated constructor stub
			this.buffer = buffer;
			this.nbmsg=0;
	}

	@Override
	public int nombreDeMessages() {
		return nbmsg;
	}

	//Mettre consommateur dans un thread ?
	public void run() {
		System.out.println("Coucou");
		Message msg;
		try {
			msg = buffer.get(this);
			nbmsg++;
			System.out.println(msg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
