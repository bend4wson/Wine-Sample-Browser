package uk.ac.sheffield.assignment2021.gui;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.List;
import uk.ac.sheffield.assignment2021.codeprovided.gui.AbstractHistogram;
import uk.ac.sheffield.assignment2021.codeprovided.gui.AbstractHistogramPanel;
import uk.ac.sheffield.assignment2021.codeprovided.gui.AbstractWineSampleBrowserPanel;
import uk.ac.sheffield.assignment2021.codeprovided.gui.HistogramBin;

/**
 * 
 * HistogramPanel extends AbstractHistogramPanel and is used to draw the histogram when needed, 
 * including the axis and labels.
 * @author Ben Dawson
 *
 */
public class HistogramPanel extends AbstractHistogramPanel
{
    public HistogramPanel(AbstractWineSampleBrowserPanel parentPanel, AbstractHistogram histogram)
    {
        super(parentPanel, histogram);
    }

    /**
     * paintComponent will redraw the histogram on-screen.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension d = getSize();
        Graphics2D g2 = (Graphics2D) g;
        
        //This will draw the lines at different points on the Y axis, representing different frequency values.
        //It will also write out the frequency on the Y axis.
        
        FontRenderContext frc = g2.getFontRenderContext();
        g2.setColor(Color.darkGray);
        Font font1 = new Font("Courier", Font.BOLD, 20);
   
        int lineX1 = 200; 
        int lineX2 = 1795;
        int lineY = 350;
        int yAxisInterval = 30;
        
        for (int i=0; i<11; i++) {
            g2.drawLine(lineX1, lineY, lineX2, lineY);
            lineY -= yAxisInterval;
        }   
        
        String xTitle = new String("Bin Intervals");
        TextLayout tl = new TextLayout(xTitle, font1, frc);
        tl.draw(g2, 897, 405);

        //This will draw the y-axis label
        String yTitle = new String("Frequency");
        TextLayout t2 = new TextLayout(yTitle, font1, frc);
        t2.draw(g2, 25, 200);
        
        Histogram newHistogram = (Histogram) getHistogram();
        
        double widthOfBar = (lineX2-lineX1)/11;
        List<HistogramBin> bins = newHistogram.getBinsInBoundaryOrder();
        double highestFrequency = 0;
        for (int i=0; i<bins.size(); i++) {
            if (newHistogram.getNumWinesInBin(bins.get(i)) > highestFrequency) {
                highestFrequency = newHistogram.getNumWinesInBin(bins.get(i));
            }
        }
        
        int frequencyInterval = (int)highestFrequency/10;
        for (int i=0; i<bins.size(); i++) {
            g2.setColor(Color.BLACK);
            String xAxisString = "(" + Math.round(bins.get(i).getLowerBoundary()*100.0)/100.0 + " - " + Math.round(bins.get(i).getUpperBoundary()*100.0)/100.0 + ")";
            g2.drawString(xAxisString, 200+((i)*150), 370);
            
            String yAxisString = String.valueOf(frequencyInterval*i);
            g2.drawString(yAxisString, 160, (350-(30*i)));
            g2.setColor(Color.RED);
            
            //This value takes the height of the histogram graph, and divides it by the highest bin frequency of the selected product.
            //This is then multiplied by the current bin frequency to give the bin a relative height.
            
            g2.fillRect((int)(lineX1+(widthOfBar*(i))), (int)(350-((300/highestFrequency)*newHistogram.getNumWinesInBin(bins.get(i)))), (int)widthOfBar, (int)(((300/highestFrequency)*newHistogram.getNumWinesInBin(bins.get(i)))));  //(i+1)*yAxisInterval)); //Swap 0 for -350
        }


        
    }

     
}
