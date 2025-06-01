package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.EtatTable;
import model.TableRestaurant;

public class TableDao {
    private Connection conn;

    // Constructeur : initialise la connexion à la base
    public TableDao() {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔁 1. Récupérer toutes les tables
    public List<TableRestaurant> getAllTables() throws SQLException {
        List<TableRestaurant> tables = new ArrayList<>();
        String sql = "SELECT * FROM tables"; // ⚠ Assure-toi que la table s'appelle bien "tables"
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            TableRestaurant table = new TableRestaurant(
                rs.getInt("table_id"),
                rs.getInt("numero"),
                rs.getInt("capacite"),
                EtatTable.valueOf(rs.getString("etat"))
            );
            tables.add(table);
        }
        return tables;
    }

    // ✏️ 2. Modifier l'état d'une table (DISPONIBLE, OCCUPEE, RESERVEE)
    public void updateEtat(int tableId, EtatTable nouvelEtat) throws SQLException {
        String sql = "UPDATE tables SET etat = ? WHERE table_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nouvelEtat.name()); // Enum → String
        ps.setInt(2, tableId);
        ps.executeUpdate();
    }

    // 🔎 3. Récupérer les tables DISPONIBLES uniquement
    public List<TableRestaurant> getTablesDisponibles() throws SQLException {
        List<TableRestaurant> tables = new ArrayList<>();
        String sql = "SELECT * FROM tables WHERE etat = 'DISPONIBLE'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            TableRestaurant table = new TableRestaurant(
                rs.getInt("table_id"),
                rs.getInt("numero"),
                rs.getInt("capacite"),
                EtatTable.valueOf(rs.getString("etat"))
            );
            tables.add(table);
        }
        return tables;
    }

    // ➕ 4. Ajouter une nouvelle table
    public void ajouterTable(TableRestaurant table) throws SQLException {
        String sql = "INSERT INTO tables (numero, capacite, etat) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, table.getNumero());
        ps.setInt(2, table.getCapacite());
        ps.setString(3, table.getEtat().name());
        ps.executeUpdate();
    }
}
