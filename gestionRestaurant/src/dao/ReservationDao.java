package dao;

import model.Reservation;
import model.StatutReservation;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDao {

    private Connection connection;

    public ReservationDao() {
        try {
            connection = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🆕 Ajouter une réservation
    public void ajouterReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservations (table_id, client_nom, date_reservation, heure_reservation, statut) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reservation.getTableId());
            stmt.setString(2, reservation.getNomClient());
            stmt.setDate(3, Date.valueOf(reservation.getDateReservation()));   // LocalDate → java.sql.Date
            stmt.setTime(4, Time.valueOf(reservation.getHeureReservation())); // LocalTime → java.sql.Time
            stmt.setString(5, reservation.getStatut().getLabel());            // Enum → String

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de l'ajout de la réservation.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservation.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // 🔍 Récupérer une réservation par ID
    public Reservation getReservationById(int id) throws SQLException {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReservation(rs);
                }
            }
        }
        return null;
    }

    // 📋 Lister toutes les réservations
    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations ORDER BY date_reservation, heure_reservation";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        }
        return reservations;
    }

    // ✏️ Mettre à jour une réservation
    public void updateReservation(Reservation reservation) throws SQLException {
        String sql = "UPDATE reservations SET table_id = ?, client_nom = ?, date_reservation = ?, heure_reservation = ?, statut = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reservation.getTableId());
            stmt.setString(2, reservation.getNomClient());
            stmt.setDate(3, Date.valueOf(reservation.getDateReservation()));
            stmt.setTime(4, Time.valueOf(reservation.getHeureReservation()));
            stmt.setString(5, reservation.getStatut().getLabel()); // Enum → String
            stmt.setInt(6, reservation.getId());

            stmt.executeUpdate();
        }
    }

    // ❌ Supprimer une réservation
    public void supprimerReservation(int id) throws SQLException {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // 🔄 Mapper ResultSet → Reservation
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        int id = rs.getInt("reservation_id");
        int tableId = rs.getInt("table_id");
        String nomClient = rs.getString("client_nom");
        LocalDate dateReservation = rs.getDate("date_reservation").toLocalDate();
        LocalTime heureReservation = rs.getTime("heure_reservation").toLocalTime();
        StatutReservation statut = StatutReservation.fromString(rs.getString("statut")); // String → Enum

        return new Reservation(id, tableId, nomClient, dateReservation, heureReservation, statut);
    }
    
    public Reservation getReservationById1(int id) throws SQLException {
        String sql = "SELECT * FROM reservation WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Reservation res = new Reservation();
                res.setId(rs.getInt("id"));
                res.setTableId(rs.getInt("table_id"));
                res.setNomClient(rs.getString("nom_client"));
                // Conversion de java.sql.Date en java.time.LocalDate
                Date sqlDate = rs.getDate("date_reservation");
                if (sqlDate != null) {
                    res.setDateReservation(sqlDate.toLocalDate());
                }
                // Conversion de java.sql.Time en java.time.LocalTime
                Time sqlTime = rs.getTime("heure_reservation");
                if (sqlTime != null) {
                    res.setHeureReservation(sqlTime.toLocalTime());
                }
                // Pour le statut, suppose que c'est un enum avec une colonne texte
                String statutStr = rs.getString("statut");
                if (statutStr != null) {
                    res.setStatut(StatutReservation.valueOf(statutStr.toUpperCase()));
                }
                return res;
            } else {
                return null; // Pas trouvé
            }
        }
    }

}
