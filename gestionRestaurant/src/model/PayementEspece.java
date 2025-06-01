package model;

import java.sql.SQLException;

public class PayementEspece implements PayementStrategy {
    @Override
    public void payer(int commandeId, double montant) throws SQLException {
        System.out.println("Paiement en espèces effectué pour la commande " + commandeId + ": " + montant + "€");
        // Ici on pourrait ajouter la logique de paiement en espèces
    }
}
