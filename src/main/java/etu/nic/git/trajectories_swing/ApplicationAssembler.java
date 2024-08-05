package etu.nic.git.trajectories_swing;

import etu.nic.git.trajectories_swing.display_components.FileDisplay;
import etu.nic.git.trajectories_swing.display_components.TableDisplay;
import etu.nic.git.trajectories_swing.model.TrajectoryRow;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;
import etu.nic.git.trajectories_swing.tools.FileDataLoader;
import etu.nic.git.trajectories_swing.tools.TrajectoryFile;
import etu.nic.git.trajectories_swing.tools.TrajectoryFileStorage;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class ApplicationAssembler {
    private TrajectoryFileStorage fileStorage;
    private TrajectoryRowTableModel model;
    private MainFrame mainFrame;
    private TableDisplay tableDisplay;
    private FileDisplay fileDisplay;

    public ApplicationAssembler() {
        model = initModel();
        fileStorage = initFileStorage();
        tableDisplay = new TableDisplay(model);
        fileDisplay = new FileDisplay(fileStorage);

        model.setTrajectoryRowList(FileDataLoader.parseToTrajectoryRowList(fileStorage.getCurrentFile().getFileData()));
        fileDisplay.updateDisplayedInfo();
    }

    // fixme инициализация файлов пока не реализован FileChooser
    public TrajectoryFileStorage initFileStorage() {
        String filePath = "data/traject1.txt";
        TrajectoryFileStorage fileStorage = new TrajectoryFileStorage();
        fileStorage.add(new TrajectoryFile(filePath));

        return fileStorage;
    }

    public TrajectoryRowTableModel initModel() {
        TrajectoryRowTableModel model = new TrajectoryRowTableModel();
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                TrajectoryRowTableModel model = (TrajectoryRowTableModel) e.getSource();
                model.sortByTime(); // если было изменение значения ячейки в таблице,
                // то произойдет сортировка по времени для данных модели
                fileStorage.updateFileDataByIndex(
                        fileStorage.getCurrentFileIndex(),
                        model.getTableDataInString()
                );    // при изменении данных модели подгрузятся
                      // изменения и в объект, соответствующий этому файлу
                fileDisplay.updateDisplayedInfo();
            }
        });

        return model;
    }

    public void showGUI() {
        mainFrame.showMainFrame();
    }

    public void assemble() {
        mainFrame = new MainFrame(tableDisplay.getComponent(), fileDisplay.getComponent());
    }

    public TrajectoryFileStorage getFileStorage() {
        return fileStorage;
    }

    public void setFileStorage(TrajectoryFileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public TableDisplay getTableDisplay() {
        return tableDisplay;
    }

    public void setTableDisplay(TableDisplay tableDisplay) {
        this.tableDisplay = tableDisplay;
    }

    public FileDisplay getFileDisplay() {
        return fileDisplay;
    }

    public void setFileDisplay(FileDisplay fileDisplay) {
        this.fileDisplay = fileDisplay;
    }
}
