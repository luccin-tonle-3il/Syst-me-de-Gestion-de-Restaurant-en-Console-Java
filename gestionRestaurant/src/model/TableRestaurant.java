package model;


public class TableRestaurant {
    private int tableId;
    private int numero;
    private int capacite;
    private EtatTable etat; // DISPONIBLE, OCCUPEE, RESERVEE

    // Constructeur
    public TableRestaurant(int tableId, int numero, int capacite, EtatTable etat) {
        this.tableId = tableId;
        this.numero = numero;
        this.capacite = capacite;
        this.etat = etat;
    }


	// Getters & Setters
    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public EtatTable getEtat() { return etat; }
    public void setEtat(EtatTable etat) { this.etat = etat; }

    @Override
    public String toString() {
        return "Table #" + numero + " - Capacité: " + capacite + " - État: " + etat;
    }
}
