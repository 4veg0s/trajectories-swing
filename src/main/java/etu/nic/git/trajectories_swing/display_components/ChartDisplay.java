package etu.nic.git.trajectories_swing.display_components;

import etu.nic.git.trajectories_swing.model.TrajectoryRow;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;
import etu.nic.git.trajectories_swing.tools.TrajectoryFileStorage;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class ChartDisplay {
    private final TrajectoryFileStorage fileStorage;
    private final JLabel displayHeader;
    private final JPanel background;
    private final TrajectoryRowTableModel model;
    private ChartPanel chartPanel;
    private JPanel checkBoxPanel;
    private final JPanel checksAndChart;
    private JFreeChart chart;
    private XYPlot plot;

    public ChartDisplay(TrajectoryRowTableModel tableModel, TrajectoryFileStorage fileStorage) {
        this.model = tableModel;
        this.fileStorage = fileStorage;

        displayHeader = new JLabel("График");

        displayHeader.setHorizontalAlignment(SwingConstants.CENTER);
        displayHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

        JLabel coordinatesLabel = new JLabel("Координаты:");
        JCheckBox coordinateXCheckBox = new JCheckBox("X, м");
        JCheckBox coordinateYCheckBox = new JCheckBox("Y, м");
        JCheckBox coordinateZCheckBox = new JCheckBox("Z, м");

        JLabel velocitiesLabel = new JLabel("Проекции скорости:");
        JCheckBox velocityXCheckBox = new JCheckBox("Vx, м/с");
        JCheckBox velocityYCheckBox = new JCheckBox("Vy, м/с");
        JCheckBox velocityZCheckBox = new JCheckBox("Vz, м/с");

        Box horizontalBox = new Box(BoxLayout.X_AXIS);
        horizontalBox.add(coordinatesLabel);
        horizontalBox.add(coordinateXCheckBox);
        horizontalBox.add(coordinateYCheckBox);
        horizontalBox.add(coordinateZCheckBox);
        horizontalBox.add(velocitiesLabel);
        horizontalBox.add(velocityXCheckBox);
        horizontalBox.add(velocityYCheckBox);
        horizontalBox.add(velocityZCheckBox);

        checkBoxPanel = new JPanel();
        checkBoxPanel.add(horizontalBox);

        checksAndChart = new JPanel(new BorderLayout());
        checksAndChart.add(BorderLayout.NORTH, checkBoxPanel);

        background = new JPanel(new BorderLayout());
        background.add(BorderLayout.NORTH, displayHeader);
//        background.add(BorderLayout.CENTER, scrollPane);

        chart = createChart();
    }

    public JComponent getComponent() {
        return background;
    }

    private JFreeChart createChart() {
        // Создаем график с основной осью Y
        JFreeChart chart = ChartFactory.createXYLineChart(
                null,
                "Время, с",
                null,
                null
        );
        plot = chart.getXYPlot();

        return chart;
    }

    public void updatePlot() {
        List<TrajectoryRow> trajectoryRowList = model.getTrajectoryRowList();
        List<XYSeries> trajectorySeries = new ArrayList<>();
        XYSeries series;
        for (int i = 1; i < TrajectoryRow.AMOUNT_OF_PARAMETERS; i++) {
            series = new XYSeries(TrajectoryRow.PARAMETER_NAMES[i]);
            for (int j = 0; j < trajectoryRowList.size(); j++) {  // для каждого из параметров траектории, кроме времени
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
        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(true, true);
        renderer1.setSeriesPaint(0, Color.RED);
        renderer1.setSeriesPaint(1, Color.BLUE);
        renderer1.setSeriesPaint(2, new Color(89, 65, 0));

        Shape circle = new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0);

        renderer1.setSeriesShape(0, circle);
        renderer1.setSeriesShape(1, circle);
        renderer1.setSeriesShape(2, circle);

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

        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, true);
        renderer2.setSeriesPaint(0, Color.RED);
        renderer2.setSeriesPaint(1, Color.BLUE);
        renderer2.setSeriesPaint(2, new Color(89, 65, 0));

        // создание формы треугольника для маркера скорости
        Path2D.Double triangle = new Path2D.Double();
        triangle.moveTo(0.0, -3.0);
        triangle.lineTo(3.0, 0.0);
        triangle.lineTo(0.0, 3.0);
        triangle.closePath();

        // установка маркера в виде треугольника для каждой из серий рендерера
        renderer2.setSeriesShape(0, triangle);
        renderer2.setSeriesShape(1, triangle);
        renderer2.setSeriesShape(2, triangle);
        plot.setRenderer(1, renderer2);

        // Размещение дополнительных осей справа
        plot.setRangeAxisLocation(0, AxisLocation.BOTTOM_OR_LEFT);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);

        chart.getLegend().setItemFont(new Font(Font.DIALOG, Font.PLAIN, 16));

        replaceChart();
    }

    /**
     * метод очищает основную панель
     */
    private void replaceChart() {
        if (!fileStorage.isEmpty()) {
            if (chartPanel != null) {
                checksAndChart.remove(chartPanel);
                chartPanel = new ChartPanel(chart);
                checksAndChart.add(BorderLayout.CENTER, chartPanel);
                background.add(BorderLayout.CENTER, checksAndChart);
            } else {
                chartPanel = new ChartPanel(chart);
                checksAndChart.add(BorderLayout.CENTER, chartPanel);
                background.add(BorderLayout.CENTER, checksAndChart);
            }
        }
    }

    public void restoreDefaultState() {
        background.remove(checksAndChart);
        background.revalidate();
        background.repaint();
    }
}
