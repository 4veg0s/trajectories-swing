package etu.nic.git.trajectories_swing.tools;

import etu.nic.git.trajectories_swing.model.TrajectoryRow;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;

import java.util.Arrays;
import java.util.List;

// fixme ограничить вычисление статистических параметров при малом количестве строк в таблице
public class StatisticsTool {
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
