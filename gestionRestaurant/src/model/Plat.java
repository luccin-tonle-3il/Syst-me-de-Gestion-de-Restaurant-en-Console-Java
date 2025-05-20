package model;

public class Plat extends MenuItem {
	private Ingredient ingredients;

	

	public Plat(String name, double price, Ingredient ingredients) {
		super(name, price);
		this.ingredients = ingredients;
	}



	@Override
	public void afficherDetails() {
		// TODO Auto-generated method stub
		 System.out.println("Plat: " + getName() + " - Prix: " + getPrice());
	}

}
