package service;

import model.Commande;
import model.MenuItem;


public abstract class EtatCommande {
	
	public abstract void suivant(Commande commande);
    public abstract void annuler(Commande commande);
    public abstract String getEtat();

    public abstract void operationPaye(Commande commande);
    public abstract void operationNouvelle(Commande commande);
    public abstract void operationEnCours(Commande commande);
    public abstract void operationLivree(Commande commande);
    public abstract void operationPret(Commande commande);

    public abstract void doAction(Commande commande);
    }
