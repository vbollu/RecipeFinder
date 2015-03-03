/**
 * 
 */
package com.pactera.test;

/**
 * @author vikram
 *
 */
public enum Unit {

	OF("Of"), GRAMS("Grams"), ML("Milliliters"), SLICES("Slices");

	private String name;

	private Unit(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
