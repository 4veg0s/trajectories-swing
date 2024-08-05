package etu.nic.git.trajectories_swing.model;

// Время, x-координата, y-координата, z-координата, x-составляющая скорости, yсоставляющая скорости, z-составляющая скорости
// "T, с", "X, м", "Y, м", "Z, м", "Vx, м/с", "Vy, м/с", "Vz, м/с"

import java.util.regex.Pattern;

public class TrajectoryRow {
    private static final int AMOUNT_OF_PARAMETERS = 7;
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
     * Метод возвращает -1, если все параметры входной строки валидны, иначе -- индекс первого невалидного параметра
     * (FIXME) Выбрасывает исключение, если количество параметров в строке неверное
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
            throw new RuntimeException("Как сюда попало неверное число параметров?");   // FIXME
        }
    }

    private static boolean isValidAmountOfParametersInTrajectoryString(String rawString) {
        String[] columns = rawString.split("\\s+");
        return columns.length == AMOUNT_OF_PARAMETERS;
    }

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

    public String toFileString() {  // "toString", но в том же виде, как и строка из исходного файла
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
}
