package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class PieChart {

    public static void main(String[] args) {
        String csvFilePath = "cac_demo\\src\\resource\\Ha_dataset.csv";

        DefaultPieDataset dataset = new DefaultPieDataset();

        try (Scanner scanner = new Scanner(new File(csvFilePath))) {
            // Skip the header line if present
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Read the data
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(";");
                if (values.length > 5) {
                    String category = values[5];
                    double value = Double.parseDouble(values[1]);
                    dataset.setValue(category, value);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Dataset Visualization - Medication & Age", // Chart title
                dataset, // Dataset
                false, // Include legend
                true, // Include tooltips
                false // Include URLs
        );

        // Customizing the plot
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Lipitor", new Color(238, 130, 238)); // color for Category1
        plot.setSectionPaint("Ibuprofen", new Color(221, 210, 206)); // color for Category2
        plot.setSectionPaint("Aspirin", new Color(148, 177, 255));
        plot.setSectionPaint("Penicillin", new Color(167, 142, 184));
        plot.setSectionPaint("Paracetamol", new Color(201, 255, 149));

        // To Display percentages
        plot.setLabelGenerator(new org.jfree.chart.labels.StandardPieSectionLabelGenerator("{0} ({2})",
                new DecimalFormat("0"), new DecimalFormat("0.00%")));
        plot.setExplodePercent("Lipitor", 0.10);
        plot.setExplodePercent("Aspirin", 0.10);
        plot.setExplodePercent("Ibuprofen", 0.10);
        plot.setExplodePercent("Penicillin", 0.10);
        plot.setExplodePercent("Paracetamol", 0.10); // To explode a section

        // Creating and seting up the chart panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        // Saving the chart as a JPEG file
        try {
            ChartUtilities.saveChartAsJPEG(new File("PieChart.jpg"), chart, 800, 600);
            System.out.println("Pie chart created!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
