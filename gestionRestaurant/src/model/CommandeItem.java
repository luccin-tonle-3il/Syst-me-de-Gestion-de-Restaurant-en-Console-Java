package model;

public class CommandeItem {
	private int idCommande;
    private MenuItem item;
    private int quantite;

    public CommandeItem(int idCommande, MenuItem item, int quantite) {
        this.idCommande = idCommande;
        this.item = item;
        this.quantite = quantite;
    }

    public int getIdCommande() { return idCommande; }
    public MenuItem getItem() { return item; }
    public int getQuantite() { return quantite; }

    public void setQuantite(int quantite) { this.quantite = quantite; }
}
