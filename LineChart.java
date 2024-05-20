package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LineChart {

    public static void main(String[] args) {
        String csvFilePath = "cac_demo/src/resource/Ha_dataset.csv"; // Use forward slashes for file path consistency

        Map<String, Integer> categoryCounts = new HashMap<>();

        try (Scanner scanner = new Scanner(new File(csvFilePath))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Reading the data and count occurrences of each category
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(";");
                String category = values[3];
                categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + 1);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Creating dataset for the line chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int minValue = Integer.MAX_VALUE;
        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            int value = entry.getValue();
            dataset.addValue(value, "Count", entry.getKey());
            if (value < minValue) {
                minValue = value;
            }
        }

        // Creating a line chart
        JFreeChart chart = ChartFactory.createLineChart(
                "Category Counts - Type", // Chart title
                "Type", // X-Axis label
                "Count", // Y-Axis label
                dataset, // Dataset
                PlotOrientation.VERTICAL, // Plot orientation
                false, // Include legend
                true, // Include tooltips
                false // Exclude URLs
        );

        // Customizing the plot
        CategoryPlot plot = chart.getCategoryPlot();
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, true);
        plot.setRenderer(renderer);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setLowerBound(minValue);
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.black);
        plot.setOutlinePaint(Color.black);

       
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        try {
            ChartUtilities.saveChartAsJPEG(new File("LineChart.jpg"), chart, 800, 600);
            System.out.println("Line chart created");
        } catch (IOException e) {
            e.printStackTrace();
        }

       
    }
}
