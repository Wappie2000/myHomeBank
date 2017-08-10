package flexibleHomeBank;
import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {

	String name;
	ArrayList<String> descriptions;
	
	
	public Category(String name, ArrayList<String> descriptions) {
		this.name = name;
		this.descriptions = descriptions;
	}


	public String toString(){
		return name;
	}

	
}
