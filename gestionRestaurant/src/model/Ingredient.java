package model;

public class Ingredient {
	 private String nom;
	    private int quantite;

	    public Ingredient(String nom, int quantite) {
	        this.nom = nom;
	        this.quantite = quantite;
	    }

	    public String getNom() {
	        return nom;
	    }

	    public void setNom(String nom) {
	        this.nom = nom;
	    }

	    public int getQuantite() {
	        return quantite;
	    }

	    public void setQuantite(int quantite) {
	        this.quantite = quantite;
	    }

	    public void utiliserIngredient(int quantiteUtilisee) {
	        this.quantite -= quantiteUtilisee;
	    }

	    public void afficherDetails() {
	        System.out.println("Ingrédient: " + nom + " - Quantité: " + quantite);
	    }
}
