package service;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import dao.DBConnection;

public class PayementEspece implements PayementStrategy {
    @Override
    public void payer(int commandeId, double montant) throws SQLException {
        System.out.println("💰 Paiement en espèces de " + montant + " € effectué.");
        enregistrerPaiement(commandeId, montant, "Espèces");
    }

    private void enregistrerPaiement(int commandeId, double montant, String type) throws SQLException {
        String sql = "INSERT INTO Paiement (commande_id, type, montant, date_paiement) VALUES (?, ?, ?, NOW())";
        PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql);
        stmt.setInt(1, commandeId);
        stmt.setString(2, type);
        stmt.setDouble(3, montant);
        stmt.executeUpdate();
    }
}
