package etu.nic.git.trajectories_swing.tools;

import java.io.File;
import java.nio.file.Paths;

public class TrajectoryFile {
    private String filePath;
    private String fileName;
    private String fileData;

    public TrajectoryFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            this.filePath = file.getAbsolutePath();
            this.fileName = file.getName();  // пока имя файла в приложении не переназначили
            loadDataFromFile(filePath);
        } else {
            // fixme случаи если файла не существует
        }
    }

    public void loadDataFromFile(String filePath) {
        setFileData(FileDataLoader.loadDataFromFile(filePath));
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }
}
