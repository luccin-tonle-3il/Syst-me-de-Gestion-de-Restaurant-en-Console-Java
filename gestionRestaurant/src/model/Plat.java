package model;

public class Plat extends MenuItem {
	
	

	public Plat(int id, String name, double price) {
		super(id, name, price);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void afficherDetails() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return getId()+". "+ getName() + ", " + getPrice() 
				;
	}
	
	




	

}
