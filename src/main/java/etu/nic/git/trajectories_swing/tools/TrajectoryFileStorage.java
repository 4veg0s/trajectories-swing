package etu.nic.git.trajectories_swing.tools;


import java.util.ArrayList;
import java.util.List;

/**
 * Хранилище файлов с траекториями
 * содержит индекс файла, открытого на данный момоент
 */
public class TrajectoryFileStorage {
    private int currentFileIndex;
    private List<TrajectoryFile> fileList;

    public TrajectoryFileStorage() {
        fileList = new ArrayList<>();
    }

    public void add(TrajectoryFile file) {
        fileList.add(file);
        currentFileIndex = fileList.size() - 1; // при добавлении нового файла будем переключаться на него
    }

    public void updateFileDataByIndex(int index, String fileData) {
        findFileByIndex(index).setFileData(fileData);
    }

    public TrajectoryFile findFileByIndex(int index) {
        return (fileList.size() - 1 >= index) ? fileList.get(index) : null;
    }

    public int getCurrentFileIndex() {
        return currentFileIndex;
    }

    public boolean isEmpty() {
        return fileList.isEmpty();
    }

    public TrajectoryFile getCurrentFile() {
        return findFileByIndex(currentFileIndex);
    }

    public void setCurrentFileIndex(int currentFileIndex) {
        this.currentFileIndex = currentFileIndex;
    }

    public List<TrajectoryFile> getFileList() {
        return fileList;
    }

    public void setFileList(List<TrajectoryFile> fileList) {
        this.fileList = fileList;
    }
}
