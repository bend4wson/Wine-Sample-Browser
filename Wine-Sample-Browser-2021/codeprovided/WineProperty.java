package uk.ac.sheffield.assignment2021.codeprovided;

import java.util.NoSuchElementException;

/**
 * Provides a helper enum with constants representing the wine properties of a wine sample.
 *
 * @version 1.0  06/04/2019
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 *
 * Copyright (c) University of Sheffield 2021
 */

public enum WineProperty {
    FixedAcidity("Fixed Acidity"),
    VolatileAcidity("Volatile Acidity"),
    CitricAcid("Citric Acid"),
    ResidualSugar("Residual Sugar"),
    Chlorides("Chlorides"),
    FreeSulfurDioxide("Free Sulfur Dioxide"),
    TotalSulfurDioxide("Total Sulfur Dioxide"),
    Density("Density"),
    PH("pH"),
    Sulphates("Sulphates"),
    Alcohol("Alcohol"),
    Quality("Quality");

    private final String propertyName;

    WineProperty(String pName) { propertyName = pName; }

    public String getName() { return this.propertyName; }

    /**
     * Convert an identifier String (e.g. "qual") to the matching WineProperty
     * @param queryFileIdentifier the identifier to convert
     * @return the matching WineProperty
     * @throws NoSuchElementException if the String does not match any WineProperties
     */
    public static WineProperty fromFileIdentifier(String queryFileIdentifier) throws NoSuchElementException
    {
        switch (queryFileIdentifier)
        {
            case "qual":
                return WineProperty.Quality;
            case "f_acid":
                return WineProperty.FixedAcidity;
            case "v_acid":
                return WineProperty.VolatileAcidity;
            case "c_acid":
                return WineProperty.CitricAcid;
            case "r_sugar":
                return WineProperty.ResidualSugar;
            case "chlorid":
                return WineProperty.Chlorides;
            case "f_sulf":
                return WineProperty.FreeSulfurDioxide;
            case "t_sulf":
                return WineProperty.TotalSulfurDioxide;
            case "dens":
                return WineProperty.Density;
            case "ph":
                return WineProperty.PH;
            case "sulph":
                return WineProperty.Sulphates;
            case "alc":
                return WineProperty.Alcohol;
        }
        throw new NoSuchElementException("No such property!");
    }

    /**
     * Convert a name String (e.g. "Fixed Acidity") to the matching WineProperty
     * @param name the String to convert
     * @return the matching WineProperty
     * @throws NoSuchElementException if the String does not match any WineProperties
     */
    public static WineProperty fromName(String name) throws NoSuchElementException {
        for (WineProperty p : values()) {
            if (p.getName().equals(name))
                return p;
        }
        throw new NoSuchElementException("No such property (" + name + ")!");
    }
}