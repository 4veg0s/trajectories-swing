package etu.nic.git.trajectories_swing.file_handling;

import etu.nic.git.trajectories_swing.exceptions.InvalidAmountOfParametersException;
import etu.nic.git.trajectories_swing.exceptions.InvalidFileFormatException;
import etu.nic.git.trajectories_swing.model.TrajectoryRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

/**
 * Класс, характеризующий файл траекторной информации
 */
public class TrajectoryFile {
    private static final Logger logger = LoggerFactory.getLogger(TrajectoryFile.class);
    public static final String TRAJECTORY_NAME_PREFIX = "Траектория ";
    private static int nextTrajectoryIndex = 1;     // индекс, подставляющийся в текстовое поле задания наименования траектории
    private String path;
    private String name;
    private String data;
    private String dataOnCreation;

    /**
     * Создает объект файла траекторной информации на основе файла из системы
     *
     * @param file файл траектории
     * @param name имя траектории (например, "Траектория 1")
     */
    public TrajectoryFile(File file, String name) throws FileNotFoundException {
        if (file.exists()) {
            this.path = file.getAbsolutePath();
            this.name = name;
            this.data = readDataFromFile();
            this.dataOnCreation = this.data;
        }
    }

    /**
     * Читает данные из этого файла
     *
     * @return
     */
    public String readDataFromFile() {
        return FileDataLoader.readDataFromFile(this.getPath()); // считать данные из этого файла и вернуть их
    }

    /**
     * Записывает данные в соответсвующий файл, если приложение изменяло их
     */
    public void writeCurrentDataToFileIfHasChanges() {
        if (this.hasChanges()) {    // если в файле были изменения
            this.writeDataToFileWithNoConditions();
        }   // если не было изменений в файле, то и записывать нечего
    }

    /**
     * Записывает данные в соответсвующий файл, если приложение изменяло их или создается новый файл (через "Сохранить как...")
     */
    public void writeCurrentDataToFileIfHasChangesOrIsNewFile(String oldFilePath) {
        if (!this.getPath().equals(oldFilePath) || this.hasChanges()) {    // если в файле были изменения или необходимо записать данные по новому пути
            this.writeDataToFileWithNoConditions();
        }   // если не было изменений в файле и путь до файла тот же, то и записывать нечего
    }

    /**
     * Запись данных в файл без проверок
     */
    private void writeDataToFileWithNoConditions() {
        FileDataLoader.writeDataToFile(this.getPath(), this.getData()); // записываем данные траектории в соответсвующий текстовый файл
        this.setDataOnCreation(this.getData()); // устанавливаем данные "до изменения" на новые (текущие)
    }

    public void setDataOnCreation(String dataOnCreation) {
        this.dataOnCreation = dataOnCreation;
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

    /**
     * Метод обрезает звездочку после имени файла
     *
     * @param rawName имя, в котором может быть звездочка
     * @return имя без звездочки
     */
    public static String stripAsteriskFromNameString(String rawName) {
        return rawName.replace("*", "");
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

    /**
     * Проверяет валидность текстовой информации, потенциально являющейся траекторией
     *
     * @param data данные, которые необходимо проверить
     * @return true, если траекторная информация валидна, false - иначе
     * @throws InvalidFileFormatException
     */
    public static boolean checkTrajectoryDataValidity(String data) throws InvalidFileFormatException {
        if (!data.trim().isEmpty()) {
            String[] lines = data.split("\n");
            StringBuilder invalidAmountOfParametersMessage = new StringBuilder();
            StringBuilder invalidTrajectoryRowMessage = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                try {
                    TrajectoryRow.isValidTrajectoryStringWithExceptions(line);  // проверка валидности строчки с выбросом исключений
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
        } else {
            throw new InvalidFileFormatException("Данный файл пуст");
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
