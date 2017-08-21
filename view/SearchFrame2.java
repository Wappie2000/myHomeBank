package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;

import model.*;
import controller.*;  // important! do not remove, acces of controller via mainview requires this to work


import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import java.awt.Rectangle;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class SearchFrame2 extends JFrame implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// Parent Observer
	BankModelObserver mainview;
	
	//IV's
	LocalDate startDate;
	LocalDate endDate;
	Category category;
	int minAmount;
	String debCred;
	
	//Model to GUI item helper IV's
	DefaultComboBoxModel categoryComboBox;
	
	
	
	private JPanel contentPane;
	private JTextField txtStartDate;
	private JTextField txtEndDate;
	private JLabel lblCategory;
	private JComboBox<Category> cbCategory;
	private JTextField txtMinAmount;
	private JButton btnApply;
	private JCheckBox chckbxD;
	private JCheckBox chckbxC;

	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchFrame2 frame = new SearchFrame2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * For display purpose in design view
	 */
	public SearchFrame2() {
		initComponents();
		createEvents();

	}
	public SearchFrame2(BankModelObserver mainview) {
		this.mainview = mainview;
		initComponents();
		createEvents();

	}
	private void close(){

	}
	

	private void initComponents() {

		
		setTitle("Choose Transactions");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 279, 238);
		setLocationRelativeTo((Component) mainview);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblStartDate = new JLabel("Start Date: ");
		lblStartDate.setBounds(new Rectangle(0, 0, 0, 4));
		
		txtStartDate = new JTextField();
		txtStartDate.setHorizontalAlignment(SwingConstants.RIGHT);
		
		txtStartDate.setText("01-06-2016");
		
		
		
		txtStartDate.setColumns(10);
		
		JLabel lblEndDate = new JLabel("End Date: ");
		lblEndDate.setBounds(new Rectangle(0, 5, 0, 5));
		
		txtEndDate = new JTextField();
		txtEndDate.setHorizontalAlignment(SwingConstants.RIGHT);
		txtEndDate.setText("01-06-2017");
		txtEndDate.setColumns(10);
		
		lblCategory = new JLabel("Category");
		
		System.out.println(mainview.getController().getCategories());
		
		
		ArrayList<Category> tempCategories = mainview.getController().getCategories();
		Collections.sort(tempCategories); 
		Object[] categoryHelperArray = tempCategories.toArray();
		
		
		categoryComboBox= new DefaultComboBoxModel(categoryHelperArray);
		
		cbCategory = new JComboBox(categoryComboBox);
		
		
		JLabel lblMinAmount = new JLabel("Min amount:");
		
		txtMinAmount = new JTextField();
		txtMinAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		txtMinAmount.setText("50");
		txtMinAmount.setColumns(10);
		
		btnApply = new JButton("Apply");
		
		JLabel lblDebetcredit = new JLabel("Debet/Credit:");
		
		chckbxD = new JCheckBox("D");
		chckbxD.setSelected(true);
		
		chckbxC = new JCheckBox("C");
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblCategory)
									.addGap(18)
									.addComponent(cbCategory, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblMinAmount)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtMinAmount, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
									.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(lblEndDate)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(txtEndDate, 0, 0, Short.MAX_VALUE))
									.addGroup(gl_contentPane.createSequentialGroup()
										.addComponent(lblStartDate)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(txtStartDate, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(98)
							.addComponent(btnApply))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblDebetcredit)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxD)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxC)))
					.addContainerGap(24, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStartDate)
						.addComponent(txtStartDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(4)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtEndDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEndDate))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(cbCategory, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCategory))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMinAmount)
						.addComponent(txtMinAmount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDebetcredit)
						.addComponent(chckbxD)
						.addComponent(chckbxC))
					.addPreferredGap(ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
					.addComponent(btnApply))
		);
		contentPane.setLayout(gl_contentPane);
		
		
		
	}
	private void createEvents() {
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				parseStartDate();
				parseEndDate();
				parseCategory();
				parseMinAmount();
				parseDebCred();
				setSearchresultToMainview();
				
			}
		});
		
	}
	

	protected void parseStartDate() {
		try {
			startDate = LocalDate.of(Integer.parseInt(txtStartDate.getText(6, 4)), Integer.parseInt(txtStartDate.getText(3, 2)), Integer.parseInt(txtStartDate.getText(0, 2)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	protected void parseEndDate() {
		try {
			endDate = LocalDate.of(Integer.parseInt(txtEndDate.getText(6, 4)), Integer.parseInt(txtEndDate.getText(3, 2)), Integer.parseInt(txtEndDate.getText(0, 2)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	protected void parseCategory() {
		category = (Category) cbCategory.getSelectedItem();
	}
	
	protected void parseMinAmount() {
		minAmount = Integer.parseInt(txtMinAmount.getText());
	}
	protected void parseDebCred() {
		if (!chckbxC.isSelected() && !chckbxD.isSelected()){
			JOptionPane.showMessageDialog(null, "Please select one option on Debet/Credit");
		}
		if (chckbxC.isSelected() && !chckbxD.isSelected()){
			debCred = "C";
		}
		if (!chckbxC.isSelected() && chckbxD.isSelected()){
			debCred = "D";
		}
		if(chckbxC.isSelected() && chckbxD.isSelected()){
			debCred = "B";   // B meaning both
		} 
	}

	protected void setSearchresultToMainview() {
		if(debCred == "B"){
			ArrayList<Transaction> temp = mainview.getController().searchTransactions(startDate, endDate, category.getName(), minAmount);
			mainview.setTempSearch(temp);
		} else {
			ArrayList<Transaction> temp = mainview.getController().searchTransactions(startDate, endDate, category.getName(), minAmount, debCred);
			mainview.setTempSearch(temp);
		}
		mainview.addTempSearchToJlist();
		
	}
}
