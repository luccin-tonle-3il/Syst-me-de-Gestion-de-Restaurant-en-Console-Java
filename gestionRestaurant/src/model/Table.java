package model;

public class Table {
	private int id;
	private boolean estOccupee;
	private Commande commande;
	private int numero;
	private int capacite;

	public Table(int numero, String statut) {
		super();
		this.id = id;
		this.estOccupee = false;
		this.numero = numero;
		this.capacite = capacite;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isEstOccupee() {
		return estOccupee;
	}

	public void setEstOccupee(boolean estOccupee) {
		this.estOccupee = estOccupee;
	}

	public Commande getCommande() {
		return commande;
	}

	public void setCommande(Commande commande) {
		this.commande = commande;
	}

	  public void afficherStatut() {
	        System.out.println("Table " + id + " - Statut: " + (estOccupee ? "Occupée" : "Disponible"));
	    }

	public int getNumero() {
		// TODO Auto-generated method stub
		return this.numero;
	}

	public int getCapacite() {
		// TODO Auto-generated method stub
		return this.capacite;
	}



}
