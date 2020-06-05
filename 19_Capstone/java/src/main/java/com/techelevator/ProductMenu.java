package com.techelevator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import com.techelevator.view.Menu;

public class ProductMenu extends Menu {

	public ProductMenu(InputStream input, OutputStream output) {
		super(input, output);
	}
	
	public Object getChoiceFromOptions(Object[] options, BigDecimal currentBalance) {
		Object choice = null;
		while (choice == null) {
		//	displayMenuOptions(options, currentBalance);
			super.out.println();
			super.out.println("Current Money Provided: $" + currentBalance);
			super.out.print("\nPlease choose an option >>> ");
			super.out.flush();
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}
	
	@Override
	protected Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		IOException e = new IOException();
		String userInput = super.in.nextLine();
		try {
			for (int i = 0; i < options.length; i++) {
				if (options[i].equals(userInput.toUpperCase())) {
					choice = options[i];
					return choice;
				}
			}
			throw e;
			
		} catch (IOException e2) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println("\n*** " + userInput + " is not a valid option ***\n");
		}
		return choice;
	}


}
