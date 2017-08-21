package model;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.UIManager;

import controller.*;
import view.*;

public class BankTestdrive {

BankModelInterface model;
BankControllerInterface controller;
BankModelObserver view;

	
	public static void main(String[] args) {
		
		BankTestdrive test = new BankTestdrive();
		test.initModel();
		test.initController();
		test.initView();
		

	}

	void initModel(){
	
		
		
		System.out.println("init model");
		model = new BankModel();

		File file = new File("HBcurrentstate.ser");
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));

			model = (BankModel) is.readObject();

			System.out.println("serfile read");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		controller = new BankController1(model);
	
		
	}
	
	void initController(){
		controller = new BankController1(model);
	}
	
	void initView(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestGui3 window = new TestGui3(controller);
					window.setVisible(true);
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

}
