package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;



import model.*;




public class BankController1 implements BankControllerInterface {

	BankModelInterface bankModel;




	public BankController1(BankModelInterface bankModel) {
		this.bankModel = bankModel;
	}




	@Override
	public DefaultPieDataset createPieDataset(LocalDate date1, LocalDate date2, String debCred) {
		System.out.println("Creating pie dataset");
		DefaultPieDataset data = new DefaultPieDataset();
		for(Category c : bankModel.getCategories()){

			ArrayList<Transaction> temp = null;
			if(debCred.equals("B")){
				temp = searchTransactions(date1, date2, c.getName(), 0);
			} else {
				temp = searchTransactions(date1, date2, c.getName(), 0, debCred);
			}
			int tempAmount = 0;
			for (Transaction t : temp){
				tempAmount += t.getAmount();
			}
			if(tempAmount>0){
				data.setValue(c.getName(), tempAmount);
			}
		}
		return data;
	}

	
	
	@Override
	public DefaultCategoryDataset createMontlyDataset(LocalDate date1, LocalDate date2, String debCred){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		Period p = Period.between(date1, date2);
		int maxI =  p.getMonths() + p.getYears()*12;
		System.out.println(maxI);
		for(int i = 0; i < maxI; i++){
			for(Category category : bankModel.getCategories()){
				double m1 = categoryTotal(category.getName(), debCred, date1.plusMonths(i), date1.plusMonths(1 + i));
				if(m1 > 0)
				dataset.addValue(m1, category.getName(), date1.plusMonths(i).getMonth().toString().substring(0,3));
			}
		}
		return dataset;
	}
	@Override
	public DefaultCategoryDataset createMontlyDataset(LocalDate date, String label){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(int i = 0; i < 12; i++){
			double m1 = Total("D", date.plusMonths(i), date.plusMonths(1 + i));
			dataset.addValue(m1, label, date.plusMonths(i).getMonth().toString().substring(0, 3));
		}
		return dataset;
	}
	
	@Override
	public void createMontlyReport(LocalDate date1, LocalDate date2, String debCred, File file){
		try{

			FileWriter writer = new FileWriter(file);
			writer.write("Report of all monthly moneyflow of type " + debCred + "from the period starting " + date1+ " \r\n");
			writer.write("------------------------------------------------------------------------\r\n");
			
			writer.write("Residence \t\t\t"  + (int)categoryTotal("Residence" ,debCred,date1, date2)/12 + " Nibud number is undefined \r\n"); 

			writer.write("Gas, Water Electricity \t\t" + (int)categoryTotal("Gas, Water Electricity" ,debCred,date1, date2)/12+ "Nibud number is 135\r\n") ;
			writer.write("Insurance \t\t\t" + (int)categoryTotal("Insurance" ,debCred,date1, date2)/12 + "Nibud number is 163\r\n")  ;
			writer.write("Subscriptions & Bubbles\t\t" + (int)categoryTotal("Subscriptions & Bubbles" ,debCred,date1, date2)/12 + "Nibud number is 103 \r\n")  ;
			
			writer.write("Education \t\t\t" + (int)categoryTotal("Education" ,debCred,date1, date2)/12 + "Nibud number is\r\n")  ;
			writer.write("Transport \t\t\t" + (int)categoryTotal("Transport" ,debCred,date1, date2)/12 + "Nibud number is 165 \r\n")   ;
			writer.write("Clothing & Shoes \t\t" + (int)categoryTotal("Clothing & Shoes" ,debCred,date1, date2)/12 + "Nibud number is 56\r\n")   ;
			writer.write("Inventory, home & garden \t" + (int)categoryTotal("Inventory, home & garden" ,debCred,date1, date2)/12 + "Nibud number is 98\r\n") ;
			writer.write("Non-reimbursed healthcare \t" + (int)categoryTotal("Non-reimbursed healthcare costs" ,debCred,date1, date2)/12 + "Nibud number is 44\r\n") ;
			writer.write("Leisure expenses \t\t" + (int)categoryTotal("Leisure expenses" ,debCred,date1, date2)/12 + "Nibud number is 88\r\n") ;
			writer.write("Household expenses \t\t" + (int)categoryTotal("Household expenses" ,debCred,date1, date2)/12 + "Nibud number is 271 \r\n") ;
			writer.write("Other fixed charges \t\t" + (int)categoryTotal("Other fixed charges" ,debCred,date1, date2)/12+ "Nibud number is\r\n\r\n")   ;
			writer.write("Total \t\t\t\t"+ (int)Total(debCred ,date1, date2)/12);
			
			/*
			writer.write("------------------------------------------------------------------------\r\n");
			
			
			for(Category c : bankModel.getCategories()){
				if (
						c.getName().equals("Residence") || 
						c.getName().equals("Gas, Water Electricity") ||
						c.getName().equals("Insurance") || 
						c.getName().equals("Subscriptions & Bubbles") || 
						c.getName().equals("Education") || 
						c.getName().equals("Transport") ||  
						c.getName().equals("Gas, Water Electricity") ||  
						c.getName().equals("Other fixed charges") ||  
						c.getName().equals("Clothing & Shoes") ||  
						c.getName().equals("Inventory, home & garden") ||
						c.getName().equals("Non-reimbursed healthcare costs") ||
						c.getName().equals("Leisure expenses") ||
						c.getName().equals("Household expenses") 
						)   
				{writer.write(c.getName()+ "\t\t" + (int)categoryTotal(c.getName(),debCred,date1, date2)/12);
				}
				writer.write("\r\n");
			}
			writer.write("Total "+ Total(debCred ,date1, date2)/12);
			
			*/
			writer.close();

		} catch(IOException ex) {
			System.out.println("couldn't write the report out");
			ex.printStackTrace();
		}

	}

	
	double categoryTotal(String category, String debcred, LocalDate date1, LocalDate date2){
		double total = 0;
		for(Transaction t: bankModel.getTransactions()){
			if(date1.isEqual(t.getTransactionDate()) || date1.isBefore(t.getTransactionDate()) && date2.isAfter(t.getTransactionDate()))
				if(t.getDebetOrCredit().equals(debcred))
					if(t.getCategory().getName() != null){
						if(t.getCategory().getName().equals(category)){
							total += t.getAmount();
						}}
		}
		return total;
	}
	
