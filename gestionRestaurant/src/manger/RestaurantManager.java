package manger;

import java.util.ArrayList;
import java.util.List;

import observer.AdminObserver;
import observer.CommandeObserver;
import observer.ObserverA;

public class RestaurantManager {


	// Singleton instance
    private static RestaurantManager instance;
    private List<CommandeObserver> observers;

    // Constructeur privé (singleton)
    private RestaurantManager() {
        observers = new ArrayList<>();
    }

    // Accès au singleton
    public static RestaurantManager getInstance() {
        if (instance == null) {
            instance = new RestaurantManager();
        }
        return instance;
    }

    // Ajouter un observateur
    public void ajouterObserver(CommandeObserver observer) {
        observers.add(observer);
    }

    // Retirer un observateur
    public void retirerObserver(CommandeObserver observer) {
        observers.remove(observer);
    }

    // Notifier tous les observateurs
    public void notifierObservers(String message) {
        for (CommandeObserver obs : observers) {
            obs.update(message);
        }
    }

    // Exemple d'action possible
    public void afficherMenuConsole() {
        // Action dans l'application
        System.out.println("Menu affiché");
    }

    // Exemple de méthode qui déclenche une alerte
    public void alerterStockBas() {
        notifierObservers("Stock insuffisant !");
    }

    public void alerterPlatPret() {
        notifierObservers("Votre plat est prêt !");
    }

	public boolean ajouterPlatACommande(int commandeId, int platId, int quantite) {
		// TODO Auto-generated method stub
		return false;
	}
}
