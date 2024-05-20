package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HistogramPlot {

    public static void main(String[] args) {
        String csvFilePath = "cac_demo/src/resource/Ha_dataset.csv"; // Update the path as needed
        ArrayList<Double> values = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(csvFilePath))) {
            // Skip the header line if present
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Read data and add to the values list
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(";");
                double value = Double.parseDouble(data[1].trim());
                    values.add(value);
            }
        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found: " + csvFilePath);
            e.printStackTrace();
        }

        // Converting list to array
        double[] valueArray = values.stream().mapToDouble(Double::doubleValue).toArray();

        // Creating a histogram dataset
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);
        dataset.addSeries("Histogram", valueArray, 20); // Adjust the bin count as needed

        // Creating a histogram chart
        JFreeChart histogram = ChartFactory.createHistogram(
                "Histogram",
                "Value",
                "Frequency",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Customizing the plot
        var plot = histogram.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.black);
        plot.setDomainGridlinePaint(Color.black);
        plot.setOutlinePaint(Color.black);

        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(188, 143, 143)); // Set custom color for the bars
        renderer.setDrawBarOutline(false);

        // Creating and setting up the chart panel
        ChartPanel chartPanel = new ChartPanel(histogram);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        // Saving the chart as a JPEG file
        try {
            ChartUtilities.saveChartAsJPEG(new File("Histogram.jpg"), histogram, 800, 600);
            System.out.println("Histogram Created!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
