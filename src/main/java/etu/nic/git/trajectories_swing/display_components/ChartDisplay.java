package etu.nic.git.trajectories_swing.display_components;

import etu.nic.git.trajectories_swing.model.TrajectoryRow;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;
import etu.nic.git.trajectories_swing.file_handling.TrajectoryFileStorage;
import etu.nic.git.trajectories_swing.tools.ChartCheckBoxToXYSeriesPair;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class ChartDisplay extends AbstractDisplay {
    public static final Color[] PARAMETER_COLORS = new Color[]{
            Color.RED,
            Color.BLUE,
            new Color(89, 65, 0),
            Color.RED,
            Color.BLUE,
            new Color(89, 65, 0)
    };
    private static final String DISPLAY_NAME = "График";
    private final TrajectoryFileStorage fileStorage;
    private final TrajectoryRowTableModel model;
    private ChartPanel chartPanel;
    private JPanel checkBoxPanel;
    private final JPanel checksAndChart;
    private JFreeChart chart;
    private XYPlot plot;
    private List<ChartCheckBoxToXYSeriesPair> checksToSeriesList;

    public ChartDisplay(TrajectoryRowTableModel tableModel, TrajectoryFileStorage fileStorage) {
        super(DISPLAY_NAME);

        this.model = tableModel;
        this.fileStorage = fileStorage;

        ItemListener checkBoxItemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updatePlot();
            }
        };

        checksToSeriesList = new ArrayList<>();

        JLabel coordinatesLabel = new JLabel("Координаты:");
        JCheckBox coordinateXCheckBox = new JCheckBox("X, м");
        JCheckBox coordinateYCheckBox = new JCheckBox("Y, м");
        JCheckBox coordinateZCheckBox = new JCheckBox("Z, м");

        checksToSeriesList.add(new ChartCheckBoxToXYSeriesPair(coordinateXCheckBox));
        checksToSeriesList.add(new ChartCheckBoxToXYSeriesPair(coordinateYCheckBox));
        checksToSeriesList.add(new ChartCheckBoxToXYSeriesPair(coordinateZCheckBox));

        JLabel velocitiesLabel = new JLabel("Проекции скорости:");
        JCheckBox velocityXCheckBox = new JCheckBox("Vx, м/с");
        JCheckBox velocityYCheckBox = new JCheckBox("Vy, м/с");
        JCheckBox velocityZCheckBox = new JCheckBox("Vz, м/с");

        checksToSeriesList.add(new ChartCheckBoxToXYSeriesPair(velocityXCheckBox));
        checksToSeriesList.add(new ChartCheckBoxToXYSeriesPair(velocityYCheckBox));
        checksToSeriesList.add(new ChartCheckBoxToXYSeriesPair(velocityZCheckBox));

        for (ChartCheckBoxToXYSeriesPair pair : checksToSeriesList) {
            pair.getCheckBox().setSelected(true);
            pair.getCheckBox().addItemListener(checkBoxItemListener);
//            pair.getCheckBox().addChangeListener(checkBoxChangeListener);
        }

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

        hideMainInfo();

        background.add(BorderLayout.NORTH, displayHeader);

        background.add(BorderLayout.CENTER, checksAndChart);

        chart = createChart();
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
        clearPlot();

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
            checksToSeriesList.get(i - 1).setSeries(series);
        }

        XYSeriesCollection coordinatesDataset = new XYSeriesCollection();

//        for (int i = 0; i < 3; i++) {
//            series = checksToSeriesList.get(i).getSeriesIfCheckBoxSelectedOrNullOtherwise();
//            if (series != null) {
//                coordinatesDataset.addSeries(series);
//            }
//        }
        coordinatesDataset.addSeries(checksToSeriesList.get(0).getSeries());
        coordinatesDataset.addSeries(checksToSeriesList.get(1).getSeries());
        coordinatesDataset.addSeries(checksToSeriesList.get(2).getSeries());

        XYSeriesCollection velocitiesDataset = new XYSeriesCollection();
