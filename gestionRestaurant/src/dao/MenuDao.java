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

    // ✅ Afficher tous les plats
    public List<Plat> getAllPlats() throws SQLException  {
        List<Plat> plats = new ArrayList<>();
        String sql = "SELECT * FROM plat";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Plat p = new Plat(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getDouble("prix")
            );
            plats.add(p);
        }

        return plats;
    }
    
    public List<Boisson> getAllBoissons() throws SQLException {
        List<Boisson> boissons = new ArrayList<>();
        String sql = "SELECT * FROM boisson";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Boisson b = new Boisson(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getDouble("prix")
            );
            boissons.add(b);
        }

        return boissons;
    }
    
 // Ajouter un plat
    public void ajouterPlat(String nom, double prix) throws SQLException {
        String sql = "INSERT INTO Plat (nom, prix) VALUES (?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, nom);
        stmt.setDouble(2, prix);
        stmt.executeUpdate();
    }

    // Supprimer un plat
    public void supprimerPlat(int id) throws SQLException {
        String sql = "DELETE FROM Plat WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    // Ajouter une boisson
    public void ajouterBoisson(String nom, double prix) throws SQLException {
        String sql = "INSERT INTO Boisson (nom, prix) VALUES (?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, nom);
        stmt.setDouble(2, prix);
        stmt.executeUpdate();
    }

    // Supprimer une boisson
    public void supprimerBoisson(int id) throws SQLException {
        String sql = "DELETE FROM Boisson WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    


}
