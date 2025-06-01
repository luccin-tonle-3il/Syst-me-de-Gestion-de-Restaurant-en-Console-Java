package service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import model.Commande;
import model.MenuItem;
import model.TableRestaurant;
import model.EtatTable;
import service.NouvelleCommande;
import service.PayementCarte;
import service.PayementEspece;
import service.PayementStrategy;
import service.CommandeUpdateThread;
import dao.CommandeDao;
import dao.TableDao;

public class CommandeService {
    private final CommandeDao commandeDao;
    private final TableDao tableDao;

    public CommandeService() {
        this.commandeDao = new CommandeDao();
        this.tableDao = new TableDao();
    }

    public void creerCommande(List<MenuItem> items, int tableId) throws SQLException {
        TableRestaurant table = tableDao.getTableById(tableId);
        if (table == null || table.getEtat() != EtatTable.DISPONIBLE) {
            throw new SQLException("Table invalide ou déjà occupée");
        }

        Commande commande = new Commande(genererIdCommande());
        commande.setTableId(tableId);
        commande.setDatecom(new Date());
        commande.setEtat(new NouvelleCommande());

        // Ajouter les items à la commande
        commande.getItems().addAll(items);

        commandeDao.ajouterCommande(commande);
        table.setEtat(EtatTable.OCCUPEE);
        tableDao.updateTable(table);
    }

    private int genererIdCommande() {
        // Implémentation simple pour générer un ID unique
        return (int) (Math.random() * 10000);
    }

    // === 8. Démarrer la mise à jour automatique de l'état ===
    public void demarrerMiseAJourEtat(Commande commande) {
        CommandeUpdateThread updateThread = new CommandeUpdateThread(commande);
        updateThread.start();
    }

    public void passerAuPaiement(Commande commande) throws SQLException {
        commande.getEtat().operationEnCours(commande);
        commande.getEtat().operationPret(commande);
        commande.getEtat().operationLivree(commande);
    }

    public void payerCommande(Commande commande, double montant, String methodePaiement) throws SQLException {
        // Vérifier si le montant est suffisant
        double total = calculerTotalCommande(commande);
        if (montant < total) {
            throw new SQLException("Montant insuffisant");
        }

        // Appliquer la stratégie de paiement
        PayementStrategy strategy;
        switch (methodePaiement.toLowerCase()) {
            case "carte":
                strategy = new PayementCarte();
                break;
            case "espece":
                strategy = new PayementEspece();
                break;
            default:
                throw new SQLException("Méthode de paiement non supportée");
        }

        commande.setStrategy(strategy);
        commande.payerCommande(montant);
        commande.getEtat().operationPaye(commande);
    }

    private double calculerTotalCommande(Commande commande) {
        double total = 0.0;
        for (MenuItem item : commande.getItems()) {
            total += item.getPrice();
        }
        return total;
    }
}
