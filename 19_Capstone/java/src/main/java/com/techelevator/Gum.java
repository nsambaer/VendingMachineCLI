package com.techelevator;

import java.math.BigDecimal;

public class Gum implements Vendable {

	private String name;
	private BigDecimal price;
	private String sound;
	private String slot;
	private int stock;
	private boolean soldOut;
	private int sales;
	
	public Gum(String name, BigDecimal price, String slot) {
		this.name = name;
		this.price = price;
		this.price = this.price.setScale(2);
		this.slot = slot;
		this.sound = "Chew Chew, yum!";
		this.stock = 5;
		this.soldOut = false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public BigDecimal getPrice() {
		return price;
	}

	@Override
	public String getSound() {
		return sound;
	}

	@Override
	public String getSlot() {
		return slot;
	}

	@Override
	public String getStock() {
		if (!soldOut) {
		return Integer.toString(stock);
		} else return "SOLD OUT";
	}
	
	public int getSales() {
		return sales;
	}

	@Override
	public void buy() {
		this.stock--;
		if (stock == 0) {
			soldOut = true;
		}
		sales++;
	}

	public boolean isSoldOut() {
		return soldOut;
	}

}
