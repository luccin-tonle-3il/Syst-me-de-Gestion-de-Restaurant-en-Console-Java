package model;

import java.sql.SQLException;

public interface PayementStrategy {
    void payer(int commandeId, double montant) throws SQLException;
}
