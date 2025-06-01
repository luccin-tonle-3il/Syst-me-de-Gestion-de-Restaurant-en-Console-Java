package service;

import model.Commande;


public class NouvelleCommande extends EtatCommande {

	 @Override
	    public void suivant(Commande commande) {
	        commande.setEtat(new CommandeEnCours());
	        System.out.println("Commande en cours.");
	    }

	    @Override
	    public void annuler(Commande commande) {
	        System.out.println("Commande annulée.");
	    }

	    @Override
	    public String getEtat() {
	        return "Nouvelle";
	    }

	    @Override
	    public void operationPaye(Commande commande) {
	        System.out.println("Impossible. Commande pas encore livrée.");
	    }

	    @Override
	    public void operationNouvelle(Commande commande) {
	        System.out.println("Déjà à l'état Nouvelle.");
	    }

	    @Override
	    public void operationEnCours(Commande commande) {
	        suivant(commande);
	    }

	    @Override
	    public void operationLivree(Commande commande) {
	        System.out.println("Pas possible. Commande pas prête.");
	    }

	    @Override
	    public void operationPret(Commande commande) {
	        System.out.println("Pas possible. Commande pas en cours.");
	    }

	    @Override
	    public void doAction(Commande commande) {
	        System.out.println("Nouvelle commande créée.");
	    }
	

}
