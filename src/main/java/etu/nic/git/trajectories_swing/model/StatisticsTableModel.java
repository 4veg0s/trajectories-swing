package etu.nic.git.trajectories_swing.model;

import etu.nic.git.trajectories_swing.tools.StatisticsTool;

import javax.swing.table.AbstractTableModel;

public class StatisticsTableModel extends AbstractTableModel {
    private final String[] ROW_NAMES = new String[] {
            "Среднее",
            "Дисперсия",
            "Выбор. момент 2-го порядка",
            "Выбор. момент 3-го порядка"
    };
    private final String[] columnNames = new String[]{
            "", "X, м", "Y, м", "Z, м", "Vx, м/с", "Vy, м/с", "Vz, м/с"
    };
    private final Class[] columnClass = new Class[]{
            String.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class
    };
    private Object[][] data;
    private TrajectoryRowTableModel model;

    public StatisticsTableModel() {
    }

    public StatisticsTableModel(TrajectoryRowTableModel tableModel) {
        this.model = tableModel;

        final int AMOUNT_OF_COLUMNS = 7;
        final int AMOUNT_OF_ROWS = 4;
        data = new Object[AMOUNT_OF_ROWS][AMOUNT_OF_COLUMNS];
        for (int i = 0; i < AMOUNT_OF_ROWS; i++) {
            data[i][0] = ROW_NAMES[i];
            for (int j = 1; j < AMOUNT_OF_COLUMNS; j++) {
                switch (i) {
                    case 0:
                        data[i][j] = StatisticsTool.getAverageOfArray(model.getTrajectoryColumnByParameterIndex(j));
                        break;
                    case 1:
                        data[i][j] = StatisticsTool.getDispersionOfArray(model.getTrajectoryColumnByParameterIndex(j));
                        break;
                    case 2:
                        data[i][j] = StatisticsTool.getSecondMomentOfArray(model.getTrajectoryColumnByParameterIndex(j));
                        break;
                    case 3:
                        data[i][j] = StatisticsTool.getThirdMomentOfArray(model.getTrajectoryColumnByParameterIndex(j));
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
