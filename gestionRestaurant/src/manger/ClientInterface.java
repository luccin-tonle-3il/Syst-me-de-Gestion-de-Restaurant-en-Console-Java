package manger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import dao.CommandeDao;
import dao.DBConnection;
import dao.MenuDao;
import dao.ReservationDao;
import dao.TableDao;
import model.Boisson;
import model.Commande;
import model.MenuItem;
import model.Plat;
import model.Reservation;
import observer.ClientObserver;
import service.PayementCarte;
import service.PayementEspeces;
import service.PayementStrategy;
import service.PaymentContext;



public class ClientInterface extends ConsoleInterface {
	private ClientObserver observer;
	private CommandeDao commandeDao;
	private MenuDao menuDao;
	private ReservationDao reservationDao;
	private TableDao tableDao;
	private Scanner scanner;
	private SimpleDateFormat dateFormat;
	private List<Reservation> reservations ;


	public ClientInterface() throws SQLException {
		this.commandeDao = new CommandeDao();
		this.menuDao = new MenuDao();
		this.reservationDao = new ReservationDao();
		this.tableDao = new TableDao();
		this.scanner = new Scanner(System.in);
		this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		this.reservations = new ArrayList<>();
	}

	public void setObserver(ClientObserver observer) {
		this.observer = observer;
	}

	// ===============================
	// INTERFACE UTILISATEUR PRINCIPALE
	// ===============================

	

