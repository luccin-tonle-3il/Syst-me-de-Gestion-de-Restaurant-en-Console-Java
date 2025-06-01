package observer;

import model.Commande;

public class ConsoleCommandeObserver implements CommandeObserver {
    private Commande commande;

    public ConsoleCommandeObserver(Commande commande) {
        this.commande = commande;
    }

    @Override
    public void update(String message) {
        // Afficher la notification dans la console
        System.out.println("\n=== Notification Commande ===");
        System.out.println("L'état de votre commande a changé");
        System.out.println("Commande ID: " + commande.getCommandeId());
        System.out.println("Nouvel état: " + message);
        System.out.println("=============================");
    }
}