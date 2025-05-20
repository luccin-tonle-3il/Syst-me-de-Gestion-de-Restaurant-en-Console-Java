package model;

public class MenuItemFactory {
	public static MenuItem create(String type, String name, double price,Ingredient ingre) {
        return switch (type.toLowerCase()) {
            case "plat" -> new Plat(name, price, ingre);
            case "boisson" -> new Boisson(name, price);
            default -> throw new IllegalArgumentException("Type invalide");
        };
    }
}
