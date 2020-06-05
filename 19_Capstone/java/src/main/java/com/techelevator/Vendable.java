package com.techelevator;

import java.math.BigDecimal;

public interface Vendable {

	public String getName();
	public BigDecimal getPrice();
	public String getSound();
	public String getSlot();
	public String getStock();
	public void buy();
	public boolean isSoldOut();
	public int getSales();
}
