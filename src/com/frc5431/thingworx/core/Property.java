package com.frc5431.thingworx.core;

public class Property {

	public Object value;

	public Property(Object def) {
		value = def;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

}
