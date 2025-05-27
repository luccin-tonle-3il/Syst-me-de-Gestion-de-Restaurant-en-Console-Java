package manger;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import dao.CommandeDao;
import dao.MenuDao;
import dao.ReservationDao;
import dao.TableDao;
import model.Commande;
import model.MenuItem;
import observer.ClientObserver;



public class ClientInterface extends ConsoleInterface {
	private ClientObserver observer;
	private CommandeDao commandeDao;
	private MenuDao menuDao;
	private ReservationDao reservationDao;
	private TableDao tableDao;
	private Scanner scanner;
	private SimpleDateFormat dateFormat;

	public ClientInterface() {
		this.commandeDao = new CommandeDao();
		this.menuDao = new MenuDao();
		this.reservationDao = new ReservationDao();
		this.tableDao = new TableDao();
		this.scanner = new Scanner(System.in);
		this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	}

	public void setObserver(ClientObserver observer) {
		this.observer = observer;
	}

	// ===============================
	// INTERFACE UTILISATEUR PRINCIPALE
	// ===============================

	

	public void demarrerInterface() {
		System.out.println("=== BIENVENUE AU RESTAURANT LES TRIPLES===");
		System.out.println("\n=== MENU PRINCIPAL ===");
		System.out.println("1. Consulter le menu");
		System.out.println("2. Gérer mes commandes");
		System.out.println("3. Gérer mes réservations");
		System.out.println("4. Consulter les tables disponibles");
		System.out.println("5. Effectuer un paiement");
		System.out.println("6. Aide");
		System.out.println("7. Quitter");
		System.out.print("Votre choix (1-7): ");
	}
	
	public void processChoice(int choix) {
			switch (choix) {
			case 1:
				gererConsultationMenuUI();
				break;
			case 2:
				gererCommandesUI();
				break;
			case 3:
				gererReservationsUI();
				break;
			case 4:
				consulterTablesUI();
				break;
			case 5:
				effectuerPaiementUI();
				break;
			case 6:
				afficherAide();
				break;
			case 7:
				System.out.println("Au revoir et merci de votre visite !");
				close();
				break;
			 default:
	                printError("Option invalide");
			}
		}
	

	private void gererConsultationMenuUI() {
		boolean retour = false;
		while (!retour) {
			System.out.println("\n=== CONSULTATION DU MENU ===");
			System.out.println("1. Voir tout le menu");
			System.out.println("2. Voir les plats");
			System.out.println("3. Voir les boissons");
			System.out.println("4. Rechercher un plat");
			System.out.println("5. Détails d'un plat");
			System.out.println("6. Retour au menu principal");
			System.out.print("Votre choix (1-6): ");

			int choix = lireChoixNumerique(1, 8);

			switch (choix) {
			case 1:
				afficherMenu(consulterMenu());
				break;
			case 2:
				afficherMenu(consulterMenuParCategorie("PLAT"));
				break;
			case 3:
				afficherMenu(consulterMenuParCategorie("BOISSON"));
				break;
			case 4:
				rechercherPlatUI();
				break;
			case 5:
				retour = true;
				break;
		
			
			}
		}
	}

	private void rechercherPlatUI() {
		System.out.print("Entrez le nom du plat à rechercher: ");
		String nom = scanner.nextLine();
		List<MenuItem> resultats = rechercherMenuItem(nom);

		if (resultats.isEmpty()) {
			System.out.println("Aucun plat trouvé avec ce nom.");
		} else {
			afficherMenu(resultats);
		}
	}

	

	private void gererCommandesUI() {
		boolean retour = false;
		while (!retour) {
			System.out.println("\n=== GESTION DES COMMANDES ===");
			System.out.println("1. Créer une nouvelle commande");
			System.out.println("2. Ajouter un plat à une commande");
			System.out.println("3. Supprimer un plat d'une commande");
			System.out.println("4. Valider une commande");
			System.out.println("5. Consulter une commande");
			System.out.println("6. Calculer le total d'une commande");
			System.out.println("7. Retour au menu principal");
			System.out.print("Votre choix (1-7): ");

			int choix = lireChoixNumerique(1, 7);

			switch (choix) {
			case 1:
				creerCommandeUI();
				break;
			case 2:
				ajouterPlatCommandeUI();
				break;
			case 3:
				supprimerPlatCommandeUI();
				break;
			case 4:
				validerCommandeUI();
				break;
			case 5:
				consulterCommandeUI();
				break;
			case 6:
				calculerTotalCommandeUI();
				break;
			case 7:
				retour = true;
				break;
			}
		}
	}

