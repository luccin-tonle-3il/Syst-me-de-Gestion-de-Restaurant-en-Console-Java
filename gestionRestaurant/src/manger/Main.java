package manger;

public class Main {
	public static void main(String[] args) {
		public static void main(String[] args) {
	        System.out.println("=== Système de Gestion de Restaurant ===");
	        System.out.println("1. Interface Client");
	        System.out.println("2. Interface Administrateur");
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