package uk.ac.sheffield.assignment2021.codeprovided;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * A simplified Map wrapper to be used by a WineSample, storing the values for each of its properties.
 * This wrapper stores a HashMap rather than extending it to de-expose unneeded methods.
 * Copyright (c) University of Sheffield 2021
 *
 * @version 1.0 10/02/2021
 *
 * @author Ben Clegg
 */
public class WinePropertyMap {

    // Consult the Javadocs for Map & HashMap for more information
    private final Map<WineProperty, Double> propertyToValuesMap = new HashMap<>();

    /**
     * Add a WineProperty and value pair. See HashMap.put() for more technical details.
     * @param wineProperty the property to store
     * @param value the value associated with the property
     */
    public void put(WineProperty wineProperty, double value) {
        propertyToValuesMap.put(wineProperty, value);
    }

    /**
     * Retrieve a value associated with a given WineProperty. See HashMap.get() for more technical details.
     * @param wineProperty the WineProperty to retrieve the value of
     * @return the retrieved value
     */
    public double get(WineProperty wineProperty) {
        return propertyToValuesMap.get(wineProperty);
    }

    /**
     * Get the properties stored in the map. See HashMap.keySet() for more technical details.
     * @return the properties stored in the map
     */
    public Set<WineProperty> propertySet() {
        return propertyToValuesMap.keySet();
    }

}
