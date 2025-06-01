package manger;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import dao.MenuDao;
import dao.ReservationDao;
import dao.TableDao;
import model.Boisson;
import model.EtatTable;
import model.Plat;
import model.Reservation;
import model.StatutReservation;
import model.TableRestaurant;

public class AdminInterface extends ConsoleInterface {
    private final Scanner scanner;
    private final MenuDao menuDao;
    private final TableDao tableDao;
    private boolean running = true;
    private final ReservationDao reservationDao;

    // ✅-------------------------------- Constructeur initialisant les DAO correctement
    public AdminInterface() {
        super();
        this.menuDao = new MenuDao();
        this.tableDao = new TableDao();
        this.scanner = new Scanner(System.in);
        this.reservationDao = new ReservationDao();
    }
    
    // 🔁----------------- Méthode principale de démarrage
    public void start() {
        while (running) {
        	demarrerInterface();
            System.out.print("Choisissez une option: ");
            try {
                int choix = Integer.parseInt(scanner.nextLine());
                processChoice(choix);
            } catch (NumberFormatException | SQLException e) {
                System.out.println("❌ Erreur: " + e.getMessage());
            }
        }
        scanner.close();
    }
    
    // 📌 --------------------------Affichage du menu principal
    public void demarrerInterface() {
        System.out.println("\n=== ************************** BIENVENUE CHEZ VOUS MONSIEUR ***********************************===");
        System.out.println("1. Gérer le menu");
        System.out.println("2. Gérer les tables");
        System.out.println("3. Gérer les commandes");
        System.out.println("4. Gerer les reservation");
        System.out.println("5. Gérer les stocks");
        System.out.println("6. Voir rapports de vente");
        System.out.println("7. Quitter");
    }
    
    // 🔀 ---------------------------Redirection selon le choix
    public void processChoice(int choix) throws SQLException {
        switch (choix) {
            case 1 -> gererMenuUI();
            case 2 -> gererTableUI();
            case 3 -> System.out.println("🛠 Gestion des commandes (à implémenter)");
            case 4 -> gererReservation();
            case 5 -> System.out.println("📊 Rapport des ventes (à implémenter)");
            case 6 -> System.out.println("📊 Rapport des ventes (à implémenter)");
            case 7 -> {
                System.out.println("👋 Déconnexion...");
                running = false;
            }
            default -> System.out.println("❌ Option invalide.");
        }
    }

   

   

    // ====================== 🥘 GESTION DU MENU ======================

    private void gererMenuUI() throws SQLException {
        boolean retour = true;
        while (retour) {
            System.out.println("\n=== GESTION DU MENU ===");
            System.out.println("1. Ajouter un plat");
            System.out.println("2. Supprimer un plat");
            System.out.println("3. Ajouter une boisson");
            System.out.println("4. Supprimer une boisson");
            System.out.println("5. Voir tous les plats");
            System.out.println("6. Voir toutes les boissons");
            System.out.println("7. Retour");

            int choix = scanner.nextInt();
            scanner.nextLine(); // flush

            switch (choix) {
                case 1 -> ajouterPlat();
                case 2 -> supprimerPlat();
                case 3 -> ajouterBoisson();
                case 4 -> supprimerBoisson();
                case 5 -> afficherPlats();
                case 6 -> afficherBoissons();
                case 7 -> retour = false;
                default -> System.out.println("❌ Option invalide.");
            }
        }
    }

    private void ajouterPlat() throws SQLException {
        System.out.print("Nom du plat : ");
        String nom = scanner.nextLine();
        System.out.print("Prix du plat : ");
        double prix = scanner.nextDouble();
        scanner.nextLine();
        menuDao.ajouterPlat(nom, prix);
        System.out.println("✅ Plat ajouté.");
    }

