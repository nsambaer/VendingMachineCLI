package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.techelevator.view.Menu;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE };
	private List<Vendable> itemList = new ArrayList<>();
	
	
	private Menu menu;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
		try {
			this.stockVendingMachine();
			
		} catch (FileNotFoundException e) {
			System.out.println("Can't find inventory file");
		}
	}

	public void run() {
		while (true) {		//MAIN MENU
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			
			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				// display vending machine items
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				// do purchase
			}
		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		
		cli.run();
	}
	
	private void stockVendingMachine() throws FileNotFoundException {
		File vendingInventory = new File("vendingmachine.csv");
		Scanner inventory = new Scanner(vendingInventory);
		
		while (inventory.hasNextLine()) {
			String line = inventory.nextLine();
			String[] data = line.split("\\|");
			BigDecimal price = BigDecimal.valueOf(Double.parseDouble(data[2]));
			if (data[3].contains("Chip")) {
				itemList.add(new Chip(data[1], price, data[0]));
			}
			else if (data[3].contains("Candy")) {
				itemList.add(new Candy(data[1], price, data[0]));
			}
			else if (data[3].contains("Drink")) {
				itemList.add(new Drink(data[1], price, data[0]));
			}
			else if (data[3].contains("Gum")) {
				itemList.add(new Gum(data[1], price, data[0]));
			}
		}
		inventory.close();
	}
	
//	public void testInventory(){
//		for (Vendable item: itemList) {
//			System.out.println(item.getSlot() + " " + item.getName() + " " + item.getPrice() + " " + item.getSound() + " " + item.getStock());
//		}
//	}
	
}
