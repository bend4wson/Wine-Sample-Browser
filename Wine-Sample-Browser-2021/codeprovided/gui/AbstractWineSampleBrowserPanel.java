package uk.ac.sheffield.assignment2021.codeprovided.gui;
// import statements

import uk.ac.sheffield.assignment2021.codeprovided.*;
import uk.ac.sheffield.assignment2021.gui.Histogram;
import uk.ac.sheffield.assignment2021.gui.HistogramPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * Class that provides a basic implementation of the main elements in the GUI as discussed in the handout.
 * Please consider the methods that need to be implemented by overriding, including the method described in the
 * note at the end of this class.
 *
 * Should be implemented as uk.ac.sheffield.assignment2021.gui.WineSampleBrowserPanel
 *
 * @version 1.1 09/02/2021
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ben Clegg
 *
 * Copyright (c) University of Sheffield 2021
 */

// constructor
@SuppressWarnings("ALL")
public abstract class AbstractWineSampleBrowserPanel extends JPanel {

    // instance variables
	
    // cellar provides a convenient access to the original datasets - might be of use
	protected final AbstractWineSampleCellar cellar;
	// filteredWineSampleList is the list that needs to be kept up to date with the results of
	// running the list of SubQuery objects in subQueryList
    protected List<WineSample> filteredWineSampleList;
    
    // subQueryList contains all the subqueries as they are added when the button buttonAddFilter is clicked
	// the contents of subQueryList will need to be cleared if the buttonClearFilters is clicked
	protected List<SubQuery> subQueryList = new ArrayList<>();
	    
    // by default all wine samples are used
    protected WineType wineType = WineType.ALL;
    
    // histogram being shown
    protected AbstractHistogram histogram;
    
   
    // starting the definition of buttons in GUI
    // buttonAddFilter will add a new subquery to subQueryList when clicked
    // clicking this button implies calling the method addFilter(...)
    protected final JButton buttonAddFilter = new JButton("Add Filter");
    // buttonClearFilters will remove all subqueries in subQueryList when clicked
    // clicking this button implies calling the method clearFilters(...)
    protected final JButton buttonClearFilters = new JButton("Clear All Filters");

    // defining the combobox used to select the wine type to which the filters (subQueryList or list of SubQuery object) need to be applied
    protected String[] wineTypes = { WineType.ALL.name(), WineType.RED.name(), WineType.WHITE.name() };
    protected JComboBox<String> comboWineTypes = new JComboBox<>(wineTypes);

    Vector<String> propertyValues = new Vector<>(Arrays.stream(WineProperty.values())
            .map(WineProperty::getName).collect(Collectors.toList()));

    protected JComboBox<String> comboQueryProperties = new JComboBox<>(propertyValues);

    // defining the combobox used to select the operator that will be used to build the filter (or SubQuery object) than will be applied
    protected String[] operators = {">", ">=", "<", "<=", "=", "!="};
    protected JComboBox<String> comboOperators = new JComboBox<>(operators);

    // defining the textfield where the value of the SubQuery (or filter)
    protected JTextField value = new JTextField(5);

    // defining all the labels to facilitate the what goes where in the GUI
    protected JLabel wineTypeSelectorLabel = new JLabel("Wine list:", SwingConstants.LEFT);
    protected JLabel subQueryLabel = new JLabel("Filter by property:", SwingConstants.LEFT);
    protected JLabel operatorLabel = new JLabel("Operator:", SwingConstants.LEFT);
    protected JLabel operatorValueLabel = new JLabel("Value:", SwingConstants.LEFT);
    protected JLabel subQueryListLabel = new JLabel("List of filters (or subqueries):", SwingConstants.LEFT);
    protected JLabel histogramPropertyLabel = new JLabel("Histogram wine property:", SwingConstants.LEFT);
    
    // defining the three JTextAreas that will need to be updated every time the buttons
    // buttonAddFilter and buttonClearFilters are clicked
    // subQueriesTextArea will show the contents of subQueryList
    protected JTextArea subQueriesTextArea = new JTextArea(1, 50);
    // statisticsTextArea will show basic summary statistics for the filteredWineSampleList 
    // (which contains the results after executing the filters or SubQuery in subQueryList)
    protected JTextArea statisticsTextArea = new JTextArea(20, 50);
    // samplesTextArea will show the results contained in the filteredWineSampleList object
    protected JTextArea filteredWineSamplesTextArea = new JTextArea(20, 50);

