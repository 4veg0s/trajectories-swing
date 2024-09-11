package etu.nic.git.trajectories_swing.model;

import etu.nic.git.trajectories_swing.tools.StatisticsTool;

import javax.swing.table.AbstractTableModel;

/**
 * Модель данных таблицы статистической информации
 */
public class StatisticsTableModel extends AbstractTableModel {
    private final String[] ROW_NAMES = new String[]{
            "Среднее",
            "Дисперсия",
            "Выбор. момент 2-го порядка",
            "Выбор. момент 3-го порядка"
    };
    private final String[] columnNames = new String[]{
            "", "X, м", "Y, м", "Z, м", "Vx, м/с", "Vy, м/с", "Vz, м/с"
    };
    private final Class[] columnClass = new Class[]{
            String.class, String.class, String.class, String.class, String.class, String.class, String.class
    };
    private Object[][] data;
    private TrajectoryRowTableModel model;

    public StatisticsTableModel() {
    }

    /**
     * Создает модель данных таблицы со статистической информацией
     * @param tableModel модель данных таблицы траекторной информации
     */
    public StatisticsTableModel(TrajectoryRowTableModel tableModel) {
        this.model = tableModel;

        final int AMOUNT_OF_COLUMNS = 7;
        final int AMOUNT_OF_ROWS = 4;

        double value;
        data = new Object[AMOUNT_OF_ROWS][AMOUNT_OF_COLUMNS];

        // заполнение ячеек таблицы с первым столбцом в качестве наименований характеристик
        for (int i = 0; i < AMOUNT_OF_ROWS; i++) {
            data[i][0] = ROW_NAMES[i];
            for (int j = 1; j < AMOUNT_OF_COLUMNS; j++) {
                switch (i) {
                    case 0:
                        value = StatisticsTool.getAverageOfArray(model.getTrajectoryColumnByParameterIndex(j));
                        // если пришел NaN, то в таблицу попадет прочерк, иначе число с 8 знаками после запятой или ноль
                        data[i][j] = (Double.isNaN(value)) ? "--" : (value < Math.pow(10, -8)) ? 0 : String.format("%.8f", value);
                        break;
                    case 1:
                        value = StatisticsTool.getDispersionOfArray(model.getTrajectoryColumnByParameterIndex(j));
                        data[i][j] = (Double.isNaN(value)) ? "--" : (value < Math.pow(10, -8)) ? 0 : String.format("%.8f", value);
                        break;
                    case 2:
                        value = StatisticsTool.getSecondMomentOfArray(model.getTrajectoryColumnByParameterIndex(j));
                        data[i][j] = (Double.isNaN(value)) ? "--" : (value < Math.pow(10, -8)) ? 0 : String.format("%.8f", value);
                        break;
                    case 3:
                        value = StatisticsTool.getThirdMomentOfArray(model.getTrajectoryColumnByParameterIndex(j));
                        data[i][j] = (Double.isNaN(value)) ? "--" : (value < Math.pow(10, -8)) ? 0 : String.format("%.8f", value);
                        break;
                }
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClass[columnIndex];
    }

    @Override
    public int getRowCount() {
        return ROW_NAMES.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }
}
