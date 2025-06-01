package service;

import model.Commande;

public class Livree extends EtatCommande {
    public Livree(Commande commande) {
        super(commande);
    }

    @Override
    public void operationPaye(Commande commande) {
        System.out.println("La commande est déjà livrée");
    }

    @Override
    public void operationNouvelle(Commande commande) {
        System.out.println("La commande est déjà livrée");
    }

    @Override
    public void operationEnCours(Commande commande) {
        System.out.println("La commande est déjà livrée");
    }

    @Override
    public void operationLivree(Commande commande) {
        System.out.println("La commande est déjà livrée");
    }

    @Override
    public void operationPret(Commande commande) {
        System.out.println("La commande est déjà livrée");
    }

    @Override
    public void doAction(Commande commande) {
        System.out.println("La commande est livrée");
    }

    @Override
    public String getEtat() {
        return "Livré";
    }

    @Override
    public void suivant(Commande commande) {
        System.out.println("La commande est déjà livrée");
    }

    @Override
    public void annuler(Commande commande) {
        System.out.println("La commande est déjà livrée");
    }
}
