package uk.ac.sheffield.assignment2021.codeprovided.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Creates the GUI
 *
 * @version 1.1  09/02/2021
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 *
 * Copyright (c) University of Sheffield 2021
 */
public class WineSampleBrowser extends JFrame { 

	public WineSampleBrowser(AbstractWineSampleBrowserPanel panel) {
		setTitle("Portuguese Wine Sample Browser");
		add(panel);
		// maximises the JFrame
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
