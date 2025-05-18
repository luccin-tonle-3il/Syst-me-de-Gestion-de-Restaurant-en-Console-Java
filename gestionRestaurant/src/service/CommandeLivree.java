package service;

import model.Commande;

public class CommandeLivree extends CommandeState {

	public CommandeLivree(Commande commade) {
		super(commade);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void operationPaye() {
		// TODO Auto-generated method stub
		System.out.println ("déja payé");
	}

	@Override
	public void operationNouvelle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void operationEnCours() {
		// TODO Auto-generated method stub
		System.out.println ("impossible");
		
	}

	@Override
	public void operationLivree() {
		// TODO Auto-generated method stub
		System.out.println ("Commande livree");
		
	}

	@Override
	public void operationPret() {
		// TODO Auto-generated method stub
		System.out.println ("impossible");
		
	}

	@Override
	public void doAction() {
		// TODO Auto-generated method stub
		System.out.println ("Commande Livree");
		
	}

}
