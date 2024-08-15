package etu.nic.git.trajectories_swing.tools;


import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

/**
 * Хранилище файлов с траекториями
 * содержит индекс файла, открытого на данный момоент
 *
 */
public class TrajectoryFileStorage {
    private int currentFileIndex;
    private List<TrajectoryFile> fileList;

    public TrajectoryFileStorage() {
        fileList = new ArrayList<>();
    }

    public void add(TrajectoryFile file) throws FileAlreadyExistsException {
        if (findFileByName(file.getName()) == null) {
            fileList.add(file);
            currentFileIndex = fileList.size() - 1; // при добавлении нового файла будем переключаться на него
        } else {
            throw new FileAlreadyExistsException("Траектория с таким именем уже загружена");
        }

    }

    public void updateFileDataByIndex(int index, String fileData) {
        findFileByIndex(index).setData(fileData);
    }

    public TrajectoryFile findFileByIndex(int index) {
        return (fileList.size() - 1 >= index) ? fileList.get(index) : null;
    }

    public TrajectoryFile findFileByName(String fileName) {
        for (TrajectoryFile file : fileList) {
            if (file.getName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }

    public void updateCurrentFileIndexByFile(TrajectoryFile file) {
        setCurrentFileIndex(fileList.indexOf(file));
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
