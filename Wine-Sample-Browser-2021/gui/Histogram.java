package uk.ac.sheffield.assignment2021.gui;

import uk.ac.sheffield.assignment2021.codeprovided.AbstractWineSampleCellar;
import uk.ac.sheffield.assignment2021.codeprovided.WineProperty;
import uk.ac.sheffield.assignment2021.codeprovided.WineSample;
import uk.ac.sheffield.assignment2021.codeprovided.gui.AbstractHistogram;
import uk.ac.sheffield.assignment2021.codeprovided.gui.HistogramBin;

import java.util.List;
import java.util.NoSuchElementException;


/**
 * 
 * Class extends the AbstractHistogram class. It does most of the calculations relating to the histogram
 * including frequency and bin intervals.
 * @author Ben Dawson
 *
 */
public class Histogram extends AbstractHistogram {
    
    /**
     * Constructor. Called by AbstractWineSampleBrowserPanel
     *
     * @param cellar              to allow for getting min / max / avg values
     * @param filteredWineSamples a List of WineSamples to generate a histogram for.
     *                            These have already been filtered by the GUI's queries.
     * @param property            the WineProperty to generate a histogram for.
     */
    public Histogram(AbstractWineSampleCellar cellar, List<WineSample> filteredWineSamples, WineProperty property)
    {
        super(cellar, filteredWineSamples, property);
    }
    
    /** 
     * Completely updates (i.e: resets) the Histogram, based on a newly selected property and wine samples.
     * @param property the WineProperty that the Histogram should plot
     * @param filteredWineSamples the WineSamples that have currently been filtered by the GUI
     */
    @Override
    public void updateHistogramContents(WineProperty property, List<WineSample> filteredWineSamples) {
        double minVal = 0, maxVal = 0, binInterval = 0;
        if (filteredWineSamples.size() > 0) {
            minVal = cellar.getMinimumValue(property, filteredWineSamples);
            maxVal = cellar.getMaximumValue(property, filteredWineSamples);
            binInterval = (((maxVal-minVal)/11.0)*100.0)/100.0;
        }
        
        wineCountsPerBin.clear();
        if (filteredWineSamples.size() == 0) {;
          
        } 
        else if (maxVal == minVal && ((maxVal > 0) || (minVal > 0))) {
            HistogramBin singleBin = new HistogramBin(minVal, maxVal, true);
            wineCountsPerBin.put(singleBin, filteredWineSamples.size());
        } 
        else {
            int wineAmount = 0;
            boolean finalBin = false;
            double currentLowerBound = minVal;
            double currentUpperBound = minVal+binInterval;
            for (int i=0; i<11; i++) {
                if (i==10) {
                    finalBin = true;
                }
                wineAmount = 0;
                for (int j=0; j<filteredWineSamples.size(); j++) {
                    double propertyValue = filteredWineSamples.get(j).getProperty(property);
                    if ((propertyValue >= currentLowerBound) && (propertyValue < currentUpperBound)) {
                        wineAmount += 1;
                    }
                }            
                wineCountsPerBin.put((new HistogramBin(currentLowerBound, currentUpperBound, finalBin)), wineAmount);
                currentLowerBound = currentUpperBound;
                currentUpperBound += binInterval;
            }
        }
    }

    /**
     * Finds the mean value of the given property within the given array.=
     * @return The average value
     */
    @Override
    public double getAveragePropertyValue() throws NoSuchElementException { 
        double avgVal = cellar.getMeanAverageValue(property, filteredWineSamples);
        return avgVal;
    }
}
