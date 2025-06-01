package manger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import dao.CommandeDao;
import dao.DBConnection;
import dao.MenuDao;
import dao.ReservationDao;
import dao.TableDao;
import model.Boisson;
import model.Commande;
import model.CommandeItem;
import model.MenuItem;
import model.Plat;
import model.Reservation;
import model.StatutReservation;
import model.TableRestaurant;
import service.EtatCommande;
import service.NouvelleCommande;
import service.PayementCarte;
import service.PayementEspeces;
import service.PayementStrategy;
import service.PaymentContext;

public class ClientInterface extends ConsoleInterface {
    private CommandeDao commandeDao;
    private MenuDao menuDao;
    private ReservationDao reservationDao;
    private TableDao tableDao;
    private Scanner scanner;
    private SimpleDateFormat dateFormat;
    
    // État du processus client
    private boolean menuConsulte = false;
    private boolean commandeCreee = false;
    private boolean commandeValidee = false;
    private boolean commandePayee = false;
    private boolean commandePrete = false;
    private boolean commandeRecuperee = false;
    private Commande commandeActuelle = null;
    private int prochainIdCommande = 1;
    
    // Timer pour la notification de commande prête
    private Timer timerCommande;
    private boolean notificationAffichee = false;

    public ClientInterface() throws SQLException {
        this.commandeDao = new CommandeDao();
        this.menuDao = new MenuDao();
        this.reservationDao = new ReservationDao();
        this.tableDao = new TableDao();
        this.scanner = new Scanner(System.in);
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    // ===============================
    // INTERFACE UTILISATEUR PRINCIPALE - PROCESSUS STRICT
    // ===============================

    public void demarrerInterface() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🍔 BIENVENUE DANS NOTRE FAST-FOOD - SUIVEZ LE PROCESSUS 🍔");
        System.out.println("=".repeat(80));
        
        afficherEtatProcessus();
        
        System.out.println("\n=== ÉTAPES OBLIGATOIRES ===");
        
        if (!menuConsulte) {
            System.out.println("1. 📋 Consulter le menu (OBLIGATOIRE)");
            System.out.println("9. ❌ Quitter");
            System.out.print("Votre choix: ");
        } else if (!commandeCreee) {
            System.out.println("2. 🛒 Créer votre commande (OBLIGATOIRE)");
            System.out.println("1. 📋 Revoir le menu");
            System.out.println("9. ❌ Quitter");
            System.out.print("Votre choix: ");
        } else if (!commandeValidee) {
            System.out.println("3. ➕ Ajouter des articles à votre commande");
            System.out.println("4. ➖ Retirer un article de votre commande");
            System.out.println("5. 📄 Consulter votre commande");
            System.out.println("6. ✅ Valider votre commande (OBLIGATOIRE)");
            System.out.println("1. 📋 Revoir le menu");
            System.out.println("9. ❌ Annuler et quitter");
            System.out.print("Votre choix: ");
        } else if (!commandePayee) {
            System.out.println("7. 💳 Payer votre commande (OBLIGATOIRE)");
            System.out.println("5. 📄 Revoir votre commande");
            System.out.println("9. ❌ Annuler et quitter");
            System.out.print("Votre choix: ");
        } else if (!commandePrete) {
            System.out.println("⏳ Votre commande est en préparation...");
            System.out.println("5. 📄 Revoir votre commande");
            System.out.println("10. 🔔 Vérifier si la commande est prête");
            System.out.println("9. ❌ Quitter (commande sera perdue)");
            System.out.print("Votre choix: ");
        } else if (!commandeRecuperee) {
            System.out.println("8. 🎉 Récupérer votre commande (OBLIGATOIRE)");
            System.out.println("5. 📄 Revoir votre commande");
            System.out.print("Votre choix: ");
        } else {
            System.out.println("✅ Processus terminé! Merci de votre visite!");
            System.out.println("9. 👋 Quitter");
            System.out.print("Votre choix: ");
        }
    }
    
