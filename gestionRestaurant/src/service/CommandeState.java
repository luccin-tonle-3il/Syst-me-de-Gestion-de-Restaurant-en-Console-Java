package service;

import model.Commande;


public abstract class CommandeState {

	protected Commande commade;
	
	
	public CommandeState(Commande commade) {
		super();
		this.commade = commade;
	}
	
	public abstract void operationPaye();
	public abstract void operationNouvelle();
	public abstract void operationEnCours();
	public abstract void operationLivree();
	public abstract void operationPret();
	

	public abstract void doAction();
}
