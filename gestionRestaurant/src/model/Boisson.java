package model;

public class Boisson extends MenuItem {

	public Boisson(String name, double price) {
		super(name, price);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void afficherDetails() {
		// TODO Auto-generated method stub
		 System.out.println("Boisson: " + getName() + " - Prix: " + getPrice());
	}
	
	
}
