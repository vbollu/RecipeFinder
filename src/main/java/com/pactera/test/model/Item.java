package com.pactera.test.model;

import java.util.Date;

import com.pactera.test.Unit;

public class Item {

	private String itemName;
	private Integer amount;
	private Unit unit;
	private Date  useBy;
	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	/**
	 * @return the amount
	 */
	public Integer getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	/**
	 * @return the useBy
	 */
	public Date getUseBy() {
		return useBy;
	}
	/**
	 * @param useBy the useBy to set
	 */
	public void setUseBy(Date useBy) {
		this.useBy = useBy;
	}
	/**
	 * @return the unit
	 */
	public Unit getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
}
 