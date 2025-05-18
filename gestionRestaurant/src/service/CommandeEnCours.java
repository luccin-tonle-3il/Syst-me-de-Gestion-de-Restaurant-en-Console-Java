package service;

import model.Commande;

public class CommandeEnCours extends CommandeState {

	public CommandeEnCours(Commande commade) {
		super(commade);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void operationPaye() {
		// TODO Auto-generated method stub
		System.out.println ("deja");
	}

	@Override
	public void operationNouvelle() {
		// TODO Auto-generated method stub
		System.out.println ("impossible");
	}

	@Override
	public void operationEnCours() {
		// TODO Auto-generated method stub
		System.out.println ("EnCours");
		
	}

	@Override
	public void operationLivree() {
		// TODO Auto-generated method stub
		System.out.println ("impossible");
		
	}

	@Override
	public void operationPret() {
		// TODO Auto-generated method stub
		commade.setEtat(new CommandePret(commade));
		System.out.println ("Commande prete");
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		System.out.println ("Commande En Cours");
	}
	
}
