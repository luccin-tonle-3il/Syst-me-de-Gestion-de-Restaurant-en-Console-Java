package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	 private static Connection connection;
	    private DBConnection() {}
	    public static Connection getConnection() throws SQLException {
	        if (connection == null || connection.isClosed()) {
	            try {
	                // Remplacez les paramètres suivants par vos propres informations de connexion à la base de données
	                String url = "jdbc:mysql://localhost:3306/gestionrestaurant";
	                String username = "root";
	                String password = "password";

	                connection = DriverManager.getConnection(url, username, password);
	            } catch (SQLException e) {
	                e.printStackTrace();
	                throw new SQLException("Erreur lors de la connexion à la base de données.");
	            }
	        }
	        return connection;
	    }
}