    private void afficherEtatProcessus() {
        System.out.println("\n📊 ÉTAT DE VOTRE PROCESSUS:");
        System.out.println("1. Consulter le menu: " + (menuConsulte ? "✅ Terminé" : "❌ À faire"));
        System.out.println("2. Créer commande: " + (commandeCreee ? "✅ Terminé" : "❌ À faire"));
        System.out.println("3. Valider commande: " + (commandeValidee ? "✅ Terminé" : "❌ À faire"));
        System.out.println("4. Payer commande: " + (commandePayee ? "✅ Terminé" : "❌ À faire"));
        
        if (commandePayee) {
            if (commandePrete) {
                System.out.println("5. Commande prête: 🎉 PRÊTE À RÉCUPÉRER!");
            } else {
                System.out.println("5. Commande en préparation: 👨‍🍳 En cours...");
            }
        }
        
        // Afficher notification si commande prête
        if (commandePrete && !commandeRecuperee) {
            System.out.println("\n🔔 ═══ NOTIFICATION IMPORTANTE ═══ 🔔");
            System.out.println("🍽️  VOTRE COMMANDE EST PRÊTE!");
            System.out.println("📍 Veuillez vous présenter au comptoir");
            System.out.println("═".repeat(40));
        }
    }
    
    public void processChoice(int choix) throws SQLException {
        switch (choix) {
            case 1:
                if (!menuConsulte || commandeCreee) {
                    consulterMenuStrict();
                } else {
                    System.out.println("❌ Vous devez d'abord créer une commande!");
                }
                break;
                
            case 2:
                if (menuConsulte && !commandeCreee) {
                    creerCommandeStrict();
                } else if (!menuConsulte) {
                    System.out.println("❌ Consultez d'abord le menu!");
                } else {
                    System.out.println("❌ Vous avez déjà une commande en cours!");
                }
                break;
                
            case 3:
                if (commandeCreee && !commandeValidee) {
                    ajouterItemStrict();
                } else {
                    System.out.println("❌ Action non disponible à cette étape!");
                }
                break;
                
            case 4:
                if (commandeCreee && !commandeValidee) {
                    supprimerItemStrict();
                } else {
                    System.out.println("❌ Action non disponible à cette étape!");
                }
                break;
                
            case 5:
                if (commandeCreee) {
                    afficherCommandeStrict();
                } else {
                    System.out.println("❌ Aucune commande à afficher!");
                }
                break;
                
            case 6:
                if (commandeCreee && !commandeValidee) {
                    validerCommandeStrict();
                } else {
                    System.out.println("❌ Action non disponible à cette étape!");
                }
                break;
                
            case 7:
                if (commandeValidee && !commandePayee) {
                    payerCommandeStrict();
                } else {
                    System.out.println("❌ Validez d'abord votre commande!");
                }
                break;
                
            case 8:
                if (commandePrete && !commandeRecuperee) {
                    recupererCommande();
                } else if (commandeRecuperee) {
                    terminerProcessus();
                } else {
                    System.out.println("❌ Votre commande n'est pas encore prête!");
                }
                break;
                
            case 9:
                quitterAvecConfirmation();
                break;
                
            case 10:
                if (commandePayee && !commandePrete) {
                    verifierStatutCommande();
                } else {
                    System.out.println("❌ Action non disponible à cette étape!");
                }
                break;
                
            default:
                printError("Option invalide pour cette étape du processus");
        }
    }

    

	// ===============================
    // ÉTAPE 1: CONSULTATION DU MENU
    // ===============================
    
