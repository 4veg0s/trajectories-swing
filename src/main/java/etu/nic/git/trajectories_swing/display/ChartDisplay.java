package etu.nic.git.trajectories_swing.display;

import etu.nic.git.trajectories_swing.model.TrajectoryRow;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;
import etu.nic.git.trajectories_swing.file.TrajectoryFileStorage;
import etu.nic.git.trajectories_swing.tool.ChartCheckBoxToXYSeriesPair;
import etu.nic.git.trajectories_swing.tool.MarkerShapes;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс содержащий все необходимое для отображения графика с панелью чекбоксов
 */
public class ChartDisplay extends AbstractDisplay {
    private static final String DISPLAY_NAME = "График";
    private final TrajectoryFileStorage fileStorage;
    private final TrajectoryRowTableModel model;
    private ChartPanel chartPanel;
    private JPanel checkBoxPanel;
    private final JPanel checksAndChart;
    private JFreeChart chart;
    private XYPlot plot;
    private List<ChartCheckBoxToXYSeriesPair> checksToSeriesList;
    private boolean markersAsLettersOnChart = false;

    /**
     * Создает объект и внедряет зависимости
     * @param tableModel модель таблицы траекторной информации
     * @param fileStorage хранилище файлов траекторий
     */
    public ChartDisplay(TrajectoryRowTableModel tableModel, TrajectoryFileStorage fileStorage) {
        super(DISPLAY_NAME);

        this.model = tableModel;
        this.fileStorage = fileStorage;

        // создание слушателя изменения значения в чекбоксе, при срабатывании которого происходит перерисовка графика
        ItemListener checkBoxItemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updatePlot();
            }
        };

        checksToSeriesList = new ArrayList<>();

        Font font14 = new Font(Font.DIALOG, Font.BOLD, 14);

        JLabel coordinatesLabel = new JLabel("Координаты:");
        coordinatesLabel.setFont(font14);
        JCheckBox coordinateXCheckBox = new JCheckBox("X, м");
        coordinateXCheckBox.setFont(font14);
        JCheckBox coordinateYCheckBox = new JCheckBox("Y, м");
        coordinateYCheckBox.setFont(font14);
        JCheckBox coordinateZCheckBox = new JCheckBox("Z, м");
        coordinateZCheckBox.setFont(font14);

        // заполнение списка объектов-пар чекбокса и серии данных с графика
        checksToSeriesList.add(new ChartCheckBoxToXYSeriesPair(coordinateXCheckBox));
        checksToSeriesList.add(new ChartCheckBoxToXYSeriesPair(coordinateYCheckBox));
        checksToSeriesList.add(new ChartCheckBoxToXYSeriesPair(coordinateZCheckBox));

        JLabel velocitiesLabel = new JLabel("Проекции скорости:");
        velocitiesLabel.setFont(font14);
        JCheckBox velocityXCheckBox = new JCheckBox("Vx, м/с");
        velocityXCheckBox.setFont(font14);
        JCheckBox velocityYCheckBox = new JCheckBox("Vy, м/с");
        velocityYCheckBox.setFont(font14);
        JCheckBox velocityZCheckBox = new JCheckBox("Vz, м/с");
        velocityZCheckBox.setFont(font14);

        // заполнение списка объектов-пар чекбокса и серии данных с графика
        checksToSeriesList.add(new ChartCheckBoxToXYSeriesPair(velocityXCheckBox));
        checksToSeriesList.add(new ChartCheckBoxToXYSeriesPair(velocityYCheckBox));
        checksToSeriesList.add(new ChartCheckBoxToXYSeriesPair(velocityZCheckBox));

        // при создании панели с чекбоксами все будут установлены на selected
        for (ChartCheckBoxToXYSeriesPair pair : checksToSeriesList) {
            pair.getCheckBox().setSelected(true);
            pair.getCheckBox().addItemListener(checkBoxItemListener);   // добавление слушателя на чекбокс после установки selected,
                                                                        // чтобы не стриггерить событие раньше времени
        }

        Box horizontalBox = new Box(BoxLayout.X_AXIS);
        horizontalBox.add(coordinatesLabel);
        horizontalBox.add(coordinateXCheckBox);
        horizontalBox.add(coordinateYCheckBox);
        horizontalBox.add(coordinateZCheckBox);

        horizontalBox.add(new JLabel("     "));     // болванка - пустое пространство между чекбоксами для координат и скорости

        horizontalBox.add(velocitiesLabel);
        horizontalBox.add(velocityXCheckBox);
        horizontalBox.add(velocityYCheckBox);
        horizontalBox.add(velocityZCheckBox);

        // панель чекбоксов с горизонтальным BoxLayout'ом
        checkBoxPanel = new JPanel();
        checkBoxPanel.add(horizontalBox);

        checksAndChart = new JPanel(new BorderLayout());
        checksAndChart.add(BorderLayout.NORTH, checkBoxPanel);

        hideMainInfo();

        background.add(BorderLayout.NORTH, displayHeader);

        background.add(BorderLayout.CENTER, checksAndChart);

        chart = createChart();
    }


    /**
     * Создает объект графика, инициализирует поле plot
     * @return соответствующий объект с инициализированной осью абсцисс
     */
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

    /**
     * Обновляет информацию на графике, в соответствии с данными модели таблицы и выбранными чекбоксами
     */
    public void updatePlot() {
        clearPlot();

        List<TrajectoryRow> trajectoryRowList = model.getTrajectoryRowList();

        XYSeries series;
        for (int i = 1; i < TrajectoryRow.AMOUNT_OF_PARAMETERS; i++) {
            series = new XYSeries(TrajectoryRow.PARAMETER_NAMES[i]);
            for (int j = 0; j < trajectoryRowList.size(); j++) {  // для каждого из параметров траектории, кроме времени
                double[] rowParameters = trajectoryRowList.get(j).toDoubleArray();
                series.add(rowParameters[0], rowParameters[i]);
            }
            checksToSeriesList.get(i - 1).setSeries(series);
        }

        // датасет координат
        XYSeriesCollection coordinatesDataset = new XYSeriesCollection();
        // добавляем в датасет координаты
        coordinatesDataset.addSeries(checksToSeriesList.get(0).getSeries());
        coordinatesDataset.addSeries(checksToSeriesList.get(1).getSeries());
        coordinatesDataset.addSeries(checksToSeriesList.get(2).getSeries());

        // датасет проекций скоростей
        XYSeriesCollection velocitiesDataset = new XYSeriesCollection();

        // добавляем в датасет проекции скоростей
        velocitiesDataset.addSeries(checksToSeriesList.get(3).getSeries());
        velocitiesDataset.addSeries(checksToSeriesList.get(4).getSeries());
        velocitiesDataset.addSeries(checksToSeriesList.get(5).getSeries());

        int axisIndex = 0;
        // если хоть один чекбокс из серии координат выбран, то будет добавлена ось координат
        NumberAxis axis1 = new NumberAxis("Координата, м");
        axis1.setLabelPaint(Color.BLUE);
        axis1.setTickLabelPaint(Color.BLUE);
        axis1.setLabelFont(new Font(Font.DIALOG, Font.PLAIN, 14));
        plot.setRangeAxis(axisIndex, axis1);
        plot.setDataset(axisIndex, coordinatesDataset);
        plot.mapDatasetToRangeAxis(axisIndex, 0);

        // Настройка первого рендерера
        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(true, true);
        if (isMarkersAsLettersOnChart()) {
            // установка маркера в виде соответствующей буквы и цвета для каждой из серий рендерера
            renderer1.setSeriesPaint(0, Color.BLUE);
            renderer1.setSeriesShape(0, MarkerShapes.getShapeX());
            renderer1.setSeriesVisible(0, (checksToSeriesList.get(0).getCheckBox().isSelected()));

            renderer1.setSeriesPaint(1, Color.BLUE);
            renderer1.setSeriesShape(1, MarkerShapes.getShapeY());
            renderer1.setSeriesVisible(1, (checksToSeriesList.get(1).getCheckBox().isSelected()));

            renderer1.setSeriesPaint(2, Color.BLUE);
            renderer1.setSeriesShape(2, MarkerShapes.getShapeZ());
            renderer1.setSeriesVisible(2, (checksToSeriesList.get(2).getCheckBox().isSelected()));
        } else {
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
        }

        // добавляем в график рендерер координат
        plot.setRenderer(axisIndex, renderer1);
        plot.setRangeAxisLocation(axisIndex, AxisLocation.BOTTOM_OR_LEFT);
        axisIndex++;

        // если хоть один чекбокс из серии проекций скоростей выбран, то будет добавлена ось координат
        NumberAxis axis2 = new NumberAxis("Скорость, м/с");
        axis2.setLabelFont(new Font(Font.DIALOG, Font.PLAIN, 14));
        axis2.setLabelPaint(Color.RED);
        axis2.setTickLabelPaint(Color.RED);
        plot.setRangeAxis(axisIndex, axis2);
        plot.setDataset(axisIndex, velocitiesDataset);
        plot.mapDatasetToRangeAxis(axisIndex, 1);

        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, true);

        if (isMarkersAsLettersOnChart()) {
            // установка маркера в виде соответствующей буквы и цвета для каждой из серий рендерера
            renderer2.setSeriesPaint(0, Color.RED);
            renderer2.setSeriesShape(0, MarkerShapes.getShapeX());
            renderer2.setSeriesVisible(0, (checksToSeriesList.get(3).getCheckBox().isSelected()));

            renderer2.setSeriesPaint(1, Color.RED);
            renderer2.setSeriesShape(1, MarkerShapes.getShapeY());
            renderer2.setSeriesVisible(1, (checksToSeriesList.get(4).getCheckBox().isSelected()));

            renderer2.setSeriesPaint(2, Color.RED);
            renderer2.setSeriesShape(2, MarkerShapes.getShapeZ());
            renderer2.setSeriesVisible(2, (checksToSeriesList.get(5).getCheckBox().isSelected()));
        } else {
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
        }

        // добавляем в график рендерер проекций скорости
        plot.setRenderer(axisIndex, renderer2);
        plot.setRangeAxisLocation(axisIndex, AxisLocation.BOTTOM_OR_RIGHT);

        // установка видимости осей ординат, в зависимости от условий
        plot.getRangeAxis(0).setVisible(this.shouldHaveCoordinatesAxis());
        plot.getRangeAxis(1).setVisible(this.shouldHaveVelocitiesAxis());
        // установка видимости оси абсцисс, в зависимости от условий
        plot.getDomainAxis().setVisible(this.shouldHaveCoordinatesAxis() || this.shouldHaveVelocitiesAxis());

        // изменение цвета фона у графика
        plot.setBackgroundPaint(Color.WHITE);

        // изменение цвета сетки
        plot.setRangeGridlinePaint(Color.DARK_GRAY);
        plot.setDomainGridlinePaint(Color.DARK_GRAY);

        // установка шрифта для легенды под графиком
        chart.getLegend().setItemFont(new Font(Font.DIALOG, Font.PLAIN, 16));

        replaceChart();
    }

    /**
     * Метод удаляет предыдущую панель графика и добавляет новую с перерисовкой компонента
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

    /**
     * Скрывает компоненты дисплея
     */
    public void hideMainInfo() {
        checksAndChart.setVisible(false);
    }

    /**
     * Показывает компоненты дисплея
     */
    public void showMainInfo() {
        checksAndChart.setVisible(true);
    }

    /**
     * Метод сообщает: нужно ли графику иметь ось координат
     * @return true, если хоть одна из координат выбрана, false, если все галочки координат отключены
     */
    public boolean shouldHaveCoordinatesAxis() {
        return checksToSeriesList.get(0).getCheckBox().isSelected() ||
                checksToSeriesList.get(1).getCheckBox().isSelected() ||
                checksToSeriesList.get(2).getCheckBox().isSelected();
    }

    /**
     * Метод сообщает: нужно ли графику иметь ось проекций скорости
     * @return true, если хоть одна из проекций выбрана, false, если все галочки проекций скорости отключены
     */
    public boolean shouldHaveVelocitiesAxis() {
        return checksToSeriesList.get(3).getCheckBox().isSelected() ||
                checksToSeriesList.get(4).getCheckBox().isSelected() ||
                checksToSeriesList.get(5).getCheckBox().isSelected();
    }

    /**
     * Очищает график от предыдущих датасетов
     */
    private void clearPlot() {
        plot.setDataset(null); // Удаление всех наборов данных
        plot.clearRangeAxes(); // Очистка осей Y
        plot.setRenderer(null); // Удаление рендерера
    }

    public boolean isMarkersAsLettersOnChart() {
        return markersAsLettersOnChart;
    }

    /**
     * Устанавливает параметр, отвечающий за отображение маркеров на графике в виде соответствующих параметрам букв X, Y или Z
     * @param markersAsLettersOnChart true, если необходимо, чтобы при описанных обстоятельствах маркеры отображались как буквы,
     *                                          false, если необходимо чтобы у датасета соответствующего координатам были одни маркеры, а у проекций скорости - другие
     */
    public void setMarkersAsLettersOnChart(boolean markersAsLettersOnChart) {
        this.markersAsLettersOnChart = markersAsLettersOnChart;
    }
}