	private void creerCommandeUI() {
		System.out.print("Entrez le numéro de table: ");
		int tableId = lireChoixNumerique(1, Integer.MAX_VALUE);

		int commandeId = creerCommande(tableId);
		if (commandeId > 0) {
			System.out.println("Commande créée avec succès ! ID: " + commandeId);
		} else {
			System.out.println("Erreur lors de la création de la commande.");
		}
	}

	private int creerCommande(int tableId) {
		// TODO Auto-generated method stub
		return 0;
	}

	private void ajouterPlatCommandeUI() {
		System.out.print("Entrez l'ID de la commande: ");
		int commandeId = lireChoixNumerique(1, Integer.MAX_VALUE);

		System.out.print("Entrez l'ID du plat: ");
		int platId = lireChoixNumerique(1, Integer.MAX_VALUE);

		System.out.print("Entrez la quantité: ");
		int quantite = lireChoixNumerique(1, Integer.MAX_VALUE);

		boolean success = (commandeId, platId, quantite);
		if (success) {
			System.out.println("Plat ajouté à la commande avec succès !");
		} else {
			System.out.println("Erreur lors de l'ajout du plat.");
		}
	}

	private void supprimerPlatCommandeUI() {
		System.out.print("Entrez l'ID de la commande: ");
		int commandeId = lireChoixNumerique(1, Integer.MAX_VALUE);

		System.out.print("Entrez l'ID du plat à supprimer: ");
		int platId = lireChoixNumerique(1, Integer.MAX_VALUE);

		boolean success = supprimerItemDeCommande(commandeId, platId);
		if (success) {
			System.out.println("Plat supprimé de la commande avec succès !");
		} else {
			System.out.println("Erreur lors de la suppression du plat.");
		}
	}


	private void validerCommandeUI() {
		System.out.print("Entrez l'ID de la commande à valider: ");
		int commandeId = lireChoixNumerique(1, Integer.MAX_VALUE);

		boolean success = validerCommande(commandeId);
		if (success) {
			System.out.println("Commande validée avec succès !");
		} else {
			System.out.println("Erreur lors de la validation de la commande.");
		}
	}

	private void consulterCommandeUI() {
		System.out.print("Entrez l'ID de la commande: ");
		int commandeId = lireChoixNumerique(1, Integer.MAX_VALUE);

		Commande commande = consulterCommande(commandeId);
		if (commande != null) {
			afficherDetailsCommande(commande);
		} else {
			System.out.println("Commande non trouvée.");
		}
	}

	private void calculerTotalCommandeUI() {
		System.out.print("Entrez l'ID de la commande: ");
		int commandeId = lireChoixNumerique(1, Integer.MAX_VALUE);

		double total = calculerTotalCommande(commandeId);
		System.out.println("Total de la commande: " + total + " €");
	}

	private void gererReservationsUI() {
		boolean retour = false;
		while (!retour) {
			System.out.println("\n=== GESTION DES RÉSERVATIONS ===");
			System.out.println("1. Créer une réservation");
			System.out.println("2. Modifier une réservation");
			System.out.println("3. Annuler une réservation");
			System.out.println("4. Consulter une réservation");
			System.out.println("5. Retour au menu principal");
			System.out.print("Votre choix (1-5): ");

			int choix = lireChoixNumerique(1, 5);

			switch (choix) {
			case 1:
				creerReservationUI();
				break;
			case 2:
				modifierReservationUI();
				break;
			case 3:
				annulerReservationUI();
				break;
			case 4:
				consulterReservationUI();
				break;
			case 5:
				retour = true;
				break;
			}
		}
	}

	private void creerReservationUI() {
		System.out.print("Entrez votre nom: ");
		String nom = scanner.nextLine();

		System.out.print("Entrez votre téléphone: ");
		String telephone = scanner.nextLine();

		System.out.print("Entrez votre email: ");
		String email = scanner.nextLine();

		System.out.print("Nombre de personnes: ");
		int nombrePersonnes = lireChoixNumerique(1, Integer.MAX_VALUE);

		System.out.print("Date et heure (dd/MM/yyyy HH:mm): ");
		String dateStr = scanner.nextLine();
		Date dateHeure = null;

		try {
			dateHeure = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			System.out.println("Format de date invalide.");
			return;
		}

		System.out.print("Commentaires (optionnel): ");
		String commentaires = scanner.nextLine();

		int reservationId = creerReservation(nom, telephone, email, nombrePersonnes, dateHeure, commentaires);

		if (reservationId > 0) {
			System.out.println("Réservation créée avec succès ! ID: " + reservationId);
		} else {
			System.out.println("Erreur lors de la création de la réservation.");
		}
	}

