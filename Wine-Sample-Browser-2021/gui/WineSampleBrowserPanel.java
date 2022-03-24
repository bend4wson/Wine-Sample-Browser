package uk.ac.sheffield.assignment2021.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import uk.ac.sheffield.assignment2021.codeprovided.AbstractWineSampleCellar;
import uk.ac.sheffield.assignment2021.codeprovided.Query;
import uk.ac.sheffield.assignment2021.codeprovided.SubQuery;
import uk.ac.sheffield.assignment2021.codeprovided.WineProperty;
import uk.ac.sheffield.assignment2021.codeprovided.WineSample;
import uk.ac.sheffield.assignment2021.codeprovided.WineType;
import uk.ac.sheffield.assignment2021.codeprovided.gui.AbstractWineSampleBrowserPanel;

/**
 * WineSampleBrowserPanel will take any valid interactions with the wine browser that the user makes,
 * and will takes the appropriate action. It will then update components of the browser in order to 
 * take into account any filters that the user may have inputted.
 * @author Ben Dawson
 *
 */
public class WineSampleBrowserPanel extends AbstractWineSampleBrowserPanel {
    
    static final WineProperty[] VALID_PROPERTIES = {WineProperty.Quality, WineProperty.FixedAcidity, WineProperty.VolatileAcidity, WineProperty.CitricAcid, WineProperty.ResidualSugar, WineProperty.Chlorides, WineProperty.FreeSulfurDioxide, WineProperty.TotalSulfurDioxide, WineProperty.Density, WineProperty.PH, WineProperty.Sulphates, WineProperty.Alcohol};
    String statisticsText = "";
    public WineSampleBrowserPanel(AbstractWineSampleCellar cellar) {
        super(cellar);
    }
    
    /**
     * Adds relevant actionListeners to the GUI components
     */
    @Override
    public void addListeners() {
        updateStatistics();
        updateHistogram();
        buttonAddFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFilter();
                updateHistogram();
                repaint();
            }
        });
        
        buttonClearFilters.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFilters();
                repaint();
            }
        });
        
        comboWineTypes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String subQueryText = subQueriesTextArea.getText();
                clearFilters();
                executeQuery();
                updateHistogram();
                repaint();
                subQueriesTextArea.setText(subQueryText);
            }
        });
        
        comboHistogramProperties.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateHistogram();
                repaint();
            }
        });
    }
    
    /**
     * Called when the JButton buttonAddFilter is clicked.
     * It adds a new filter (a SubQuery object) to subQueryList ArrayList, and
     * Updates the GUI results accordingly.
     */
    @Override
    public void addFilter() {
        String wineValue = value.getText();
        if (value.getText().isEmpty()) {
            wineValue = "0";
        } 
        WineProperty wineProperty = WineProperty.fromName(comboQueryProperties.getSelectedItem().toString());
        String wineOperator = comboOperators.getSelectedItem().toString();
        subQueryList.add(new SubQuery(wineProperty, wineOperator, Double.parseDouble(wineValue)));
        if (subQueriesTextArea.getText() != null) {
            subQueriesTextArea.setText(subQueriesTextArea.getText() + " , " + subQueryList.get(subQueryList.size()-1).toString());
        } else {
            subQueriesTextArea.setText(subQueryList.get(subQueryList.size()-1).toString());
        }
        executeQuery();
        updateHistogram();
    }
    
    /**
     * Clears all filters from the subQueryList ArrayList and updates
     * the relevant GUI components when the button buttonClearFilters is clicked.
     */
    @Override
    public void clearFilters() {
        subQueryList.clear();
        subQueriesTextArea.setText(null);
        filteredWineSamplesTextArea.setText(null);
        statisticsTextArea.setText(null);
        executeQuery();
        //updateStatistics();
        updateHistogram();
    }

    /**
     * Updates the statistics to be displayed in the 
     * statisticsTextArea when the results being shown in the GUI need to be updated.
     * Also recalculates the average, minimum and maximum values for each wine property.
     */
    @Override
    public void updateStatistics() {
        statisticsText = "";
        for (int i=0; i<VALID_PROPERTIES.length; i++) {
            double maxVal = cellar.getMaximumValue(VALID_PROPERTIES[i], filteredWineSampleList);
            double minVal = cellar.getMinimumValue(VALID_PROPERTIES[i], filteredWineSampleList);
            double avgVal = cellar.getMeanAverageValue(VALID_PROPERTIES[i], filteredWineSampleList);
            statisticsText += VALID_PROPERTIES[i] + "\nMaximum:  " + maxVal + "   Minimum:   " + minVal + "   Mean:   " + Math.round((avgVal*100)/100) + "\n\n"; 
        }
        statisticsText += "\nShowing " + filteredWineSampleList.size() + " out of " + cellar.getNumberWineSamples(wineType);
        updateWineDetailsBox();
    }
    
    /**
     * Updates the wine details panel when changes are made
     */
    @Override
    public void updateWineDetailsBox() {
        statisticsTextArea.setText(null);
        statisticsTextArea.setText(statisticsText);
        
        filteredWineSamplesTextArea.setText(null);
        filteredWineSamplesTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        String newText = "Type    ID"; 
        for (WineProperty property : VALID_PROPERTIES) {
            newText += "    " + property.getName();
        }
        newText += "\n";
        
        for (WineSample currentSample : filteredWineSampleList) {
            newText += currentSample.getWineType().getName() + "       " + Integer.toString(currentSample.getId());
            for (WineProperty property : VALID_PROPERTIES) {
                newText += "     " + currentSample.getProperty(property);
            }
            newText += "\n";
        }
        filteredWineSamplesTextArea.setText(newText);
    }

    /**
     * Executes the complete query to the relevant wine list
     */
    @Override
    public void executeQuery() {
        WineType wineType = WineType.valueOf(comboWineTypes.getSelectedItem().toString());
        Query newQuery = new Query(subQueryList, wineType);
        filteredWineSampleList = newQuery.executeQuery(cellar);
        updateStatistics();
        
    }
}