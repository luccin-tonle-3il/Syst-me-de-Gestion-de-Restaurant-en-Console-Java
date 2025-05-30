package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import model.Reservation;

public class ReservationDao {
	  private Connection connection;

	    public ReservationDao() {
	        try {
	            connection = DBConnection.getConnection();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // Ajouter une réservation
	    public void ajouter(Reservation reservation) throws SQLException {
	        String query = "INSERT INTO reservations (table_id, nom_client, date_reservation, heure_reservation) VALUES (?, ?, ?, ?)";
	        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	            stmt.setInt(1, reservation.getId());
	            stmt.setString(2, reservation.getNomClient());
	            stmt.setDate(3, Date.valueOf(reservation.getDateReservation()));
	            stmt.executeUpdate();

	            // Obtenir l'ID généré pour la réservation
	            try (ResultSet rs = stmt.getGeneratedKeys()) {
	                if (rs.next()) {
	                    int reservationId = rs.getInt(1);
	                    reservation.setId(reservationId);
	                }
	            }
	        }
	    }

	    // Supprimer une réservation
	    public void supprimer(int id) throws SQLException {
	        String query = "DELETE FROM reservations WHERE id = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setInt(1, id);
	            stmt.executeUpdate();
	        }
	    }

	    // Lister toutes les réservations
	    public List<Reservation> lister() throws SQLException {
	        List<Reservation> reservations = new ArrayList<>();
	        String query = "SELECT * FROM reservations";
	        try (Statement stmt = connection.createStatement();
	             ResultSet rs = stmt.executeQuery(query)) {

	            while (rs.next()) {
	                int id = rs.getInt("id");
	                int tableId = rs.getInt("table_id");
	                String nomClient = rs.getString("nom_client");
	                String dateReservation = rs.getString("date_reservation");
	          

	                Reservation reservation = new Reservation(id, dateReservation, tableId, nomClient);
	                reservations.add(reservation);
	            }
	        }
	        return reservations;
	    }

	    // Trouver une réservation par ID
	    public Reservation trouverParId(int id) throws SQLException {
	        String query = "SELECT * FROM reservations WHERE id = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setInt(1, id);
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    int tableId = rs.getInt("table_id");
	                    String nomClient = rs.getString("nom_client");
	                    Date dateReservation = rs.getDate("date_reservation");
	                    Time heureReservation = rs.getTime("heure_reservation");

	                    return new Reservation(id, nomClient, tableId, nomClient);
	                }
	            }
	        }
	        return null; // Retourne null si la réservation n'existe pas
	    }

	    // Mettre à jour une réservation
	    public void mettreAJour(Reservation reservation) throws SQLException {
	        String query = "UPDATE reservations SET table_id = ?, nom_client = ?, date_reservation = ? WHERE id = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setInt(1, reservation.getId());
	            stmt.setString(2, reservation.getNomClient());
	            stmt.setDate(3, Date.valueOf(reservation.getDateReservation()));
	            stmt.setInt(4, reservation.getId());
	            stmt.executeUpdate();
	        }
	    }
}
