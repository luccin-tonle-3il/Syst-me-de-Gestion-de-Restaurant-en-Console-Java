package dao;

import model.MenuItem;
import model.Plat;
import model.Boisson;
import model.MenuItemFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDao {
    private Connection connection;

    public MenuDao() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int getNextIdForType(String type) throws SQLException {
        String sql = "SELECT MAX(id) AS max_id FROM menu_item WHERE type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("max_id") + 1;
            } else {
                return 1;
            }
        }
    }

    public void ajouterItem(String nom, double prix, String type) throws SQLException {
        int id = getNextIdForType(type); // ➕ Id indépendant
        String sql = "INSERT INTO menu_item (id, nom, prix, type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, nom);
            stmt.setDouble(3, prix);
            stmt.setString(4, type.toLowerCase());
            stmt.executeUpdate();
        }
    }


    // 🔁 Obtenir tous les MenuItem (plats + boissons)
    public List<MenuItem> getAllItems() throws SQLException {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT * FROM menu_item";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MenuItem item = MenuItemFactory.creerDepuisResultSet(rs);
                items.add(item);
            }
        }
        return items;
    }

    // ✅ Obtenir tous les plats uniquement
    public List<Plat> getAllPlats() throws SQLException {
        List<Plat> plats = new ArrayList<>();
        String sql = "SELECT * FROM menu_item WHERE type = 'plat'";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Plat p = new Plat(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("prix")
                );
                plats.add(p);
            }
        }
        return plats;
    }

    // ✅ Obtenir toutes les boissons uniquement
    public List<Boisson> getAllBoissons() throws SQLException {
        List<Boisson> boissons = new ArrayList<>();
        String sql = "SELECT * FROM menu_item WHERE type = 'boisson'";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Boisson b = new Boisson(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("prix")
                );
                boissons.add(b);
            }
        }
        return boissons;
    }



    // ❌ Supprimer un item par ID
    public void supprimerItem(int id) throws SQLException {
        String sql = "DELETE FROM menu_item WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


}
