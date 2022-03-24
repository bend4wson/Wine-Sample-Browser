package uk.ac.sheffield.assignment2021.codeprovided;

import java.util.Arrays;

/**
 * Class designed to create SubQuery objects which make up parts of an individual query.
 *
 * @version 1.1  09/02/2021
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ben Clegg
 *
 * Copyright (c) University of Sheffield 2021
 */
public class SubQuery
{
	public static final String[] VALID_OPERATORS = {"<", "<=", "=", ">=", ">", "!="};

    // Instance variables
	final WineProperty wineProperty;
    final String operator;
    final double value;

    // Constructor
    public SubQuery(WineProperty wineProperty, String operator, double value) {
        this.wineProperty = wineProperty;
        this.operator = operator;
        this.value = value;
    }

    public WineProperty getWineProperty()
    {
        return wineProperty;
    }

    public String getOperator() {
        return operator;
    }

    public double getValue() {
        return value;
    }
    
    public String toString() {
    	return this.getWineProperty() + " " +
                this.getOperator() + " " +
                this.getValue();
    }

	/**
	 * Check if a WineSample satisfies the SubQuery
	 * @param wineSample the WineSample to check
	 * @return true if the wine matches the SubQuery; false otherwise
	 */
	protected boolean wineMatchesSubQuery(WineSample wineSample) {
		WineProperty wineProperty = getWineProperty();
		double propertyValue = getValue();

		switch (getOperator()) {

			case ">":
				if (wineSample.getProperty(wineProperty) > propertyValue)
					return true;
				break;
			case ">=":
				if (wineSample.getProperty(wineProperty) >= propertyValue)
					return true;
				break;
			case "<":
				if (wineSample.getProperty(wineProperty) < propertyValue)
					return true;
				break;
			case "<=":
				if (wineSample.getProperty(wineProperty) <= propertyValue)
					return true;
				break;
			case "=":
				if (wineSample.getProperty(wineProperty) == propertyValue)
					return true;
				break;
			case "!=":
				if (wineSample.getProperty(wineProperty) != propertyValue)
					return true;
				break;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
		long temp;
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((wineProperty == null) ? 0 : wineProperty.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubQuery other = (SubQuery) obj;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		if (Double.doubleToLongBits(value) != Double.doubleToLongBits(other.value))
			return false;
		return wineProperty == other.wineProperty;
	}

	/**
	 * Check if a String is a valid operator
	 * @param operatorToCheck the String to check
	 * @return true if the String is a valid operator; false otherwise
	 */
	public static boolean isValidOperator(String operatorToCheck) {
    	return Arrays.asList(VALID_OPERATORS).contains(operatorToCheck);
	}
    
}
