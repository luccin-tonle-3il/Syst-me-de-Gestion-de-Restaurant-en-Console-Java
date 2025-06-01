package model;

public enum StatutReservation {
	 CONFIRMEE("Confirmée"),
	    ANNULEE("Annulée"),
	    TERMINEE("Terminée");

	    private final String label;

	    StatutReservation(String label) {
	        this.label = label;
	    }

	    public String getLabel() {
	        return label;
	    }

	    @Override
	    public String toString() {
	        return label;
	    }

	    // Méthode pour récupérer l'enum depuis String
	    public static StatutReservation fromString(String text) {
	        for (StatutReservation s : StatutReservation.values()) {
	            if (s.label.equalsIgnoreCase(text)) {
	                return s;
	            }
	        }
	        throw new IllegalArgumentException("Statut invalide : " + text);
	    }

	    public static StatutReservation fromInt(int choix) {
	        return switch (choix) {
	            case 1 -> CONFIRMEE;
	            case 2 -> ANNULEE;
	            case 3 -> TERMINEE;
	            default -> throw new IllegalArgumentException("Choix de statut invalide : " + choix);
	        };
	    }
}