//        for (int i = 3; i < 6; i++) {
//            series = checksToSeriesList.get(i).getSeriesIfCheckBoxSelectedOrNullOtherwise();
//            if (series != null) {
//                velocitiesDataset.addSeries(series);
//            }
//        }
        velocitiesDataset.addSeries(checksToSeriesList.get(3).getSeries());
        velocitiesDataset.addSeries(checksToSeriesList.get(4).getSeries());
        velocitiesDataset.addSeries(checksToSeriesList.get(5).getSeries());

        int axisIndex = 0;
        int seriesIndex = 0;
        // если хоть один чекбокс из серии координат выбран, то будет добавлена ось координат
        NumberAxis axis1 = new NumberAxis("Координата, м");
        axis1.setLabelPaint(Color.BLUE);
        axis1.setTickLabelPaint(Color.BLUE);
        plot.setRangeAxis(axisIndex, axis1);
        plot.setDataset(axisIndex, coordinatesDataset);
        plot.mapDatasetToRangeAxis(axisIndex, 0);

        // Настройка первого рендерера
        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(true, true);
        // создание формы круга для маркера координаты
        Shape circle = new Ellipse2D.Double(-3.0, -3.0, 6.0, 6.0);

        renderer1.setSeriesPaint(0, Color.RED);
        renderer1.setSeriesShape(0, circle);
        renderer1.setSeriesVisible(0, (checksToSeriesList.get(0).getCheckBox().isSelected()));

        renderer1.setSeriesPaint(1, Color.BLUE);
        renderer1.setSeriesShape(1, circle);
        renderer1.setSeriesVisible(1, (checksToSeriesList.get(1).getCheckBox().isSelected()));

        renderer1.setSeriesPaint(2, new Color(89, 65, 0));
        renderer1.setSeriesShape(2, circle);
        renderer1.setSeriesVisible(2, (checksToSeriesList.get(2).getCheckBox().isSelected()));

        plot.setRenderer(axisIndex, renderer1);
        plot.setRangeAxisLocation(axisIndex, AxisLocation.BOTTOM_OR_LEFT);
        axisIndex++;

        // если хоть один чекбокс из серии проекций скоростей выбран, то будет добавлена ось координат
        NumberAxis axis2 = new NumberAxis("Скорость, м/с");
        axis2.setLabelPaint(Color.RED);
        axis2.setTickLabelPaint(Color.RED);
        plot.setRangeAxis(axisIndex, axis2);
        plot.setDataset(axisIndex, velocitiesDataset);
        plot.mapDatasetToRangeAxis(axisIndex, 1);

        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, true);
        // создание формы треугольника для маркера скорости
        Path2D.Double triangle = new Path2D.Double();
        triangle.moveTo(0.0, -3.0);
        triangle.lineTo(3.0, 0.0);
        triangle.lineTo(0.0, 3.0);
        triangle.closePath();

        // установка маркера в виде треугольника и цвета для каждой из серий рендерера
        renderer2.setSeriesPaint(0, Color.RED);
        renderer2.setSeriesShape(0, triangle);
        renderer2.setSeriesVisible(0, (checksToSeriesList.get(3).getCheckBox().isSelected()));

        renderer2.setSeriesPaint(1, Color.BLUE);
        renderer2.setSeriesShape(1, triangle);
        renderer2.setSeriesVisible(1, (checksToSeriesList.get(4).getCheckBox().isSelected()));

        renderer2.setSeriesPaint(2, new Color(89, 65, 0));
        renderer2.setSeriesShape(2, triangle);
        renderer2.setSeriesVisible(2, (checksToSeriesList.get(5).getCheckBox().isSelected()));

        plot.setRenderer(axisIndex, renderer2);
        plot.setRangeAxisLocation(axisIndex, AxisLocation.BOTTOM_OR_RIGHT);

        plot.getRangeAxis(0).setVisible(this.shouldHaveCoordinatesAxis());
        plot.getRangeAxis(1).setVisible(this.shouldHaveVelocitiesAxis());
        plot.getDomainAxis().setVisible(this.shouldHaveCoordinatesAxis() || this.shouldHaveVelocitiesAxis());

        // изменение цвета фона у графика
        plot.setBackgroundPaint(Color.WHITE);

        // изменение цвета сетки
        plot.setRangeGridlinePaint(Color.DARK_GRAY);
        plot.setDomainGridlinePaint(Color.DARK_GRAY);

        chart.getLegend().setItemFont(new Font(Font.DIALOG, Font.PLAIN, 16));

        replaceChart();
    }

    /**
     * Метод очищает основную панель
     */
    private void replaceChart() {
        if (!fileStorage.isEmpty()) {
            if (chartPanel != null) {
                checksAndChart.remove(chartPanel);
                chartPanel = new ChartPanel(chart);
                checksAndChart.add(BorderLayout.CENTER, chartPanel);
            } else {
                chartPanel = new ChartPanel(chart);
                checksAndChart.add(BorderLayout.CENTER, chartPanel);
            }
            showMainInfo();
        }
        background.revalidate();
        background.repaint();
    }

    @Override
    public void updateComponentView() {
        updatePlot();
    }

    @Override
    public void restoreDefaultState() {
        hideMainInfo();
        background.revalidate();
        background.repaint();
    }

    public void hideMainInfo() {
        checksAndChart.setVisible(false);
    }

    public void showMainInfo() {
        checksAndChart.setVisible(true);
    }

    public boolean shouldHaveCoordinatesAxis() {
        return checksToSeriesList.get(0).getCheckBox().isSelected() ||
                checksToSeriesList.get(1).getCheckBox().isSelected() ||
                checksToSeriesList.get(2).getCheckBox().isSelected();
    }

    public boolean shouldHaveVelocitiesAxis() {
        return checksToSeriesList.get(3).getCheckBox().isSelected() ||
                checksToSeriesList.get(4).getCheckBox().isSelected() ||
                checksToSeriesList.get(5).getCheckBox().isSelected();
    }

    private void clearPlot() {
        plot.setDataset(null); // Удаление всех наборов данных
//        plot.clearDomainAxes(); // Очистка осей X
        plot.clearRangeAxes(); // Очистка осей Y
        plot.setRenderer(null); // Удаление рендерера
    }
}
