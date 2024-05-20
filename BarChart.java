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
import java.util.Scanner;

public class BarChart {

    public static void main(String[] args) {
        String csvFilePath = "cac_demo\\src\\resource\\Ha_dataset.csv";

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try (Scanner scanner = new Scanner(new File(csvFilePath))) {
            // Skip the header line if present
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Read data and add it to the dataset
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(";");
                String category = values[2];
                double value  = Double.parseDouble(values[7]);
                dataset.addValue(value, "Data", category);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Creating a bar chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Dataset Visualization - Type and Price", // Chart title
                "Type",             // X-Axis label
                "Price",                // Y-Axis label
                dataset,               // Dataset
                PlotOrientation.VERTICAL,  // Orientation
                false,                  // Include legend
                true,                  // Include tooltips
                false                  // Include URLs
        );
        

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.black);
        plot.setOutlinePaint(Color.black);


        Paint[] colors = {
            new Color(149, 169, 255 ), 
            new Color(192, 80, 77),  
        };

        for (int i = 0; i < colors.length; i++) {
            renderer.setSeriesPaint(i, colors[i % colors.length]);
        }

        // Creating and seting up the chart panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        // Saving the chart as a JPEG file
        try {
            ChartUtilities.saveChartAsJPEG(new File("BarChart.jpg"), chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
}
}
