package manger;

import java.sql.SQLException;
import java.util.Scanner;

import dao.MenuDao;
import service.MenuService;
import service.ReservationService;
import service.TableService;

public class AdminInterface {
	int a;
	public MenuDao menu;
	Scanner sc = new Scanner(System.in);

	public void afficherMenuAdmin() {
		System.out.println("=== Interface Admin ===");
		System.out.println("1. Gérer le menu");
		System.out.println("2. Gérer les tables");
		System.out.println("3. Gérer les commandes");
		System.out.println("4. Gérer les stocks");
		System.out.println("5. Voir rapports de vente");
		System.out.println("6. Quitter");

	}
	public void choix(int a) throws SQLException {
		a= sc.nextInt();
		switch (a) {
		case 1:

			System.out.println(menu.lister());
			break;

		case 2:

			System.out.println("menu");
			break;

		case 3:

			System.out.println("Option 3");
			break;
		case 4:

			System.out.println("Option 3");
			break;

		default:
			System.out.println("Option invalide. Veuillez réessayer.");
			break;
		}
	}
	public void start() {
        boolean running;
		while (running) {
            displayMenu();
            System.out.print("\nChoisissez une option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            processChoice(choice);
        }
    }

}
