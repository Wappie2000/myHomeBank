package controller;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import model.Category;
import model.Transaction;

public interface BankControllerInterface {

	
	void loadCategoryFile(File file);
	void loadTransactionFile(File file);
	void saveState();
	
	public ArrayList<Category> getCategories();
	public ArrayList<Transaction> getTransactions();
	
	public ArrayList<Transaction> searchTransactions(LocalDate date1, LocalDate date2, String Category, int minAmount);
	void catogeriseTransaction(Transaction t, String category, String keyword);
	
	String toString();
	
	ArrayList<Transaction> searchTransactions(LocalDate date1, LocalDate date2, String category, int minAmount,
			String debcred);
	void excludeTransactionFromAnalisys(Transaction t);
	DefaultPieDataset createPieDataset(LocalDate date1, LocalDate date2, String debCred);
	
	void saveCategories(File file);
	DefaultCategoryDataset createMontlyDataset(LocalDate date, String label);
	
	
	DefaultCategoryDataset createMontlyDataset(LocalDate date1, LocalDate date2, String debCred);

	void createMontlyReport(LocalDate date1, LocalDate date2, String debCred, File file);
}
