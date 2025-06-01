package service;

import model.Commande;
import model.MenuItem;

public class CommandePayee extends EtatCommande{
	 @Override
	    public void suivant(Commande commande) {
	        System.out.println("Commande déjà finalisée.");
	    }

	    @Override
	    public void annuler(Commande commande) {
	        System.out.println("Impossible : commande déjà payée.");
	    }

	    @Override
	    public String getEtat() {
	        return "Payée";
	    }

	    @Override
	    public void operationPaye(Commande commande) {
	        System.out.println("Déjà payée.");
	    }

	    @Override
	    public void operationNouvelle(Commande commande) {
	        System.out.println("Impossible.");
	    }

	    @Override
	    public void operationEnCours(Commande commande) {
	        System.out.println("Impossible.");
	    }

	    @Override
	    public void operationLivree(Commande commande) {
	        System.out.println("Déjà payée.");
	    }

	    @Override
	    public void operationPret(Commande commande) {
	        System.out.println("Impossible.");
	    }

	    @Override
	    public void doAction(Commande commande) {
	        System.out.println("Commande terminée. Merci !");
	    }

}