    private void consulterMenuStrict() throws SQLException {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📋 CONSULTATION DU MENU");
        System.out.println("=".repeat(50));
        
        // Afficher les plats
        List<Plat> plats = menuDao.getAllPlats();
        System.out.println("\n🍽️ === PLATS DISPONIBLES ===");
        for (Plat p : plats) {
            System.out.println(p);
        }
        
        // Afficher les boissons
        List<Boisson> boissons = menuDao.getAllBoissons();
        System.out.println("\n🥤 === BOISSONS DISPONIBLES ===");
        for (Boisson b : boissons) {
            System.out.println(b);
        }
        
        menuConsulte = true;
        System.out.println("\n✅ Menu consulté! Vous pouvez maintenant créer votre commande.");
        
        System.out.println("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    // ===============================
    // ÉTAPE 2: CRÉATION DE COMMANDE
    // ===============================
    
    private void creerCommandeStrict() {
        try {
            // Sélection de table
            List<TableRestaurant> tablesDisponibles = tableDao.getTablesDisponibles();
            if (tablesDisponibles.isEmpty()) {
                System.out.println("❌ Aucune table disponible actuellement.");
                return;
            }
            
            System.out.println("\n🪑 Tables disponibles:");
            for (TableRestaurant table : tablesDisponibles) {
                System.out.println(table);
            }
            
            System.out.print("Choisissez le numéro de votre table: ");
            int tableId = Integer.parseInt(scanner.nextLine());
            
            // Créer la commande
            commandeActuelle = new Commande(prochainIdCommande++);
            commandeActuelle.setTableId(tableId);
            commandeActuelle.setDatecom(new Date());
            commandeActuelle.setEtat(new NouvelleCommande());
            
            commandeDao.ajouterCommande(commandeActuelle);
            commandeCreee = true;
            
            System.out.println("\n✅ Commande créée avec succès!");
            System.out.println("📋 Commande ID: " + commandeActuelle.getCommandeId());
            System.out.println("🪑 Table: " + tableId);
            System.out.println("\nVous pouvez maintenant ajouter des articles à votre commande.");
            
        } catch (SQLException | NumberFormatException e) {
            System.out.println("❌ Erreur lors de la création: " + e.getMessage());
        }
    }

    // ===============================
    // ÉTAPE 3: GESTION DES ARTICLES
    // ===============================
    
    private void ajouterItemStrict() {
        try {
            System.out.println("\n➕ AJOUTER UN ARTICLE");
            
            // Afficher le menu simplifié
            List<MenuItem> menuItems = menuDao.getAllItems();
            System.out.println("\n=== MENU RAPIDE ===");
            for (MenuItem item : menuItems) {
                System.out.printf("%-3d - %-25s %.2f€%n", item.getId(), item.getName(), item.getPrice());
            }
            
            System.out.print("\nID de l'article à ajouter: ");
            int itemId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Quantité: ");
            int quantite = Integer.parseInt(scanner.nextLine());
            
            MenuItem menuItem = menuItems.stream()
                .filter(i -> i.getId() == itemId)
                .findFirst()
                .orElse(null);
            
            if (menuItem == null) {
                System.out.println("❌ Article non trouvé!");
                return;
            }
            
            commandeDao.ajouterItemCommande(commandeActuelle.getCommandeId(), menuItem, quantite);
            System.out.println("✅ " + quantite + "x " + menuItem.getName() + " ajouté(s) à votre commande!");
            
        } catch (SQLException | NumberFormatException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }
    
    private void supprimerItemStrict() {
        try {
            afficherCommandeStrict();
            
            System.out.print("\nID de l'article à supprimer: ");
            int itemId = Integer.parseInt(scanner.nextLine());
            
            commandeDao.supprimerItemCommande(commandeActuelle.getCommandeId(), itemId);
            System.out.println("✅ Article supprimé de votre commande!");
            
        } catch (SQLException | NumberFormatException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }
    
    private void afficherCommandeStrict() {
        try {
            Commande commande = commandeDao.getCommandeById(commandeActuelle.getCommandeId());
            if (commande == null) {
                System.out.println("❌ Erreur lors de la récupération de la commande.");
                return;
            }
            
            System.out.println("\n" + "=".repeat(50));
            System.out.println("📄 VOTRE COMMANDE ACTUELLE");
            System.out.println("=".repeat(50));
            System.out.println("🆔 Commande: " + commande.getCommandeId());
            System.out.println("🪑 Table: " + commande.getTableId());
            System.out.println("📅 Date: " + commande.getDatecom());
            System.out.println("📊 État: " + commande.getEtat().getEtat());
            
            System.out.println("\n🍽️ Articles commandés:");
            double total = 0;
            
            if (commande.getItems().isEmpty()) {
                System.out.println("   (Aucun article pour le moment)");
            } else {
                for (CommandeItem item : commande.getItems()) {
                    double sousTotal = item.getItem().getPrice() * item.getQuantite();
                    System.out.printf("   - %s x%d = %.2f€%n", 
                        item.getItem().getName(), item.getQuantite(), sousTotal);
                    total += sousTotal;
                }
            }
            
            System.out.println("   " + "-".repeat(30));
            System.out.printf("   💰 TOTAL: %.2f€%n", total);
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    // ===============================
    // ÉTAPE 4: VALIDATION DE COMMANDE
    // ===============================
    
    private void validerCommandeStrict() {
        try {
            Commande commande = commandeDao.getCommandeById(commandeActuelle.getCommandeId());
            
            if (commande.getItems().isEmpty()) {
                System.out.println("❌ Impossible de valider une commande vide!");
                System.out.println("   Ajoutez au moins un article avant de valider.");
                return;
            }
            
            afficherCommandeStrict();
            
            System.out.println("\n⚠️  ATTENTION: Une fois validée, vous ne pourrez plus modifier votre commande!");
            System.out.print("Confirmer la validation? (oui/non): ");
            String confirmation = scanner.nextLine().toLowerCase();
            
            if (confirmation.equals("oui") || confirmation.equals("o")) {
                commandeValidee = true;
                System.out.println("\n✅ Commande validée avec succès!");
                System.out.println("👉 Procédez maintenant au paiement.");
            } else {
                System.out.println("❌ Validation annulée. Vous pouvez encore modifier votre commande.");
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    // ===============================
    // ÉTAPE 5: PAIEMENT
    // ===============================
    
    private void payerCommandeStrict() {
        try {
            afficherCommandeStrict();
            
            double total = calculerTotalCommande();
            
            System.out.println("\n💳 === PAIEMENT ===");
            System.out.printf("💰 Montant à payer: %.2f€%n", total);
            
            System.out.println("\nMéthodes de paiement disponibles:");
            System.out.println("1. 💳 Carte bancaire");
            System.out.println("2. 💵 Espèces");
            System.out.print("Votre choix: ");
            
            int choixPaiement = Integer.parseInt(scanner.nextLine());
            
            PaymentContext context = new PaymentContext();
            
            switch (choixPaiement) {
                case 1:
                    context.setStrategy(new PayementCarte());
                    System.out.println("💳 Paiement par carte en cours...");
                    break;
                case 2:
                    context.setStrategy(new PayementEspeces());
                    System.out.println("💵 Paiement en espèces...");
                    break;
                default:
                    System.out.println("❌ Méthode de paiement invalide!");
                    return;
            }
            
            // Simuler le paiement
            context.executerPaiement(commandeActuelle.getCommandeId(), total);
            commandePayee = true;
            
            System.out.println("\n🎉 PAIEMENT RÉUSSI!");
            System.out.printf("💰 Montant payé: %.2f€%n", total);
            System.out.println("🧾 Reçu généré.");
            System.out.println("\n👨‍🍳 Votre commande est en préparation!");
            
        } catch (SQLException | NumberFormatException e) {
            System.out.println("❌ Erreur lors du paiement: " + e.getMessage());
        }
    }
    
    private double calculerTotalCommande() throws SQLException {
        Commande commande = commandeDao.getCommandeById(commandeActuelle.getCommandeId());
        double total = 0;
        
        for (CommandeItem item : commande.getItems()) {
            total += item.getItem().getPrice() * item.getQuantite();
        }
        
        return total;
    }

    // ===============================
    // ÉTAPE 6: FINALISATION
    // ===============================
    
    private void terminerProcessus() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🎉 COMMANDE TERMINÉE - MERCI DE VOTRE VISITE! 🎉");
        System.out.println("=".repeat(60));
        
        afficherCommandeStrict();
        
        System.out.println("\n📋 Récapitulatif de votre expérience:");
        System.out.println("✅ Menu consulté");
        System.out.println("✅ Commande créée");
        System.out.println("✅ Commande validée");
        System.out.println("✅ Paiement effectué");
        System.out.println("✅ Processus terminé");
        
        System.out.println("\n👋 Au revoir et à bientôt dans notre fast-food!");
        System.out.println("🌟 N'hésitez pas à nous recommander!");
        
        close();
    }
    
    private void quitterAvecConfirmation() {
        if (commandeCreee && !commandePayee) {
            System.out.println("\n⚠️  ATTENTION!");
            System.out.println("Vous avez une commande en cours qui ne sera pas sauvegardée.");
            System.out.print("Êtes-vous sûr de vouloir quitter? (oui/non): ");
            
            String confirmation = scanner.nextLine().toLowerCase();
            if (!confirmation.equals("oui") && !confirmation.equals("o")) {
                System.out.println("👍 Parfait! Continuons votre commande.");
                return;
            }
        }
        
        System.out.println("\n👋 Merci de votre visite!");
        System.out.println("💡 N'hésitez pas à revenir pour suivre notre processus complet!");
        close();
    }
    
    /**
     * Vérifie le statut de la commande en cours et gère les notifications
     * Cette méthode est appelée quand le client veut savoir si sa commande est prête
     */
    private void verifierStatutCommande() {
        try {
            if (commandeActuelle == null) {
                System.out.println("❌ Aucune commande en cours!");
                return;
            }
            
            System.out.println("\n🔍 === VÉRIFICATION DU STATUT DE COMMANDE ===");
            System.out.println("🆔 Commande ID: " + commandeActuelle.getCommandeId());
            System.out.println("⏳ Vérification en cours...");
            
            // Simuler une vérification avec une petite pause
            Thread.sleep(1000);
            
            // Récupérer la commande mise à jour depuis la base de données
            Commande commandeMiseAJour = commandeDao.getCommandeById(commandeActuelle.getCommandeId());
            
            if (commandeMiseAJour == null) {
                System.out.println("❌ Erreur: Commande introuvable!");
                return;
            }
            
            // Vérifier l'état de la commande
            EtatCommande etatActuel = commandeMiseAJour.getEtat();
            String statutCommande = etatActuel.getEtat();
            
            System.out.println("\n📊 === STATUT ACTUEL ===");
            System.out.println("📋 État: " + statutCommande);
            
            // Simuler la logique de préparation (pour la démonstration)
            // Dans un vrai système, ceci viendrait de la base de données ou d'un service externe
            if (!commandePrete && shouldCommandeBeReady()) {
                commandePrete = true;
                
                // Démarrer le timer de notification si pas déjà fait
                if (timerCommande == null) {
                    demarrerTimerNotification();
                }
            }
            
            if (commandePrete) {
                System.out.println("🎉 === BONNE NOUVELLE! ===");
                System.out.println("✅ Votre commande est PRÊTE!");
                System.out.println("📍 Veuillez vous présenter au comptoir");
                System.out.println("🎯 Choisissez l'option 8 pour récupérer votre commande");
                
                // Afficher une notification visuelle
                afficherNotificationCommandePrete();
                
            } else {
                System.out.println("⏳ === STATUT EN COURS ===");
                System.out.println("👨‍🍳 Votre commande est encore en préparation");
                System.out.println("🕐 Temps estimé restant: " + calculerTempsEstime() + " minutes");
                System.out.println("💡 Vous pouvez revérifier dans quelques minutes");
            }
            
            // Afficher les détails de la commande
            System.out.println("\n📄 === RAPPEL DE VOTRE COMMANDE ===");
            afficherResumeMiniCommande();
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la vérification: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("❌ Vérification interrompue");
        }
    }

    /**
     * Détermine si la commande devrait être prête (simulation)
     * Dans un vrai système, ceci serait géré par le système de cuisine
     */
    private boolean shouldCommandeBeReady() {
        // Simulation: commande prête après 3 vérifications ou de manière aléatoire
        // Dans un vrai système, ceci dépendrait de l'état réel de la cuisine
        return Math.random() > 0.6; // 40% de chance d'être prête à chaque vérification
    }

    /**
     * Calcule le temps estimé restant pour la préparation
     */
    private int calculerTempsEstime() {
        if (commandeActuelle == null) return 0;
        
        try {
            Commande commande = commandeDao.getCommandeById(commandeActuelle.getCommandeId());
            int nombreItems = commande.getItems().size();
            
            // Estimation basée sur le nombre d'articles (2-5 minutes par article)
            int tempsBase = nombreItems * 3;
            int tempsAleatoire = (int)(Math.random() * 8) + 2; // 2-10 minutes
            
            return Math.max(1, tempsBase + tempsAleatoire);
            
        } catch (SQLException e) {
            return 5; // Temps par défaut
        }
    }

    /**
     * Affiche une notification visuelle que la commande est prête
     */
    private void afficherNotificationCommandePrete() {
        if (!notificationAffichee) {
            System.out.println("\n" + "🔔".repeat(20));
            System.out.println("🎉 🍽️  VOTRE COMMANDE EST PRÊTE! 🍽️  🎉");
            System.out.println("📍 PRÉSENTEZ-VOUS AU COMPTOIR");
            System.out.println("🔔".repeat(20));
            notificationAffichee = true;
        }
    }

    /**
     * Affiche un résumé condensé de la commande
     */
    private void afficherResumeMiniCommande() {
        try {
            Commande commande = commandeDao.getCommandeById(commandeActuelle.getCommandeId());
            if (commande == null) return;
            
            System.out.println("🆔 Commande: " + commande.getCommandeId());
            System.out.println("🪑 Table: " + commande.getTableId());
            System.out.println("📦 Nombre d'articles: " + commande.getItems().size());
            
            double total = 0;
            for (CommandeItem item : commande.getItems()) {
                total += item.getItem().getPrice() * item.getQuantite();
            }
            System.out.printf("💰 Total payé: %.2f€%n", total);
            
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'affichage du résumé");
        }
    }

    /**
     * Démarre un timer pour les notifications périodiques
     */
    private void demarrerTimerNotification() {
        timerCommande = new Timer();
        timerCommande.schedule(new TimerTask() {
            @Override
            public void run() {
                if (commandePrete && !commandeRecuperee) {
                    System.out.println("\n🔔 RAPPEL: Votre commande est prête!");
                    System.out.println("📍 N'oubliez pas de la récupérer au comptoir");
                }
            }
        }, 30000, 60000); // Premier rappel après 30s, puis toutes les minutes
    }

    /**
     * Méthode pour récupérer la commande (à appeler depuis processChoice case 8)
     */
    private void recupererCommande() {
        if (!commandePrete) {
            System.out.println("❌ Votre commande n'est pas encore prête!");
            System.out.println("💡 Utilisez l'option 10 pour vérifier le statut");
            return;
        }
        
        System.out.println("\n🎉 === RÉCUPÉRATION DE COMMANDE ===");
        System.out.println("✅ Votre commande est prête à être récupérée!");
        
        // Afficher les détails finaux
        afficherResumeMiniCommande();
        
        System.out.println("\n📋 Vérification des articles...");
        try {
            Thread.sleep(1500); // Simulation de la vérification
            
            System.out.println("✅ Tous les articles sont présents");
            System.out.println("🍽️ Bon appétit!");
            
            commandeRecuperee = true;
            
            // Arrêter le timer de notification
            if (timerCommande != null) {
                timerCommande.cancel();
                timerCommande = null;
            }
            
            System.out.println("\n🌟 Merci d'avoir choisi notre fast-food!");
            System.out.println("💯 N'hésitez pas à nous laisser un avis!");
            
        } catch (InterruptedException e) {
            System.out.println("❌ Interruption lors de la récupération");
        }
    }

    // ===============================
    // MÉTHODES UTILITAIRES
    // ===============================
    
    private int lireChoixNumerique(int min, int max) {
        while (true) {
            try {
                int choix = Integer.parseInt(scanner.nextLine());
                if (choix >= min && choix <= max) {
                    return choix;
                } else {
                    System.out.print("⚠️  Entrez un nombre entre " + min + " et " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("⚠️  Entrez un nombre valide: ");
            }
        }
    }

    @Override
    public void start() throws SQLException {
        System.out.println("🚀 Démarrage du système Fast-Food...");
        
        while (running) {
            demarrerInterface();
            
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                processChoice(choice);
                
                // Pause entre les actions
                if (running) {
                    System.out.println("\nAppuyez sur Entrée pour continuer...");
                    scanner.nextLine();
                }
                
            } catch (NumberFormatException e) {
                System.out.println("❌ Veuillez entrer un nombre valide!");
                System.out.println("Appuyez sur Entrée pour continuer...");
                scanner.nextLine();
            }
        }
    }
}