package model;

import java.util.HashMap;
import java.util.Map;

public class Stock {
	 private Map<String, Ingredient> ingredients;

	    public Stock() {
	        ingredients = new HashMap<>();
	    }

	    public void ajouterIngredient(Ingredient ingredient) {
	        ingredients.put(ingredient.getNom(), ingredient);
	    }

	    public Ingredient getIngredient(String nom) {
	        return ingredients.get(nom);
	    }

	    public void afficherStock() {
	        for (Ingredient ingredient : ingredients.values()) {
	            ingredient.afficherDetails();
	        }
	    }

	    public boolean isStockBas(String nom) {
	        Ingredient ingredient = ingredients.get(nom);
	        return ingredient != null && ingredient.getQuantite() < 5; // Seuil bas défini à 5
	    }
}
