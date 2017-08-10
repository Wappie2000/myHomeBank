package flexibleHomeBank;
import java.io.Serializable;
import java.time.LocalDate;

public class Transaction implements Cloneable, Serializable, Comparable<Transaction>{

	
	LocalDate transactionDate;
	String otherEndOfTransactionName;
	String otherEndOfTransactionAccount;
	float amount;
	String debetOrCredit;
	String description;
	Category category;
	
	
	public Transaction(LocalDate transactionDate, float amount, String debetOrCredit, String description) {
		this.transactionDate = transactionDate;
		this.amount = amount;
		this.debetOrCredit = debetOrCredit;
		this.description = description;
	}
	
	@Override
	public String toString(){
		return "At the date of "+transactionDate + " transaction of " + debetOrCredit + " " + amount+ " with the description: "+description + " and the category " + category;
	}

	@Override
	public int compareTo(Transaction o) {
		// TODO Auto-generated method stub
		return (int) (amount - o.amount);
	}
	

}
