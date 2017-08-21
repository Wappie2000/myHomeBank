package model;
import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable, Comparable<Category> {

	
	
	//Fields
	String name;

	ArrayList<String> descriptions;


	

	
	
	
	// Constructors
	public Category(String name) {
		this.name = name;
	}
	public Category(String name, ArrayList<String> descriptions) {
		this.name = name;
		this.descriptions = descriptions;
	}



	// getters and setters
	public String getName() {
		return name;
	}
	public ArrayList<String> getDescriptions() {
		return descriptions;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescriptions(ArrayList<String> descriptions) {
		this.descriptions = descriptions;
	}




	// Overriden behaviour
	@Override
	public String toString(){
		return name;
	}
	@Override
	public int compareTo(Category o) {

		return name.compareTo(o.name);
	}
}
