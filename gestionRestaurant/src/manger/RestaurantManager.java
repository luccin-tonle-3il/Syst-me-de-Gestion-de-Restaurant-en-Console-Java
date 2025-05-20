package manger;

import java.util.ArrayList;
import java.util.List;

import observer.AdminObserver;
import observer.ClientObserver;
import observer.ObserverA;
import observer.ObserverC;

public class RestaurantManager {


	// Singleton instance
    private static RestaurantManager instance;

    // Liste des observateurs
    private List<ObserverA> observers;
    private List<ObserverC> observerclient;

    // Constructeur privé (singleton)
    private RestaurantManager() {
        observers = new ArrayList<>();
        observerclient = new ArrayList<>();
    }

    // Accès au singleton
    public static RestaurantManager getInstance() {
        if (instance == null) {
            instance = new RestaurantManager();
        }
        return instance;
    }

    // Ajouter un observateur
    public void ajouterObserver(AdminObserver observer) {
        observers.add(observer);
    }
    // Ajouter un observateurclient
    public void ajouterObserver(ClientObserver observer) {
        observerclient.add(observer);
    }

    // Retirer un observateurclient
    public void retirerObserver(ClientObserver observer) {
        observerclient.remove(observer);
    }

    // Notifier tous les observateurclients
    public void notifierObservers(String message) {
        for (ObserverC obs : observerclient) {
            String reponse = obs.notifier(message);
            System.out.println("Notification envoyée : " + reponse);
        }
    }
    
    // Notifier tous les observateursadmin
    public void notifierObserversA(String message) {
        for (ObserverA obs : observers) {
            String reponse = obs.notifier(message);
            System.out.println("Notification envoyée : " + reponse);
        }
    }

    // Exemple d'action possible
    public void afficherMenuConsole() {
        // Action dans l'application
        System.out.println("Menu affiché");
    }

    // Exemple de méthode qui déclenche une alerte
    public void alerterStockBas() {
        notifierObserversA("Stock insuffisant !");
    }

    public void alerterPlatPret() {
        notifierObservers("Votre plat est prêt !");
    }


	public boolean ajouterPlatACommande(int commandeId, int platId, int quantite) {
		// TODO Auto-generated method stub
		return false;
	}

}
