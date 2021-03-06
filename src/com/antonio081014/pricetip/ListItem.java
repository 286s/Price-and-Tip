/**
 * 
 */
package com.antonio081014.pricetip;

/**
 * @author antonio081014
 * 
 */
public class ListItem {

	private double price;
	private int quantity;

	/**
	 * @param price
	 * @param quantity
	 */
	public ListItem(double price, int quantity) {
		this.price = price;
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