	private void modifierReservationUI() {
		System.out.print("Entrez l'ID de la réservation: ");
		int reservationId = lireChoixNumerique(1, Integer.MAX_VALUE);

		System.out.print("Nouveau nom: ");
		String nom = scanner.nextLine();

		System.out.print("Nouveau téléphone: ");
		String telephone = scanner.nextLine();

		System.out.print("Nouvel email: ");
		String email = scanner.nextLine();

		System.out.print("Nouveau nombre de personnes: ");
		int nombrePersonnes = lireChoixNumerique(1, Integer.MAX_VALUE);

		System.out.print("Nouvelle date et heure (dd/MM/yyyy HH:mm): ");
		String dateStr = scanner.nextLine();
		Date dateHeure = null;

		try {
			dateHeure = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			System.out.println("Format de date invalide.");
			return;
		}

		System.out.print("Nouveaux commentaires: ");
		String commentaires = scanner.nextLine();

		boolean success = modifierReservation(reservationId, nom, telephone, email, nombrePersonnes, dateHeure, commentaires);

		if (success) {
			System.out.println("Réservation modifiée avec succès !");
		} else {
			System.out.println("Erreur lors de la modification de la réservation.");
		}
	}

	private void annulerReservationUI() {
		System.out.print("Entrez l'ID de la réservation à annuler: ");
		int reservationId = lireChoixNumerique(1, Integer.MAX_VALUE);

		boolean success = annulerReservation(reservationId);
		if (success) {
			System.out.println("Réservation annulée avec succès !");
		} else {
			System.out.println("Erreur lors de l'annulation de la réservation.");
		}
	}

	private void consulterReservationUI() {
		System.out.print("Entrez l'ID de la réservation: ");
		int reservationId = lireChoixNumerique(1, Integer.MAX_VALUE);

		Reservation reservation = consulterReservation(reservationId);
		if (reservation != null) {
			afficherDetailsReservation(reservation);
		} else {
			System.out.println("Réservation non trouvée.");
		}
	}

	private void consulterTablesUI() {
		System.out.print("Date et heure souhaitées (dd/MM/yyyy HH:mm): ");
		String dateStr = scanner.nextLine();
		Date dateHeure = null;

		try {
			dateHeure = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			System.out.println("Format de date invalide.");
			return;
		}

		System.out.print("Nombre de personnes: ");
		int nombrePersonnes = lireChoixNumerique(1, Integer.MAX_VALUE);

		List<Table> tables = consulterTablesDisponibles(dateHeure, nombrePersonnes);

		if (tables.isEmpty()) {
			System.out.println("Aucune table disponible pour ces critères.");
		} else {
			System.out.println("\n=== TABLES DISPONIBLES ===");
			for (Table table : tables) {
				System.out.println("Table " + table.getNumero() + " - Capacité: " + 
						table.getCapacite() + " - Emplacement: " + table.getEmplacement());
			}
		}
	}

	private void effectuerPaiementUI() {
		System.out.print("Entrez l'ID de la commande à payer: ");
		int commandeId = lireChoixNumerique(1, Integer.MAX_VALUE);

		System.out.println("\n=== MODE DE PAIEMENT ===");
		System.out.println("1. Carte bancaire");
		System.out.println("2. Espèces");
		System.out.print("Votre choix (1-2): ");

		int choixPaiement = lireChoixNumerique(1, 2);
		PayementStrategy strategie = null;

		switch (choixPaiement) {
		case 1:
			System.out.print("Numéro de carte (16 chiffres): ");
			String numeroCarte = scanner.nextLine();
			strategie = new PayementCarte(numeroCarte);
			break;
		case 2:
			strategie = new PayementEspeces();
			break;
		}

		boolean success = payerCommande(commandeId, strategie);
		if (success) {
			System.out.println("Paiement effectué avec succès !");
		} else {
			System.out.println("Erreur lors du paiement.");
		}
	}

	private void afficherAide() {
		System.out.println("\n=== AIDE ===");
		System.out.println("Navigation:");
		System.out.println("- Utilisez les chiffres pour naviguer dans les menus");
		System.out.println("- Suivez les instructions à l'écran");
		System.out.println("- Les dates doivent être au format dd/MM/yyyy HH:mm");
		System.out.println("\nEtapes pour commander:");
		System.out.println("1. Consultez le menu (option 1)");
		System.out.println("2. Créez une commande (option 2 > 1)");
		System.out.println("3. Ajoutez des plats (option 2 > 2)");
		System.out.println("4. Validez la commande (option 2 > 4)");
		System.out.println("5. Payez quand c'est prêt (option 5)");
	}

