package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuItemFactory {
	public static MenuItem create(String type, int id, String name, double price) {
        return switch (type.toLowerCase()) {
            case "plat" -> new Plat(id, name, price);
            case "boisson" -> new Boisson(id, name, price);
            default -> throw new IllegalArgumentException("Type invalide");
        };
    }

	 public static MenuItem creerDepuisResultSet(ResultSet rs) throws SQLException {
	        String type = rs.getString("type"); // "plat", "boisson", "dessert"

	        int id = rs.getInt("id");
	        String nom = rs.getString("nom");
	        double prix = rs.getDouble("prix");

	        switch (type.toLowerCase()) {
	            case "plat":
	                return new Plat(id, nom, prix);
	            case "boisson":
	                return new Boisson(id, nom, prix);
	            default:
	                throw new IllegalArgumentException("Type de menu inconnu : " + type);
	        }
	    }
}
