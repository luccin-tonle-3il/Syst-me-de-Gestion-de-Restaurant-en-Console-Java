package manger;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws SQLException {
	
	        System.out.println("=== ************************ BIENVENUE AU RESTAURANT LES TRIPLES ***********************===");
	        System.out.println("1. Client");
	        System.out.println("2. Administrateur");
	        System.out.print("\nChoisissez votre rôle (1 ou 2): ");
	        
	        Scanner scanner = new Scanner(System.in);
	        int choice = scanner.nextInt();
	        scanner.nextLine(); // Consume newline
	        
	        switch (choice) {
	            case 1:
	                new ClientInterface().start();
	                break;
	            case 2:
	                new AdminInterface().start();
	                break;
	            default:
	                System.out.println("Option invalide. Au revoir!");
	        }
	        scanner.close();
		}
}