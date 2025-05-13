package model;

public class Plat extends MenuItem {

	public Plat(String name, double price) {
		super(name, price);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void afficherDetails() {
		// TODO Auto-generated method stub
		 System.out.println("Plat: " + getName() + " - Prix: " + getPrice());
	}

}
