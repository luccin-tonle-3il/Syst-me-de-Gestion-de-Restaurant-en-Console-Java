package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Boisson;
import model.Commande;
import model.MenuItem;
import model.Plat;
import service.CommandeEnCours;
import service.CommandeLivree;
import service.CommandeState;
import service.NouvelleCommande;


public class CommandeDao {
	private Connection connection;
    private MenuDao menuDao;

    public CommandeDao() {
	try {
        connection = DBConnection.getConnection();
        menuDao = new MenuDao();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// ===============================
// MÉTHODES PRINCIPALES CRUD
// ===============================

/**
 * Sauvegarder une nouvelle commande
 */
    public Commande getCommandeById(int commandeId) {
        String query = "SELECT * FROM commandes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, commandeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Commande commande = new Commande();
                    commande.setCommandeId(rs.getInt("id"));
                    commande.setTableId(rs.getInt("table_id"));
                    commande.setDatecom(rs.getTimestamp("date_creation"));
                    commande.setDateValidation(rs.getTimestamp("date_validation"));
                    commande.setTotal(rs.getDouble("total"));
                    
                    // Charger les items de la commande
                    Map<MenuItem, Integer> items = getItemsForCommande(commandeId);
                    commande.setItems(items);
                    
                    // Définir l'état basé sur le statut string récupéré de la DB
                    String statutString = rs.getString("statut");
                    commande.setEtat(createEtatFromStatutString(statutString, commande));
                    
                    return commande;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la commande: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Mettre à jour une commande existante
     */
    public boolean updateCommande(Commande commande) {
        String query = "UPDATE commandes SET statut = ?, date_validation = ?, total = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, commande.getEtat().getClass().getSimpleName());
            stmt.setTimestamp(2, commande.getDateValidation());
            stmt.setDouble(3, commande.getTotal());
            stmt.setInt(4, commande.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la commande: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


/**
 * Récupérer une commande par son ID
 */
public Commande getCommandeById(int commandeId) {
    String query = "SELECT * FROM commandes WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, commandeId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Commande commande = new Commande();
                commande.setId(rs.getInt("id"));
                commande.setTableId(rs.getInt("table_id"));
                commande.setStatut(rs.getString("statut"));
                commande.setDateCreation(rs.getTimestamp("date_creation"));
                commande.setDateValidation(rs.getTimestamp("date_validation"));
                commande.setTotal(rs.getDouble("total"));
                
                // Charger les items de la commande
                Map<MenuItem, Integer> items = getItemsForCommande(commandeId);
                commande.setItems(items);
                
                // Définir l'état basé sur le statut
                commande.setEtat(createEtatFromStatut(commande.getStatut(), commande));
                
                return commande;
            }
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération de la commande: " + e.getMessage());
        e.printStackTrace();
    }
    return null;
}

/**
 * Mettre à jour une commande existante
 */

/**
 * Supprimer une commande
 */
public boolean deleteCommande(int commandeId) {
    try {
        connection.setAutoCommit(false);
        
        // Supprimer d'abord les items de la commande
        String deleteItemsQuery = "DELETE FROM commande_items WHERE commande_id = ?";
        try (PreparedStatement stmt1 = connection.prepareStatement(deleteItemsQuery)) {
            stmt1.setInt(1, commandeId);
            stmt1.executeUpdate();
        }
        
        // Ensuite supprimer la commande
        String deleteCommandeQuery = "DELETE FROM commandes WHERE id = ?";
        try (PreparedStatement stmt2 = connection.prepareStatement(deleteCommandeQuery)) {
            stmt2.setInt(1, commandeId);
            int affected = stmt2.executeUpdate();
            
            connection.commit();
            return affected > 0;
        }
    } catch (SQLException e) {
        try {
            connection.rollback();
        } catch (SQLException rollbackEx) {
            System.err.println("Erreur lors du rollback: " + rollbackEx.getMessage());
        }
        System.err.println("Erreur lors de la suppression de la commande: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return false;
}

// ===============================
// GESTION DES ITEMS DE COMMANDE
// ===============================

/**
 * Ajouter un item à une commande
 */
public boolean addItemToCommande(int commandeId, int menuItemId, int quantite, double prixUnitaire) {
    // Vérifier si l'item existe déjà dans la commande
    String checkQuery = "SELECT quantite FROM commande_items WHERE commande_id = ? AND menu_item_id = ?";
    try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
        checkStmt.setInt(1, commandeId);
        checkStmt.setInt(2, menuItemId);
        
        try (ResultSet rs = checkStmt.executeQuery()) {
            if (rs.next()) {
                // L'item existe déjà, mettre à jour la quantité
                int quantiteExistante = rs.getInt("quantite");
                return updateItemQuantiteInCommande(commandeId, menuItemId, quantiteExistante + quantite);
            } else {
                // Nouvel item, l'insérer
                String insertQuery = "INSERT INTO commande_items (commande_id, menu_item_id, quantite, prix_unitaire) VALUES (?, ?, ?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, commandeId);
                    insertStmt.setInt(2, menuItemId);
                    insertStmt.setInt(3, quantite);
                    insertStmt.setDouble(4, prixUnitaire);
                    return insertStmt.executeUpdate() > 0;
                }
            }
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de l'ajout d'item à la commande: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
}

/**
 * Supprimer un item d'une commande
 */
public boolean removeItemFromCommande(int commandeId, int menuItemId) {
    String query = "DELETE FROM commande_items WHERE commande_id = ? AND menu_item_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, commandeId);
        stmt.setInt(2, menuItemId);
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Erreur lors de la suppression d'item de la commande: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
}

/**
 * Mettre à jour la quantité d'un item dans une commande
 */
public boolean updateItemQuantiteInCommande(int commandeId, int menuItemId, int nouvelleQuantite) {
    if (nouvelleQuantite <= 0) {
        return removeItemFromCommande(commandeId, menuItemId);
    }
    
    String query = "UPDATE commande_items SET quantite = ? WHERE commande_id = ? AND menu_item_id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, nouvelleQuantite);
        stmt.setInt(2, commandeId);
        stmt.setInt(3, menuItemId);
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Erreur lors de la mise à jour de la quantité: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
}

/**
 * Récupérer tous les items d'une commande
 */
private Map<MenuItem, Integer> getItemsForCommande(int commandeId) {
    Map<MenuItem, Integer> items = new HashMap<>();
    String query = "SELECT ci.menu_item_id, ci.quantite, ci.prix_unitaire " +
                  "FROM commande_items ci WHERE ci.commande_id = ?";
    
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, commandeId);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int menuItemId = rs.getInt("menu_item_id");
                int quantite = rs.getInt("quantite");
                
                MenuItem menuItem = menuDao.getMenuItemById(menuItemId);
                if (menuItem != null) {
                    items.put(menuItem, quantite);
                }
            }
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération des items de la commande: " + e.getMessage());
        e.printStackTrace();
    }
    return items;
}

// ===============================
// MÉTHODES DE LISTE ET RECHERCHE
// ===============================

/**
 * Lister toutes les commandes
 */
public List<Commande> getAllCommandes() {
    List<Commande> commandes = new ArrayList<>();
    String query = "SELECT id FROM commandes ORDER BY date_creation DESC";
    
    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        
        while (rs.next()) {
            int commandeId = rs.getInt("id");
            Commande commande = getCommandeById(commandeId);
            if (commande != null) {
                commandes.add(commande);
            }
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération de toutes les commandes: " + e.getMessage());
        e.printStackTrace();
    }
    return commandes;
}

/**
 * Lister les commandes par statut
 */
public List<Commande> getCommandesByStatut(String statut) {
    List<Commande> commandes = new ArrayList<>();
    String query = "SELECT id FROM commandes WHERE statut = ? ORDER BY date_creation DESC";
    
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, statut);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int commandeId = rs.getInt("id");
                Commande commande = getCommandeById(commandeId);
                if (commande != null) {
                    commandes.add(commande);
                }
            }
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération des commandes par statut: " + e.getMessage());
        e.printStackTrace();
    }
    return commandes;
}

