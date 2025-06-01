package service;

import model.Commande;

public class CommandeLivree extends EtatCommande {
	  @Override
	    public void operationPaye(Commande commande) {
	        suivant(commande);
	    }

	    @Override
	    public void operationNouvelle(Commande commande) {
	        System.out.println("Commande déjà livrée.");
	    }

	    @Override
	    public void operationEnCours(Commande commande) {
	        System.out.println("Commande déjà livrée.");
	    }

	    @Override
	    public void operationLivree(Commande commande) {
	        System.out.println("Déjà livrée.");
	    }

	    @Override
	    public void operationPret(Commande commande) {
	        System.out.println("Commande déjà livrée.");
	    }

	    @Override
	    public void doAction(Commande commande) {
	        System.out.println("Commande livrée au client.");
	    }
	    
	    @Override
	    public String getEtat() {
	        return "Livrée";
	    }

	 @Override
	    public void suivant(Commande commande) {
	        commande.setEtat(new CommandePayee());
	        System.out.println("Commande payée.");
	    }

	    @Override
	    public void annuler(Commande commande) {
	        System.out.println("Commande livrée ne peut être annulée.");
	    }

	  
	  

}
