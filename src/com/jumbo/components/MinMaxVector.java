package com.jumbo.components;

import com.jumbo.tools.calculations.Dice;

public class MinMaxVector {
	public int min = 0, max = 0;

	public boolean inRange(int i) {
		return i >= min && i <= max;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + max;
		result = prime * result + min;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MinMaxVector other = (MinMaxVector) obj;
		if (max != other.max) {
			return false;
		}
		if (min != other.min) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "MinMaxVector[ min: " + min + ", max: " + max + " ]";
	}

	public MinMaxVector(int minoffset, int maxoffset) {
		min = minoffset;
		max = maxoffset;
	}

	public int getMinimum() {
		return min;
	}

	public void setMinimum(int minOffset) {
		this.min = minOffset;
	}

	public int getMaximum() {
		return max;
	}

	public void setMaximum(int maxOffset) {
		this.max = maxOffset;
	}

	public int roll() {
		if (max <= 0 && min <= 0) {
			return -((Dice.roll(Math.abs(max) - Math.abs(min) + 1) - 1) + Math.abs(min));
		}
		return (Dice.roll(max - min + 1) - 1) + min;
	}
}
