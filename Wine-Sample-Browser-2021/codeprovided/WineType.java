package uk.ac.sheffield.assignment2021.codeprovided;

/**
 * WineType.java
 *
 * Provides a helper enum with constants representing the accepted wine types.
 *
 * @version 1.0  06/04/2019
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 *
 * Copyright (c) University of Sheffield 2021
 */

public enum WineType {
    ALL("all"),
    RED("red"),
    WHITE("white");

    private final String wineTypeName;

    WineType(String wTName) { wineTypeName = wTName; }

    public String getName()
    {
        return wineTypeName;
    }
}