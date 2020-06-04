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
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE,
			MAIN_MENU_OPTION_EXIT };

	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_OPTION_FEED_MONEY,
			PURCHASE_MENU_OPTION_SELECT_PRODUCT, PURCHASE_MENU_FINISH_TRANSACTION };

	private static final String FEED_MONEY_OPTION_ONE_DOLLAR = "Feed in $1";
	private static final String FEED_MONEY_OPTION_TWO_DOLLARS = "Feed in $2";
	private static final String FEED_MONEY_OPTION_FIVE_DOLLARS = "Feed in $5";
	private static final String FEED_MONEY_OPTION_TEN_DOLLARS = "Feed in $10";
	private static final String FEED_MONEY_OPTION_RETURN = "Return to purchase menu";
	private static final String[] FEED_MONEY_OPTIONS = { FEED_MONEY_OPTION_ONE_DOLLAR, FEED_MONEY_OPTION_TWO_DOLLARS,
			FEED_MONEY_OPTION_FIVE_DOLLARS, FEED_MONEY_OPTION_TEN_DOLLARS, FEED_MONEY_OPTION_RETURN };

	private BigDecimal currentBalance = BigDecimal.ZERO;

	private List<Vendable> itemList = new ArrayList<>();

	private Menu menu;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
		try {
			this.stockVendingMachine();

		} catch (FileNotFoundException e) {
			System.out.println("Error: Can't find inventory file");
			System.exit(1);
		}
	}

	public void run() {
		while (true) { // MAIN MENU
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				// display vending machine items
				displayMenu();
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				// do purchase
				purchase();
			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				System.out.println("Thank you for shopping!");
				System.exit(1);
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
			} else if (data[3].contains("Candy")) {
				itemList.add(new Candy(data[1], price, data[0]));
			} else if (data[3].contains("Drink")) {
				itemList.add(new Drink(data[1], price, data[0]));
			} else if (data[3].contains("Gum")) {
				itemList.add(new Gum(data[1], price, data[0]));
			}
		}
		inventory.close();
	}

	public void displayMenu() {
		System.out.println(" _____________________________________________");
		System.out.println("|---------------------------------------------|");
		System.out.println("|       Umbrella Corp Vending Machine         |");
		System.out.println("|---------------------------------------------|");
		System.out.println("|              Items Available                |");
		System.out.println("|---------------------------------------------|");
		System.out.println("|  Slot  |  Name  |  Price  |  # Available    |");
		System.out.println("|---------------------------------------------|");
		for (Vendable item : itemList) {
			System.out.println("|   " + item.getSlot() + "   | " + item.getName() + "   |   $" + item.getPrice()
					+ "   |   Qty: " + item.getStock() + "  |");
		}
		System.out.println("|---------------------------------------------|");
		System.out.println("|_____________________________________________|");
	}

	public void purchase() {
		PurchaseMenu purchaseMenu = new PurchaseMenu(System.in, System.out, currentBalance);
		while (true) { // PURCHASE MENU
			String choice = (String) purchaseMenu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);

			if (choice.equals(PURCHASE_MENU_OPTION_FEED_MONEY)) {
				feedMoney();
			} else if (choice.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT)) {

			} else if (choice.equals(PURCHASE_MENU_FINISH_TRANSACTION)) {
				// Finish transaction
				System.exit(1);
			}
		}
	}

	private void feedMoney() {
		FeedMoneyMenu feedMoneyMenu = new FeedMoneyMenu(System.in, System.out, currentBalance);
		boolean loop = true;
		while (loop) { // FEED MONEY MENU
			String choice = (String) feedMoneyMenu.getChoiceFromOptions(FEED_MONEY_OPTIONS);
			
			if (choice.equals(FEED_MONEY_OPTION_ONE_DOLLAR)) {
				currentBalance = currentBalance.add(BigDecimal.valueOf(1.00));
			} else if (choice.equals(FEED_MONEY_OPTION_TWO_DOLLARS)) {
				currentBalance = currentBalance.add(BigDecimal.valueOf(2.00));
			} else if (choice.equals(FEED_MONEY_OPTION_FIVE_DOLLARS)) {
				currentBalance = currentBalance.add(BigDecimal.valueOf(5.00));
			} else if (choice.equals(FEED_MONEY_OPTION_TEN_DOLLARS)) {
				currentBalance = currentBalance.add(BigDecimal.valueOf(10.00));
			} else if (choice.equals(FEED_MONEY_OPTION_RETURN)) {
				loop = false;
			}

		}
	}

}
