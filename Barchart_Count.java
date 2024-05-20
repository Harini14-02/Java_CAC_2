package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Barchart_Count {

    public static void main(String[] args) {
        String csvFilePath = "cac_demo\\src\\resource\\Ha_dataset.csv";

        Map<String, Integer> categoryCounts = new HashMap<>();

        try (Scanner scanner = new Scanner(new File(csvFilePath))) {
            // Skip the header line if present
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Read data and count occurrences of each category
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(";");
                if (values.length > 2) {
                    String category = values[11];

                    // Update category count
                    categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + 1);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Create dataset for the bar chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            dataset.addValue(entry.getValue(), "Count", entry.getKey());
        }

        // Create a bar chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Dataset Visualization", // Chart title
                "Admission Type",             // X-Axis label
                "Count",            // Y-Axis label
                dataset,            // Dataset
                PlotOrientation.VERTICAL, // Orientation
                false,              // Include legend
                true,               // Include tooltips
                false               // Include URLs
        );
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.black);
        plot.setOutlinePaint(Color.black);

        Paint[] colors = { // Blue
            new Color(169, 149, 255),
            new Color(169, 149, 255),
            new Color(169, 149, 255),
            new Color(169, 149, 255),
            new Color(169, 149, 255),  // Red
        };

        for (int i = 0; i < colors.length; i++) {
            renderer.setSeriesPaint(i, colors[i % colors.length]);
        }

        // Create and set up the chart panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        // Save the chart as a JPEG file
        try {
            ChartUtilities.saveChartAsJPEG(new File("BarChart - Count.jpg"), chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
