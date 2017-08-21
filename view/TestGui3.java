package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import controller.BankControllerInterface;
import model.Category;
import model.Transaction;

import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.UIManager;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.Point;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.JToggleButton;
import java.awt.Toolkit;
import javax.swing.JTree;
import java.awt.FlowLayout;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class TestGui3 extends JFrame implements BankModelObserver{

	//Observer IV
	BankControllerInterface controller;

	public BankControllerInterface getController() {
		return controller;
	}


	// Manually added helper IV
	public ArrayList<Transaction> tempSearch;

	public void setTempSearch(ArrayList<Transaction> tempSearch){
		this.tempSearch = tempSearch;
	}


	private DefaultListModel<Transaction> searchedTransactionList = new DefaultListModel<>();




	SearchFrame2 searchFrame;
	TestGui3 testGui3 = this;

	// WindowBuilder IV
	private JPanel contentPane;
	private JMenuItem mntmPiechart;
	private JPanel pnlAccountInfo;
	private JPanel pnlGraphDisplay;
	private JPanel pnlSearchTransactions;
	private JList lstSearchTransactions;
	private JMenuItem mntmLoadTransactionFile;
	private JMenu mnSearch;
	private JMenuItem mntmTransactions;
	private JMenuItem mntmLoadCategoryFile;
	private JMenuItem mntmPrintCategories;
	private JMenuItem mntmSaveState;
	private JButton btnUpdateList;
	private JLabel lblAddKeyword;
	private JTextField txtKeyWord;
	private JPanel pnlAdvancedOptions;
	private JLabel lblMinAmount;
	private JTextField txtMinAmount;
	private JLabel lblMaxAmount;
	private JTextField txtMaxAmount;
	private JLabel lblReceivingAccount;
	private JTextField txtReceivingAccount;
	private JToggleButton tglAdvanced;
	private JButton btnApplySetCategory;
	private JComboBox cbSetCategory;
	private JPanel pnlSetCategory;
	private JButton btnExcludeFromAnalisys;
	private JMenuItem mntmSaveCategories;
	private JMenuItem mntmBarchart;
	private JMenuItem mntmReport;
	private JMenu mnGraph;
	private JTextField txtSelectedTransaction;

	/**
	 * Launch the application.
	 */


	/**
	 * Create the frame.
	 */
	public TestGui3(BankControllerInterface controller) {
		


		this.controller = controller;
		initComponents();
		createEvents();
	
		
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}



	private void initComponents() {
		setTitle("MyHomeBank");
		setIconImage(Toolkit.getDefaultToolkit().getImage(TestGui3.class.getResource("/resources/euroHead.jpg")));
		setFont(new Font("Tahoma", Font.PLAIN, 11));


		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 653);
		setLocationRelativeTo(null);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmSaveState = new JMenuItem("Save state");

		mnFile.add(mntmSaveState);
		
		mntmSaveCategories = new JMenuItem("Save categories");
		
		mnFile.add(mntmSaveCategories);

		mntmLoadCategoryFile = new JMenuItem("Load category file");

		mnFile.add(mntmLoadCategoryFile);

		mntmLoadTransactionFile = new JMenuItem("Load transaction file");

		mnFile.add(mntmLoadTransactionFile);

		mnSearch = new JMenu("Search");
		menuBar.add(mnSearch);

		mntmTransactions = new JMenuItem("Transactions");

		mnSearch.add(mntmTransactions);

		mntmPrintCategories = new JMenuItem("Print Categories");

		mnSearch.add(mntmPrintCategories);

		mnGraph = new JMenu("Graph");
		menuBar.add(mnGraph);

		mntmPiechart = new JMenuItem("Piechart");

		mnGraph.add(mntmPiechart);
		
		mntmBarchart = new JMenuItem("Barchart");
		
		mnGraph.add(mntmBarchart);
		
		mntmReport = new JMenuItem("Report");
		
		mnGraph.add(mntmReport);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(176, 224, 230));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		pnlGraphDisplay = new JPanel();
		pnlGraphDisplay.setBackground(new Color(143, 188, 143));
		pnlGraphDisplay.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Graphics Display", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		pnlSearchTransactions = new JPanel();
		pnlSearchTransactions.setBackground(new Color(144, 238, 144));
		pnlSearchTransactions.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Search Transactions", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		pnlAccountInfo = new JPanel();
		pnlAccountInfo.setAlignmentY(Component.TOP_ALIGNMENT);
		pnlAccountInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
		pnlAccountInfo.setBackground(new Color(60, 179, 113));
		pnlAccountInfo.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Account Information", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(pnlAccountInfo, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pnlGraphDisplay, GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addComponent(pnlSearchTransactions, GroupLayout.PREFERRED_SIZE, 772, GroupLayout.PREFERRED_SIZE)
					.addGap(202))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(pnlGraphDisplay, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
						.addComponent(pnlAccountInfo, GroupLayout.PREFERRED_SIZE, 314, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(pnlSearchTransactions, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
		);
		pnlGraphDisplay.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		pnlSetCategory = new JPanel();
		pnlSetCategory.setBackground(new Color(60, 179, 113));




		JLabel lblSetCategory = new JLabel("Set Category");

		// UGLY code to be replaced!!!
		ArrayList<Category> tempCategories = controller.getCategories();
		Object[] categoryHelperArray = tempCategories.toArray();
		DefaultComboBoxModel categoryComboBox= new DefaultComboBoxModel(categoryHelperArray);
		// UGLY code to be replaced!!!
		
		
		cbSetCategory = new JComboBox(categoryComboBox);

		lblAddKeyword = new JLabel("Add keyword (optional)");
		lblAddKeyword.setToolTipText("Other transactions which contain this keyword will automatically get this category assigned");

		txtKeyWord = new JTextField();
		txtKeyWord.setColumns(10);

		tglAdvanced = new JToggleButton("Advanced");

		tglAdvanced.setMargin(new Insets(2, 2, 2, 2));
		tglAdvanced.setFont(new Font("Tahoma", Font.PLAIN, 9));

		pnlAdvancedOptions = new JPanel();
		pnlAdvancedOptions.setBackground(new Color(60, 179, 113));
		pnlAdvancedOptions.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		pnlAdvancedOptions.setAlignmentX(Component.RIGHT_ALIGNMENT);
		pnlAdvancedOptions.setVisible(false);
		pnlAdvancedOptions.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Advanced rules by this keyword (optional)", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));

		lblMinAmount = new JLabel("Min amount");

		txtMinAmount = new JTextField();
		txtMinAmount.setAlignmentX(Component.RIGHT_ALIGNMENT);
		txtMinAmount.setColumns(10);

		lblMaxAmount = new JLabel("Max amount");

		txtMaxAmount = new JTextField();
		txtMaxAmount.setAlignmentX(Component.RIGHT_ALIGNMENT);
		txtMaxAmount.setColumns(10);

		lblReceivingAccount = new JLabel("Receiving account");

		txtReceivingAccount = new JTextField();
		txtReceivingAccount.setColumns(10);
		GroupLayout gl_pnlAdvancedOptions = new GroupLayout(pnlAdvancedOptions);
		gl_pnlAdvancedOptions.setHorizontalGroup(
				gl_pnlAdvancedOptions.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_pnlAdvancedOptions.createSequentialGroup()
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_pnlAdvancedOptions.createParallelGroup(Alignment.LEADING)
								.addComponent(lblReceivingAccount)
								.addComponent(lblMinAmount)
								.addComponent(lblMaxAmount))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_pnlAdvancedOptions.createParallelGroup(Alignment.LEADING)
								.addComponent(txtMaxAmount, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtMinAmount, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtReceivingAccount, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE))
						.addGap(54))
				);
		gl_pnlAdvancedOptions.setVerticalGroup(
				gl_pnlAdvancedOptions.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlAdvancedOptions.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_pnlAdvancedOptions.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblMinAmount)
								.addComponent(txtMinAmount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_pnlAdvancedOptions.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblMaxAmount)
								.addComponent(txtMaxAmount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_pnlAdvancedOptions.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblReceivingAccount)
								.addComponent(txtReceivingAccount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				);
		pnlAdvancedOptions.setLayout(gl_pnlAdvancedOptions);

		btnApplySetCategory = new JButton("Apply");

		btnApplySetCategory.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		btnApplySetCategory.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		JLabel lblTransaction = new JLabel("Transaction");
		
		txtSelectedTransaction = new JTextField();
		txtSelectedTransaction.setColumns(10);
		GroupLayout gl_pnlSetCategory = new GroupLayout(pnlSetCategory);
		gl_pnlSetCategory.setHorizontalGroup(
			gl_pnlSetCategory.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_pnlSetCategory.createSequentialGroup()
					.addGroup(gl_pnlSetCategory.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_pnlSetCategory.createSequentialGroup()
							.addGap(19)
							.addComponent(tglAdvanced)
							.addPreferredGap(ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
							.addComponent(pnlAdvancedOptions, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_pnlSetCategory.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_pnlSetCategory.createParallelGroup(Alignment.LEADING)
								.addComponent(lblAddKeyword)
								.addComponent(lblTransaction)
								.addComponent(lblSetCategory))
							.addGap(18)
							.addGroup(gl_pnlSetCategory.createParallelGroup(Alignment.LEADING)
								.addComponent(cbSetCategory, 0, 218, Short.MAX_VALUE)
								.addComponent(txtKeyWord, 218, 218, 218)
								.addComponent(txtSelectedTransaction, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
				.addGroup(gl_pnlSetCategory.createSequentialGroup()
					.addContainerGap(161, Short.MAX_VALUE)
					.addComponent(btnApplySetCategory)
					.addGap(142))
		);
		gl_pnlSetCategory.setVerticalGroup(
			gl_pnlSetCategory.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_pnlSetCategory.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlSetCategory.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTransaction)
						.addComponent(txtSelectedTransaction, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlSetCategory.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSetCategory)
						.addComponent(cbSetCategory, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlSetCategory.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAddKeyword)
						.addComponent(txtKeyWord, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
					.addGroup(gl_pnlSetCategory.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_pnlSetCategory.createSequentialGroup()
							.addComponent(pnlAdvancedOptions, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(29))
						.addGroup(gl_pnlSetCategory.createSequentialGroup()
							.addComponent(tglAdvanced)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnApplySetCategory))
		);
		pnlSetCategory.setLayout(gl_pnlSetCategory);
		

		JScrollPane scrollPane = new JScrollPane();


		btnUpdateList = new JButton("Update List");
		
		btnExcludeFromAnalisys = new JButton("Exclude from Analisys");
		

		GroupLayout gl_pnlSearchTransactions = new GroupLayout(pnlSearchTransactions);
		gl_pnlSearchTransactions.setHorizontalGroup(
			gl_pnlSearchTransactions.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlSearchTransactions.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 578, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlSearchTransactions.createParallelGroup(Alignment.LEADING)
						.addComponent(btnUpdateList, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
						.addComponent(btnExcludeFromAnalisys, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_pnlSearchTransactions.setVerticalGroup(
			gl_pnlSearchTransactions.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlSearchTransactions.createSequentialGroup()
					.addGroup(gl_pnlSearchTransactions.createParallelGroup(Alignment.BASELINE)
						.addGroup(gl_pnlSearchTransactions.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_pnlSearchTransactions.createSequentialGroup()
							.addGap(45)
							.addComponent(btnUpdateList)
							.addPreferredGap(ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
							.addComponent(btnExcludeFromAnalisys)
							.addGap(52)))
					.addContainerGap())
		);


		lstSearchTransactions = new JList(searchedTransactionList);
		lstSearchTransactions.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
			}
		});


		scrollPane.setViewportView(lstSearchTransactions);
		pnlSearchTransactions.setLayout(gl_pnlSearchTransactions);
		contentPane.setLayout(gl_contentPane);

	}





	private void createEvents() {
		mntmLoadTransactionFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileOpen = new JFileChooser();
				fileOpen.showOpenDialog(getParent());
				controller.loadTransactionFile(fileOpen.getSelectedFile());
			}
		});

		mntmLoadCategoryFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileOpen = new JFileChooser();
				fileOpen.showOpenDialog(getParent());
				controller.loadCategoryFile(fileOpen.getSelectedFile());
			}
		});
		mntmSaveState.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.saveState();
			}
		});
		mntmTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(searchFrame== null){
				searchFrame = new SearchFrame2(testGui3);
				searchFrame.setVisible(true);
				} else {
					searchFrame.setVisible(true);
				}
			}
		});
		mntmPiechart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buildPieChart();

			}
		});
		mntmPrintCategories.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(Category c : controller.getCategories()){
					System.out.println(c);
				}
			}
		});
		btnUpdateList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchFrame.setSearchresultToMainview();
			}
		});
		tglAdvanced.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tglAdvanced.isSelected()){
					pnlAdvancedOptions.setVisible(true);
				} else {
					pnlAdvancedOptions.setVisible(false);
				}
			}
		});
		btnApplySetCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.catogeriseTransaction((Transaction) lstSearchTransactions.getSelectedValue(), cbSetCategory.getSelectedItem().toString(), txtKeyWord.getText());
				txtKeyWord.setText("");
			}
		});
		btnExcludeFromAnalisys.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.excludeTransactionFromAnalisys((Transaction) lstSearchTransactions.getSelectedValue());
			}

			
		});
		mntmSaveCategories.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileSave = new JFileChooser();
				fileSave.showOpenDialog(getParent());
				controller.saveCategories(fileSave.getSelectedFile());
			}
		});
		mntmBarchart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				buildBarChart();
			}
		});
		mntmReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createMonthlyReport();
			}
		});
		lstSearchTransactions.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				addSetCategoryPanel();
			}
		});
	}



	// Helper functions
	
	private void createMonthlyReport(){
		JFileChooser fileSave = new JFileChooser();
		fileSave.showOpenDialog(getParent());
		controller.createMontlyReport(searchFrame.startDate, searchFrame.endDate, searchFrame.debCred,fileSave.getSelectedFile());
		
		
	}
	
	
	
	@Override
	public void addTempSearchToJlist(){
		searchedTransactionList.clear();
		for(Transaction t : tempSearch){
			searchedTransactionList.addElement(t);
		}


	}
	private void buildPieChart(){
		DefaultPieDataset data = null;
		try {
			if(searchFrame.startDate == null) {
				System.out.println("Try demo graph");
				data = controller.createPieDataset(LocalDate.of(2015, 6, 1), LocalDate.of(2017, 6, 1), "D");
			} else {
				data = controller.createPieDataset(searchFrame.startDate, searchFrame.endDate, searchFrame.debCred);	
			}
		} catch (Exception e) {
			data = controller.createPieDataset(LocalDate.of(2015, 6, 1), LocalDate.of(2017, 6, 1), "D");
			e.printStackTrace();
		}
		
		JFreeChart chart = ChartFactory.createPieChart("Pie chart of searched results", data);
		Image image = new ImageIcon("grey-background-website-8013.jpg").getImage();
		chart.setBackgroundImage(image);
		
		
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(pnlGraphDisplay.getWidth()-50, pnlGraphDisplay.getHeight()-50));
		pnlGraphDisplay.removeAll();
		pnlGraphDisplay.add(chartPanel);
		System.out.println("Building pie chart");
		this.revalidate();
		
	}
	private void buildBarChart(){
		
		DefaultCategoryDataset data = null;
		try {
			data = null;
			if(searchFrame.startDate == null) {
				data = controller.createMontlyDataset(LocalDate.of(2015, 6, 1), LocalDate.of(2017, 6, 1), "D");
			} else {
				data = controller.createMontlyDataset(searchFrame.startDate, searchFrame.endDate, searchFrame.debCred);	
			}
		} catch (Exception e) {
			data = controller.createMontlyDataset(LocalDate.of(2015, 6, 1), LocalDate.of(2017, 6, 1), "D");
			e.printStackTrace();
		}
		Image image = new ImageIcon("solid light blue.jpg").getImage();
		JFreeChart chart = ChartFactory.createStackedBarChart("Monthly expenses", null, "Eur", data);
		chart.setBackgroundImage(image);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(pnlGraphDisplay.getWidth()-30, pnlGraphDisplay.getHeight()-30));
		pnlGraphDisplay.removeAll();
		pnlGraphDisplay.add(chartPanel);
		System.out.println("Building pbar chart");
		this.revalidate();
		
	}
	
	
	

	@Override
	public void addSetCategoryPanel(){
		JFrame frame = new JFrame("Set Category");
		frame.setSize(450, 300);
		frame.setLocationRelativeTo(null);
		
		
		GroupLayout gl_pnlAccountInfo = new GroupLayout(pnlAccountInfo);
		pnlAccountInfo.setLayout(gl_pnlAccountInfo);
		gl_pnlAccountInfo.setHorizontalGroup(
				gl_pnlAccountInfo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlAccountInfo.createSequentialGroup()
						.addContainerGap()
						.addComponent(pnlSetCategory, GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
						.addContainerGap())
				);
		gl_pnlAccountInfo.setVerticalGroup(
				gl_pnlAccountInfo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlAccountInfo.createSequentialGroup()
						.addComponent(pnlSetCategory, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
						.addContainerGap())
				);
		pnlAccountInfo.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Set Category", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		try {
			Transaction temp = (Transaction) lstSearchTransactions.getSelectedValue();
			
			txtSelectedTransaction.setText(temp.getTransactionDate()+  " " + temp.getAmount());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frame.getContentPane().add(pnlAccountInfo);
		
		frame.setVisible(true);
		
	}




	@Override
	public void updateData() {
		// TODO Auto-generated method stub

	}
	@Override
	public String toString(){
		return "Mainview";
	}



	@Override
	public void viewRevalidate() {
		
		System.out.println("viewrevalidate reached");

		this.revalidate();

	}
}