    private void supprimerPlat() throws SQLException {
        System.out.print("ID du plat à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        menuDao.supprimerPlat(id);
        System.out.println("✅ Plat supprimé.");
    }

    private void ajouterBoisson() throws SQLException {
        System.out.print("Nom de la boisson : ");
        String nom = scanner.nextLine();
        System.out.print("Prix : ");
        double prix = scanner.nextDouble();
        scanner.nextLine();
        menuDao.ajouterBoisson(nom, prix);
        System.out.println("✅ Boisson ajoutée.");
    }

    private void supprimerBoisson() throws SQLException {
        System.out.print("ID de la boisson à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        menuDao.supprimerBoisson(id);
        System.out.println("✅ Boisson supprimée.");
    }

    private void afficherPlats() throws SQLException {
        List<Plat> plats = menuDao.getAllPlats();
        plats.forEach(System.out::println);
    }

    private void afficherBoissons() throws SQLException {
        List<Boisson> boissons = menuDao.getAllBoissons();
        boissons.forEach(System.out::println);
    }

    // ====================== 🍽️ GESTION DES TABLES ======================

    private void gererTableUI() throws SQLException {
        boolean retour = true;

        while (retour) {
            System.out.println("\n=== GESTION DES TABLES ===");
            System.out.println("1. Voir toutes les tables");
            System.out.println("2. Voir les tables disponibles");
            System.out.println("3. Ajouter une table");
            System.out.println("4. Changer état d'une table");
            System.out.println("5. Retour");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1 -> afficherToutesLesTables();
                case 2 -> afficherTablesDisponibles();
                case 3 -> ajouterNouvelleTable();
                case 4 -> changerEtatTable();
                case 5 -> retour = false;
                default -> System.out.println("❌ Option invalide.");
            }
        }
    }

    private void afficherToutesLesTables() {
        try {
            List<TableRestaurant> tables = tableDao.getAllTables();
            tables.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("❌ Erreur d'affichage : " + e.getMessage());
        }
    }

    private void afficherTablesDisponibles() {
        try {
            List<TableRestaurant> tables = tableDao.getTablesDisponibles();
            System.out.println("--- Tables Disponibles ---");
            tables.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("❌ Erreur : " + e.getMessage());
        }
    }

    private void ajouterNouvelleTable() {
        try {
            System.out.print("Numéro de table : ");
            int numero = scanner.nextInt();
            System.out.print("Capacité : ");
            int capacite = scanner.nextInt();
            scanner.nextLine();

            TableRestaurant table = new TableRestaurant(0, numero, capacite, EtatTable.DISPONIBLE);
            tableDao.ajouterTable(table);
            System.out.println("✅ Table ajoutée !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur d'ajout : " + e.getMessage());
        }
    }

    private void changerEtatTable() {
        try {
            System.out.print("ID de la table : ");
            int id = scanner.nextInt();

            System.out.println("Choisissez le nouvel état : ");
            System.out.println("1. DISPONIBLE");
            System.out.println("2. OCCUPEE");
            System.out.println("3. RESERVEE");

            int choix = scanner.nextInt();
            scanner.nextLine();

            EtatTable etat = switch (choix) {
                case 1 -> EtatTable.DISPONIBLE;
                case 2 -> EtatTable.OCCUPEE;
                case 3 -> EtatTable.RESERVEE;
                default -> {
                    System.out.println("❌ Choix invalide.");
                    yield null;
                }
            };

            if (etat != null) {
                tableDao.updateEtat(id, etat);
                System.out.println("✅ État mis à jour.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur de mise à jour : " + e.getMessage());
        }
    }
    
    
    
    
    //--------------------------------------RESERVATION-------------------------------------------------
    
    private void gererReservation() throws SQLException {
        boolean retour = true;
        while (retour) {
            System.out.println("\n=== GESTION DES RÉSERVATIONS ===");
            System.out.println("1. Ajouter une réservation");
            System.out.println("2. Modifier une réservation");
            System.out.println("3. Supprimer une réservation");
            System.out.println("4. Voir toutes les réservations");
            System.out.println("5. Retour");

            int choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1 -> ajouterReservationUI();
                case 2 -> modifierReservationUI();
                case 3 -> supprimerReservationUI();
                case 4 -> afficherReservations();
                case 5 -> retour = false;
                default -> System.out.println("❌ Option invalide.");
            }
        }
    }

    private void ajouterReservationUI() {
        try {
            System.out.print("ID de la table : ");
            int tableId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Nom du client : ");
            String nomClient = scanner.nextLine();

            System.out.print("Date de la réservation (YYYY-MM-DD) : ");
            LocalDate date = LocalDate.parse(scanner.nextLine());

            System.out.print("Heure de la réservation (HH:MM) : ");
            LocalTime heure = LocalTime.parse(scanner.nextLine());

            Reservation res = new Reservation(tableId, nomClient, date, heure, StatutReservation.CONFIRMEE);
            reservationDao.ajouterReservation(res);
            System.out.println("✅ Réservation ajoutée avec ID: " + res.getId());

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }
    
    private void modifierReservationUI() {
        try {
            System.out.print("ID de la réservation à modifier : ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Reservation res = reservationDao.getReservationById(id);
            if (res == null) {
                System.out.println("❌ Réservation non trouvée.");
                return;
            }

            System.out.println("Laissez vide pour ne pas modifier.");

            System.out.print("Nouveau ID de table (" + res.getTableId() + ") : ");
            String input = scanner.nextLine();
            if (!input.isBlank()) res.setTableId(Integer.parseInt(input));

            System.out.print("Nouveau nom client (" + res.getNomClient() + ") : ");
            input = scanner.nextLine();
            if (!input.isBlank()) res.setNomClient(input);

            System.out.print("Nouvelle date (YYYY-MM-DD) (" + res.getDateReservation() + ") : ");
            input = scanner.nextLine();
            if (!input.isBlank()) res.setDateReservation(LocalDate.parse(input));

            System.out.print("Nouvelle heure (HH:MM) (" + res.getHeureReservation() + ") : ");
            input = scanner.nextLine();
            if (!input.isBlank()) res.setHeureReservation(LocalTime.parse(input));

            System.out.println("Nouveau statut : ");
            System.out.println(" 1. Confirmée");
            System.out.println(" 2. Annulée");
            System.out.println(" 3. Terminée");
            System.out.print("Laissez vide pour garder le statut actuel (" + res.getStatut().getLabel() + ") : ");
            input = scanner.nextLine();
            if (!input.isBlank()) {
                int statutChoix = Integer.parseInt(input);
                res.setStatut(StatutReservation.fromInt(statutChoix));
            }

            reservationDao.updateReservation(res);
            System.out.println("✅ Réservation mise à jour.");

        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la modification : " + e.getMessage());
        }
    }

    private void supprimerReservationUI() {
        try {
            System.out.print("ID de la réservation à supprimer : ");
            int id = scanner.nextInt();
            scanner.nextLine();

            reservationDao.supprimerReservation(id);
            System.out.println("✅ Réservation supprimée.");
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    private void afficherReservations() {
        try {
            List<Reservation> reservations = reservationDao.getAllReservations();
            if (reservations.isEmpty()) {
                System.out.println("Aucune réservation trouvée.");
            } else {
                System.out.println("--- Liste des réservations ---");
                reservations.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération : " + e.getMessage());
        }
    }
}
