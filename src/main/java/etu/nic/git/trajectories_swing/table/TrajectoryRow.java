package etu.nic.git.trajectories_swing.table;

// Время, x-координата, y-координата, z-координата, x-составляющая скорости, yсоставляющая скорости, z-составляющая скорости
// "T, с", "X, м", "Y, м", "Z, м", "Vx, м/с", "Vy, м/с", "Vz, м/с"

import java.util.regex.Pattern;

public class TrajectoryRow {
    private static final int AMOUNT_OF_PARAMETERS = 7;
    private double time;
    private double CoordinateX;
    private double CoordinateY;
    private double CoordinateZ;
    private double VelocityX;
    private double VelocityY;
    private double VelocityZ;

    public TrajectoryRow(double time, double CoordinateX, double CoordinateY, double CoordinateZ, double VelocityX, double VelocityY, double VelocityZ) {
        this.time = time;
        this.CoordinateX = CoordinateX;
        this.CoordinateY = CoordinateY;
        this.CoordinateZ = CoordinateZ;
        this.VelocityX = VelocityX;
        this.VelocityY = VelocityY;
        this.VelocityZ = VelocityZ;
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
        return CoordinateX;
    }

    public void setCoordinateX(double coordinateX) {
        this.CoordinateX = coordinateX;
    }

    public double getCoordinateY() {
        return CoordinateY;
    }

    public void setCoordinateY(double coordinateY) {
        this.CoordinateY = coordinateY;
    }

    public double getCoordinateZ() {
        return CoordinateZ;
    }

    public void setCoordinateZ(double coordinateZ) {
        this.CoordinateZ = coordinateZ;
    }

    public double getVelocityX() {
        return VelocityX;
    }

    public void setVelocityX(double velocityX) {
        this.VelocityX = velocityX;
    }

    public double getVelocityY() {
        return VelocityY;
    }

    public void setVelocityY(double velocityY) {
        this.VelocityY = velocityY;
    }

    public double getVelocityZ() {
        return VelocityZ;
    }

    public void setVelocityZ(double velocityZ) {
        this.VelocityZ = velocityZ;
    }
}
