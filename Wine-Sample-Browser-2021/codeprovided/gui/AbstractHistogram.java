package uk.ac.sheffield.assignment2021.codeprovided.gui;

import uk.ac.sheffield.assignment2021.codeprovided.AbstractWineSampleCellar;
import uk.ac.sheffield.assignment2021.codeprovided.WineProperty;
import uk.ac.sheffield.assignment2021.codeprovided.WineSample;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract class providing some functionality to store and access a histogram
 * Histograms split a set of values (e.g. property measurements) into a number of "bins", between the minimum
 * and maximum values of the set of values. Each value is added to the bin that it matches to.
 * These bins can then be plotted as bars; see AbstractHistogramPanel
 *
 * Should be implemented as uk.ac.sheffield.assignment2021.gui.Histogram
 *
 * @version 1.0 02/03/2021
 *
 * @author Ben Clegg
 *
 * Copyright (c) University of Sheffield 2021
 */
public abstract class AbstractHistogram {

    protected static final int NUMBER_BINS = 11;

    protected Map<HistogramBin, Integer> wineCountsPerBin;

    protected final AbstractWineSampleCellar cellar;
    protected List<WineSample> filteredWineSamples;
    protected WineProperty property;

    protected double minPropertyValue = 0;
    protected double maxPropertyValue = 1;


    /**
     * Constructor. Called by AbstractWineSampleBrowserPanel
     * @param cellar to allow for getting min / max / avg values
     * @param filteredWineSamples a List of WineSamples to generate a histogram for.
     *                            These have already been filtered by the GUI's queries.
     * @param property the WineProperty to generate a histogram for.
     */
    public AbstractHistogram(AbstractWineSampleCellar cellar,
                             List<WineSample> filteredWineSamples,
                             WineProperty property) {
        this.cellar = cellar;
        this.filteredWineSamples = filteredWineSamples;
        this.property = property;
        this.wineCountsPerBin = new TreeMap<>();
        updateHistogramContents(property, filteredWineSamples);
    }

    /**
     * This method should completely update (i.e. reset) the Histogram,
     * based on a newly selected property and wine samples.
     * Since these values may have changed completely, it is recommended that you generate an entirely new
     * set of HistogramBins with the appropriate boundary values according to the target number of bins.
     * WinePropertyMap may give you some hints on how to use the Map interface.
     * You may find it useful to use numWinesInBins.put(bin, 0) to initialise each new bin.
     * Note: if all of the values of a property are equal, it is sufficient to only use one bin.
     * Similarly, if there are no wines, the histogram should not have any bins.
     * In other cases, the conventional 11 bins should work.
     * @param property the WineProperty that the Histogram should plot
     * @param filteredWineSamples the WineSamples that have currently been filtered by the GUI
     */
    public abstract void updateHistogramContents(WineProperty property, List<WineSample> filteredWineSamples);

    /**
     * Calculate the average (mean) value of a given property from the current filteredWineSamples
     * @return the average value of property in filteredWineSamples
     */
    public abstract double getAveragePropertyValue() throws NoSuchElementException;

    /**
     * Get the filled bins of the histogram.
     * @return bins, in ascending order of their boundaries.
     */
    public List<HistogramBin> getBinsInBoundaryOrder() {
        return wineCountsPerBin.keySet().stream().sorted().collect(Collectors.toList());
    }

    /**
     *
     * @param bin the bin to evaluate
     * @return the number of wines in the bin
     */
    public int getNumWinesInBin(HistogramBin bin) {
        return wineCountsPerBin.get(bin);
    }

    public double getMinPropertyValue() {
        return minPropertyValue;
    }

    public double getMaxPropertyValue() {
        return maxPropertyValue;
    }

    /**
     * Get the size of the biggest bin. May be useful for scaling the plot.
     * @return the number of wines in the bin with the most wines
     */
    public int largestBinCount() {
        int largestCount = 0;
        for (int count : wineCountsPerBin.values()) {
            if (count > largestCount)
                largestCount = count;
        }
        return largestCount;
    }

    public Map<HistogramBin, Integer> getWineCountsPerBin()
    {
        return wineCountsPerBin;
    }
}
