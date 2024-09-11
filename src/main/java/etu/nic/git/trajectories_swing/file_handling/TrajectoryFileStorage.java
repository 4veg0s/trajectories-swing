package etu.nic.git.trajectories_swing.file_handling;


import java.util.ArrayList;
import java.util.List;

/**
 * Хранилище файлов с траекториями.
 * Содержит индекс файла, открытого на данный момоент
 */
public class TrajectoryFileStorage {
    private int currentFileIndex;
    private List<TrajectoryFile> fileList;

    public TrajectoryFileStorage() {
        fileList = new ArrayList<>();
    }


    /**
     * Добавляет файл траекторной информации в хранилище
     * @param file файл, который необходимо добавить
     */
    public void add(TrajectoryFile file) {
        fileList.add(file);
        currentFileIndex = fileList.size() - 1; // при добавлении нового файла будем переключаться на него
        TrajectoryFile.incrementTrajectoryIndex();  // увеличиваем приписываемый к имени траектории индекс, если добавили файл
    }

    /**
     * Заменяет имеющийся в хранилище файл по имени
     * @param name имя существующего файла
     * @param newFile новый файл, который заменит предыдущий с таким именем
     */
    public void replaceFileByName(String name, TrajectoryFile newFile) {
        TrajectoryFile file = findFileByName(name);
        int indexOfFileToReplace = fileList.indexOf(file);
        fileList.remove(indexOfFileToReplace);
        fileList.add(indexOfFileToReplace, newFile);
    }

    /**
     * Удаляет файл из хранилища по имени
     * @param name имя файла, который необходимо удалить
     */
    public void removeFileByName(String name) {
        TrajectoryFile file = findFileByName(name);
        int indexOfFileToReplace = fileList.indexOf(file);
        fileList.remove(indexOfFileToReplace);

        // обработка крайнего случая с индексом открытого файла
        if (fileList.isEmpty()) {
            currentFileIndex = 0;
        } else if (currentFileIndex != 0) {
            currentFileIndex--;
        }
    }

    /**
     * Обновляет данные объекта {@link TrajectoryFile} по индексу в хранилище
     * @param index индекс файла в хранилище
     * @param fileData новые данные файла
     */
    public void updateFileDataByIndex(int index, String fileData) {
        findFileByIndex(index).setData(fileData);
    }

    /**
     * Находит файл по индексу в хранилище
     * @param index индекс файла в хранилище
     * @return файл траекторной информации ({@link TrajectoryFile}), если в хранилище есть файл с таким индексом, null - иначе
     */
    public TrajectoryFile findFileByIndex(int index) {
        return (fileList.size() - 1 >= index) ? fileList.get(index) : null;
    }

    /**
     * Находит файл по имени, заданному пользователем, в хранилище
     * @param fileName имя файла в хранилище
     * @return файл траекторной информации ({@link TrajectoryFile}), если в хранилище есть файл с таким именем, null - иначе
     */
    public TrajectoryFile findFileByName(String fileName) {
        for (TrajectoryFile file : fileList) {
            if (file.getName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }

    /**
     * Находит файл в хранилище по пути до него в системе
     * @param filePath путь файла в системе
     * @return файл траекторной информации ({@link TrajectoryFile}), если в хранилище есть файл с таким путем, null - иначе
     */
    public TrajectoryFile findFileByPath(String filePath) {
        for (TrajectoryFile file : fileList) {
            if (file.getPath().equals(filePath)) {
                return file;
            }
        }
        return null;
    }

    /**
     * Устанавливает индекс текущего файла на значение, соответствующее переданному файлу из хранилища
     * @param file файл из хранилища
     */
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
}
