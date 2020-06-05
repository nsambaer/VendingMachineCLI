package com.techelevator;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import com.techelevator.view.Menu;

public class FeedMoneyMenu extends Menu {


	public FeedMoneyMenu(InputStream input, OutputStream output) {
		super(input, output);
	}
	
	public Object getChoiceFromOptions(Object[] options, BigDecimal currentBalance) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options, currentBalance);
			choice = super.getChoiceFromUserInput(options);
		}
		return choice;
	}
	
	protected void displayMenuOptions(Object[] options, BigDecimal currentBalance) {
		super.out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			super.out.println(optionNum + ") " + options[i]);
		}
		super.out.println("Current Money Provided: $" + currentBalance);
		super.out.print("\nPlease choose an option >>> ");
		super.out.flush();
	}
	
	
}