	public void demarrerInterface() {
		System.out.println("===*********************** BIENVENUE PASSER UN BON MOMENT ********************************===");
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
	
	public void processChoice(int choix) throws SQLException {
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
	

	private void gererConsultationMenuUI() throws SQLException {
	    boolean retour = true;
	    while (retour) {
	        System.out.println("\n=== CONSULTATION DU MENU ===");
	        System.out.println("1. Voir les plats");
	        System.out.println("2. Voir les boissons");
	        System.out.println("3. Retour au menu principal");
	        System.out.print("Votre choix (1-3): ");

	        int choix = lireChoixNumerique(1, 3); // méthode perso supposée existante

	        switch (choix) {
	            case 1:
	                List<Plat> plats = menuDao.getAllPlats();
	                System.out.println("\n--- 🍽️ Liste des Plats ---");
	                for (Plat p : plats) {
	                    System.out.println(p);
	                }
	                break;

	            case 2:
	                List<Boisson> boissons = menuDao.getAllBoissons();
	                System.out.println("\n--- 🥤 Liste des Boissons ---");
	                for (Boisson b : boissons) {
	                    System.out.println(b);
	                }
	                break;

	            case 3:
	                retour = false;
	                break;
	        }
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
	
	/*private void effectuerPaiement() throws SQLException {
    System.out.println("Entrez l’ID de la commande à payer : ");
    int commandeId = scanner.nextInt();
    scanner.nextLine();

    double montant = calculerMontantCommande(commandeId); // méthode à implémenter

    System.out.println("Méthodes de paiement disponibles :");
    System.out.println("1. Carte");
    System.out.println("2. Espèces");
    System.out.print("Votre choix : ");
    int choix = scanner.nextInt();

    PaymentContext context = new PaymentContext();

    switch (choix) {
        case 1:
            context.setStrategy(new PayementCarte());
            break;
        case 2:
            context.setStrategy(new PayementEspeces());
            break;
        default:
            System.out.println("Méthode inconnue.");
            return;
    }


    context.executerPaiement(commandeId, montant);
	FacturePdfGenerator.genererFacture(commandeId); // ✅ Générer le PDF

}


	private double calculerMontantCommande(int commandeId) throws SQLException {
	    String sql = """
	        SELECT 
	            SUM(COALESCE(p.prix, 0) * ci.quantite) + 
	            SUM(COALESCE(b.prix, 0) * ci.quantite) AS total
	        FROM CommandeItem ci
	        LEFT JOIN Plat p ON ci.plat_id = p.id
	        LEFT JOIN Boisson b ON ci.boisson_id = b.id
	        WHERE ci.commande_id = ?
	    """;

	    PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql);
	    stmt.setInt(1, commandeId);
	    ResultSet rs = stmt.executeQuery();

	    if (rs.next()) {
	        return rs.getDouble("total");
	    }
	    return 0;
	}*/
	
//---------------ValiderCommanderUI----------------------
	private void supprimerPlatCommandeUI() {
	    try {
	        System.out.print("ID de la commande : ");
	        int commandeId = scanner.nextInt();

	        System.out.print("ID du plat/boisson à supprimer : ");
	        int itemId = scanner.nextInt();

	        commandeDao.supprimerItemCommande(commandeId, itemId);
	        System.out.println("Item supprimé de la commande.");
	    } catch (SQLException e) {
	        System.out.println("Erreur lors de la suppression : " + e.getMessage());
	    }
	}

//------------------ValiderCommanderUI----------
	private void validerCommandeUI() {
	    try {
	        System.out.print("ID de la commande à valider : ");
	        int commandeId = scanner.nextInt();

	        commandeDao.validerCommande(commandeId);
	        System.out.println("Commande validée.");
	    } catch (SQLException e) {
	        System.out.println("Erreur lors de la validation : " + e.getMessage());
	    }
	}

	
	private void calculerTotalCommandeUI() {
	    try {
	        System.out.print("ID de la commande : ");
	        int commandeId = scanner.nextInt();

	        Commande commande = commandeDao.getCommandeById(commandeId);
	        if (commande != null) {
	            double total = 0;
	            for (MenuItem item : commande.getItems()) {
	                total += item.getPrice();
	            }
	            System.out.println("Total de la commande : " + total + " €");
	        } else {
	            System.out.println("Commande non trouvée.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Erreur lors du calcul : " + e.getMessage());
	    }
	}



	
//*************************Reservation*******************

	
	

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

		/*List<Table> tables = consulterTablesDisponibles(dateHeure, nombrePersonnes);

		if (tables.isEmpty()) {
		    System.out.println("Aucune table disponible pour ces critères.");
		} else {
		    System.out.println("\n=== TABLES DISPONIBLES ===");
		    for (Table table : tables) {
		        System.out.println("Table " + table.getNumero() + " - Capacité: " + 
		            table.getCapacite() + " - Statut: " + (table.isEstOccupee() ? "Occupée" : "Disponible"));
		    }
		}*/

	}

	

	private void effectuerPaiementUI() {
	    System.out.print("Entrez l'ID de la commande à payer: ");
	    int commandeId = lireChoixNumerique(1, Integer.MAX_VALUE);

	    System.out.println("\n=== MODE DE PAIEMENT ===");
	    System.out.println("1. Carte bancaire");
	    System.out.println("2. Espèces");
	    System.out.print("Votre choix (1-2): ");

	    int choixPaiement = lireChoixNumerique(1, 2);

	    switch (choixPaiement) {
	        case 1:
	            System.out.print("Numéro de carte (16 chiffres): ");
	            String numeroCarte = scanner.nextLine().trim();

	            // Validation simple du numéro de carte (16 chiffres)
	            if (!numeroCarte.matches("\\d{16}")) {
	                System.out.println("Numéro de carte invalide. Paiement annulé.");
	                return;  // Sortie prématurée si invalide
	            }

	            strategie = new CreditCarte();
	            break;

	        case 2:
	            strategie = new CashPayement();
	            break;

	        default:
	            System.out.println("Mode de paiement non reconnu.");
	            return;
	    }

	  /*  boolean success = payerCommande(commandeId, strategie);
	    if (success) {
	        System.out.println("Paiement effectué avec succès !");
	    } else {
	        System.out.println("Erreur lors du paiement.");
	    }*/
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
		if (items == null || items.isEmpty()) {
			System.out.println("Aucun élément disponible.");
			return;
		}

		System.out.println("\n=== MENU ===");
		for (MenuItem item : items) {
			System.out.println("ID: " + item.getId() + " | " + item.getName() + 
					" - " + item.getPrice() + "€");
			/*if (item.getDescription() != null && !item.getDescription().isEmpty()) {
				System.out.println("   Description: " + item.getDescription());
			}*/
			System.out.println();
		}
	}

	


	private void afficherDetailsMenuItem(MenuItem item) {
		System.out.println("\n=== DÉTAILS DU PLAT ===");
		System.out.println("ID: " + item.getId());
		System.out.println("Nom: " + item.getName());
		System.out.println("Prix: " + item.getPrice() + "€");
	}

	private void afficherDetailsCommande(Commande commande) {
		System.out.println("\n=== DÉTAILS DE LA COMMANDE ===");
		System.out.println("ID: " + commande.getCommandeId());
		System.out.println("Table: " + commande.getTableId());
		System.out.println("Statut: " + commande.getEtat().getClass().getSimpleName());
		System.out.println("Date création: " + commande.getDatecom());
		System.out.println("Total: " + calculerTotal(commande.getItems()) + "€");

		if (!commande.getItems().isEmpty()) {
			System.out.println("\nArticles commandés:");
			
			// Map<MenuItem, Integer> items attendu, mais ici c'est List<MenuItem>
			// Donc on va afficher tous les items sans les quantités
			for (MenuItem item : commande.getItems()) {
				System.out.println("- " + item.getName() + " (" + item.getPrice() + "€)");
			}
			}
		}

	private String calculerTotal(List<MenuItem> items) {
		// TODO Auto-generated method stub
		return null;
	}

	private void afficherDetailsReservation(Reservation reservation) {
		System.out.println("\n=== DÉTAILS DE LA RÉSERVATION ===");
		System.out.println("ID: " + reservation.getId());
		System.out.println("Nom: " + reservation.getNomClient());
		System.out.println("Date et heure: " + reservation.getDateHeure());

		// Vérifie que la table n'est pas null avant d'y accéder
		if (reservation.getTable() != null) {
		    System.out.println("Table: " + reservation.getTable().getId());
		    System.out.println("Statut: " + reservation.getTable().isEstOccupee());
		    
		}   
		   
		}




		@Override
	    public void start() throws SQLException {
	        while (running) {
	        	demarrerInterface();
	            System.out.print("\nChoisissez une option: ");
	            int choice = scanner.nextInt();
	            scanner.nextLine();
	            processChoice(choice);
	        }
	    }



}
