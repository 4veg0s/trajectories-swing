package etu.nic.git.trajectories_swing.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class TrajectoryFile {
    private String path;
    private String name;
    private String data;

    public TrajectoryFile(String path) throws FileNotFoundException {
        File file = new File(path);
        if (file.exists()) {
            this.path = file.getAbsolutePath();
            this.name = file.getName();  // пока имя файла в приложении не переназначили
            loadDataFromFile(path);
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
