package uk.ac.sheffield.assignment2021.codeprovided.gui;

/**
 * A bin of a histogram.
 * See AbstractHistogram for more details.
 *
 * @version 1.0 02/03/2021
 *
 * @author Ben Clegg
 *
 * Copyright (c) University of Sheffield 2021
 */
public class HistogramBin implements Comparable<HistogramBin>
{
    private final double lowerBoundary;
    private final double upperBoundary;
    private final boolean finalBin;

    /**
     * Constructor
     * @param lowerBoundary the lower value of the bin
     * @param upperBoundary the upper value of the bin
     * @param finalBin true if this is the last bin in the histogram (i.e. the bin for the highest measurements)
     */
    public HistogramBin(double lowerBoundary, double upperBoundary, boolean finalBin)
    {
        this.lowerBoundary = lowerBoundary;
        this.upperBoundary = upperBoundary;
        this.finalBin = finalBin;
    }

    /**
     * Is a given property measurement in this bin?
     * @param valueToCheck the property measurement to check
     * @return true if the value is in this bin's range; false otherwise.
     */
    public boolean valueInBin(double valueToCheck)
    {
        if (valueToCheck < lowerBoundary)
            return false;
        // Special case for final bin (i.e. the bin that should contain maximum values)
        if (finalBin && valueToCheck == upperBoundary)
            return true;
        //noinspection RedundantIfStatement
        if (valueToCheck >= upperBoundary)
            return false;
        return true;
    }

    public double getLowerBoundary()
    {
        return lowerBoundary;
    }

    public double getUpperBoundary()
    {
        return upperBoundary;
    }

    // Comparator for sorting bins; used in AbstractHistogram.getBinsInBoundaryOrder()
    @Override
    public int compareTo(HistogramBin other)
    {
        return Double.compare(this.lowerBoundary, other.lowerBoundary);
    }
}
