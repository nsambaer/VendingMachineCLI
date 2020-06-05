package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.techelevator.view.Menu;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String MAIN_MENU_OPTION_SALES = "";
	private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE,
			MAIN_MENU_OPTION_EXIT, MAIN_MENU_OPTION_SALES};

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

	private BigDecimal currentBalance;

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
		currentBalance = BigDecimal.ZERO;
		currentBalance = currentBalance.setScale(2);
		
		vmLogger(LocalDate.now() + " " + LocalTime.now() + " Opening Log Session");
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
				vmLogger(LocalDate.now() + " " + LocalTime.now() + " Closing Log Session\n");
				System.out.println("Thank you for shopping!");
				System.exit(1);
			} else if (choice.equals(MAIN_MENU_OPTION_SALES)) {
				salesLog();
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

	private void displayMenu() {
		System.out.println(" ___________________________________________________");
		System.out.println("|---------------------------------------------------|");
		System.out.println("|           Umbrella Corp Vending Machine           |");
		System.out.println("|---------------------------------------------------|");
		System.out.println("|                  Items Available                  |");
		System.out.println("|---------------------------------------------------|");
		System.out.println("| Slot |        Name        | Price |  # Available  |");
		System.out.println("|---------------------------------------------------|");
	
		for (Vendable item : itemList) {
			String nameWithSpacing = item.getName();
			for (int i = 0; i <= (18 - item.getName().length()); i++) {
				nameWithSpacing += " ";
			}
			String stock;
			if (!item.isSoldOut()) {
				stock = item.getStock() + "       ";
			} else {
				stock = item.getStock();
			}
			System.out.println("|  " + item.getSlot() + "  | " + nameWithSpacing + "| $" + item.getPrice() + " | Qty: " + stock + " |");				
		}
		
		System.out.println("|---------------------------------------------------|");
		System.out.println("|___________________________________________________|");
	}

	private void purchase() {
		PurchaseMenu purchaseMenu = new PurchaseMenu(System.in, System.out);
		boolean loop = true;
		while (loop) { // PURCHASE MENU
			String choice = (String) purchaseMenu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS, currentBalance);

			if (choice.equals(PURCHASE_MENU_OPTION_FEED_MONEY)) {
				feedMoney();
			} else if (choice.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT)) {
				if (currentBalance.compareTo(BigDecimal.ZERO) < 1) {
					System.out.println("\n$$$ Please feed money into machine first $$$");
				} else {
					selectProduct();
				}
			} else if (choice.equals(PURCHASE_MENU_FINISH_TRANSACTION)) {
				// Finish transaction
				finishTransaction();
				loop = false;
			}
		}
	}

	private void vmLogger(String logMessage) {
		try (Logger log = new Logger("log.txt");){
			log.Write(logMessage);
		} 
		catch (IOException ex) {
			System.out.println("IO Exception at:" + ex.getMessage());
		} 
		catch (Exception ex) {
			System.out.println("General Exception at:" + ex.getMessage());
		}		
	}
	
	private void salesLog() {
		String fileName = LocalDate.now() + " " + LocalTime.now();
		fileName = fileName.replaceAll(":", "_");
		fileName = fileName.substring(0, 19);
		String output = "";
		BigDecimal totalSales = BigDecimal.ZERO;
		try (Logger log = new Logger("SalesReport/" + fileName + " Sales Log.txt");){
			for (Vendable item: itemList) {
				output += item.getName() + "|" + item.getSales() + "\n";
				totalSales = totalSales.add(item.getPrice().multiply(BigDecimal.valueOf((double) (item.getSales()))));
				totalSales = totalSales.setScale(2);
			}
			output += "TOTAL SALES: $" + totalSales;
			log.Write(output);
		} 
		catch (IOException ex) {
			System.out.println("IO Exception at:" + ex.getMessage());
		} 
		catch (Exception ex) {
			System.out.println("General Exception at:" + ex.getMessage());
		}	
	}
	
	private void feedMoney() {
		FeedMoneyMenu feedMoneyMenu = new FeedMoneyMenu(System.in, System.out);
		boolean loop = true;
		while (loop) { // FEED MONEY MENU
			String choice = (String) feedMoneyMenu.getChoiceFromOptions(FEED_MONEY_OPTIONS, currentBalance);
			BigDecimal add = BigDecimal.ZERO;
			add = add.setScale(2);
			if (choice.equals(FEED_MONEY_OPTION_ONE_DOLLAR)) {
				add = add.add(BigDecimal.valueOf(1.00));
				currentBalance = currentBalance.add(add);
			} else if (choice.equals(FEED_MONEY_OPTION_TWO_DOLLARS)) {
				add = add.add(BigDecimal.valueOf(2.00));
				currentBalance = currentBalance.add(add);
			} else if (choice.equals(FEED_MONEY_OPTION_FIVE_DOLLARS)) {
				add = add.add(BigDecimal.valueOf(5.00));
				currentBalance = currentBalance.add(add);
			} else if (choice.equals(FEED_MONEY_OPTION_TEN_DOLLARS)) {
				add = add.add(BigDecimal.valueOf(10.00));
				currentBalance = currentBalance.add(add);
			} else if (choice.equals(FEED_MONEY_OPTION_RETURN)) {
				loop = false;
				break;
			}
			
			vmLogger(LocalDate.now() + " " + LocalTime.now() + " FEED MONEY: $" + add + " $" + currentBalance);
		}
	}
	
	private void selectProduct() {
		displayMenu();
		ProductMenu productMenu = new ProductMenu(System.in, System.out);
		
		String[] productOptions = new String[itemList.size()];
		for (int i = 0; i < itemList.size(); i++) {
			productOptions[i] = itemList.get(i).getSlot();
		}
		
		String choice = (String) productMenu.getChoiceFromOptions(productOptions, currentBalance);
		
		for (int i = 0; i < itemList.size(); i++) {
			if (choice.equals(itemList.get(i).getSlot())) {
				if (itemList.get(i).isSoldOut()) {
					System.out.println("Product is sold out.  Please make another selection.");
				} else if (currentBalance.compareTo(itemList.get(i).getPrice()) < 0) {
					System.out.println("Not enough balance.  Please feed me more money");
				} else {
					vmLogger(LocalDate.now() + " " + LocalTime.now() + " " + itemList.get(i).getName() + " " + itemList.get(i).getSlot() + " $" + currentBalance + " $" +currentBalance.subtract(itemList.get(i).getPrice()));
					} 
					
					currentBalance = currentBalance.subtract(itemList.get(i).getPrice());
					itemList.get(i).buy();					
					
					System.out.println("You have selected " + itemList.get(i).getName());
					System.out.println("Item Price: $" + itemList.get(i).getPrice());
					System.out.println("Balance remaining: $" + currentBalance);
					System.out.println();
					System.out.println(itemList.get(i).getSound());
				}
			}
		}
	
	private void finishTransaction() {
		vmLogger(LocalDate.now() + " " + LocalTime.now() + " GIVE CHANGE: $" + currentBalance + " $0.00");
		System.out.print("Your change is " + currentBalance + ", given as: ");
		
		int quarters = currentBalance.divide(BigDecimal.valueOf(0.25)).intValue();
		currentBalance = currentBalance.remainder(BigDecimal.valueOf(0.25));
		
		int dimes = currentBalance.divide(BigDecimal.valueOf(0.10)).intValue();
		currentBalance = currentBalance.remainder(BigDecimal.valueOf(0.10));
		
		int nickels = currentBalance.divide(BigDecimal.valueOf(0.05)).intValue();
		currentBalance = currentBalance.remainder(BigDecimal.valueOf(0.05));
		
		System.out.println(quarters + " quarters, " + dimes + " dimes, and " + nickels + " nickels.");
//		System.out.println(currentBalance);
	}
	
	
	
}
