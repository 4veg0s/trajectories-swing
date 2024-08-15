package etu.nic.git.trajectories_swing.tools;

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

            nextTrajectoryIndex++; // fixme
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

            nextTrajectoryIndex++; // fixme
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
     *
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
