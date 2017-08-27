package view.FXML;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.ResourceBundle;

import controller.*;
import model.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;


public class MainController implements Initializable {


	BankControllerInterface controller;

	public String debetOrCredit = "D";
	boolean undoPanelOn = false;

	//MenuBar Elements
	@FXML public MenuItem close;
	@FXML public MenuItem save;



	//TransactionTable elements
	public ToggleGroup tgDebCred;
	ObservableList<Transaction> list = FXCollections.observableArrayList();
	String categoryNameForTransactionTable = "not yet assigned";
	
	String searchedKeyWord;
	int searchedKeywordRow;
	String searchedAccount;
	int searchedAccountRow;
	Rule searchedRule;
	int searchedRuleRow;

	@FXML public TableView<Transaction> transactionTable;
	@FXML public TableColumn<Transaction, String> date;
	@FXML public TableColumn<Transaction, Number> amount;
	@FXML public TableColumn<Transaction, String> discription;
	@FXML public TableColumn<Transaction, String> otherAccount;
	@FXML public ToggleButton credit;
	@FXML public ToggleButton debet;



	//Rule panel elements
	@FXML public TextArea txtKeyword;
	@FXML public TextField txtMinAmount;
	@FXML public TextField txtMaxAmount;
	@FXML public DatePicker fromDate;
	@FXML public DatePicker untillDate;
	@FXML public TextField txtCounterAccount;
	@FXML public Button btnApplyIncludingRules;
	@FXML public Button btnApplyOnlyCategory;
	@FXML public CheckBox chcKeyWord;
	@FXML public CheckBox chcAmount;
	@FXML public CheckBox chcDate;
	@FXML public CheckBox chcAccount;
	@FXML public Button btnUndoRule;
	@FXML public Button btnBack;
	@FXML public Button btnShowRule;
	@FXML public Button btnDeleteRule;



	//CategoryTable elements
	@FXML public TreeTableView<Category> categoryTable;
	@FXML public TreeTableColumn<Category,String> collumn1;
	@FXML public TreeTableColumn<Category,Number> collumn2;
	@FXML public Button btnExclude;
	@FXML public ProgressBar progressBar;
	@FXML public Label lblProgressPercentage;

	TreeItem<Category> root;




	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initModelAndController();

		//Set Category table
		collumn1.setCellValueFactory(   
				(TreeTableColumn.CellDataFeatures<Category, String> param)   -> param.getValue().getValue().getSimpleName());
		//collumn2.setCellValueFactory(   
		//		(TreeTableColumn.CellDataFeatures<Category, Number> param)   -> param.getValue().getValue().getAmount());
		refreshCategoryTable();

		//Set Transaction table
		date.setCellValueFactory(
				(TableColumn.CellDataFeatures<Transaction, String> param)   -> param.getValue().getSimpleTransactionDate()
				);
		amount.setCellValueFactory(
				(TableColumn.CellDataFeatures<Transaction, Number> param)   -> param.getValue().getSimpleAmount()
				);
		discription.setCellValueFactory(
				(TableColumn.CellDataFeatures<Transaction, String> param)   -> param.getValue().getSimpleDescription()
				);
		otherAccount.setCellValueFactory(
				(TableColumn.CellDataFeatures<Transaction, String> param)   -> param.getValue().getSimpleOtherEndOfTransactionAccount()
				);

		refreshList();
		refreshProgressBar();
		initDateValues();

		tgDebCred = new ToggleGroup();
		tgDebCred.getToggles().add(credit);
		tgDebCred.getToggles().add(debet);

