package service;

import model.Commande;

public class CommandePret extends CommandeState {
	
	
	public CommandePret(Commande commade) {
		super(commade);
	}

	@Override
	public void operationPaye() {
		// TODO Auto-generated method stub
		System.out.println ("Deja paye");

	}

	@Override
	public void operationNouvelle() {
		// TODO Auto-generated method 
		System.out.println ("impossible");

	}

	@Override
	public void operationEnCours() {
		// TODO Auto-generated method stub
		System.out.println ("impossible");
	}

	@Override
	public void operationLivree() {
		// TODO Auto-generated method stub
		commade.setEtat(new CommandeLivree(commade));
		System.out.println ("Commande Livree");
	}

	@Override
	public void operationPret() {
		// TODO Auto-generated method stub
		System.out.println ("commande pret");
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		System.out.println ("Commande Prete");
	}

}
