package service;

import model.Commande;

public class CommandeEnCours extends EtatCommande {

	@Override
    public void suivant(Commande commande) {
        commande.setEtat(new CommandePrete());
        System.out.println("Commande prête.");
    }

    @Override
    public void annuler(Commande commande) {
        System.out.println("Commande annulée.");
    }

    @Override
    public String getEtat() {
        return "En cours";
    }

    @Override
    public void operationPaye(Commande commande) {
        System.out.println("Pas encore livrée.");
    }

    @Override
    public void operationNouvelle(Commande commande) {
        System.out.println("Impossible.");
    }

    @Override
    public void operationEnCours(Commande commande) {
        System.out.println("Déjà en cours.");
    }

    @Override
    public void operationLivree(Commande commande) {
        System.out.println("Commande pas encore prête.");
    }

    @Override
    public void operationPret(Commande commande) {
        suivant(commande);
    }

    @Override
    public void doAction(Commande commande) {
        System.out.println("Commande en préparation.");
    }
}
