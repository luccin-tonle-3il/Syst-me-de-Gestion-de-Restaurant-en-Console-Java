package manger;

import service.MenuService;
import service.ReservationService;
import service.TableService;

public class AdminInterface {
	

	    public void afficherMenuAdmin() {
	        System.out.println("=== Interface Admin ===");
	        System.out.println("1. Gérer le menu");
	        System.out.println("2. Gérer les tables");
	        System.out.println("3. Gérer les commandes");
	        System.out.println("4. Gérer les stocks");
	        System.out.println("5. Voir rapports de vente");
	        System.out.println("6. Quitter");
	        
	    }
}