	double Total(String debcred, LocalDate date1, LocalDate date2){
		double total = 0;
		for(Transaction t: bankModel.getTransactions()){
			if(date1.isEqual(t.getTransactionDate()) || date1.isBefore(t.getTransactionDate()) && date2.isAfter(t.getTransactionDate()))
				if(t.getDebetOrCredit().equals(debcred))

					total += t.getAmount();


		}
		return total;
	}



	@Override
	public void loadTransactionFile(File file) {
		bankModel.loadTransactionFile(file);
	}

	@Override
	public void loadCategoryFile(File file) {
		bankModel.loadCategoryFile(file);

	}
	@Override
	public void saveState() {
		bankModel.saveState();

	}
	@Override
	public void excludeTransactionFromAnalisys(Transaction t){
		bankModel.getTransactions().remove(t);
		bankModel.getExcludedTransactions().add(t);
		
	}

	@Override
	public ArrayList<Category> getCategories() {
		System.out.println("Controller get category");
		return bankModel.getCategories();
	}




	@Override
	public ArrayList<Transaction> getTransactions() {

		return bankModel.getTransactions();
	}
	@Override
	public String toString(){
		return "controller 1";
	}




	@Override
	public ArrayList<Transaction> searchTransactions(LocalDate date1, LocalDate date2, String category, int minAmount) {
		ArrayList<Transaction> transactionsToBeCategorised = new ArrayList<Transaction>();

		// Creates a local ArrayList temp to list the transactions that match the criteria, and reverses the ordering to biggest amount on index 0 etc.
		int size = bankModel.getTransactions().size();
		for(int i = 1; i < size-1; i++){
			Transaction t = bankModel.getTransactions().get(size-i);
			LocalDate d = bankModel.getTransactions().get(size-i).getTransactionDate();


			if(date1.isEqual(d) || date1.isBefore(d) && date2.isAfter(d))
				if(t.getAmount()> minAmount)
					if(t.getCategory().getName().equals(category)) { //If criteria match transaction t is added to the list to be categorized 
						transactionsToBeCategorised.add(t);          
					}
		}
		return transactionsToBeCategorised;
	}
	
	@Override
	public ArrayList<Transaction> searchTransactions(LocalDate date1, LocalDate date2, String category, int minAmount, String debcred) {
		ArrayList<Transaction> transactionsToBeCategorised = new ArrayList<Transaction>();


		int size = bankModel.getTransactions().size();
		for(int i = 1; i < size-1; i++){
			Transaction t = bankModel.getTransactions().get(size-i);
			LocalDate d = bankModel.getTransactions().get(size-i).getTransactionDate();

			if(date1.isEqual(d) || date1.isBefore(d) && date2.isAfter(d))
				if(t.getAmount()> minAmount)
					if(t.getDebetOrCredit().equals(debcred))
						if(t.getCategory().getName().equals(category)) { 
							transactionsToBeCategorised.add(t);          
						}
		}
		return transactionsToBeCategorised;
	}




	@Override
	public void catogeriseTransaction(Transaction t, String category, String keyword) {
		boolean isCat = false;

		for(Category c : bankModel.getCategories()){
			String tempname = c.getName();

			if(tempname.equalsIgnoreCase(category)){
				isCat = true;
				if(!keyword.equals("")){
					c.getDescriptions().add(keyword);
					System.out.println("Excisting category " + c + " added to transaction, keyword :" +keyword +"added to the category" );
				}
				t.setCategory(c);

				System.out.println("Excisting category " + c + " added to transaction");

				if(!isCat){
					ArrayList<String> names = new ArrayList<>();
					names.add(keyword);
					Category newCat = new Category(category, names);
					bankModel.getCategories().add(newCat);
					t.setCategory(newCat);

					System.out.println("New category " + newCat + " added to transaction. With keyword " + keyword);
				}
				enrichTransactions2();

			}
		}
	}
	
	

	void enrichTransactions2(){
		for(Transaction t: bankModel.getTransactions()){
			for(Category category : bankModel.getCategories()){
				for(String s : category.getDescriptions()){
					if(t.getDescription().contains(s)){
						t.setCategory(category);
					}
				}
			}

		}
	}


	@Override
	public void saveCategories(File file){
		try{

			FileWriter writer = new FileWriter(file);

			for(Category c : bankModel.getCategories()){
				ArrayList<String> tempDisc = c.getDescriptions();
				System.out.println(c);
				writer.write(c.getName()+ "; ");
				if(!tempDisc.isEmpty()){
					for(int i = 0; i < tempDisc.size()-1; i++){
						writer.write(tempDisc.get(i) +"; ");
					}
					writer.write(tempDisc.get(tempDisc.size()-1));

				}
				writer.write("\r\n");
			}
			writer.close();
		} catch(IOException ex) {
			System.out.println("couldn't write the categoryList out");
			ex.printStackTrace();
		}
	}



	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
