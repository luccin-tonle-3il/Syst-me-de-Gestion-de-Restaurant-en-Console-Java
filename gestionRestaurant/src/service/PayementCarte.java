package service;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import dao.DBConnection;

public class PayementCarte implements PayementStrategy {
	@Override
    public void payer(int commandeId, double montant) throws SQLException {
        System.out.println("💳 Paiement par carte de " + montant + " € effectué.");
        enregistrerPaiement(commandeId, montant, "Carte");
    }

    private void enregistrerPaiement(int commandeId, double montant, String type) throws SQLException {
        String sql = "INSERT INTO Paiement (commande_id, type, montant, date_paiement) VALUES (?, ?, ?, NOW())";
        PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql);
        stmt.setInt(1, commandeId);
        stmt.setString(2, type);
        stmt.setDouble(3, montant);
        stmt.executeUpdate();
    }
	public void pay() {
		// TODO Auto-generated method stub
		System.out.println("Paiement par carte");
	}

}
