package model;

import java.time.LocalDate;

public class Paiement {
	private int idPaiement;
    private int idCommande;
    private double montant;
    private LocalDate datePaiement;
    private String modePaiement;

    // Constructeur par défaut
    public Paiement() {}

    // Constructeur avec paramètres
    public Paiement(int idPaiement, int idCommande, double montant, LocalDate datePaiement, String modePaiement) {
        this.idPaiement = idPaiement;
        this.idCommande = idCommande;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.modePaiement = modePaiement;
    }

    // Constructeur sans ID (pour l'insertion en base)
    public Paiement(int idCommande, double montant, LocalDate datePaiement, String modePaiement) {
        this.idCommande = idCommande;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.modePaiement = modePaiement;
    }

    // ==================== GETTERS ====================
    public int getIdPaiement() {
        return idPaiement;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public double getMontant() {
        return montant;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    // ==================== SETTERS ====================
    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    // ==================== MÉTHODES UTILES ====================
    @Override
    public String toString() {
        return String.format("Paiement{ID=%d, Commande=%d, Montant=%.2f€, Date=%s, Mode=%s}",
                idPaiement, idCommande, montant, datePaiement, modePaiement);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Paiement paiement = (Paiement) obj;
        return idPaiement == paiement.idPaiement;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idPaiement);
    }
}

