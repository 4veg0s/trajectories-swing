package etu.nic.git.trajectories_swing.model;

import etu.nic.git.trajectories_swing.exception.InvalidAmountOfParametersException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Класс траекторной информации в конкретную секунду времени (одна строка таблицы траекторной информации)
 */
public class TrajectoryRow {
    private static final Logger logger = LoggerFactory.getLogger(TrajectoryRow.class);
    public static final int AMOUNT_OF_PARAMETERS = 7;
    public static final String[] PARAMETER_NAMES = new String[]{
            "time",
            "X, м",
            "Y, м",
            "Z, м",
            "Vx, м/с",
            "Vy, м/с",
            "Vz, м/с"
    };
    private double time;
    private double coordinateX;
    private double coordinateY;
    private double coordinateZ;
    private double velocityX;
    private double velocityY;
    private double velocityZ;

    public TrajectoryRow() {
    }

    public TrajectoryRow(double time, double CoordinateX, double CoordinateY, double CoordinateZ, double VelocityX, double VelocityY, double VelocityZ) {
        this.time = time;
        this.coordinateX = CoordinateX;
        this.coordinateY = CoordinateY;
        this.coordinateZ = CoordinateZ;
        this.velocityX = VelocityX;
        this.velocityY = VelocityY;
        this.velocityZ = VelocityZ;
    }

    /**
     * Метод создает объект на основе строки
     * @param rawString строка, потенциально являющаяся строкой траектории
     * @return объект траекторной строки
     */
    public static TrajectoryRow buildTrajectoryRowFromString(String rawString) {
        String[] columns = rawString.split("\\s+");
        Double[] convertedValues = new Double[columns.length];

        for (int i = 0; i < columns.length; i++) {
            convertedValues[i] = Double.parseDouble(columns[i]);
        }
        return new TrajectoryRow(
                convertedValues[0],
                convertedValues[1],
                convertedValues[2],
                convertedValues[3],
                convertedValues[4],
                convertedValues[5],
                convertedValues[6]
        );
    }

    /**
     * Получение массива double из траектории
     * @return массив double, соответствующий траекторной строке
     */
    public double[] toDoubleArray() {
        return new double[]{
                this.time,
                this.coordinateX,
                this.coordinateY,
                this.coordinateZ,
                this.velocityX,
                this.velocityY,
                this.velocityZ
        };
    }

    /**
     * Метод парсит потенциальную строку с данными траектории и возвращает результат валидации
     */
    // парсит потенциальную строку с данными траектории и возвращает результат валидации данных
    public static boolean isValidTrajectoryString(String rawString) {

        // если количество элементов после дробления не соответствует количеству параметров траектории
        if (!isValidAmountOfParametersInTrajectoryString(rawString)) {
            return false;
        }

        String[] columns = rawString.split("\\s+");

        for (String column : columns) {
            if (!isDoubleOrSciDouble(column)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Метод парсит потенциальную строку с данными траектории и возвращает результат валидации
     *
     * @throws InvalidAmountOfParametersException если в строке неверное число параметров траектории
     */
    // парсит потенциальную строку с данными траектории и возвращает результат валидации данных
    public static void isValidTrajectoryStringWithExceptions(String rawString) throws InvalidAmountOfParametersException, NumberFormatException {

        // если количество элементов после дробления не соответствует количеству параметров траектории
        if (!isValidAmountOfParametersInTrajectoryString(rawString)) {
            throw new InvalidAmountOfParametersException();
        }

        String[] columns = rawString.split("\\s+");

        for (String column : columns) {
            Double.parseDouble(column);
        }
    }

    /**
     * Метод возвращает -1, если все параметры входной строки валидны, иначе - индекс первого невалидного параметра.
     * Выбрасывает исключение, если количество параметров в строке неверное
     */
    public static int indexOfInvalidParameter(String rawString) {
        if (isValidAmountOfParametersInTrajectoryString(rawString)) {
            String[] split = rawString.split("\\s+");
            for (int i = 0; i < split.length; i++) {
                if (!isDoubleOrSciDouble(split[i])) {
                    return i;
                }
            }
            return -1;
        } else {
            logger.error("Как сюда попало неверное число параметров?");
            throw new RuntimeException("Как сюда попало неверное число параметров?");
        }
    }

    /**
     * Проверяет строку на соответствие количеству параметров в траектории
     * @param rawString строка для проверки
     * @return
     */
    public static boolean isValidAmountOfParametersInTrajectoryString(String rawString) {
        String[] columns = rawString.split("\\s+");
        return columns.length == AMOUNT_OF_PARAMETERS;
    }

    /**
     * Проверяет строку на соответствие формату числа с плавающей точкой, в том числе - в научном формате (число содержит E)
     * @param stringToCheck строка для проверки
     * @return true, если строка соответствует критерию, иначе - false
     */
    public static boolean isDoubleOrSciDouble(String stringToCheck) {
        return Pattern.matches("^[+-]?(\\d+(\\.\\d*)?|\\.\\d+)([eE][+-]?\\d+)?$", stringToCheck);
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(double coordinateX) {
        this.coordinateX = coordinateX;
    }

    public double getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(double coordinateY) {
        this.coordinateY = coordinateY;
    }

    public double getCoordinateZ() {
        return coordinateZ;
    }

    public void setCoordinateZ(double coordinateZ) {
        this.coordinateZ = coordinateZ;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public double getVelocityZ() {
        return velocityZ;
    }

    public void setVelocityZ(double velocityZ) {
        this.velocityZ = velocityZ;
    }

    /**
     * Метод для нахождения индекса параметра траектории по его имени
     * @param name строка, индекс в массиве параметров которой необходимо найти
     * @return Индекс найденного параметра.
     * -1, если такой параметр не был найден
     */
    public static int getIndexOfMatchingParameterName(String name) {
        for (int i = 0; i < PARAMETER_NAMES.length; i++) {
            if (name.equals(PARAMETER_NAMES[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * "toString", но в формате, как строка из исходного файла
     * @return строка с параметрами траектории через два пробела
     */
    public String toFileString() {  //
        return String.format("%.3f", time).replace(",", ".") + "  " +
                String.format("%.1f", coordinateX).replace(",", ".") + "  " +
                String.format("%.1f", coordinateY).replace(",", ".") + "  " +
                String.format("%.1f", coordinateZ).replace(",", ".") + "  " +
                String.format("%.3f", velocityX).replace(",", ".") + "  " +
                String.format("%.3f", velocityY).replace(",", ".") + "  " +
                String.format("%.3f", velocityZ).replace(",", ".");
    }

    @Override
    public String toString() {
        return "TrajectoryRow{" +
                "time=" + time +
                ", coordinateX=" + coordinateX +
                ", coordinateY=" + coordinateY +
                ", coordinateZ=" + coordinateZ +
                ", velocityX=" + velocityX +
                ", velocityY=" + velocityY +
                ", velocityZ=" + velocityZ +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrajectoryRow row = (TrajectoryRow) o;
        return Double.compare(time, row.time) == 0 && Double.compare(coordinateX, row.coordinateX) == 0 && Double.compare(coordinateY, row.coordinateY) == 0 && Double.compare(coordinateZ, row.coordinateZ) == 0 && Double.compare(velocityX, row.velocityX) == 0 && Double.compare(velocityY, row.velocityY) == 0 && Double.compare(velocityZ, row.velocityZ) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, coordinateX, coordinateY, coordinateZ, velocityX, velocityY, velocityZ);
    }
}
