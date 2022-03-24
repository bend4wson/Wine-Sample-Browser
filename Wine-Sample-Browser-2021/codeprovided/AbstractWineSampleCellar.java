package uk.ac.sheffield.assignment2021.codeprovided;

// Import statements

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Abstract class designed to be extended. 
 * Provides basic reading functionalities of wine sample datasets and queries.
 *
 * @version 1.1 09/02/2021
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ben Clegg
 *
 * Copyright (c) University of Sheffield 2021
 */

public abstract class AbstractWineSampleCellar {

	protected final Map<WineType, List<WineSample>> wineSampleRacks;

	/**
	 * Constructor - reads wine sample datasets and list of queries from text file,
	 * and initialises the wineSampleRacks Map
     */
    public AbstractWineSampleCellar(String redWineFilename, String whiteWineFilename) {

        wineSampleRacks = new HashMap<>();
        // editWineList will read the data and will insert the list of samples into the wineSampleRacks variable 
        editWineList(WineType.RED, redWineFilename);
        editWineList(WineType.WHITE, whiteWineFilename);

        // updateCellar will update wineSampleRacks to contain 'also' an additional list containing all wine samples
        // (in this case red and white)
        updateCellar();
    }

    public int getNumberWineSamples(WineType wineType)
    {
        return wineSampleRacks.get(wineType).size();
    }

    /**
     * Reads the two CSV files and the type passed by main. It then reads the contents of the files
     * and creates the relevant wine sample objects and returns them into a list. Catches exception errors
     * should they occur.
     *
     * @param wineFile This is either the redWineFile or whiteWineFile depending on which list called the method
     * @param wineType This is a WineType enum containing either red or white depending on which list called the method
     * @return List of WineSample objects
     */
    public List<WineSample> readWineFile(String wineFile,  WineType wineType) throws IllegalArgumentException {
        List<WineSample> wineList = new ArrayList<>();
        int count = 1;
        wineFile = wineFile.replaceAll(" ", "");

        try (BufferedReader br = new BufferedReader(new FileReader(wineFile))) {
            String line = br.readLine();
            if (line == null) {
                throw new IllegalArgumentException("File is empty. Provide a valid dataset.");
            }
            while ((line = br.readLine()) != null) {
                try {
                    // The wine sample ID is created by this reader; it is not provided in the original files
                    // The ID should _not_ be modified later
                    int id = count;		
                    WineSample wine = new WineSample(id, wineType, parseWineFileLine(line));
                    wineList.add(wine);
                    count++;

                } catch (NumberFormatException e) {
                    System.err.println("File format is incorrect; only double values are allowed.");
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    System.err.println("Malformed wine sample line: " + line);
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println(wineFile + " could not be found. Provide a correct filename.");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return wineList;
    }

    /**
     * Parse the properties from a given line from a wine file.
     * You can expect that each value appears in the same order as the columns in the file),
     * and that this order will not change.
     * Example line:
     * 7.9;0.6;0.06;1.6;0.069;15;59;0.9964;3.3;0.46;9.4;5
     *
     * @param line the line to parse
     * @return a WinePropertyMap constructed from the parsed row, containing values for every property
     * @throws IllegalArgumentException if the line is malformed (i.e. does not include every property
     * for a single wine, or contains undefined properties)
     */
    public abstract WinePropertyMap parseWineFileLine(String line) throws IllegalArgumentException;
    
    
    /**
     * Updates wineSampleRacks to contain 'also' an additional list
     * containing ALL wine samples (in this case red and white)
     * Note: this should not modify the other wine lists
     */
    public abstract void updateCellar();

    /**
     * Read the contents of filename and stores it
     * @param wineType wine rack type to modify
     * @param filename the name of the .csv to read
     */
    public void editWineList(WineType wineType, String filename){
        wineSampleRacks.put(wineType, new ArrayList<>(readWineFile(filename, wineType)));
    }
    
    /**
     * returns the list of wine samples relevant to the specified Wine Type.
     * Returns a List<WineSample>.
     *
     * @param wineType the wine type to retrieve
     * @return a list of RED, WHITE, or ALL WineSamples
     */
    public List<WineSample> getWineSampleList(WineType wineType) {
        return wineSampleRacks.get(wineType);
    }

    /**
     * getWineSampleCount method - returns how many wine samples of the given type are stored
     *
     * @param wineType Either RED, WHITE or ALL
     * @return number of wines held of type <code>wineType</code>
     */
    public int getWineSampleCount(WineType wineType) {
        List<WineSample> list = getWineSampleList(wineType);
        return list.size(); // list should never be null
    }

    /**
     * Get the minimum value of the given property for wines of wineType in this Cellar
     * @param wineProperty the property to evaluate
     * @param wineType the WineType to use
     * @return the minimum measurement of the property
     */
    public double getMinimumValue(WineProperty wineProperty, WineType wineType) {
        return getMinimumValue(wineProperty, getWineSampleList(wineType));
    }

    /**
     * Get the maximum value of the given property for wines of wineType in this Cellar
     * @param wineProperty the property to evaluate
     * @param wineType the WineType to use
     * @return the maximum measurement of the property
     */
    public double getMaximumValue(WineProperty wineProperty, WineType wineType) {
        return getMaximumValue(wineProperty, getWineSampleList(wineType));
    }

    /**
     * Get the mean value of the given property for wines of wineType in this Cellar
     * @param wineProperty the property to evaluate
     * @param wineType the WineType to use
     * @return the mean measurement of the property
     */
    public double getMeanAverageValue(WineProperty wineProperty, WineType wineType) {
        return getMeanAverageValue(wineProperty, getWineSampleList(wineType));
    }

    /**
     * Get the minimum value of the given property for the specified wines
     * Note: these wines do not necessarily belong to the current cellar
     * @param wineProperty the property to evaluate
     * @param wineList the wines to check
     * @return the minimum measurement of the property present in the specified wines
     * @throws NoSuchElementException if there are no minimum values for the property, because wineList is invalid
     */
    public abstract double getMinimumValue(WineProperty wineProperty, List<WineSample> wineList)
            throws NoSuchElementException;

    /**
     * Get the maximum value of the given property for the specified wines
     * Note: these wines do not necessarily belong to the current cellar
     * @param wineProperty the property to evaluate
     * @param wineList the wines to check
     * @return the maximum measurement of the property present in the specified wines
     * @throws NoSuchElementException if there are no maximum values for the property, because wineList is invalid
     */
    public abstract double getMaximumValue(WineProperty wineProperty, List<WineSample> wineList)
            throws NoSuchElementException;

    /**
     * Get the mean value of the given property for the specified wines
     * Note: these wines do not necessarily belong to the current cellar
     * @param wineProperty the property to evaluate
     * @param wineList the wines to check
     * @return the mean measurement of the property present in the specified wines
     * @throws NoSuchElementException if there are no maximum values for the property, because wineList is invalid
     */
    public abstract double getMeanAverageValue(WineProperty wineProperty, List<WineSample> wineList)
            throws NoSuchElementException;

    /**
     * Get the first 5 wines of the given wine type
     * Note: this will only be applied to RED and WHITE wines; not WineType.ALL
     * @param type the WineType to get the first wines of
     * @return a List of the first 5 wines of the given type
     */
    public abstract List<WineSample> getFirstFiveWines(WineType type);
}
