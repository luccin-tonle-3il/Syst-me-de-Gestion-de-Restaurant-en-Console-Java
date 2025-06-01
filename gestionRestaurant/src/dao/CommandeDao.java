package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.*;
import service.*;

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

    /**
     * Ajoute une commande avec ses items dans la base de données.
     */
    public void ajouterCommande(Commande commande) throws SQLException {
        String sql = "INSERT INTO commande (id_commande, id_table, date_commande, etat) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commande.getCommandeId());
            stmt.setInt(2, commande.getTableId());
            stmt.setTimestamp(3, new Timestamp(commande.getDatecom().getTime()));
            stmt.setString(4, commande.getEtat().getEtat());
            stmt.executeUpdate();
        }

        // Ajouter chaque item avec la quantité
        for (CommandeItem item : commande.getItems()) {
            ajouterItemCommande(commande.getCommandeId(), item.getItem(), item.getQuantite());
        }
    }

    /**
     * Ajoute un item dans une commande avec la quantité
     */
    public void ajouterItemCommande(int commandeId, MenuItem menuItem, int quantite) throws SQLException {
        String sql = "INSERT INTO commande_item (commande_id, menu_item_id, quantite) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commandeId);
            stmt.setInt(2, menuItem.getId());
            stmt.setInt(3, quantite);
            stmt.executeUpdate();
        }
    }

    /**
     * Supprime un item d'une commande
     */
    public void supprimerItemCommande(int commandeId, int menuItemId) throws SQLException {
        String sql = "DELETE FROM commande_item WHERE commande_id = ? AND menu_item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commandeId);
            stmt.setInt(2, menuItemId);
            stmt.executeUpdate();
        }
    }

    /**
     * Récupère une commande complète (métadonnées + items)
     */
    public Commande getCommandeById(int commandeId) throws SQLException {
        String sql = "SELECT * FROM commande WHERE id_commande = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commandeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Commande commande = new Commande(commandeId);
                commande.setTableId(rs.getInt("id_table"));
                commande.setDatecom(rs.getTimestamp("date_commande"));

                // Crée l'état
                String etatStr = rs.getString("etat");
                EtatCommande etat;
                switch (etatStr) {
                    case "Nouvelle" -> etat = new NouvelleCommande();
                    case "EnCours" -> etat = new CommandeEnCours();
                    case "Prete" -> etat = new CommandePrete();
                    case "Livree" -> etat = new CommandeLivree();
                    default -> etat = new NouvelleCommande();
                }
                commande.setEtat(etat);

                // Récupère les items
                List<CommandeItem> items = getItemsCommande(commandeId);
                for (CommandeItem item : items) {
                    commande.ajouterItem(item);
                }

                return commande;
            }
        }

        return null;
    }

    /**
     * Récupère les items d'une commande
     */
    public List<CommandeItem> getItemsCommande(int commandeId) throws SQLException {
        List<CommandeItem> items = new ArrayList<>();

        String sql = """
            SELECT mi.*, ci.quantite 
            FROM menu_item mi
            INNER JOIN commande_item ci ON mi.id = ci.menu_item_id
            WHERE ci.commande_id = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commandeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MenuItem item = MenuItemFactory.creerDepuisResultSet(rs);
                int quantite = rs.getInt("quantite");
                items.add(new CommandeItem(commandeId, item, quantite));
            }
        }

        return items;
    }
}
