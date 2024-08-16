package etu.nic.git.trajectories_swing.tools;

import etu.nic.git.trajectories_swing.exceptions.InvalidAmountOfParametersException;
import etu.nic.git.trajectories_swing.exceptions.InvalidFileFormatException;
import etu.nic.git.trajectories_swing.exceptions.InvalidTrajectoryRowException;
import etu.nic.git.trajectories_swing.model.TrajectoryRow;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class TrajectoryFile {
    public static final String TRAJECTORY_NAME_PREFIX = "Траектория ";
    private static int nextTrajectoryIndex = 1;
    private String path;
    private String name;
    private String data;
    private final String dataOnCreation;

    public TrajectoryFile(String path, String name) throws FileNotFoundException {
        File file = new File(path);
        if (file.exists()) {
            this.path = file.getAbsolutePath();
            this.name = name;  // пока имя файла в приложении не переназначили
            this.data = getDataFromFile(path);
            this.dataOnCreation = this.data;
        } else {
            // fixme случаи если файла не существует
            throw new FileNotFoundException();
        }
    }

    public TrajectoryFile(File file, String name) throws FileNotFoundException {
        if (file.exists()) {
            this.path = file.getAbsolutePath();
            this.name = name;
            this.data = getDataFromFile(path);
            this.dataOnCreation = this.data;
        } else {
            // fixme случаи если файла не существует
            throw new FileNotFoundException();
        }
    }

    public String getDataFromFile(String filePath) {
        return FileDataLoader.getDataFromFile(filePath);
    }

    public String getDataOnCreation() {
        return dataOnCreation;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    /**
     * @return имя траектории со звездочкой, если данные были изменены, иначе - без звездочки
     */
    public String getNameWithAsterisk() {
        return name + ((dataOnCreation.equals(data)) ? "" : "*");
    }

    public static String stripAsteriskFromNameString(String rawName) {
        return rawName.replace("*", "");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static int getNextTrajectoryIndex() {
        return nextTrajectoryIndex;
    }

    public boolean hasChanges() {
        return !dataOnCreation.equals(data);
    }

    public static boolean checkTrajectoryDataValidity(String data) throws InvalidFileFormatException {
        String[] lines = data.split("\n");
        StringBuilder invalidAmountOfParametersMessage = new StringBuilder();
        StringBuilder invalidTrajectoryRowMessage = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            try {
                TrajectoryRow.isValidTrajectoryStringWithExceptions(line);
            } catch (InvalidAmountOfParametersException e) {
                if (invalidAmountOfParametersMessage.toString().isEmpty()) {
                    invalidAmountOfParametersMessage.append("\tСтрока №");
                    invalidAmountOfParametersMessage.append(i + 1);
                } else {
                    invalidAmountOfParametersMessage.append(",\n\tСтрока №");
                    invalidAmountOfParametersMessage.append(i + 1);
                }
            } catch (NumberFormatException e) {
                if (invalidTrajectoryRowMessage.toString().isEmpty()) {
                    invalidTrajectoryRowMessage.append("\tСтрока №");
                    invalidTrajectoryRowMessage.append(i + 1);
                    invalidTrajectoryRowMessage.append(" (параметр №");
                    invalidTrajectoryRowMessage.append(TrajectoryRow.indexOfInvalidParameter(line) + 1);
                    invalidTrajectoryRowMessage.append(")");

                } else {
                    invalidTrajectoryRowMessage.append(",\n\tСтрока №");
                    invalidTrajectoryRowMessage.append(i + 1);
                    invalidTrajectoryRowMessage.append(" (параметр №");
                    invalidTrajectoryRowMessage.append(TrajectoryRow.indexOfInvalidParameter(line) + 1);
                    invalidTrajectoryRowMessage.append(")");
                }
            }
        }
        if (!invalidAmountOfParametersMessage.toString().isEmpty() || !invalidTrajectoryRowMessage.toString().isEmpty()) {
            if (!invalidAmountOfParametersMessage.toString().isEmpty()) {
                invalidAmountOfParametersMessage.insert(0, "Неверное количество параметров:\n");
            }
            if (!invalidTrajectoryRowMessage.toString().isEmpty()) {
                invalidTrajectoryRowMessage.insert(0, "Неверный формат параметров:\n");
            }
            if (!invalidAmountOfParametersMessage.toString().isEmpty() && !invalidTrajectoryRowMessage.toString().isEmpty()) {
                invalidAmountOfParametersMessage.append("\n\n");
            }
            invalidAmountOfParametersMessage.append(invalidTrajectoryRowMessage);
            throw new InvalidFileFormatException(invalidAmountOfParametersMessage.toString());
        } else {
            return true;
        }

    }

    public static void incrementTrajectoryIndex() {
        nextTrajectoryIndex++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrajectoryFile that = (TrajectoryFile) o;
        return Objects.equals(path, that.path) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, name);
    }
}
