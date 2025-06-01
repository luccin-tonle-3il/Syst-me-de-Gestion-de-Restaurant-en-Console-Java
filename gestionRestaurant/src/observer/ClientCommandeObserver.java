package observer;

import model.Commande;
import model.MenuItem;
import service.CommandeLivree;
import service.CommandePrete;

public class ClientCommandeObserver implements CommandeObserver {
    private Commande commande;

    public ClientCommandeObserver(Commande commande) {
        this.commande = commande;
    }

    @Override
    public void update(String message) {
        // Afficher la notification à l'interface du client
        StringBuilder notification = new StringBuilder("Notification: ");
        notification.append(message);
        
        // Ajouter des détails selon l'état de la commande
        if (commande.getEtat() != null) {
            notification.append(" (État actuel: ").append(commande.getEtat().getEtat()).append(")");
        }
        
        System.out.println(notification.toString());
        
        // Afficher les détails de la commande si elle est prête ou livrée
        if (commande.getEtat() instanceof CommandePrete || commande.getEtat() instanceof CommandeLivree) {
            afficherDetailsCommande();
        }
    }

    private void afficherDetailsCommande() {
        System.out.println("\nDétails de la commande: ");
        System.out.println("ID: " + commande.getCommandeId());
        System.out.println("Table: " + commande.getTableId());
        System.out.println("Date: " + commande.getDatecom());
        System.out.println("\nItems commandés:");
        commande.getItems().forEach(item -> {
            
        });
        System.out.println("\nTotal: " + calculerTotal() + "€");
    }

	private String calculerTotal() {
		// TODO Auto-generated method stub
		return null;
	}

 
}
