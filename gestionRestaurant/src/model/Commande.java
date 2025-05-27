package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import service.CommandeState;
import service.NouvelleCommande;
import service.PayementStartegy;

public class Commande {
	private List<MenuItem>items;
	private int commandeId;
	private int tableId;
	private Date datecom;
	private CommandeState etat;
	protected PayementStartegy strategy;
	private MenuItem menu;
	
	
	
	public Date getDatecom() {
		return datecom;
	}


	public void seatDatecom(Date datecom) {
		this.datecom = datecom;
	}


	public int getTableId() {
		return tableId;
	}
	
	
	public int getCommandeId() {
		return commandeId;
	}


	public void setCommandeId(int commandeId) {
		this.commandeId = commandeId;
	}


	public void setTableId(int tableId) {
		this.tableId = tableId;
	}

	public void appliquerstrategy() {
		strategy.pay();
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

		

	public PayementStartegy getStrategy() {
		return strategy;
	}

	public void setStrategy(PayementStartegy strategy) {
		this.strategy = strategy;
	}

	public void afficherDetails() {
		System.out.println("Commande :");
		for (MenuItem item : items) {
			item.afficherDetails();
		}
		System.out.println("État de la commande : " + etat.getClass().getSimpleName());
	}
	
	public void operationPaye() {etat.operationPaye();}
	public  void operationNouvelle() {etat.operationNouvelle();}
	public  void operationEnCours() {etat.operationEnCours();}
	public  void operationLivree() {etat.operationLivree();}
	public  void operationPret() {etat.operationPret();}
	
	public void doAction() {etat.doAction();}

	
	

}
