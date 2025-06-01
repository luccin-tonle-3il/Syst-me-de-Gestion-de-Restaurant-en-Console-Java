package model;

public class Boisson extends MenuItem {

	public Boisson(int id, String name, double price) {
		super(id, name, price);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void afficherDetails() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return  getId() + ", " + getName() + ", 1"
				+ "" + getPrice()
				;
	}

	

	
	
}
