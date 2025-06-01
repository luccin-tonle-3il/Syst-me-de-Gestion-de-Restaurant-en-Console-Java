package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Boisson;
import model.Commande;
import model.MenuItem;
import model.MenuItemFactory;
import model.Plat;
import service.CommandeEnCours;
import service.CommandeLivree;
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
    // === 1. Ajouter une commande ===
    public void ajouterCommande(Commande commande) throws SQLException {
        String sql = "INSERT INTO commande (id_commande, id_table, date_commande, etat) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commande.getCommandeId());
            stmt.setInt(2, commande.getTableId());
            stmt.setTimestamp(3, new Timestamp(commande.getDatecom().getTime()));
            stmt.setString(4, "nouvelle");
            stmt.executeUpdate();
        }

        // Enregistrer les items de la commande
        for (MenuItem item : commande.getItems()) {
            ajouterItemCommande(commande.getCommandeId(), item.getId());
        }
    }

    // === 2. Ajouter un item à une commande ===
    public void ajouterItemCommande(int commandeId, int menuItemId) throws SQLException {
        String sql = "INSERT INTO commande_item (commande_id, menu_item_id) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commandeId);
            stmt.setInt(2, menuItemId);
            stmt.executeUpdate();
        }
    }

    // === 3. Supprimer un item d'une commande ===
    public void supprimerItemCommande(int commandeId, int menuItemId) throws SQLException {
        String sql = "DELETE FROM commande_item WHERE commande_id = ? AND menu_item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commandeId);
            stmt.setInt(2, menuItemId);
            stmt.executeUpdate();
        }
    }

    // === 4. Consulter une commande par ID ===
    public Commande getCommandeById(int commandeId) throws SQLException {
        String sql = "SELECT * FROM commande WHERE id_commande = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commandeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Commande commande = new Commande(commandeId);
                commande.setTableId(rs.getInt("id_table"));
                commande.setDatecom(rs.getTimestamp("date_commande"));
                commande.setEtat(new NouvelleCommande()); // à adapter selon l'état stocké

                List<MenuItem> items = getItemsCommande(commandeId);
                for (MenuItem item : items) {
                    commande.ajouterItem(item);
                }

                return commande;
            }
        }
        return null;
    }

    // === 5. Récupérer tous les items d'une commande ===
    public List<MenuItem> getItemsCommande(int commandeId) throws SQLException {
        List<MenuItem> items = new ArrayList<>();
        String sql = """
                SELECT mi.* FROM menu_item mi
                INNER JOIN commande_item ci ON mi.id = ci.menu_item_id
                WHERE ci.commande_id = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commandeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(MenuItemFactory.creerDepuisResultSet(rs));
            }
        }
        return items;
    }

    // === 6. Valider une commande ===
    public void validerCommande(int commandeId) throws SQLException {
        String sql = "UPDATE commande SET etat = ? WHERE id_commande = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "validee");
            stmt.setInt(2, commandeId);
            stmt.executeUpdate();
        }
    }

    // === 7. Lister toutes les commandes ===
    public List<Commande> getToutesLesCommandes() throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_commande");
                Commande commande = getCommandeById(id);
                commandes.add(commande);
            }
        }
        return commandes;
    }
}
