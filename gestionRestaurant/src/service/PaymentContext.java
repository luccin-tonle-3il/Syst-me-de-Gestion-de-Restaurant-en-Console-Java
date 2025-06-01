package service;

import java.sql.SQLException;

public class PaymentContext {
	private PayementStrategy strategy;

    public void setStrategy(PayementStrategy strategy) {
        this.strategy = strategy;
    }

    public void executerPaiement(int commandeId, double montant) throws SQLException {
        strategy.payer(commandeId, montant);
    }
}