    // defining the GUI combobox to select the property that will show the histogram
    protected JComboBox<String> comboHistogramProperties = new JComboBox<>(propertyValues);
    
    // titles for TitleBorder objects used to name the three main GUI areas
    protected String statisticsTitle = "WINE SAMPLES STATISTICS";
    protected String wineSamplesTitle = "WINE SAMPLES";
    protected String histogramTitle = "HISTOGRAM";
    
    
    // Constructor
    public AbstractWineSampleBrowserPanel(AbstractWineSampleCellar cellar) {
    	Border blackline = BorderFactory.createLineBorder(Color.black);
		
    	// providing access to the cellar so that this class can access its contents if required 
        this.cellar = cellar;
        // by default the GUI starts showing all wine samples 
        this.filteredWineSampleList = cellar.getWineSampleList(WineType.ALL);

        subQueriesTextArea.setName("subQueries");  
        comboQueryProperties.setName("wineProperties");
        value.setName("filterValue");
        filteredWineSamplesTextArea.setName("filteredWineSamples");
        statisticsTextArea.setName("wineStats");
        comboOperators.setName("operators");
        comboWineTypes.setName("wineTypes");
        buttonAddFilter.setName("addFilter");
        buttonClearFilters.setName("clearFilters");
        
        // building the GUI using a combination of JPanels and a range of LayoutManagers
        // to get a structured GUI
        this.setLayout(new BorderLayout());

        // Query panel
        JPanel queryPanel = new JPanel();
        queryPanel.setLayout(new GridLayout(4, 1));

        JPanel typeSelectorPanel = new JPanel();
        typeSelectorPanel.setLayout(new FlowLayout());
        typeSelectorPanel.add(wineTypeSelectorLabel);
        typeSelectorPanel.add(comboWineTypes);
        JPanel filterBuilderPanel = new JPanel();
        filterBuilderPanel.setLayout(new FlowLayout());
        filterBuilderPanel.add(subQueryLabel);
        filterBuilderPanel.add(comboQueryProperties);
        filterBuilderPanel.add(operatorLabel);
        filterBuilderPanel.add(comboOperators);
        filterBuilderPanel.add(operatorValueLabel);
        filterBuilderPanel.add(value);
        filterBuilderPanel.add(buttonAddFilter);
        filterBuilderPanel.add(buttonClearFilters);

        queryPanel.add(typeSelectorPanel);
        queryPanel.add(filterBuilderPanel);
        queryPanel.add(subQueryListLabel);

        JScrollPane jscQueries = new JScrollPane(subQueriesTextArea);
        queryPanel.add(jscQueries);
        queryPanel.setBorder(blackline);
        
        // Histogram plot panel
        JPanel histogramContainer = new JPanel();
        JPanel controlHistogramContainer = new JPanel();
        
        histogramContainer.setLayout(new BorderLayout());
        controlHistogramContainer.add(histogramPropertyLabel, BorderLayout.WEST);
        controlHistogramContainer.add(comboHistogramProperties, BorderLayout.EAST);
        
        histogram = new Histogram(cellar, filteredWineSampleList, WineProperty.Quality);
        AbstractHistogramPanel histogramPanel = new HistogramPanel(this, histogram);
        histogramContainer.add(histogramPanel, BorderLayout.CENTER);
        histogramContainer.add(controlHistogramContainer, BorderLayout.SOUTH);
        
        TitledBorder tbHistogram = BorderFactory.createTitledBorder(
		        blackline, histogramTitle);
        tbHistogram.setTitleJustification(TitledBorder.CENTER);
		histogramContainer.setBorder(tbHistogram);

        
        // Statistics panel
        JPanel wineDisplayPanel = new JPanel();
        wineDisplayPanel.setLayout(new FlowLayout());
        JPanel statisticsPanel = new JPanel();
        statisticsPanel.setLayout(new BorderLayout());
        JScrollPane statisticsScrollPane = new JScrollPane(statisticsTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        statisticsPanel.add(statisticsScrollPane, BorderLayout.CENTER);
        statisticsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        TitledBorder tbStatistics = BorderFactory.createTitledBorder(
		        blackline, statisticsTitle);
        tbStatistics.setTitleJustification(TitledBorder.CENTER);
        statisticsPanel.setBorder(tbStatistics);

        
        // Filtered samples panel
        JPanel samplesPanel = new JPanel();
        samplesPanel.setLayout(new BorderLayout());
        JScrollPane samplesScrollPane = new JScrollPane(filteredWineSamplesTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        samplesPanel.add(samplesScrollPane, BorderLayout.CENTER);
        samplesPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        TitledBorder tbWineSamples = BorderFactory.createTitledBorder(
		        blackline, wineSamplesTitle);
        tbWineSamples.setTitleJustification(TitledBorder.CENTER);
		samplesPanel.setBorder(tbWineSamples);
		        
        wineDisplayPanel.add(statisticsPanel);
        wineDisplayPanel.add(samplesPanel);

        this.add(queryPanel, BorderLayout.NORTH);
        this.add(histogramContainer, BorderLayout.CENTER);
        this.add(wineDisplayPanel, BorderLayout.SOUTH);

        // adding the listeners, you will need to implement this method to register the events generated
        // by the GUI components that will be expecting a change in the results being displayed by the GUI
        this.addListeners();
    }

    /**
     * Updates the histogram with the currently filtered wines and selected property
     */
    public void updateHistogram() {
        WineProperty property = WineProperty.fromName((String) comboHistogramProperties.getSelectedItem());
        histogram.updateHistogramContents(property, filteredWineSampleList);
    }

    /**
     * @return the active SubQueries
     */
    public List<SubQuery> getAllSubQueries() {
    	return subQueryList;
    }

    /**
     * getFilteredWineSampleList method - getter of filtered wine sample list
     * @return List of WineSample objects after running filtering SubQueries
     */
    public List<WineSample> getFilteredWineSampleList() {
    	return filteredWineSampleList;
    }

    // list of abstract methods starts
    
    /**
     * addListeners method - adds relevant actionListeners to the GUI components
     * You will need to listen (at least) to the following:
     * - buttonAddFilter
     * - buttonClearFilters
     * - comboWineTypes, if you want the samplesTextArea to be updated to show only the wine samples
     *            specified by this combobox
     * - comboHistogramProperties, to update the property that the histogram should display
     */
    public abstract void addListeners();

    /**
     * addFilter method - 
     * 1- this method is called when the JButton buttonAddFilter is clicked 
     * 2- adds a new filter (a SubQuery object) to subQueryList ArrayList
     * 3- updates the GUI results accordingly, i.e. updates the three JTextAreas as follows:
     *    3a- subQueriesTextArea will show the new SubQuery
     *    3b- statisticsTextArea will show the updated statistics for the results after applying this filter
     *    3c- samplesTextArea will show the contents of filteredWineList (the results after applying this filter)
     *    3d- the histogram is updated to display the newly filtered samples (Note: this can alternatively be done
     *    in another method)
     */
    public abstract void addFilter();

    /**
     * clearFilters method - clears all filters from the subQueryList ArrayList and updates
     * the relevant GUI components when the button buttonClearFilters is clicked
     */
    public abstract void clearFilters();

    /**
     * updateStatistics method - updates the statistics to be displayed in the 
     * statisticsTextArea when the results being shown in the GUI need to be updated,
     * recalculates the average, minimum and maximum values for each wine property.
     */
    public abstract void updateStatistics();

    /**
     * updateWineDetailsBox method - updates the wine details panel when changes are made
     */
    public abstract void updateWineDetailsBox();

    /**
     * executeQuery method - executes the complete query to the relevant wine list
     */
    public abstract void executeQuery();

    /*
     * NOTE: You will also need to override JPanel's paintComponent(Graphics g) method, to redraw the GUI
     * Remember that to redraw your panel, you should never call the paintComponent(Graphics g) explicitly,
     * you will be calling instead the repaint() method! (Check lab sheet of week 7).
     * The repaint() method will make sure that the appropriate paintComponent is called.
     */

}
