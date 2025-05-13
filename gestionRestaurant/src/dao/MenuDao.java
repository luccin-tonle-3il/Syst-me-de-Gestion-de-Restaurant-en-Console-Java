package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Boisson;
import model.MenuItem;
import model.Plat;

public class MenuDao {
	private Connection connection;

    public MenuDao() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ajouter(MenuItem item) throws SQLException {
        String query = "INSERT INTO menu_items (nom, prix, type) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
        	stmt.setInt(1, item.getId());
            stmt.setString(2, item.getName());
            stmt.setDouble(3, item.getPrice());
            stmt.setString(4, item instanceof Plat ? "plat" : "boisson");
            stmt.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM menu_items WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<MenuItem> lister() throws SQLException {
        List<MenuItem> items = new ArrayList<>();
        String query = "SELECT * FROM menu_items";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String nom = rs.getString("nom");
                double prix = rs.getDouble("prix");
                String type = rs.getString("type");

                MenuItem item;
                if ("plat".equals(type)) {
                    item = new Plat(nom, prix); // Ajoute des infos de description si nécessaire
                } else {
                    item = new Boisson(nom, prix); // Par défaut non alcoolisé, mais peut être modifié
                }

                items.add(item);
            }
        }
        return items;
    }
}
