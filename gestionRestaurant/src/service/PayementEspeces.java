package service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import dao.DBConnection;

public class PayementEspeces implements PayementStrategy {
    
    @Override
    public void payer(int commandeId, double montant) throws SQLException {
        System.out.println("💵 Paiement en espèces de " + montant + " € effectué.");
        System.out.println("💰 Veuillez donner le montant exact ou avoir la monnaie.");
        
        // Simulation de la transaction espèces
        simulerPaiementEspeces(montant);
        
        // Enregistrer le paiement
        enregistrerPaiement(commandeId, montant, "Especes");
        
        System.out.println("✅ Paiement en espèces validé!");
    }

    private void simulerPaiementEspeces(double montant) {
        System.out.println("💵 Traitement du paiement espèces...");
        System.out.printf("💰 Montant reçu: %.2f€%n", montant);
        System.out.println("🧾 Vérification des billets et pièces...");
        
        // Simulation d'une petite pause pour le traitement
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Ignorer l'interruption
        }
        
        System.out.println("✅ Espèces vérifiées et acceptées");
    }
    
    /**
     * Enregistre le paiement dans la base de données
     * Structure de table: id_paiement, id_commande, montant, date_paiement, mode_paiement
     */
    private void enregistrerPaiement(int commandeId, double montant, String type) throws SQLException {
        // Requête SQL corrigée selon la vraie structure de la table
        String sql = "INSERT INTO Paiement (id_commande, montant, date_paiement, mode_paiement) VALUES (?, ?, NOW(), ?)";
        
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, commandeId);        // id_commande
            stmt.setDouble(2, montant);        // montant
            stmt.setString(3, type);           // mode_paiement
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) 
           {
                System.out.println("💾 Paiement enregistré dans la base de données");
                System.out.println("🆔 Commande ID: " + commandeId);
                System.out.printf("💰 Montant: %.2f€%n", montant);
                System.out.println("💳 Mode: " + type);
            } else {
                throw new SQLException("Échec de l'enregistrement du paiement");
            }
        }
    }
}