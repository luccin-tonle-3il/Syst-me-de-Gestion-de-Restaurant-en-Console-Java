package service;

import model.Commande;

public class CommandePrete extends EtatCommande {
	
	  @Override
	    public void suivant(Commande commande) {
	        commande.setEtat(new CommandeLivree());
	        System.out.println("Commande livrée.");
	    }

	    @Override
	    public void annuler(Commande commande) {
	        System.out.println("Impossible d’annuler une commande prête.");
	    }

	    @Override
	    public String getEtat() {
	        return "Prête";
	    }

	    @Override
	    public void operationPaye(Commande commande) {
	        System.out.println("Impossible. Doit être livrée.");
	    }

	    @Override
	    public void operationNouvelle(Commande commande) {
	        System.out.println("Impossible.");
	    }

	    @Override
	    public void operationEnCours(Commande commande) {
	        System.out.println("Déjà passée.");
	    }

	    @Override
	    public void operationLivree(Commande commande) {
	        suivant(commande);
	    }

	    @Override
	    public void operationPret(Commande commande) {
	        System.out.println("Déjà prête.");
	    }

	    @Override
	    public void doAction(Commande commande) {
	        System.out.println("Commande prête pour la livraison.");
	    }

}
