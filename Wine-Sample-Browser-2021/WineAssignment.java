package uk.ac.sheffield.assignment2021;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.sheffield.assignment2021.codeprovided.AbstractQueryParser;
import uk.ac.sheffield.assignment2021.codeprovided.AbstractWineSampleCellar;
import uk.ac.sheffield.assignment2021.codeprovided.Query;
import uk.ac.sheffield.assignment2021.codeprovided.WineProperty;
import uk.ac.sheffield.assignment2021.codeprovided.WineSample;
import uk.ac.sheffield.assignment2021.codeprovided.WineType;
import uk.ac.sheffield.assignment2021.codeprovided.gui.AbstractWineSampleBrowserPanel;
import uk.ac.sheffield.assignment2021.codeprovided.gui.WineSampleBrowser;
import uk.ac.sheffield.assignment2021.gui.WineSampleBrowserPanel;

/**
 * Main class to run the Assignment's graphical user interface
 *
 * @version 1.1  09/03/2021
 *
 * @author Ben Clegg
 *
 * Copyright (c) University of Sheffield 2021
 */
public class WineAssignment {

	private final AbstractWineSampleCellar cellar;
	private final List<Query> queries;

	/**
	 * Main method - receives CSV files with the relevant wine sample data and a text file with relevant query data and calls the readWineFile and readQueryFile methods.
	 * <p>
	 * Method accepts 2 text files made up of one line of Strings (the header) and multiple lines of doubles (the values). It then calls the
	 * readWineFile method. Also accepts a third text file made up of lines of Strings which it calls the readQueryFile method on.
	 * <p>
	 * Method stores three ArrayLists of Wine samples. One is for red wines, the second is for white wines and the third is a combined list.
	 * <p>
	 * Method also stores ArrayLists of queries passed from the third text file.
	 * <p>
	 * Method then calls further methods to print statistical information about the wine lists passed to it. It also
	 * contains the format which the information is printed in.
	 *
	 * @param args two CSV files and a text file defined by the user
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			args = new String[] {
				"resources/winequality-red.csv",
				"resources/winequality-white.csv",
				"resources/queries.txt"};
		}
		String redWineFile = args[0];
		String whiteWineFile = args[1];
		String queriesFile = args[2];
		
		WineAssignment wineAssignment = 
				new WineAssignment(redWineFile, whiteWineFile, queriesFile);
		
		wineAssignment.startCLI();
		wineAssignment.startGUI();
	}

	public WineAssignment(String redWineFile, String whiteWineFile, String queriesFile) {
		cellar = new WineSampleCellar(redWineFile, whiteWineFile);
		List<String> queryTokens = new ArrayList<>(AbstractQueryParser.readQueryFile(queriesFile));
		queries = new ArrayList<>(new QueryParser().readQueries(queryTokens));
	}

	/**
	 * Start the simple CLI based portion of the assignment
	 */
	public void startCLI() {
		// Basic wine information
		printNumberWines();
		printQuestionAnswers();
		printFirstFiveWines();

		// Queries
		printNumberQueries();
		executeQueries();
	}

	/**
	 * Display the number of wines for each WineType
	 */
	private void printNumberWines() {
		System.out.print("The total number of wine samples (red and white) in the dataset is: ");
		System.out.println(cellar.getNumberWineSamples(WineType.RED) + cellar.getNumberWineSamples(WineType.WHITE));
		System.out.print("The total number of red wine samples in the dataset is: ");
		System.out.println(cellar.getNumberWineSamples(WineType.RED));
		System.out.print("The total number of white wine samples in the dataset is: ");
		System.out.println(cellar.getNumberWineSamples(WineType.WHITE));
	}
	
	/**
	 * Print the answers to the questions defined in the specification
	 */
	private void printQuestionAnswers() {

		// Prints highest alcohol content of the red wine samples
		System.out.println("The highest alcohol content of the red wine samples is " +
				cellar.getMaximumValue(WineProperty.Alcohol, WineType.RED));
		System.out.println();

		// Prints highest citric acid content of the white wine samples
		System.out.println("The lowest citric acid content of the white wine samples is " +
				cellar.getMinimumValue(WineProperty.CitricAcid, WineType.WHITE));
		System.out.println();

		// Prints the average alcohol content of the samples in the white wine list
		System.out.println("The average alcohol content of the white wine samples is " +
				cellar.getMeanAverageValue(WineProperty.Alcohol, WineType.WHITE));
		System.out.println();
	}

	/**
	 * Print the number of Queries (not SubQueries) identified in the queries file
	 */
	private void printNumberQueries() {
		System.out.println("In total, " + queries.size() + " queries were found.");
		System.out.println();
	}

	/**
	 * Execute each query; displaying the filtered wines for each query
	 */
	private void executeQueries() {
		System.out.println("Executing queries...");
		System.out.println(queries);
		for (Query query : queries) {
			System.out.println("/n" + query.toString() + ":");
			List<WineSample> queryResults = query.executeQuery(cellar);
			
			printWines(queryResults);
			System.out.println();
		}
	}

	/**
	 * Display the provided Wines
	 * @param wines the wines to display
	 */
	private void printWines(Collection<WineSample> wines) {
		for (WineSample w : wines)
			System.out.println(w.toString());
	}

	/**
	 * Display the first 5 wines in the AbstractWineSampleCellar object
	 */
	private void printFirstFiveWines() {
		System.out.println("\nRed wines:");
		printWines(cellar.getFirstFiveWines(WineType.RED));

		System.out.println("\nWhite wines:");
		printWines(cellar.getFirstFiveWines(WineType.WHITE));
	}

	/**
	 * Start the GUI of the assignment
	 */
	public void startGUI() {
		// Start GUI
		AbstractWineSampleBrowserPanel browserPanel = new WineSampleBrowserPanel(cellar);
		@SuppressWarnings("unused") WineSampleBrowser wineSampleBrowser = new WineSampleBrowser(browserPanel);
	}
}
