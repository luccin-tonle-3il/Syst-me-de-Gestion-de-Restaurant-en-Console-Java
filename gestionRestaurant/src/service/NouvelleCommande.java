package service;

import model.Commande;


public class NouvelleCommande extends CommandeState {

	public NouvelleCommande(Commande commade) {
		super(commade);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void operationPaye() {
		// TODO Auto-generated method stub
		System.out.println ("Deja paye");
	}

	@Override
	public void operationNouvelle() {
		// TODO Auto-generated method stub
		System.out.println ("Nouvelle Commande");
	}

	@Override
	public void operationEnCours() {
		// TODO Auto-generated method stub
			commade.setEtat(new CommandeEnCours(commade));
			System.out.println ("Méssage en attente");
	}

	@Override
	public void operationLivree() {
		// TODO Auto-generated method stub
		System.out.println ("impossible");
	}

	@Override
	public void operationPret() {
		// TODO Auto-generated method stub
		System.out.println ("impossible");
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		System.out.println ("Nouvelle Commande");
	}
	
	

}
