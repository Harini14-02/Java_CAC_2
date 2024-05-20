package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ScatterPlot {

    public static void main(String[] args) {
        String csvFilePath = "cac_demo/src/resource/Ha_dataset.csv"; 

        XYSeries series = new XYSeries("Scatter Plot");

        try (Scanner scanner = new Scanner(new File(csvFilePath))) {
            // Skip the header line if present
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Reading data and add to the series
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] values = line.split(";");
                if (values.length > 7) {
                    try {
                        double xValue = Double.parseDouble(values[1].trim()); 
                        double yValue = Double.parseDouble(values[7].trim()); 
                        series.add(xValue, yValue);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping line due to number format issue: " + line);
                    }
                } else {
                    System.err.println("Skipping line due to insufficient data: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found: " + csvFilePath);
            e.printStackTrace();
        }

        // Creating dataset for the scatter plot
        XYSeriesCollection dataset = new XYSeriesCollection(series);

        // Creating a scatter plot
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Scatter Plot",
                "Age", // X-Axis label
                "Amount Billing", // Y-Axis label
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Customizing the plot
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.black);
        plot.setDomainGridlinePaint(Color.black);
        plot.setOutlinePaint(Color.black);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesLinesVisible(0, false);
        plot.setRenderer(renderer);

        // Creating and setting up the chart panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        // Saving the chart as a JPEG file
        try {
            ChartUtilities.saveChartAsJPEG(new File("ScatterPlot.jpg"), chart, 800, 600);
            System.out.println("Scatter Plot Created!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
