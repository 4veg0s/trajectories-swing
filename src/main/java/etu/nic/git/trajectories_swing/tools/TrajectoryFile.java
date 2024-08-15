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

    public TrajectoryFile(String path, String name) throws FileNotFoundException {
        File file = new File(path);
        if (file.exists()) {
            this.path = file.getAbsolutePath();
            this.name = name;  // пока имя файла в приложении не переназначили
            loadDataFromFile(path);

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
            loadDataFromFile(path);

            nextTrajectoryIndex++; // fixme
        } else {
            // fixme случаи если файла не существует
            throw new FileNotFoundException();
        }
    }

    public void loadDataFromFile(String filePath) {
        setData(FileDataLoader.loadDataFromFile(filePath));
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
