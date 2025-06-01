package service;

import model.Commande;
import model.EtatCommande;
import model.MenuItem;

/**
 * 
 */
public abstract class CommandeState implements EtatCommande {

    protected Commande commade;
    private MenuItem menu;
    
    /**
     * @param commade
     */
    public CommandeState(Commande commade) {
        this.commade = commade;
    }
    
    public abstract void operationPaye();
    public abstract void operationNouvelle();
    public abstract void operationEnCours();
    public abstract void operationLivree();
    public abstract void operationPret();
    

    public abstract void doAction();
}
