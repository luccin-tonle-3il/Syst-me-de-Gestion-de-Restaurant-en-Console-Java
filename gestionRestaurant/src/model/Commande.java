package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import service.CommandeState;
import service.NouvelleCommande;

public class Commande {
	private List<MenuItem>items;
	private int commandeId;
    private int tableId;
    private CommandeState etat;
	public Commande(List<MenuItem> items) {
		super();
		this.items = items;
		this.tableId = tableId;
		this.etat = new NouvelleCommande();
	}
	
	
	
	public List<MenuItem> getItems() {
		return items;
	}


	
	
    
    public CommandeState getEtat() {
		return etat;
	}



	public void setEtat(CommandeState etat) {
		this.etat = etat;
	}



	public void ajouterItem(MenuItem item) {
    	items.add(item);
    }
	
	public void afficherDetails() {
        System.out.println("Commande :");
        for (MenuItem item : items) {
            item.afficherDetails();
        }
        System.out.println("État de la commande : " + etat.getClass().getSimpleName());
    }

}
