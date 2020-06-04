package com.techelevator;

import java.math.BigDecimal;

public class Chip implements Vendable {

	private String name;
	private BigDecimal price;
	private String sound;
	private String slot;
	private int stock;
	private boolean soldOut;
	
	public Chip(String name, BigDecimal price, String slot) {
		this.name = name;
		this.price = price;
		this.slot = slot;
		this.sound = "Crunch Crunch, yum!";
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

	@Override
	public void buy() {
		this.stock--;
		if (stock == 0) {
			soldOut = true;
		}
	}

}
