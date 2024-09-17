package etu.nic.git.trajectories_swing.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Модель данных таблицы траекторной информации
 */
public class TrajectoryRowTableModel extends AbstractTableModel {
    private static final Logger logger = LoggerFactory.getLogger(TrajectoryRowTableModel.class);
    private List<TrajectoryRow> trajectoryRowList;

    private final String[] columnNames = new String[]{
            "T, с", "X, м", "Y, м", "Z, м", "Vx, м/с", "Vy, м/с", "Vz, м/с"
    };
    private final Class[] columnClass = new Class[]{
            Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class
    };

    public TrajectoryRowTableModel() {
    }

    /**
     * Создает модель данных на основе списка строк траектории
     * @param trajectoryRowList список строк траектории
     */
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

    // Время, x-координата, y-координата, z-координата, x-составляющая скорости, y-составляющая скорости, z-составляющая скорости
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TrajectoryRow row = trajectoryRowList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return row.getTime();
            case 1:
                return row.getCoordinateX();
            case 2:
                return row.getCoordinateY();
            case 3:
                return row.getCoordinateZ();
            case 4:
                return row.getVelocityX();
            case 5:
                return row.getVelocityY();
            case 6:
                return row.getVelocityZ();
            default:
                return null;
        }
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

    /**
     * Сеттер списка траекторных строк с сортировкой по времени
     * @param trajectoryRowList список траекторных строк
     */
    public void setTrajectoryRowList(List<TrajectoryRow> trajectoryRowList) {
        this.trajectoryRowList = trajectoryRowList;
        this.sortByTime();
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
     * Сортирует список строк траектории по времени
     */
    public void sortByTime() {
        trajectoryRowList = trajectoryRowList.stream()
                .sorted(((o1, o2) -> Double.compare(o1.getTime(), o2.getTime())))
                .collect(Collectors.toList());
    }

    /**
     * Получает всю информацию из таблицы в формате файла траекторной информации
     * @return траекторная информация из списка в виде строки
     */
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

    /**
     * Вставляет новую строку траектории выше заданного индекса
     * @param index индекс выбранной строки
     */
    public void insertRowAbove(int index) {  // сюда не должен попадать несуществующий индекс
        if (index > trajectoryRowList.size() - 1) {
            logger.error("Как сюда попал такой индекс");
        }

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

    /**
     * Вставляет новую строку траектории ниже заданного индекса
     * @param index индекс выбранной строки
     */
    public void insertRowBelow(int index) {
        if (index > trajectoryRowList.size() - 1) {
            logger.error("Как сюда попал такой индекс");
        }

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

    /**
     * Удаляет строку траектории по заданному индексу
     * @param index индекс выбранной строки
     */
    public void deleteRow(int index) {
        if (index > trajectoryRowList.size() - 1) {
            logger.error("Как сюда попал такой индекс");
        }

        trajectoryRowList.remove(index);
    }
}
