package com.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class TimeSeriesChart {

    public static void main(String[] args) {
        String csvFilePath = "C:\\Users\\hsrut\\Downloads\\Ha_dataset.csv"; 
        TimeSeries series = new TimeSeries("Time Series Data");

        // Date formats to parse
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

        try (Scanner scanner = new Scanner(new File(csvFilePath))) {
            // Skip the header line if present
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Read data and add to the series
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                String[] values = line.split(",");
                String dateStr = values[10].trim(); 
                double value = Double.parseDouble(values[11].trim()); 

                try {
                    // Parse the date
                    java.util.Date date = null;
                    try {
                        date = dateFormat1.parse(dateStr);
                    } catch (ParseException e) {
                        date = dateFormat2.parse(dateStr);
                    }
                    series.add(new Day(date), value);
                } catch (Exception e) {
                    System.err.println("Skipping line due to date format issue: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found: " + csvFilePath);
            e.printStackTrace();
        }

        // Create dataset for the time series chart
        TimeSeriesCollection dataset = new TimeSeriesCollection(series);

        // Create a time series chart
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Time Series Chart",
                "Year", // X-Axis label
                "Value", // Y-Axis label
                dataset,
                false,
                true,
                false);

        // Customize the plot
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.black);
        plot.setDomainGridlinePaint(Color.black);

        // Customize the renderer
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(75, 0, 130)); // custom color for the line
        renderer.setSeriesShapesVisible(0, false); // Hide shapes
        plot.setRenderer(renderer);

        // Customize the date axis
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("yyyy")); // Format the date to display only the year
        axis.setTickUnit(new DateTickUnit(DateTickUnitType.YEAR, 1)); // Set tick unit to 1 year
        axis.setVerticalTickLabels(true); // Rotate tick labels for better readability

        // Create and set up the chart panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(900, 500));

        // Save the chart as a JPEG file
        try {
            ChartUtilities.saveChartAsJPEG(new File("TimeSeriesChart.jpg"), chart, 900, 500);
            System.out.println("Time Series Chart Created!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