		btnUndoRule.setVisible(false);
		btnBack.setVisible(false);
		btnShowRule.setVisible(false);
		btnDeleteRule.setVisible(false);

	}


	// the main (re)initialize functions
	private void refreshCategoryTable() {
		System.out.println("refreshing");
		if(debetOrCredit=="D"){
			root= new TreeItem<>(new Category("Uitgaven"));
		} else {
			root= new TreeItem<>(new Category("Inkomsten"));
		}
		System.out.println("Debet or credit is" + debetOrCredit );

		for(Category c : controller.getCategories()){
			try {
				if(c.getdebetOrCredit().equals(debetOrCredit)){
					root.getChildren().add( new TreeItem<>(c));
					System.out.println("category added to tree" + c );
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Category has no debcred " + c.getName());
				e.printStackTrace();
			}
		}
		categoryTable.setRoot(root);
		root.setExpanded(true);

	}
	public void refreshList(){
		list.clear();
		for (Transaction t: controller.searchTransactions(LocalDate.MIN, LocalDate.now(), categoryNameForTransactionTable, 0, debetOrCredit)  ){

			list.add(t);
			transactionTable.setItems(list);
		}
		transactionTable.getSortOrder().add(amount);
	}
	public void refreshProgressBar(){
		float tempTotalUnassignedAmount = controller.categoryTotal("not yet assigned", debetOrCredit);
		float tempTotal = controller.generalTotal(debetOrCredit);
		double progress = (double) 1-tempTotalUnassignedAmount/tempTotal;
		progressBar.setProgress(progress);
		System.out.println(tempTotalUnassignedAmount);
		System.out.println(tempTotal);
		System.out.println(progress);
		int prog = (int) (progress*100);
		System.out.println(prog);

		String progS = Integer.toString(prog);
		System.out.println(progS);
		lblProgressPercentage.setText(progS+ " %");
	}



	private void initModelAndController(){
		/* The initiation of the GUI in view.FXML/Main starts up the Main FXML file which start this 
		 * class and automatically calls the initialize method. In this method the initiation
		 * of the rest of the model is handled,
		 * The BankModel and Controller as defined in packages model, controller are initiated and coupled below
		 */
		BankModelInterface model = new BankModel();
		File file = new File("HBcurrentstate.ser");
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
			model = (BankModel) is.readObject();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		controller = new BankController1(model);
	}


	public void transactionSelected(){
		if(!undoPanelOn){
	
		txtKeyword.setText(transactionTable.getSelectionModel().getSelectedItem().getDescription());
		txtCounterAccount.setText(transactionTable.getSelectionModel().getSelectedItem().getOtherEndOfTransactionName());
		}
		chcKeyWord.setSelected(false);
		chcAccount.setSelected(false);
	}









	public void applyIncludingRules(ActionEvent event){
		/* There are 4 fields that can be used as a rule (keyword, counter account, date, min/max) in all combinations,
		 * to automatically give a category to a transaction. However to dat and min max are almost never used, and using rules 
		 * makes te programm slow really fast. Therefore to to most commen "rules" can also be added stand alone, so that 
		 * searching can be done much faster. 
		 * Rules are of the "and" type all requirements have to be met, where to standalone requirements are of the "or" type
		 * if in one case both keyword and account are checked then it is assumed to be that the user wnats an "and" 
		 * requirement, and so it becomes a rule. 
		 * This is programmed below 
		 */


		System.out.println(chcAccount.isSelected());
		System.out.println(chcAmount.isSelected());
		System.out.println(chcDate.isSelected());
		System.out.println(chcKeyWord.isSelected());


		if(chcAmount.isSelected()||chcDate.isSelected() || (chcKeyWord.isSelected()&& chcAccount.isSelected())){
			Rule r = new Rule();
			if(chcKeyWord.isSelected()){
				r.setKeyword(txtKeyword.getText().trim());
			}
			if(chcAccount.isSelected()){
				r.setCounterAcccountDiscription(txtCounterAccount.getText().trim());
			}
			if(chcDate.isSelected()){
				r.setStartDate(fromDate.getValue());
				r.setEndDate(untillDate.getValue());
			}
			if(chcAmount.isSelected()){
				r.setMinAmount(Float.parseFloat(txtMinAmount.getText()));
				r.setMaxAmount(Float.parseFloat(txtMaxAmount.getText()));
			}
			String categoryName = categoryTable.getSelectionModel().getSelectedItem().getValue().getName();
			controller.addRuleToCategory(categoryName, r);
		}

		Transaction transaction = transactionTable.getSelectionModel().getSelectedItem();
		String category = categoryTable.getSelectionModel().getSelectedItem().getValue().getName();
		String keyword = txtKeyword.getText();
		String account = txtCounterAccount.getText();

		if(chcKeyWord.isSelected()&& !chcAccount.isSelected()){
			controller.catogeriseTransaction(transaction, category, keyword);
		} else {
			transaction.setCategory(categoryTable.getSelectionModel().getSelectedItem().getValue());
		}

		if(!chcKeyWord.isSelected()&& chcAccount.isSelected()){
			controller.catogeriseTransactionAccount(transaction, category, account);
		} else {
			transaction.setCategory(categoryTable.getSelectionModel().getSelectedItem().getValue());
		}

		refreshList();
		refreshProgressBar();
		chcAccount.setSelected(false);
		chcKeyWord.setSelected(false);
		chcDate.setSelected(false);
		chcAmount.setSelected(false);
		
	}



	public void applyOnlyCategory(ActionEvent event){
		// this is more or less unnassesary, if no checkbox is checked, it is category only by default
		Transaction transaction = transactionTable.getSelectionModel().getSelectedItem();
		transaction.setCategory(categoryTable.getSelectionModel().getSelectedItem().getValue());
		refreshList();
		refreshProgressBar();
	}


	public void setCategoryNameForTransactionTable(){
		categoryNameForTransactionTable = categoryTable.getSelectionModel().getSelectedItem().getValue().getName();
	
		
		btnUndoRule.setVisible(true);
		btnShowRule.setVisible(true);
		btnBack.setVisible(true);
		btnDeleteRule.setVisible(true);
		undoPanelOn = true;
		txtKeyword.setText("");
		txtCounterAccount.setText("");
		
		refreshList();
	}
	
	public void showRule(){
		txtKeyword.setText("");
		chcKeyWord.setSelected(false);
		txtCounterAccount.setText("");
		chcAccount.setSelected(false);
		findAppliccableRule();
		
		
		if(findAppliccableKeyword()){
			txtKeyword.setText(searchedKeyWord);
			chcKeyWord.setSelected(true);
		}
		if(findAppliccableAccount()){
			txtCounterAccount.setText(searchedAccount);
			chcAccount.setSelected(true);
		}
		if(findAppliccableRule()){
			txtCounterAccount.setText(searchedRule.getCounterAcccountDiscription());
			chcAccount.setSelected(true);
		}
	}
	public void deleteRule(){
		if(findAppliccableRule()){
			categoryTable.getSelectionModel().getSelectedItem().getValue().getRules().remove(0);
		}
		if(chcAccount.isSelected()){
			System.out.println(categoryTable.getSelectionModel().getSelectedItem().getValue().getCounterAccounts());
			categoryTable.getSelectionModel().getSelectedItem().getValue().getCounterAccounts().remove(txtCounterAccount.getText());
			System.out.println(categoryTable.getSelectionModel().getSelectedItem().getValue().getCounterAccounts());
		}
		if(findAppliccableKeyword()){
			System.out.println(categoryTable.getSelectionModel().getSelectedItem().getValue().getKeyWords());
			categoryTable.getSelectionModel().getSelectedItem().getValue().getKeyWords().remove(searchedKeywordRow);
			System.out.println("Keyword should be removed row nr " +searchedKeywordRow);
			System.out.println(categoryTable.getSelectionModel().getSelectedItem().getValue().getKeyWords());
		}
		if(findAppliccableAccount()){
			categoryTable.getSelectionModel().getSelectedItem().getValue().getCounterAccounts().remove(searchedAccountRow);
			
		}
		
	}
	


	public boolean findAppliccableKeyword(){
	
		boolean temp = false;
		for(Category c : controller.getCategories()){
			for(String s : c.getKeyWords()){
				if(transactionTable.getSelectionModel().getSelectedItem().getDescription().contains(s)){
					System.out.println("Found apllicable keyword");
					searchedKeyWord = s;
					searchedKeywordRow = c.getKeyWords().indexOf(s);
					temp = true;
				}
			}
		}
		return temp;
	}

	public boolean findAppliccableAccount(){
		boolean temp = false;
		for(Category c : controller.getCategories()){
			for(String s : c.getCounterAccounts()){
				if(transactionTable.getSelectionModel().getSelectedItem().getOtherEndOfTransactionName().contains(s)){
					System.out.println("Found apllicable account");
					searchedAccount = s;
					temp = true;
				} 
			}
		}
		return temp;
	}
	public boolean findAppliccableRule(){
		boolean temp = false;
		for(Category c : controller.getCategories()){
			try {
			if(c.getName().equals(transactionTable.getSelectionModel().getSelectedItem().getCategory().getName())){
			
					if(!(c.getRules().get(0).getCounterAcccountDiscription()==null)){
						System.out.println(c.getRules().get(0).getCounterAcccountDiscription());
						searchedRule = c.getRules().get(0);
						temp = true;
					}
					
					
					System.out.println("Advanced rule in this category");
					
					System.out.println(c.getRules().get(0).getKeyword());
					System.out.println(c.getRules().get(0).getMaxAmount());
					System.out.println(c.getRules().get(0).getStartDate());
					System.out.println(c.getRules().toString());
					System.out.println("Advanced rule in this category");
				} 
				}catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("General");
			
			}
		}
		return temp;
	}


	public void chkKeywordSelected(ActionEvent event){
	}
	public void setRulePanelNormal(){
		undoPanelOn = false;
		categoryNameForTransactionTable = "not yet assigned";
		btnUndoRule.setVisible(false);
		btnBack.setVisible(false);
		btnShowRule.setVisible(false);
		btnDeleteRule.setVisible(false);
		refreshList();
	}
	public void excludeFromAnalisys(ActionEvent event){
		controller.excludeTransactionFromAnalisys(transactionTable.getSelectionModel().getSelectedItem());
		refreshList();
		refreshProgressBar();
	}
	public void close(ActionEvent event){

		controller.saveState();
		Platform.exit();
	}
	public void save(ActionEvent event){
		System.out.println("Saved");
		controller.saveState();
	}
	

	public void loadCategories(){
		FileChooser fc = new FileChooser();
		File selectedFile = fc.showOpenDialog(null);

	
		try {
			if (selectedFile != null){
				controller.loadCategoryFile(selectedFile);

			} else {
				System.out.println("File is not valid");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		refreshCategoryTable();
		
	}
	public void loadTransactions(){
		FileChooser fc = new FileChooser();
		File selectedFile = fc.showOpenDialog(null);

		try {
			if (selectedFile != null){
				controller.loadTransactionFile(selectedFile);

			} else {
				System.out.println("File is not valid");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		refreshList();
		

	}
	public void clearall(){
		controller.clearAll();
	}

	public void toggleDebet(){
		if(debetOrCredit.equals("D")){

		} else{
			debetOrCredit= "D";

			refreshList();
			refreshProgressBar();
			refreshCategoryTable();
		}
	}
	public void toggleCredit(){
		if(debetOrCredit.equals("C")){
		} else{
			debetOrCredit= "C";

			refreshList();
			refreshProgressBar();
			refreshCategoryTable();
		}
	}

	////////////////////////////////////////////////////////////
	//OUTPUT TAB COMES BELOW
	////////////////////////////////////////////////////////////

	@FXML StackedBarChart sBChart;
	@FXML BarChart bChart;
	@FXML DatePicker dpChartStart;
	@FXML DatePicker dpChartEnd;

	public void initDateValues(){
		dpChartStart.setValue(LocalDate.of(2016, 1, 1));
		dpChartEnd.setValue(LocalDate.of(2017, 1, 1));
	}


	public void initchart(){

		LocalDate chartStart = dpChartStart.getValue();
		LocalDate chartEnd = dpChartEnd.getValue();

		sBChart.getData().clear();
		ObservableList<XYChart.Series<String, Number>> dataNumber = FXCollections.observableArrayList();

		for (Category c : controller.getCategories()){
			if(c.getDebetOrCredit().equals(debetOrCredit)){
				XYChart.Series<String, Number> tempSeries = new XYChart.Series<String, Number>();
				tempSeries.setName(c.getName());


				for (LocalDate i = chartStart; i.isBefore(chartEnd); i=i.plusMonths(1)){

					tempSeries.getData().add(new XYChart.Data<String, Number>(i.toString(),controller.categoryTotal(c.getName(), debetOrCredit, LocalDate.of(i.getYear(), i.getMonth() , 1), LocalDate.of(i.plusMonths(1).getYear(), i.getMonth().plus(1) , 1))));

				}
				dataNumber.add(tempSeries);
			}
		}

		sBChart.getData().addAll(dataNumber);

		initchartnormal();
	}

	public void initchartnormal(){  // to be adjusted 
		LocalDate chartStart = dpChartStart.getValue();
		LocalDate chartEnd = dpChartEnd.getValue();

		bChart.getData().clear();

		ObservableList<XYChart.Series<String, Number>> dataNumber = FXCollections.observableArrayList();

		XYChart.Series<String, Number> tempSeries1 = new XYChart.Series<String, Number>();
		XYChart.Series<String, Number> tempSeries2 = new XYChart.Series<String, Number>();
		tempSeries1.setName("Credit"); 
		tempSeries2.setName("Debet");
	
		for (LocalDate i = chartStart; i.isBefore(chartEnd); i=i.plusMonths(1)){
			tempSeries1.getData().add(new XYChart.Data<String, Number>(i.toString(),controller.generalTotal("C", LocalDate.of(i.getYear(), i.getMonth() , 1), LocalDate.of(i.getYear(), i.getMonth().plus(1) , 1))));
		}
		dataNumber.add(tempSeries1);
		for (LocalDate i = chartStart; i.isBefore(chartEnd); i=i.plusMonths(1)){
			tempSeries2.getData().add(new XYChart.Data<String, Number>(i.toString(),controller.generalTotal("D", LocalDate.of(i.getYear(), i.getMonth() , 1), LocalDate.of(i.getYear(), i.getMonth().plus(1) , 1))));
		}
		dataNumber.add(tempSeries2);


		bChart.getData().addAll(dataNumber);
	}
	
	public void about(){
		
	}
	
	

}






