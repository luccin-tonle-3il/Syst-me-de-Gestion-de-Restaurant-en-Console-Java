package model;

import java.sql.SQLException;

public class PayementCarte implements PayementStrategy {
    @Override
    public void payer(int commandeId, double montant) throws SQLException {
        System.out.println("Paiement par carte effectué pour la commande " + commandeId + ": " + montant + "€");
        // Ici on pourrait ajouter la logique de paiement par carte
    }
}
