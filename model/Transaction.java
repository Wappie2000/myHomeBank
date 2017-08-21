package model;
import java.io.Serializable;
import java.time.LocalDate;

public class Transaction implements Cloneable, Serializable, Comparable<Transaction>{
	private static final long serialVersionUID = 1L;
	
	
	LocalDate transactionDate;
	String otherEndOfTransactionName;
	String otherEndOfTransactionAccount;
	float amount;
	String debetOrCredit;
	String description;
	Category category;


	
	
	
	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public String getOtherEndOfTransactionName() {
		return otherEndOfTransactionName;
	}

	public String getOtherEndOfTransactionAccount() {
		return otherEndOfTransactionAccount;
	}

	public float getAmount() {
		return amount;
	}

	public String getDebetOrCredit() {
		return debetOrCredit;
	}

	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}

	public Transaction(LocalDate transactionDate, float amount, String debetOrCredit, String description) {
		this.transactionDate = transactionDate;
		this.amount = amount;
		this.debetOrCredit = debetOrCredit;
		this.description = description;
	}
	
	@Override
	public String toString(){
		return transactionDate + " " + debetOrCredit + " " + amount+ " "+description + " category: " + category;
	}

	@Override
	public int compareTo(Transaction o) {
		return (int) (amount - o.amount);
	}

	public String getDescription() {
		
		return description;
	}
	

}
