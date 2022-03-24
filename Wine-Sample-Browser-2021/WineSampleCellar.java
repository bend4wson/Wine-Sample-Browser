package uk.ac.sheffield.assignment2021;

import uk.ac.sheffield.assignment2021.codeprovided.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * 
 * WineSampleCellar extends AbstractWineSampleCellar and provides functions such as parsing wine files,
 * updating the wine racks, and getting maximum, minimum and average values.
 * @author Ben Dawson
 *
 */
public class WineSampleCellar extends AbstractWineSampleCellar {
    
    /**
     * Constructor - reads wine sample datasets and list of queries from text file,
     * and initialises the wineSampleRacks Map
     *
     * @param redWineFilename
     * @param whiteWineFilename
     */
    public WineSampleCellar(String redWineFilename, String whiteWineFilename) {
        super(redWineFilename, whiteWineFilename);
    }
    
    /**
     * Parse the properties from a given line from a wine file.
     *
     * @param line the line to parse
     * @return a WinePropertyMap constructed from the parsed row, containing values for every property
     * @throws IllegalArgumentException if the line is malformed (i.e. does not include every property
     * for a single wine, or contains undefined properties)
     */
    @Override
    public WinePropertyMap parseWineFileLine(String line) throws IllegalArgumentException {
        WineProperty[] nameOfProperties = {WineProperty.FixedAcidity, WineProperty.VolatileAcidity, WineProperty.CitricAcid, WineProperty.ResidualSugar, WineProperty.Chlorides, WineProperty.FreeSulfurDioxide, WineProperty.TotalSulfurDioxide, WineProperty.Density, WineProperty.PH, WineProperty.Sulphates, WineProperty.Alcohol, WineProperty.Quality};
        String[] splitProperties = line.split(";");
        if (splitProperties.length != 12) {
            throw new IllegalArgumentException();
        }
        WinePropertyMap winePropertyMap = new WinePropertyMap();
        for (int i=0; i<splitProperties.length; i++) {
            winePropertyMap.put(nameOfProperties[i], Double.parseDouble(splitProperties[i]));
       }
        
       return winePropertyMap;
    }

    /**
     * Updates wineSampleRacks to contain 'also' an additional list
     * containing ALL wine samples (in this case red and white)
     */
    @Override
    public void updateCellar() {
        ArrayList<WineSample> allWineSamples = new ArrayList<>();
        for (WineSample currentRedSample : getWineSampleList(WineType.RED)) {
            allWineSamples.add(currentRedSample);
        }
        for (WineSample currentWhiteSample : getWineSampleList(WineType.WHITE)) {
            allWineSamples.add(currentWhiteSample);
        }

        wineSampleRacks.put(WineType.ALL, allWineSamples);
    }

    /**
     * Get the minimum value of the given property for wines of wineType in this Cellar
     * @param wineProperty the property to evaluate
     * @param wineType the WineType to use
     * @return the minimum measurement of the property
     */
    @Override
    public double getMinimumValue(WineProperty wineProperty, List<WineSample> wineList)
            throws NoSuchElementException {
        List<Double> properties = new ArrayList<Double>();
        for (WineSample val : wineList) {
            properties.add(val.getProperty(wineProperty));
        } 
        return Collections.min(properties);
    }

    /**
     * Get the maximum value of the given property for wines of wineType in this Cellar
     * @param wineProperty the property to evaluate
     * @param wineType the WineType to use
     * @return the maximum measurement of the property
     */
    @Override
    public double getMaximumValue(WineProperty wineProperty, List<WineSample> wineList)
            throws NoSuchElementException {
        
        List<Double> properties = new ArrayList<Double>();
        for (WineSample val : wineList) {     
            properties.add(val.getProperty(wineProperty));
        } 

        return Collections.max(properties);
    }

    /**
     * Get the mean value of the given property for wines of wineType in this Cellar
     * @param wineProperty the property to evaluate
     * @param wineType the WineType to use
     * @return the mean measurement of the property
     */
    @Override
    public double getMeanAverageValue(WineProperty wineProperty, List<WineSample> wineList)
            throws NoSuchElementException {
        int amountOfProperties = 0;
        double sum = 0;
        for (WineSample val : wineList) {
            sum = sum + val.getProperty(wineProperty);
            amountOfProperties += 1;
        }
        double average = sum/amountOfProperties;
        return average;
    }

    /**
     * Get the first 5 wines of the given wine type
     * Note: this will only be applied to RED and WHITE wines; not WineType.ALL
     * @param type the WineType to get the first wines of
     * @return a List of the first 5 wines of the given type
     */
    @Override
    public List<WineSample> getFirstFiveWines(WineType type) {
        List<WineSample> wineSamples = wineSampleRacks.get(type);
        List<WineSample> firstFiveWines = new ArrayList<WineSample>();
        for (int i=0; i<5; i++) {
            firstFiveWines.add(wineSamples.get(i));
        }
        return firstFiveWines;
    }
}
