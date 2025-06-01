package model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import service.CommandePrete;
import service.EtatCommande;
import service.NouvelleCommande;
import service.PayementStrategy;

public class Commande {
	private int commandeId;
    private int tableId;
    private Date datecom;


    private List<MenuItem> items = new ArrayList<>();

    // Patron State
    private EtatCommande etat;

    // Patron Strategy
    private PayementStrategy strategy;

    // === Constructeur ===
    public Commande(int commandeId) {
        this.commandeId = commandeId;
        this.etat = new NouvelleCommande(); // état initial
    }

    // === Getters / Setters ===
    public int getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(int commandeId) {
        this.commandeId = commandeId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public Date getDatecom() {
        return datecom;
    }

    public void setDatecom(Date datecom) {
        this.datecom = datecom;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public EtatCommande getEtat() {
        return etat;
    }

    public void setEtat(EtatCommande commandePrete) {
        this.etat = commandePrete;
    }

    public void setPayementStrategy(PayementStrategy strategy) {
        this.strategy = strategy;
    }

    // === Méthodes de gestion des items ===
    public void ajouterItem(MenuItem item) {
        items.add(item);
    }

    public void afficherDetails() {
        System.out.println("Commande #" + commandeId);
        for (MenuItem item : items) {
            item.afficherDetails();
        }
        System.out.println("État : " + etat.getEtat());
    }

    // === Gestion de l'état (State) ===
    public void suivantEtat() {
        etat.suivant(this);
    }

    public void annulerCommande() {
        etat.annuler(this);
    }

    public void operationPaye() {
        etat.operationPaye(this);
    }

    public void operationNouvelle() {
        etat.operationNouvelle(this);
    }

    public void operationEnCours() {
        etat.operationEnCours(this);
    }

    public void operationPret() {
        etat.operationPret(this);
    }

    public void operationLivree() {
        etat.operationLivree(this);
    }

    public void doAction() {
        etat.doAction(this);
    }

    // === Paiement (Strategy) ===
    public void payerCommande(double montant) throws SQLException {
        if (strategy != null) {
            strategy.payer(commandeId, montant);
        } else {
            System.out.println("Aucune stratégie de paiement définie !");
        }
    }
}
