package model;

import java.time.LocalDateTime;
import java.util.Date;

public class Reservation {
	private int id;
	private String dateHeure;
    private Table table;
    private String client;
    int i =table.getId();
   

	public Reservation(int id, String dateHeure, int i, String client) {
		super();
		this.id = id;
		this.dateHeure = dateHeure;
		this.i=i;
		this.client = client;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDateHeure() {
		return dateHeure;
	}

	public void setDateHeure(String dateHeure) {
		this.dateHeure = dateHeure;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}
    
	public void afficherDetails() {
        System.out.println("Réservation: " + id + " - Date et Heure: " + dateHeure);
        if (table != null) {
            table.afficherStatut();
        }
    }

	public String getNomClient() {
		// TODO Auto-generated method stub
		return client;
	}

	public String getDateReservation() {
		// TODO Auto-generated method stub
		return this.dateHeure;
	}
    
}
