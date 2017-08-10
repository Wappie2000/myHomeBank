package flexibleHomeBank;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;





public class HomeBank implements Serializable {



	public static void main (String[] args){
		HomeBank homeBank = new HomeBank();

		File file = new File("HBcurrentstate.ser");
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));

			homeBank = (HomeBank) is.readObject();



		} catch (Exception ex) {
			ex.printStackTrace();
		}



		homeBank.buildGui();
	}





	public HomeBank(ArrayList<Transaction> transactions) {
		this.transactions = transactions;
	}

	public HomeBank() {
	}

	private static final long serialVersionUID = 1L;
	// IV's input
	ArrayList<String> rawInput;
	ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	ArrayList<Transaction> excludedTransactions = new ArrayList<Transaction>();
	ArrayList<Category> categoryCollection = new ArrayList<Category>();


	// GUI IV's
	private JFrame frame;




	//temp IV's
	ArrayList<Transaction> transactionsToBeCategorised;

	//build GUI
	void buildGui(){
		frame = new JFrame("My new HomeBank");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(800, 600);


		// Creating the menu-bar with the menu items
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem saveMenuItem = new JMenuItem("Save State");
		saveMenuItem.addActionListener(e-> saveState());
		fileMenu.add(saveMenuItem);
		
		JMenuItem loadMenuItem = new JMenuItem("Load transaction file");
		loadMenuItem.addActionListener(new OpenMenuListener());
		fileMenu.add(loadMenuItem);

		JMenuItem loadcatMenuItem = new JMenuItem("Load Categories");
		loadcatMenuItem.addActionListener(e-> loadCategories());
		fileMenu.add(loadcatMenuItem);

		JMenuItem savecatMenuItem = new JMenuItem("Save Categories");
		savecatMenuItem.addActionListener(e-> saveCategories());
		fileMenu.add(savecatMenuItem);

		

		

		
		
		JMenuItem clearcatItem = new JMenuItem("Clear Categories");
		clearcatItem.addActionListener(e-> clearCategories());
		fileMenu.add(clearcatItem);
		
		JMenuItem cleartranItem = new JMenuItem("Clear Transactions");
		cleartranItem.addActionListener(e-> clearTransactions());
		fileMenu.add(cleartranItem);
		
		JMenuItem printTItem = new JMenuItem("Print T");
		printTItem.addActionListener(e-> printTrans());
		fileMenu.add(printTItem);
		
		JMenuItem printMenuItem = new JMenuItem("Print cat");
		printMenuItem.addActionListener(e-> printCat());
		fileMenu.add(printMenuItem);
		
		
		
		


		JMenu editMenu = new JMenu("Edit");
		JMenuItem enrichMenuItem = new JMenuItem("Exclude high amounts");
		enrichMenuItem.addActionListener(e-> excludeHighAmounts());
		editMenu.add(enrichMenuItem);
		JMenuItem sortMenuItem = new JMenuItem("Sort");
		sortMenuItem.addActionListener(e-> sortTransactions());
		editMenu.add(sortMenuItem);
		
		JMenuItem richMenuItem = new JMenuItem("Enrich Data");
		richMenuItem.addActionListener(e-> enrichTransactions());
		editMenu.add(richMenuItem);

		JMenuItem searchMenuItem = new JMenuItem("Search");
		searchMenuItem.addActionListener(e-> {
			try {
				searchTransactionsGui();
			} catch (NumberFormatException e1) {

				e1.printStackTrace();
			} catch (BadLocationException e1) {

				e1.printStackTrace();
			}
		});
		editMenu.add(searchMenuItem);

		JMenu graphMenu = new JMenu("Graph");
		JMenuItem buildPieChartMenuItem = new JMenuItem("Build Pie Chart");
		buildPieChartMenuItem.addActionListener(e-> buildPieChart());
		graphMenu.add(buildPieChartMenuItem);
		JMenuItem buildBarChartMenuItem = new JMenuItem("Build Bar Chart");
		buildBarChartMenuItem.addActionListener(e-> buildBarChart());
		graphMenu.add(buildBarChartMenuItem);

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(graphMenu);

		frame.setJMenuBar(menuBar);
		frame.setSize(640,500);
		frame.setVisible(true);
	}

	void buildPieChart(){
		DefaultPieDataset data = new DefaultPieDataset();

		int total=0;
		for(Category c: categoryCollection){
			if(!c.name.equals("Salary")){
				total += categoryTotal(c.name, "D");
			}
		}

		for(Category c: categoryCollection){
			int catTotal =0;
			if( !c.name.equals("Salary")){
				catTotal += categoryTotal(c.name, "D");
			
			int catPerc = 100*catTotal/total;
			data.setValue(c.name+ catPerc+ " %", catTotal );
			}
		}




		JFreeChart chart = ChartFactory.createPieChart("Total expenses per catagory", data);

		ChartPanel chartPanel = new ChartPanel(chart);

		frame.setContentPane(chartPanel);
		frame.setSize(624,500);



	}

	void buildBarChart(){

		DefaultCategoryDataset data = createMontlyDataset(LocalDate.of(2016,4,1), "Rent", "Rent");
		addMonthlyDataset(LocalDate.of(2016,4,1), data, "Total");

		for(Category c: categoryCollection){
			if(!c.name.equals("Rent")&& !c.name.equals("Salary")){
				addMonthlyDataset(LocalDate.of(2016,4,1), data, c.name, c.name);
			}
		}




		JFreeChart chart = ChartFactory.createBarChart("Monthly expenses", null, "Eur", data);
		ChartPanel chartPanel = new ChartPanel(chart);
		frame.setContentPane(chartPanel);
		frame.setSize(626,500);
	}


	// Helper functions for the bar chart
	public DefaultCategoryDataset createMontlyDataset(LocalDate date, String label){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(int i = 0; i < 12; i++){
			double m1 = categoryTotal("D", date.plusMonths(i), date.plusMonths(1 + i));
			dataset.addValue(m1, label, date.plusMonths(i).getMonth().toString().substring(0, 3));
		}
		return dataset;
	}
	public DefaultCategoryDataset createMontlyDataset(LocalDate date, String label, String category){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for(int i = 0; i < 12; i++){
			double m1 = categoryTotal(category, "D", date.plusMonths(i), date.plusMonths(1 + i));
			dataset.addValue(m1, label, date.plusMonths(i).getMonth().toString().substring(0,3));
		}
		return dataset;
	}
	public void addMonthlyDataset(LocalDate date ,DefaultCategoryDataset data, String label ){
		for(int i = 0; i < 12; i++){
			double m1 = categoryTotal("D", date.plusMonths(i), date.plusMonths(1 + i));
			data.addValue(m1, label, date.plusMonths(i).getMonth().toString().substring(0,3));
		}
	}
	public void addMonthlyDataset(LocalDate date ,DefaultCategoryDataset data, String label, String category ){
		for(int i = 0; i < 12; i++){
			double m1 = categoryTotal(category, "D", date.plusMonths(i), date.plusMonths(1 + i));
			data.addValue(m1, label, date.plusMonths(i).getMonth().toString().substring(0, 3));
		}
	}




	//Listener actions
	public class OpenMenuListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileOpen = new JFileChooser();
			fileOpen.showOpenDialog(frame);
			loadTransactionFile(fileOpen.getSelectedFile());
			System.out.println(fileOpen.getSelectedFile());
		}
	}


	void loadCategories(){
		JFileChooser fileOpen = new JFileChooser();
		fileOpen.showOpenDialog(frame);
		loadCategoryFile(fileOpen.getSelectedFile());
		System.out.println(fileOpen.getSelectedFile());

	}

	void loadCategoryFile(File file){
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
			categoryCollection.add(new Category(lineSeperated[0], keywords));

		}
	}


	private void loadTransactionFile(File file){
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
		enrichTransactions();

	} 

	void excludeHighAmounts(){
		ArrayList<Transaction> temporary = new ArrayList<>();
		for(int i = 0; i < transactions.size(); i++ ){
			if(transactions.get(i).amount > 1490){
				excludedTransactions.add(transactions.get(i));
			} else {
				temporary.add(transactions.get(i));
			}
		}
		transactions = temporary;
	}

	void enrichTransactions(){
		for(Transaction t: transactions){
			t.category = compareDecriptionToLists(t, categoryCollection);

		}
	}

	void enrichTransactions2(){
		for(Transaction t: transactions){


			for(Category category : categoryCollection){
				for(String s : category.descriptions){
					if(t.description.contains(s)){
						t.category = category;
					}
				}
			}

		}
	}

	void searchTransactionsGui() throws NumberFormatException, BadLocationException{
		JFrame frame = new JFrame();
		frame.setSize(250, 200);

		JLabel label1 = new JLabel("Type start date");
		JLabel label2 = new JLabel("Type end date");
		JLabel label3 = new JLabel("Type category");
		JLabel label4 = new JLabel("Type min amount");

		JTextField field1 = new JTextField("YYYY-MM-DD");
		JTextField field2 = new JTextField("YYYY-MM-DD");
		JTextField field3 = new JTextField("not yet assigned");
		JTextField field4 = new JTextField("400");

		Box group1 = Box.createHorizontalBox();
		Box group2 = Box.createHorizontalBox();
		Box group3 = Box.createHorizontalBox();
		Box group4 = Box.createHorizontalBox();

		group1.add(label1);
		group1.add(field1);
		group2.add(label2);
		group2.add(field2);
		group3.add(label3);
		group3.add(field3);
		group4.add(label4);
		group4.add(field4);

		JButton button = new JButton("Apply");


		button.addActionListener(e-> {
			try {
				searchTransactions(  // there might be a problem here, not als date get parsed well
						LocalDate.of(Integer.parseInt(field1.getText(0, 4)), Integer.parseInt(field1.getText(5, 2)), Integer.parseInt(field1.getText(8, 2))),
						LocalDate.of(Integer.parseInt(field2.getText(0, 4)), Integer.parseInt(field2.getText(5, 2)), Integer.parseInt(field2.getText(8, 2))),
						field3.getText(),
						Integer.parseInt(field4.getText())
						);
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		}
				);

		Box totalgroup = Box.createVerticalBox();
		totalgroup.add(group1);
		totalgroup.add(group2);
		totalgroup.add(group3);
		totalgroup.add(group4);
		totalgroup.add(button);

		frame.add(totalgroup);
		frame.setLocationRelativeTo(frame);
		frame.setVisible(true);

	}


	void searchTransactions(LocalDate date1, LocalDate date2, String Category, int minAmount){
		showHighest(date1, date2, "D", Category, minAmount );

	}

	Category compareDecriptionToLists(Transaction t, ArrayList<Category> categoryCollection){
		Category categoryToRetrun = new Category("not yet assigned", new ArrayList<String>());
		for(Category category : categoryCollection){
			for(String s : category.descriptions){
				if(t.description.contains(s)){
					categoryToRetrun = category;
				}
			}
		}
		return categoryToRetrun;
	}


	double categoryTotal(String category, String debcred){
		double total = 0;
		for(Transaction t: transactions){
			if(t.debetOrCredit.equals(debcred))
				if(t.category.name != null){
					if(t.category.name.equals(category)){
						total += t.amount;
					}}

		}
		return total;
	}

	double categoryTotal(String category, String debcred, LocalDate date1, LocalDate date2){
		double total = 0;
		for(Transaction t: transactions){
			if(date1.isEqual(t.transactionDate) || date1.isBefore(t.transactionDate) && date2.isAfter(t.transactionDate))
				if(t.debetOrCredit.equals(debcred))
					if(t.category.name != null){
						if(t.category.name.equals(category)){
							total += t.amount;
						}}

		}
		return total;
	}
	double categoryTotal(String debcred, LocalDate date1, LocalDate date2){
		double total = 0;
		for(Transaction t: transactions){
			if(date1.isEqual(t.transactionDate) || date1.isBefore(t.transactionDate) && date2.isAfter(t.transactionDate))
				if(t.debetOrCredit.equals(debcred))
					total += t.amount;
		}
		return total;
	}

	void loadState(){
		HomeBank homeBank = new HomeBank();
		JFileChooser fileOpen = new JFileChooser();
		fileOpen.showOpenDialog(frame);

		System.out.println(fileOpen.getSelectedFile());

		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileOpen.getSelectedFile()));

			homeBank = (HomeBank) is.readObject();



		} catch (Exception ex) {
			ex.printStackTrace();
		}


	}

	void saveState(){
		try {
			FileOutputStream fs = new FileOutputStream("HBcurrentstate.ser"); // het lukt niet om het in een exejar goed te krijgen.
			ObjectOutputStream os = new ObjectOutputStream(fs);
			System.out.println("writint transactions, beginning with" + transactions.get(0));
			os.writeObject(this);
			os.close();
		} catch(Exception e)
		{e.printStackTrace();
		}
	}

	void saveCategories(){
		try{
			FileWriter writer = new FileWriter("Categories.txt");

			for(Category c : categoryCollection){


				writer.write(c.name+ "; ");
				for(int i = 0; i < c.descriptions.size()-1; i++){
					writer.write(c.descriptions.get(i) +"; ");
				}
				writer.write(c.descriptions.get(c.descriptions.size()-1));
				writer.write("\r\n");
			}



			writer.close();


		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void sortTransactions(){
		Collections.sort(transactions);
		for(int i = 1; i < 6; i++){
			System.out.println(transactions.get(transactions.size()-i));
		}

	}


	private void print(LocalDate date1, LocalDate date2, String debcred, String category){
		for(Transaction t: transactions){
			if(date1.isEqual(t.transactionDate) || date1.isBefore(t.transactionDate) && date2.isAfter(t.transactionDate))
				if(t.debetOrCredit.equals(debcred))
					if(t.category.name.equals(category)) {
						System.out.println(t);
					}
		}
	}

	private void printHighest(LocalDate date1, LocalDate date2, String debcred, String category, int minAmount){
		for(int i = 1; i < transactions.size()-1; i++){
			LocalDate d = transactions.get(transactions.size()-i).transactionDate;
			Transaction t = transactions.get(transactions.size()-i);

			if(date1.isEqual(d) || date1.isBefore(d) && date2.isAfter(d))
				if(t.debetOrCredit.equals(debcred))
					if(t.amount> minAmount)
						if(t.category.name.equals(category)) {
							System.out.println(t);
						}
		}
	}

	private void showHighest(LocalDate date1, LocalDate date2, String debcred, String category, int minAmount){
		JLabel label1 = new JLabel("Transaction 1");


		JLabel label2 = new JLabel("Transaction 2");
		JLabel label3 = new JLabel("Transaction 3");
		JLabel label4 = new JLabel("No active transaction");
		JLabel label42 = new JLabel("No active transaction");

		Dimension fieldD = new Dimension(350,60);

		JTextArea area1 = new JTextArea();
		area1.setLineWrap(true);
		area1.setMaximumSize(fieldD);
		JTextArea area2 = new JTextArea();
		area2.setLineWrap(true);
		area2.setMaximumSize(fieldD);
		JTextArea area3 = new JTextArea();
		area3.setLineWrap(true);
		area3.setMaximumSize(fieldD);

		Dimension ansD = new Dimension(150,60);
		JTextArea area4 = new JTextArea("Type text here");
		area4.setLineWrap(true);
		area4.setMinimumSize(ansD);
		area4.setMaximumSize(ansD);

		JTextArea area5 = new JTextArea("Type text here");
		area5.setLineWrap(true);
		area5.setMaximumSize(ansD);
		area5.setMinimumSize(ansD);

		JButton button1 = new JButton("Set Category");
		JButton button2 = new JButton("Set Category");
		JButton button3 = new JButton("Set Category");

		Box group1 = Box.createHorizontalBox();
		Box group2 = Box.createHorizontalBox();
		Box group3 = Box.createHorizontalBox();
		Box group4 = Box.createHorizontalBox();

		group1.add(label1);
		group1.add(area1);
		group1.add(button1);

		group2.add(label2);
		group2.add(area2);
		group2.add(button2);

		group3.add(label3);
		group3.add(area3);
		group3.add(button3);

		group4.add(label4);
		group4.add(area4);
		group4.add(label42);
		group4.add(area5);

		Box totalgroup = Box.createVerticalBox();
		totalgroup.add(group1);
		totalgroup.add(group2);
		totalgroup.add(group3);


		button1.addActionListener(e->
		{
			JFrame frame = new JFrame("Categorise Transaction 1");
			frame.setSize(600, 200);
			frame.setLocationRelativeTo(frame);
			frame.setLayout(new BorderLayout());
			JLabel label1a = new JLabel("Type category");
			JLabel label1a2 = new JLabel("Type/paste keyword");
			JTextArea area6 = new JTextArea("Type text here");
			area6.setLineWrap(true);
			area6.setMinimumSize(ansD);
			area6.setMaximumSize(ansD);
			JTextArea area7 = new JTextArea("Type text here(optional)");
			area7.setLineWrap(true);
			area7.setMaximumSize(ansD);
			area7.setMinimumSize(ansD);
			Box group4a = Box.createHorizontalBox();
			group4a.add(label1a);
			group4a.add(area6);
			group4a.add(label1a2);
			group4a.add(area7);
			JButton buttonAp = new JButton("Apply");
			buttonAp.addActionListener(ex->
			{
				catogeriseTransaction(1, area6.getText(),area7.getText());

				frame.dispose() ;
			}

					);


			frame.add(group4a, BorderLayout.CENTER);
			frame.add(buttonAp, BorderLayout.SOUTH);
			frame.setVisible(true);

		}
				);
		button2.addActionListener(
				e->
				{
					JFrame frame = new JFrame("Categorise Transaction 2");
					frame.setSize(600, 200);
					frame.setLocationRelativeTo(frame);
					frame.setLayout(new BorderLayout());
					JLabel label1a = new JLabel("Type category");
					JLabel label1a2 = new JLabel("Type/paste keyword");
					JTextArea area6 = new JTextArea("Type text here");
					area6.setLineWrap(true);
					area6.setMinimumSize(ansD);
					area6.setMaximumSize(ansD);
					JTextArea area7 = new JTextArea("Type text here(optional)");
					area7.setLineWrap(true);
					area7.setMaximumSize(ansD);
					area7.setMinimumSize(ansD);
					Box group4a = Box.createHorizontalBox();
					group4a.add(label1a);
					group4a.add(area6);
					group4a.add(label1a2);
					group4a.add(area7);
					JButton buttonAp = new JButton("Apply");
					buttonAp.addActionListener(ex->
					{
						catogeriseTransaction(1, area6.getText(),area7.getText());
						frame.dispose() ;
					}
							);
					frame.add(group4a, BorderLayout.CENTER);
					frame.add(buttonAp, BorderLayout.SOUTH);
					frame.setVisible(true);
				});
		button3.addActionListener(
				e->
				{
					JFrame frame = new JFrame("Categorise Transaction 3");
					frame.setSize(600, 200);
					frame.setLocationRelativeTo(frame);
					frame.setLayout(new BorderLayout());
					JLabel label1a = new JLabel("Type category");
					JLabel label1a2 = new JLabel("Type/paste keyword");
					JTextArea area6 = new JTextArea("Type text here");
					area6.setLineWrap(true);
					area6.setMinimumSize(ansD);
					area6.setMaximumSize(ansD);
					JTextArea area7 = new JTextArea("Type text here(optional)");
					area7.setLineWrap(true);
					area7.setMaximumSize(ansD);
					area7.setMinimumSize(ansD);
					Box group4a = Box.createHorizontalBox();
					group4a.add(label1a);
					group4a.add(area6);
					group4a.add(label1a2);
					group4a.add(area7);
					JButton buttonAp = new JButton("Apply");
					buttonAp.addActionListener(ex->
					{
						catogeriseTransaction(1, area6.getText(),area7.getText());
						frame.dispose() ;
					}
							);
					frame.add(group4a, BorderLayout.CENTER);
					frame.add(buttonAp, BorderLayout.SOUTH);
					frame.setVisible(true);
				});

		frame.setContentPane(totalgroup);

		transactionsToBeCategorised = new ArrayList<Transaction>();

		// Creates a local ArrayList temp to list the transactions that match the criterea, and reverses the ordering to biggest amount on index 0 etc.
		ArrayList<String> temp = new ArrayList<>();
		for(int i = 1; i < transactions.size()-1; i++){
			LocalDate d = transactions.get(transactions.size()-i).transactionDate;
			Transaction t = transactions.get(transactions.size()-i);

			if(date1.isEqual(d) || date1.isBefore(d) && date2.isAfter(d))
				if(t.debetOrCredit.equals(debcred))
					if(t.amount> minAmount)
						if(t.category.name.equals(category)) { //Transaction to be categorized is added to the checklist and to a list that presents it in strings in the GUI
							temp.add("Date : " + d + " Amount: " + t.amount+ " Description: " + t.description);
							transactionsToBeCategorised.add(t);
							System.out.println("Category " +t.category + " maches " + t );
						}
		}
		// sets the transactions in the text fields preventing index out of bounds
		if(temp.size()>=3){
			area1.setText(temp.get(0));
			area2.setText(temp.get(1));
			area3.setText(temp.get(2));
		} else if (temp.size()>=2) {
			area1.setText(temp.get(0));
			area2.setText(temp.get(1));
		} else if (temp.size()>=1) {
			area1.setText(temp.get(0));
		}

		frame.add(totalgroup);
		frame.setSize(627,500);
	}




	private void print(LocalDate date1, LocalDate date2, String debcred){
		for(Transaction t: transactions){
			if(date1.isEqual(t.transactionDate) || date1.isBefore(t.transactionDate) && date2.isAfter(t.transactionDate))
				if(t.debetOrCredit.equals(debcred))
					System.out.println(t);
		}
	}

	private void printTrans(){
		for(Transaction t: transactions){
		
					System.out.println(t);
		}
	}
	
	void catogeriseTransaction(int tNumber, String category, String keyword){

		boolean isCat = false;
		for(Category c : categoryCollection){
			String tempname = c.name;
			System.out.println(tempname + "checked against" + category);
			if(tempname.equalsIgnoreCase(category)){


				isCat = true;
				if(!keyword.equals("Type text here(optional)")){
					c.descriptions.add(keyword);
					System.out.println("Excisting category " + c + " added to transaction, keyword :" +keyword +"added to the category" );
				}
				transactionsToBeCategorised.get(tNumber-1).category = c;
				System.out.println("Excisting category " + c + " added to transaction" + transactionsToBeCategorised.get(tNumber-1));
			}
		}


		if(!isCat){
			ArrayList<String> names = new ArrayList<>();
			names.add(keyword);
			Category newCat = new Category(category, names);
			categoryCollection.add(newCat);
			transactionsToBeCategorised.get(tNumber-1).category = newCat;
			System.out.println("new category " + transactionsToBeCategorised.get(tNumber-1).category + " added to transaction" + transactionsToBeCategorised.get(tNumber-1) );

		}
		enrichTransactions2();
	}


	void printCat(){
		for(Category c: categoryCollection){
			System.out.println("Category " +c + "amounts"+categoryTotal(c.name, "D")  +"has the following keywords: ");
			for(String name : c.descriptions){
				System.out.println(name);
			}
		}
	}

	void clearCategories(){
		categoryCollection = new ArrayList<Category>();
		for(Transaction t: transactions){
			t.category = new Category("not yet assigned", new ArrayList<String>());
		}
	}
	;
	void clearTransactions(){
		transactions = new ArrayList<Transaction>();
	};



}