/**
 * Lister les commandes par table
 */
public List<Commande> getCommandesByTable(int tableId) {
    List<Commande> commandes = new ArrayList<>();
    String query = "SELECT id FROM commandes WHERE table_id = ? ORDER BY date_creation DESC";
    
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, tableId);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int commandeId = rs.getInt("id");
                Commande commande = getCommandeById(commandeId);
                if (commande != null) {
                    commandes.add(commande);
                }
            }
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération des commandes par table: " + e.getMessage());
        e.printStackTrace();
    }
    return commandes;
}

// ===============================
// MÉTHODES UTILITAIRES
// ===============================

/**
 * Créer un état de commande basé sur le statut
 */
private CommandeState createEtatFromStatut(String statut, Commande commande) {
    switch (statut) {
        case "CREEE":
            return new NouvelleCommande(commande);
        case "VALIDEE":
        case "EN_PREPARATION":
            return new CommandeEnCours(commande);
        case "PRETE":
        case "LIVREE":
        case "PAYEE":
            return new CommandeLivree(commande);
        default:
            return new NouvelleCommande(commande);
    }
}

/**
 * Calculer le total d'une commande depuis la base de données
 */
public double calculateTotal(int commandeId) {
    String query = "SELECT SUM(ci.quantite * ci.prix_unitaire) as total " +
                  "FROM commande_items ci WHERE ci.commande_id = ?";
    
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, commandeId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors du calcul du total: " + e.getMessage());
        e.printStackTrace();
    }
    return 0.0;
}

/**
 * Vérifier si une commande existe
 */
public boolean commandeExists(int commandeId) {
    String query = "SELECT 1 FROM commandes WHERE id = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, commandeId);
        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next();
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la vérification de l'existence de la commande: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
}

// ===============================
// MÉTHODES HÉRITÉES (COMPATIBILITÉ)
// ===============================

/**
 * Méthode héritée pour la compatibilité avec l'ancienne version
 */
public void ajouter(Commande commande) throws SQLException {
    saveCommande(commande);
}

/**
 * Méthode héritée pour la compatibilité avec l'ancienne version
 */
public List<Commande> lister() throws SQLException {
    return getAllCommandes();
}

// ===============================
// FERMETURE DES RESSOURCES
// ===============================

/**
 * Fermer la connexion à la base de données
 */
public void close() {
    try {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
        e.printStackTrace();
    }
}
}
