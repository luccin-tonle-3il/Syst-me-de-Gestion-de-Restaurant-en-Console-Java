package model;

public class MenuItemFactory {
	public static MenuItem create(String type, String name, double price) {
        return switch (type.toLowerCase()) {
            case "plat" -> new Plat(name, price);
            case "boisson" -> new Boisson(name, price);
            default -> throw new IllegalArgumentException("Type invalide");
        };
    }
}
