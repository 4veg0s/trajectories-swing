package etu.nic.git.trajectories_swing.tools;

import etu.nic.git.trajectories_swing.model.TrajectoryRow;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;

import java.util.Arrays;
import java.util.List;

public class StatisticsTool {
    private final Object[] ROW_NAMES = new String[] {
            "Среднее",
            "Дисперсия",
            "Выбор. момент 2-го порядка",
            "Выбор. момент 3-го порядка"
    };
    private final TrajectoryRowTableModel model;
    private List<TrajectoryRow> trajectoryRowList;
    private Object[][] data;

    public StatisticsTool(TrajectoryRowTableModel model) {
        this.model = model;
        trajectoryRowList = model.getTrajectoryRowList();

        final int AMOUNT_OF_COLUMNS = 7;
        final int AMOUNT_OF_ROWS = 4;
        data = new Object[AMOUNT_OF_ROWS][AMOUNT_OF_COLUMNS];
        for (int i = 0; i < AMOUNT_OF_ROWS; i++) {
            data[i][0] = ROW_NAMES[i];
            for (int j = 1; j < AMOUNT_OF_COLUMNS; j++) {
                switch (i) {
                    case 0:
                        data[i][j] = StatisticsTool.getAverageOfArray(getTrajectoryColumnByParameterIndex(j));
                        break;
                    case 1:
                        data[i][j] = StatisticsTool.getDispersionOfArray(getTrajectoryColumnByParameterIndex(j));
                        break;
                    case 2:
                        data[i][j] = StatisticsTool.getSecondMomentOfArray(getTrajectoryColumnByParameterIndex(j));
                        break;
                    case 3:
                        data[i][j] = StatisticsTool.getThirdMomentOfArray(getTrajectoryColumnByParameterIndex(j));
                        break;
                }
            }
        }
    }

    public Object[][] getData() {
        return data;
    }

    /**
     * Метод получает из списка строк траекторной информации конкретный столбец по индексу его параметра в траектории
     * @param index индекс параметра траектории (их 7, см. {@link TrajectoryRow#AMOUNT_OF_PARAMETERS}, {@link TrajectoryRow#PARAMETER_NAMES})
     * @return столбец данных
     */
    public double[] getTrajectoryColumnByParameterIndex(int index) {
        double[] column = new double[trajectoryRowList.size()];
        for (int i = 0; i < trajectoryRowList.size(); i++) {
            column[i] = trajectoryRowList.get(i).toDoubleArray()[index];
        }
        return column;
    }

    /**
     * Метод вычисляющий <b>среднее значение</b> в массиве
     * @param array массив, для которого необходимо посчитать среднее
     * @return число - среднее по массиву
     */
    public static double getAverageOfArray(double[] array) {
        return Arrays.stream(array)
                .reduce(
                        0, (subtotal, element) -> subtotal + element
                ) / array.length;
    }

    /**
     * Метод вычисляющий значение <b>дисперсии</b> в массиве
     * @param array массив, для которого необходимо посчитать дисперсию
     * @return число - дисперсия массива
     */
    public static double getDispersionOfArray(double[] array) {
        double average = getAverageOfArray(array);
        return Arrays.stream(array)
                .reduce(
                        0, (subtotal, element) -> subtotal + (element - average) * (element - average)
                ) / array.length;
    }

    /**
     * Метод вычисляющий значение <b>выборочного момента второго порядка</b> в массиве
     * @param array массив, для которого необходимо посчитать выборочный момент второго порядка
     * @return число - выборочный момент второго порядка
     */
    public static double getSecondMomentOfArray(double[] array) {
        double average = getAverageOfArray(array);
        return Arrays.stream(array)
                .reduce(
                        0, (subtotal, element) -> subtotal + (element - average) * (element - average)
                ) / (array.length - 1);
    }
    /**
     * Метод вычисляющий значение <b>выборочного момента третьего порядка</b> в массиве
     * @param array массив, для которого необходимо посчитать выборочный момент третьего порядка
     * @return число - выборочный момент третьего порядка
     */
    public static double getThirdMomentOfArray(double[] array) {
        double average = getAverageOfArray(array);
        return Arrays.stream(array)
                .reduce(
                        0,
                        (subtotal, element) -> subtotal + (element - average) * (element - average) * (element - average)
                ) / array.length;
    }
}
