package etu.nic.git.trajectories_swing;

import etu.nic.git.trajectories_swing.display_components.CatalogDisplay;
import etu.nic.git.trajectories_swing.display_components.FileDisplay;
import etu.nic.git.trajectories_swing.display_components.TableDisplay;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;
import etu.nic.git.trajectories_swing.tools.FileDataLoader;
import etu.nic.git.trajectories_swing.tools.TrajectoryFile;
import etu.nic.git.trajectories_swing.tools.TrajectoryFileStorage;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class ApplicationAssembler {
    private TrajectoryFileStorage fileStorage;
    private TrajectoryRowTableModel model;
    private MainFrame mainFrame;
    private CatalogDisplay catalogDisplay;
    private TableDisplay tableDisplay;
    private FileDisplay fileDisplay;

    public ApplicationAssembler() {
        model = initModel();
        fileStorage = initFileStorage();
        catalogDisplay = new CatalogDisplay(fileStorage, initCatalogActionListener());
        tableDisplay = new TableDisplay(model);
        fileDisplay = new FileDisplay(fileStorage);

        model.setTrajectoryRowList(FileDataLoader.parseToTrajectoryRowList(fileStorage.getCurrentFile().getData()));
        fileDisplay.updateDisplayedInfo();
    }

    public void updateEntireInfo() {
        model.setTrajectoryRowList(FileDataLoader.parseToTrajectoryRowList(fileStorage.getCurrentFile().getData()));
        fileDisplay.updateDisplayedInfo();
    }

    // fixme инициализация файлов пока не реализован FileChooser
    public TrajectoryFileStorage initFileStorage() {
        String filePath1 = "data/traject1.txt";
        String filePath2 = "data/traject2.txt";
        TrajectoryFileStorage fileStorage = new TrajectoryFileStorage();
        try {
            fileStorage.add(new TrajectoryFile(filePath1));
            fileStorage.add(new TrajectoryFile(filePath2));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

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

    public ActionListener initCatalogActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                fileStorage.updateCurrentFileIndexByFile(fileStorage.findFileByName(actionCommand));
                updateEntireInfo();
                model.fireTableDataChanged();
            }
        };
    }

    public void showGUI() {
        mainFrame.showMainFrame();
    }

    public void assemble() {
        mainFrame = new MainFrame(catalogDisplay.getComponent(), tableDisplay.getComponent(), fileDisplay.getComponent());
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