	// ===============================
	// MÉTHODES UTILITAIRES UI
	// ===============================

	private int lireChoixNumerique(int min, int max) {
		while (true) {
			try {
				int choix = Integer.parseInt(scanner.nextLine());
				if (choix >= min && choix <= max) {
					return choix;
				} else {
					System.out.print("Veuillez entrer un nombre entre " + min + " et " + max + ": ");
				}
			} catch (NumberFormatException e) {
				System.out.print("Veuillez entrer un nombre valide: ");
			}
		}
	}

	private void afficherMenu(List<MenuItem> items) {
		if (items.isEmpty()) {
			System.out.println("Aucun élément disponible.");
			return;
		}

		System.out.println("\n=== MENU ===");
		for (MenuItem item : items) {
			System.out.println("ID: " + item.getId() + " | " + item.getNom() + 
					" - " + item.getPrix() + "€");
			if (item.getDescription() != null && !item.getDescription().isEmpty()) {
				System.out.println("   " + item.getDescription());
			}
			System.out.println();
		}
	}

	private void afficherDetailsMenuItem(MenuItem item) {
		System.out.println("\n=== DÉTAILS DU PLAT ===");
		System.out.println("Nom: " + item.getNom());
		System.out.println("Prix: " + item.getPrix() + "€");
		System.out.println("Catégorie: " + item.getCategorie());
		System.out.println("Description: " + (item.getDescription() != null ? item.getDescription() : "N/A"));
		System.out.println("Disponible: " + (item.isDisponible() ? "Oui" : "Non"));
		System.out.println("Temps de préparation: " + item.getTempsPreparationMinutes() + " min");
	}

	private void afficherDetailsCommande(Commande commande) {
		System.out.println("\n=== DÉTAILS DE LA COMMANDE ===");
		System.out.println("ID: " + commande.getId());
		System.out.println("Table: " + commande.getTableId());
		System.out.println("Statut: " + commande.getStatut());
		System.out.println("Date création: " + commande.getDateCreation());
		System.out.println("Total: " + commande.getTotal() + "€");

		if (!commande.getItems().isEmpty()) {
			System.out.println("\nArticles commandés:");
			commande.getItems().forEach((item, quantite) -> {
				System.out.println("- " + item.getNom() + " x" + quantite + 
						" (" + (item.getPrix() * quantite) + "€)");
			});
		}
	}

	private void afficherDetailsReservation(Reservation reservation) {
		System.out.println("\n=== DÉTAILS DE LA RÉSERVATION ===");
		System.out.println("ID: " + reservation.getId());
		System.out.println("Nom: " + reservation.getClientNom());
		System.out.println("Téléphone: " + reservation.getClientTelephone());
		System.out.println("Email: " + reservation.getClientEmail());
		System.out.println("Nombre de personnes: " + reservation.getNombrePersonnes());
		System.out.println("Date et heure: " + dateFormat.format(reservation.getDateHeure()));
		System.out.println("Table: " + reservation.getTableId());
		System.out.println("Statut: " + reservation.getStatut());
		if (reservation.getCommentaires() != null && !reservation.getCommentaires().isEmpty()) {
			System.out.println("Commentaires: " + reservation.getCommentaires());
		}
	}

	// ===============================
	// MÉTHODES MÉTIER - CORRIGÉES
	// ===============================

	/**
	 * Consulter tous les éléments du menu disponibles
	 */
	public List<MenuItem> consulterMenu() {
		List<MenuItem> menu = menuDao.getAllMenuItems();
		return menu.stream()
				.filter(MenuItem::isDisponible)
				.toList();
	}

	/**
	 * Consulter le menu par catégorie (ENTREE, PLAT, DESSERT, BOISSON)
	 */
	public List<MenuItem> consulterMenuParCategorie(String categorie) {
		List<MenuItem> items = menuDao.getMenuItemsByCategorie(categorie);
		return items.stream()
				.filter(MenuItem::isDisponible)
				.toList();
	}

	/**
	 * Rechercher un élément du menu par nom
	 */
	public List<MenuItem> rechercherMenuItem(String nom) {
		return menuDao.searchMenuItemsByName(nom);


		@Override
	    public void start() {
	        while (running) {
	        	demarrerInterface();
	            System.out.print("\nChoisissez une option: ");
	            int choice = scanner.nextInt();
	            scanner.nextLine();
	            processChoice(choice);
	        }
	    }



}
