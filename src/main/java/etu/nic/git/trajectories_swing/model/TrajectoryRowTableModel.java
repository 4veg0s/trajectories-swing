package etu.nic.git.trajectories_swing.model;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.stream.Collectors;

public class TrajectoryRowTableModel extends AbstractTableModel {
    private List<TrajectoryRow> trajectoryRowList;

    private final String[] columnNames = new String[]{
            "T, с", "X, м", "Y, м", "Z, м", "Vx, м/с", "Vy, м/с", "Vz, м/с"
    };
    private final Class[] columnClass = new Class[]{
            Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class
    };

    public TrajectoryRowTableModel() {
    }

    public TrajectoryRowTableModel(List<TrajectoryRow> trajectoryRowList) {
        this.trajectoryRowList = trajectoryRowList;
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
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return trajectoryRowList.size();
    }

    // Время, x-координата, y-координата, z-координата, x-составляющая скорости, yсоставляющая скорости, z-составляющая скорости
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TrajectoryRow row = trajectoryRowList.get(rowIndex);
        if (0 == columnIndex) {
            return row.getTime();
        } else if (1 == columnIndex) {
            return row.getCoordinateX();
        } else if (2 == columnIndex) {
            return row.getCoordinateY();
        } else if (3 == columnIndex) {
            return row.getCoordinateZ();
        } else if (4 == columnIndex) {
            return row.getVelocityX();
        } else if (5 == columnIndex) {
            return row.getVelocityY();
        } else if (6 == columnIndex) {
            return row.getVelocityZ();
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        TrajectoryRow row = trajectoryRowList.get(rowIndex);
        if (0 == columnIndex) {
            row.setTime((Double) aValue);
        } else if (1 == columnIndex) {
            row.setCoordinateX((Double) aValue);
        } else if (2 == columnIndex) {
            row.setCoordinateY((Double) aValue);
        } else if (3 == columnIndex) {
            row.setCoordinateZ((Double) aValue);
        } else if (4 == columnIndex) {
            row.setVelocityX((Double) aValue);
        } else if (5 == columnIndex) {
            row.setVelocityY((Double) aValue);
        } else if (6 == columnIndex) {
            row.setVelocityZ((Double) aValue);
        }
        fireTableDataChanged();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }

    public List<TrajectoryRow> getTrajectoryRowList() {
        return trajectoryRowList;
    }

    public void setTrajectoryRowList(List<TrajectoryRow> trajectoryRowList) {
        this.trajectoryRowList = trajectoryRowList;
        this.sortByTime();
    }

    public void sortByTime() {
        trajectoryRowList = trajectoryRowList.stream()
                .sorted(((o1, o2) -> Double.compare(o1.getTime(), o2.getTime())))
                .collect(Collectors.toList());
    }

    public String getTableDataInString() {
        StringBuilder tableData = new StringBuilder();
        for (TrajectoryRow row : trajectoryRowList) {
            tableData.append(row.toFileString());
            tableData.append("\n");
        }
        return tableData.toString();
    }
    /**
     * Метод проверяет, является ли элемент под данным индексом первым в списке
     * @param index индекс, для которого необходимо выполнить проверку
     * @return true, если элемент по данному индексу является первым, false - иначе
     */
    public boolean isFirstRowAt(int index) {
        return !trajectoryRowList.isEmpty() && index == 0;
    }

    /**
     * Метод проверяет, является ли элемент под данным индексом последним в списке
     * @param index индекс, для которого необходимо выполнить проверку
     * @return true, если элемент по данному индексу является последним, false - иначе
     */
    public boolean isLastRowAt(int index) {
        return !trajectoryRowList.isEmpty() && index == trajectoryRowList.size() - 1;
    }

    // TODO
    public void insertRowAbove(int index) {  // сюда не должен попадать несуществующий индекс
        // fixme
        if (index > trajectoryRowList.size() - 1) throw new RuntimeException("Как сюда попал такой индекс");

        TrajectoryRow newRow = new TrajectoryRow();
        if (isFirstRowAt(index)) {
            newRow.setTime(trajectoryRowList.get(index).getTime()); // копируем время из ранее крайней строки
        } else {    // если вставляем строку не на первое место
            double midTime = (trajectoryRowList.get(index).getTime() +
                    trajectoryRowList.get(index - 1).getTime()) / 2;    // устанавливаем среднее время от соседних
            newRow.setTime(midTime);
        }
        trajectoryRowList.add(index, newRow);
    }
    public void insertRowBelow(int index) {
        // fixme
        if (index > trajectoryRowList.size() - 1) throw new RuntimeException("Как сюда попал такой индекс");

        TrajectoryRow newRow = new TrajectoryRow();
        if (isLastRowAt(index)) {
            newRow.setTime(trajectoryRowList.get(index).getTime()); // копируем время из ранее крайней строки
        } else {    // если вставляем строку не на первое место
            double midTime = (trajectoryRowList.get(index).getTime() +
                    trajectoryRowList.get(index + 1).getTime()) / 2;    // устанавливаем среднее время от соседних
            newRow.setTime(midTime);
        }
        trajectoryRowList.add(index + 1, newRow);

    }
    public void deleteRow(int index) {
        // fixme
        if (index > trajectoryRowList.size() - 1) throw new RuntimeException("Как сюда попал такой индекс");

        trajectoryRowList.remove(index);
    }
}
