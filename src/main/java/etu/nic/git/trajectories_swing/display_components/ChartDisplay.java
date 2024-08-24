package etu.nic.git.trajectories_swing.display_components;

import etu.nic.git.trajectories_swing.model.TrajectoryRow;
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
import java.util.ArrayList;
import java.util.List;

public class ChartDisplay {
    private final JLabel displayHeader;
    private final JPanel background;
    private final TrajectoryRowTableModel model;
    private final ChartPanel chartPanel;
    private final JFreeChart chart;
    private XYPlot plot;

    public ChartDisplay(TrajectoryRowTableModel tableModel) {
        this.model = tableModel;    // TODO: читать данные из модели

        displayHeader = new JLabel("График");   // TODO: добавить панель с optionButton'ами

        displayHeader.setHorizontalAlignment(SwingConstants.CENTER);
        displayHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

        background = new JPanel(new BorderLayout());
        background.add(BorderLayout.NORTH, displayHeader);
//        background.add(BorderLayout.CENTER, scrollPane);

        chart = createChart();
        chartPanel = new ChartPanel(chart);
        background.add(BorderLayout.CENTER, chartPanel);
    }

    public JComponent getComponent() {
        return background;
    }

    private JFreeChart createChart() {
        List<TrajectoryRow> trajectoryRowList = model.getTrajectoryRowList();
        List<XYSeries> trajectorySeries = new ArrayList<>();
        XYSeries series;
        for (int i = 1; i < TrajectoryRow.AMOUNT_OF_PARAMETERS; i++) {
            series = new XYSeries(TrajectoryRow.PARAMETER_NAMES[i]);
            for (int j = 0; j < trajectoryRowList.size(); j++) {  // для каждого из параметров траектории, кроме времени
                //TODO: добавить каждый параметр в соответствующую серию со временем
                double[] rowParameters = trajectoryRowList.get(j).toDoubleArray();
                series.add(rowParameters[0], rowParameters[i]);
            }
            trajectorySeries.add(series);
        }

        // Создаем график с основной осью Y
        JFreeChart chart = ChartFactory.createXYLineChart(
                null,
                "Время, с",
                null,
                null
        );
        plot = chart.getXYPlot();

        XYSeriesCollection coordinatesDataset = new XYSeriesCollection();
        coordinatesDataset.addSeries(trajectorySeries.get(0));
        coordinatesDataset.addSeries(trajectorySeries.get(1));
        coordinatesDataset.addSeries(trajectorySeries.get(2));


        NumberAxis axis1 = new NumberAxis("Координата, м");
        axis1.setLabelPaint(Color.BLUE);
        axis1.setTickLabelPaint(Color.BLUE);
        plot.setRangeAxis(0, axis1);
        plot.setDataset(0, coordinatesDataset);
        plot.mapDatasetToRangeAxis(0, 0);

        // изменение цвета фона у графика
        plot.setBackgroundPaint(Color.WHITE);

        // изменение цвета сетки
        plot.setRangeGridlinePaint(Color.DARK_GRAY);
        plot.setDomainGridlinePaint(Color.DARK_GRAY);

        // Настройка первого рендера
        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(true, false);
        renderer1.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(0, renderer1);

        XYSeriesCollection velocitiesDataset = new XYSeriesCollection();
        velocitiesDataset.addSeries(trajectorySeries.get(3));
        velocitiesDataset.addSeries(trajectorySeries.get(4));
        velocitiesDataset.addSeries(trajectorySeries.get(5));

        NumberAxis axis2 = new NumberAxis("Скорость, м/с");
        axis2.setLabelPaint(Color.RED);
        axis2.setTickLabelPaint(Color.RED);
        plot.setRangeAxis(1, axis2);
        plot.setDataset(1, velocitiesDataset);
        plot.mapDatasetToRangeAxis(1, 1);

        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
        renderer2.setSeriesPaint(0, Color.RED);
        plot.setRenderer(1, renderer2);

        // Размещение дополнительных осей справа
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);

        return chart;
    }

    public void updateChart() {
        List<TrajectoryRow> trajectoryRowList = model.getTrajectoryRowList();
        List<XYSeries> trajectorySeries = new ArrayList<>();
        XYSeries series;
        for (int i = 1; i < TrajectoryRow.AMOUNT_OF_PARAMETERS; i++) {
            series = new XYSeries(TrajectoryRow.PARAMETER_NAMES[i]);
            for (int j = 0; j < trajectoryRowList.size(); j++) {  // для каждого из параметров траектории, кроме времени
                //TODO: добавить каждый параметр в соответствующую серию со временем
                double[] rowParameters = trajectoryRowList.get(j).toDoubleArray();
                series.add(rowParameters[0], rowParameters[i]);
            }
            trajectorySeries.add(series);
        }

        XYSeriesCollection coordinatesDataset = new XYSeriesCollection();
        coordinatesDataset.addSeries(trajectorySeries.get(0));
        coordinatesDataset.addSeries(trajectorySeries.get(1));
        coordinatesDataset.addSeries(trajectorySeries.get(2));

        NumberAxis axis1 = new NumberAxis("Координата, м");
        axis1.setLabelPaint(Color.BLUE);
        axis1.setTickLabelPaint(Color.BLUE);
        plot.setRangeAxis(0, axis1);
        plot.setDataset(0, coordinatesDataset);
        plot.mapDatasetToRangeAxis(0, 0);

        // изменение цвета фона у графика
        plot.setBackgroundPaint(Color.WHITE);

        // изменение цвета сетки
        plot.setRangeGridlinePaint(Color.DARK_GRAY);
        plot.setDomainGridlinePaint(Color.DARK_GRAY);

        // Настройка первого рендера
        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(true, false);
        renderer1.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(0, renderer1);

        XYSeriesCollection velocitiesDataset = new XYSeriesCollection();
        velocitiesDataset.addSeries(trajectorySeries.get(3));
        velocitiesDataset.addSeries(trajectorySeries.get(4));
        velocitiesDataset.addSeries(trajectorySeries.get(5));

        NumberAxis axis2 = new NumberAxis("Скорость, м/с");
        axis2.setLabelPaint(Color.RED);
        axis2.setTickLabelPaint(Color.RED);
        plot.setRangeAxis(1, axis2);
        plot.setDataset(1, velocitiesDataset);
        plot.mapDatasetToRangeAxis(1, 1);

        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
        renderer2.setSeriesPaint(0, Color.RED);
        plot.setRenderer(1, renderer2);

        // Размещение дополнительных осей справа
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
    }
}
