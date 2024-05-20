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

public class PieChart1 {

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
                    String category = values[6];
                    double value = Double.parseDouble(values[1]);
                    dataset.setValue(category, value);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Dataset Visualization - Insurance Provider & Age", // Chart title
                dataset, // Dataset
                false, // Include legend
                true, // Include tooltips
                false // Include URLs
        );

        // Customizing the plot
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("LiveWell", new Color(70, 130, 180)); // color for Category1
        plot.setSectionPaint("HCare", new Color(209, 123, 123 )); // color for Category2
        plot.setSectionPaint("BlueHealth", new Color(175, 122, 197 ));
        plot.setSectionPaint("Healthier", new Color(188, 143, 143));
        plot.setSectionPaint("GHealth", new Color(123, 209, 198));
        

        // To Display percentages
        plot.setLabelGenerator(new org.jfree.chart.labels.StandardPieSectionLabelGenerator("{0} ({2})",
                new DecimalFormat("0"), new DecimalFormat("0.00%")));
        plot.setExplodePercent("LiveWell", 0.10);
        plot.setExplodePercent("HCare", 0.10);
        plot.setExplodePercent("BlueHealth", 0.10);
        plot.setExplodePercent("Healthier", 0.10);
        plot.setExplodePercent("GHealth", 0.10); // To explode a section

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

