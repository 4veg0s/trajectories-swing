package etu.nic.git.trajectories_swing.tools;

// Время, x-координата, y-координата, z-координата, x-составляющая скорости, yсоставляющая скорости, z-составляющая скорости

public class TrajectoryDataRow {
    private static final int AMOUNT_OF_PARAMETERS = 7;
    private double time;
    private double xCoordinate;
    private double yCoordinate;
    private double zCoordinate;
    private double xVelocity;
    private double yVelocity;
    private double zVelocity;

    public TrajectoryDataRow(double time, double xCoordinate, double yCoordinate, double zCoordinate, double xVelocity, double yVelocity, double zVelocity) {
        this.time = time;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.zCoordinate = zCoordinate;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.zVelocity = zVelocity;
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
            try {
                Double.parseDouble(column);
            } catch (NumberFormatException e) {
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
                try {
                    Double.parseDouble(split[i]);
                } catch (NumberFormatException e) {
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

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public double getzCoordinate() {
        return zCoordinate;
    }

    public void setzCoordinate(double zCoordinate) {
        this.zCoordinate = zCoordinate;
    }

    public double getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public double getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public double getzVelocity() {
        return zVelocity;
    }

    public void setzVelocity(double zVelocity) {
        this.zVelocity = zVelocity;
    }
}
