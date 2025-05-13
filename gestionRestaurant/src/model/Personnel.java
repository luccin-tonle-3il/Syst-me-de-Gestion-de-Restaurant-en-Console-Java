package model;

public abstract class Personnel {
	protected String nom;
    protected String role;
	public Personnel(String nom) {
		super();
		this.nom = nom;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public void afficherDetails() {
        System.out.println("Personnel: " + nom + " - Rôle: " + role);
    }
	
}
