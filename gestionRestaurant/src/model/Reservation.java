package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {

    private int id;
    private int tableId;
    private String nomClient;
    private LocalDate dateReservation;     // ✔️ Meilleur que java.util.Date
    private LocalTime heureReservation;    // ✔️ Pour gérer l'heure séparément
    private StatutReservation statut;                 // e.g. Confirmée, Annulée, Terminée

    // 📦 Constructeur complet
    public Reservation(int id, int tableId, String nomClient, LocalDate dateReservation, LocalTime heureReservation, StatutReservation statut) {
        this.id = id;
        this.tableId = tableId;
        this.nomClient = nomClient;
        this.dateReservation = dateReservation;
        this.heureReservation = heureReservation;
        this.statut = statut;
    }

    // ✅ Constructeur partiel si besoin
    public Reservation(int tableId, String nomClient, LocalDate dateReservation, LocalTime heureReservation, StatutReservation statut) {
        this(0, tableId, nomClient, dateReservation, heureReservation, statut);
    }

    public Reservation() {
		// TODO Auto-generated constructor stub
	}

	// 🔁 Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public String getNomClient() { return nomClient; }
    public void setNomClient(String nomClient) { this.nomClient = nomClient; }

    public LocalDate getDateReservation() { return dateReservation; }
    public void setDateReservation(LocalDate dateReservation) { this.dateReservation = dateReservation; }

    public LocalTime getHeureReservation() { return heureReservation; }
    public void setHeureReservation(LocalTime heureReservation) { this.heureReservation = heureReservation; }

    public StatutReservation getStatut() { return statut; }
    public void setStatut(StatutReservation statut) { this.statut = statut; }

    @Override
    public String toString() {
        return "Réservation #" + id +
               " | Table: " + tableId +
               " | Date: " + dateReservation +
               " à " + heureReservation +
               " | Client: " + nomClient +
               " | Statut: " + statut;
    }
}
