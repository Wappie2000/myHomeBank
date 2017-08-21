package model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import view.BankModelObserver;

public class BankModel implements BankModelInterface , Serializable{
	private static final long serialVersionUID = 1L;

	ArrayList<String> rawInput;
	
	ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	ArrayList<Transaction> excludedTransactions = new ArrayList<Transaction>();
	ArrayList<Category> categories = new ArrayList<Category>();

	@Override
	public ArrayList<Transaction> getExcludedTransactions() {
		return excludedTransactions;
	}
	
	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}

	public ArrayList<Category> getCategories() {
		return categories;
	}

	public void loadTransactionFile(File file){
		rawInput = new ArrayList<String> ();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file)); 
			String line = null;
			
			while ((line = reader.readLine()) !=null) {              
				rawInput.add(line);
			}
			reader.close();
		} catch (Exception ex) {
			System.out.println("Could'nt read the file");
			ex.printStackTrace();
		}
		convertRawInputToTransaction();
	}

	void convertRawInputToTransaction(){
		for(String s: rawInput){
			String[] result = s.split(",");
			int year = Integer.parseInt(result[2].substring(1, 5));
			int month = Integer.parseInt(result[2].substring(5, 7));
			int day = Integer.parseInt(result[2].substring(7, 9));
			LocalDate date = LocalDate.of(year,month,day);
			String debetOrCredit = result[3].replaceAll("\"","");
			float amount = Float.parseFloat(result[4].replaceAll("\"",""));
			String description = result[10].replaceAll("\"","");

			Transaction transaction = new Transaction(date, amount, debetOrCredit, description);
			transactions.add(transaction);

		}
		Collections.sort(transactions);
		initialCategoryAssignment();
	} 

	void initialCategoryAssignment(){
		Category categoryNA = new Category("not yet assigned", new ArrayList<String>());
		categories.add(categoryNA);
		for(Transaction t: transactions){
			t.category = categoryNA;
			for(Category category : categories){
				for(String s : category.descriptions){
					if(t.description.contains(s)){
						t.category = category;
					}
				}
			}
		}
	}

	public void loadCategoryFile(File file){
		ArrayList<String> temp = new ArrayList<String> ();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file)); 

			String line = null;

			while ((line = reader.readLine()) !=null) {              
				temp.add(line);

			}
			reader.close();
		} catch (Exception ex) {
			System.out.println("Could'nt read the file");
			ex.printStackTrace();
		}

		for(String s: temp){
			String[] lineSeperated = s.split(";");
			ArrayList<String> keywords = new ArrayList<String>();
			for(int i = 1; i < lineSeperated.length; i++){  // this is on purpose, only first word excluded, this is the name of the category
				keywords.add(lineSeperated[i].trim());
			}
			categories.add(new Category(lineSeperated[0], keywords));
		}
		System.out.println("loadCategoryFile, on bankmodel completed, first category: " + categories.get(0));
	}

	
	@Override
	public void registerObserver(BankModelObserver o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeObserver(BankModelObserver o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveState() {
		try {
			FileOutputStream fs = new FileOutputStream("HBcurrentstate.ser");
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(this);
			os.close();
		} catch(Exception e)
		{e.printStackTrace();
		}
		
	}
}
