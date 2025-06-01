package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Paiement;

public class PaiementDao {
    private Connection connection;

    public PaiementDao() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour obtenir la connexion
    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DBConnection.getConnection();
        }
        return connection;
    }

    // ====================== CRÉER UN PAIEMENT ======================
    public void ajouterPaiement(Paiement paiement) throws SQLException {
        String sql = "INSERT INTO paiements (id_commande, montant, date_paiement, mode_paiement) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, paiement.getIdCommande());
            stmt.setDouble(2, paiement.getMontant());
            stmt.setDate(3, Date.valueOf(paiement.getDatePaiement()));
            stmt.setString(4, paiement.getModePaiement());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("❌ Échec de l'ajout du paiement.");
            }
            
            // Récupérer l'ID généré
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    paiement.setIdPaiement(generatedKeys.getInt(1));
                }
            }
            
            System.out.println("✅ Paiement ajouté avec ID: " + paiement.getIdPaiement());
        }
    }

    // ====================== RÉCUPÉRER TOUS LES PAIEMENTS ======================
    public List<Paiement> getAllPaiements() throws SQLException {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiements ORDER BY date_paiement DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Paiement paiement = new Paiement(
                    rs.getInt("id_paiement"),
                    rs.getInt("id_commande"),
                    rs.getDouble("montant"),
                    rs.getDate("date_paiement").toLocalDate(),
                    rs.getString("mode_paiement")
                );
                paiements.add(paiement);
            }
        }
        
        return paiements;
    }

    // ====================== RÉCUPÉRER PAIEMENT PAR ID ======================
    public Paiement getPaiementById(int idPaiement) throws SQLException {
        String sql = "SELECT * FROM paiements WHERE id_paiement = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, idPaiement);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Paiement(
                        rs.getInt("id_paiement"),
                        rs.getInt("id_commande"),
                        rs.getDouble("montant"),
                        rs.getDate("date_paiement").toLocalDate(),
                        rs.getString("mode_paiement")
                    );
                }
            }
        }
        
        return null; // Paiement non trouvé
    }

    // ====================== RÉCUPÉRER PAIEMENTS PAR COMMANDE ======================
    public List<Paiement> getPaiementsByCommande(int idCommande) throws SQLException {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiements WHERE id_commande = ? ORDER BY date_paiement DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, idCommande);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Paiement paiement = new Paiement(
                        rs.getInt("id_paiement"),
                        rs.getInt("id_commande"),
                        rs.getDouble("montant"),
                        rs.getDate("date_paiement").toLocalDate(),
                        rs.getString("mode_paiement")
                    );
                    paiements.add(paiement);
                }
            }
        }
        
        return paiements;
    }

    // ====================== RÉCUPÉRER PAIEMENTS PAR PÉRIODE ======================
    public List<Paiement> getPaiementsByPeriode(LocalDate dateDebut, LocalDate dateFin) throws SQLException {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiements WHERE date_paiement BETWEEN ? AND ? ORDER BY date_paiement DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(dateDebut));
            stmt.setDate(2, Date.valueOf(dateFin));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Paiement paiement = new Paiement(
                        rs.getInt("id_paiement"),
                        rs.getInt("id_commande"),
                        rs.getDouble("montant"),
                        rs.getDate("date_paiement").toLocalDate(),
                        rs.getString("mode_paiement")
                    );
                    paiements.add(paiement);
                }
            }
        }
        
        return paiements;
    }

    // ====================== CALCULER TOTAL DES VENTES ======================
    public double getTotalVentes() throws SQLException {
        String sql = "SELECT SUM(montant) as total FROM paiements";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        
        return 0.0;
    }

    // ====================== CALCULER TOTAL PAR PÉRIODE ======================
    public double getTotalVentesPeriode(LocalDate dateDebut, LocalDate dateFin) throws SQLException {
        String sql = "SELECT SUM(montant) as total FROM paiements WHERE date_paiement BETWEEN ? AND ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(dateDebut));
            stmt.setDate(2, Date.valueOf(dateFin));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        
        return 0.0;
    }

    // ====================== COMPTER NOMBRE DE PAIEMENTS ======================
    public int getNombrePaiements() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM paiements";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        
        return 0;
    }

    // ====================== MODIFIER UN PAIEMENT ======================
    public void updatePaiement(Paiement paiement) throws SQLException {
        String sql = "UPDATE paiements SET id_commande = ?, montant = ?, date_paiement = ?, mode_paiement = ? WHERE id_paiement = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, paiement.getIdCommande());
            stmt.setDouble(2, paiement.getMontant());
            stmt.setDate(3, Date.valueOf(paiement.getDatePaiement()));
            stmt.setString(4, paiement.getModePaiement());
            stmt.setInt(5, paiement.getIdPaiement());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("❌ Aucun paiement trouvé avec l'ID: " + paiement.getIdPaiement());
            }
            
            System.out.println("✅ Paiement mis à jour.");
        }
    }

    // ====================== SUPPRIMER UN PAIEMENT ======================
    public void supprimerPaiement(int idPaiement) throws SQLException {
        String sql = "DELETE FROM paiements WHERE id_paiement = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setInt(1, idPaiement);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("❌ Aucun paiement trouvé avec l'ID: " + idPaiement);
            }
            
            System.out.println("✅ Paiement supprimé.");
        }
    }

    // ====================== RÉCUPÉRER PAIEMENTS PAR MODE ======================
    public List<Paiement> getPaiementsByMode(String modePaiement) throws SQLException {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiements WHERE mode_paiement = ? ORDER BY date_paiement DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            
            stmt.setString(1, modePaiement);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Paiement paiement = new Paiement(
                        rs.getInt("id_paiement"),
                        rs.getInt("id_commande"),
                        rs.getDouble("montant"),
                        rs.getDate("date_paiement").toLocalDate(),
                        rs.getString("mode_paiement")
                    );
                    paiements.add(paiement);
                }
            }
        }
        
        return paiements;
    }

    // ====================== FERMER LA CONNEXION ======================
    public void fermerConnexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}