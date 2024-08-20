package etu.nic.git.trajectories_swing.display_components;

import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.*;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class ChartDisplay {
    private final JLabel displayHeader;
    private final JPanel background;
    private final TrajectoryRowTableModel model;

    public ChartDisplay(TrajectoryRowTableModel tableModel) {
        this.model = tableModel;    // TODO: читать данные из модели

        displayHeader = new JLabel("График");   // TODO: добавить панель с optionButton'ами

        displayHeader.setHorizontalAlignment(SwingConstants.CENTER);
        displayHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

        background = new JPanel(new BorderLayout());
        background.add(BorderLayout.NORTH, displayHeader);
//        background.add(BorderLayout.CENTER, scrollPane);

        JFreeChart chart = createChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        background.add(BorderLayout.CENTER, chartPanel);
    }

    public JComponent getComponent() {
        return background;
    }

    private JFreeChart createChart() {
        // Первое подмножество данных (Primary Range Axis)
        XYSeries series1 = new XYSeries("Series 1");
        series1.add(1.0, 100.0);
        series1.add(2.0, 120.0);
        series1.add(3.0, 135.0);

        XYSeriesCollection dataset1 = new XYSeriesCollection();
        dataset1.addSeries(series1);

        // Создаем график с основной осью Y
        JFreeChart chart = ChartFactory.createXYLineChart(
                null,
                "Time of Day",
                "Primary Range Axis",
                dataset1
        );

        XYPlot plot = chart.getXYPlot();

        // изменение цвета фона у графика
        plot.setBackgroundPaint(Color.WHITE);

        // изменение цвета сетки
        plot.setRangeGridlinePaint(Color.DARK_GRAY);
        plot.setDomainGridlinePaint(Color.DARK_GRAY);

        // Настройка первого рендера
        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(true, false);
        plot.setRenderer(0, renderer1);

        // Вторая ось Y (Range Axis 2)
        XYSeries series2 = new XYSeries("Series 2");
        series2.add(1.0, 900.0);
        series2.add(2.0, 1200.0);
        series2.add(3.0, 1100.0);

        XYSeriesCollection dataset2 = new XYSeriesCollection();
        dataset2.addSeries(series2);

        NumberAxis axis2 = new NumberAxis("Range Axis 2");
        axis2.setLabelPaint(Color.RED);
        axis2.setTickLabelPaint(Color.RED);
        plot.setRangeAxis(1, axis2);
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);

        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
        renderer2.setSeriesPaint(0, Color.RED);
        plot.setRenderer(1, renderer2);

        // Третья ось Y (Range Axis 3)
        XYSeries series3 = new XYSeries("Series 3");
        series3.add(1.0, 5000.0);
        series3.add(2.0, 7000.0);
        series3.add(3.0, 6000.0);

        XYSeriesCollection dataset3 = new XYSeriesCollection();
        dataset3.addSeries(series3);

        NumberAxis axis3 = new NumberAxis("Range Axis 3");
        axis3.setLabelPaint(Color.BLUE);
        axis3.setTickLabelPaint(Color.BLUE);
        plot.setRangeAxis(2, axis3);
        plot.setDataset(2, dataset3);
        plot.mapDatasetToRangeAxis(2, 2);

        XYLineAndShapeRenderer renderer3 = new XYLineAndShapeRenderer(true, false);
        renderer3.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(2, renderer3);

        // Четвертая ось Y (Range Axis 4)
        XYSeries series4 = new XYSeries("Series 4");
        series4.add(1.0, 20.0);
        series4.add(2.0, 40.0);
        series4.add(3.0, 35.0);

        XYSeriesCollection dataset4 = new XYSeriesCollection();
        dataset4.addSeries(series4);

        NumberAxis axis4 = new NumberAxis("Range Axis 4");
        axis4.setLabelPaint(Color.GREEN);
        axis4.setTickLabelPaint(Color.GREEN);
        plot.setRangeAxis(3, axis4);
        plot.setDataset(3, dataset4);
        plot.mapDatasetToRangeAxis(3, 3);

        XYLineAndShapeRenderer renderer4 = new XYLineAndShapeRenderer(true, false);
        renderer4.setSeriesPaint(0, Color.GREEN);
        plot.setRenderer(3, renderer4);

        // Размещение дополнительных осей справа
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_LEFT);
        plot.setRangeAxisLocation(2, AxisLocation.BOTTOM_OR_RIGHT);
        plot.setRangeAxisLocation(3, AxisLocation.TOP_OR_RIGHT);

        return chart;
    }
}
