package com.techelevator;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import com.techelevator.view.Menu;

public class FeedMoneyMenu extends Menu {

	private BigDecimal currentBalance;

	public FeedMoneyMenu(InputStream input, OutputStream output, BigDecimal currentBalance) {
		super(input, output);
		this.currentBalance = currentBalance;
	}
	
	@Override
	protected void displayMenuOptions(Object[] options) {
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
