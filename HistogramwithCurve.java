package com.example;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics; 
import org.apache.commons.math3.util.FastMath;// distribution curve
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset; //histogram library
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.awt.*;
import java.io.File; //library file
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner; //to get input

public class HistogramwithCurve {

    public static void main(String[] args) {
        String csvFilePath = "cac_demo/src/resource/Ha_dataset.csv"; // the path of the dataset
        ArrayList<Double> values = new ArrayList<>();

        // Reading data from CSV file
        try (Scanner scanner = new Scanner(new File(csvFilePath))) {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Reading the data and adding to the values list
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty())
                    continue;

                String[] data = line.split(";"); // split data in dataset using semi-colon
                double value = Double.parseDouble(data[7].trim());
                values.add(value);
            }
        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found: " + csvFilePath);
            e.printStackTrace();
        }

        // Convert list to array
        double[] valueArray = values.stream().mapToDouble(Double::doubleValue).toArray();

        // Create a histogram dataset
        HistogramDataset dataset = new HistogramDataset();
        dataset.setType(HistogramType.FREQUENCY);
        int bins = 20; // Adjust the bin count as needed
        dataset.addSeries("Histogram", valueArray, bins);

        // Creating a histogram chart
        JFreeChart histogram = ChartFactory.createHistogram(
                "Histogram",
                "Value",
                "Frequency",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        XYPlot plot = histogram.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinePaint(Color.black);
        plot.setDomainGridlinePaint(Color.black);
        plot.setOutlinePaint(Color.black);

        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(188, 143, 143)); // custom color for the bars
        renderer.setDrawBarOutline(false);
        // Calculating Gaussian distribution
        DescriptiveStatistics stats = new DescriptiveStatistics(valueArray);
        double mean = stats.getMean();
        double stdDev = stats.getStandardDeviation();

        XYSeries gaussianSeries = new XYSeries("Gaussian Fit");
        double min = stats.getMin();
        double max = stats.getMax();
        int numPoints = 20;
        double step = (max - min) / numPoints;

        for (double x = min; x <= max; x += step) {
            double y = (1 / (stdDev * FastMath.sqrt(2 * Math.PI)))
                    * FastMath.exp(-0.5 * FastMath.pow((x - mean) / stdDev, 2)) * valueArray.length * step;
            gaussianSeries.add(x, y);
        }

        XYSeriesCollection gaussianDataset = new XYSeriesCollection();
        gaussianDataset.addSeries(gaussianSeries);

        StandardXYItemRenderer gaussianRenderer = new StandardXYItemRenderer();
        gaussianRenderer.setSeriesPaint(0, Color.BLUE);
        plot.setDataset(1, gaussianDataset);
        plot.setRenderer(1, gaussianRenderer);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        // Saving the chart as a JPEG file
        try {
            ChartUtilities.saveChartAsJPEG(new File("Histogram_with_Gaussian.jpg"), histogram, 800, 600);
            System.out.println("Histogram Created!!");
        } catch (Exception e) {
            System.err.println("Error saving the histogram as a JPEG file.");
            e.printStackTrace();
        }
    }
}
