package manger;

import java.util.List;

import model.MenuItem;
import observer.ClientObserver;
import service.CommandeService;
import service.MenuService;
import service.ReservationService;

public class ClientInterface {
	 
	    public void afficherMenuClient() {
	        System.out.println("=== Interface Client ===");
	        System.out.println("1. Consulter le menu");
	        System.out.println("2. Réserver une table");
	        System.out.println("3. Passer une commande");
	        System.out.println("4. Quitter");
	    }
	    
	   
}
