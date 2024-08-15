package etu.nic.git.trajectories_swing;

import etu.nic.git.trajectories_swing.dialogs.FileExceptionDialog;
import etu.nic.git.trajectories_swing.dialogs.TrajectoryNameSetDialog;
import etu.nic.git.trajectories_swing.display_components.CatalogDisplay;
import etu.nic.git.trajectories_swing.display_components.FileDisplay;
import etu.nic.git.trajectories_swing.display_components.TableDisplay;
import etu.nic.git.trajectories_swing.menus.TopMenuBar;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;
import etu.nic.git.trajectories_swing.tools.FileDataLoader;
import etu.nic.git.trajectories_swing.tools.TrajectoryFile;
import etu.nic.git.trajectories_swing.tools.TrajectoryFileStorage;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

public class ApplicationAssembler {
    private TrajectoryFileStorage fileStorage;
    private TrajectoryRowTableModel model;
    private JFileChooser fileChooser;
    private MainFrame mainFrame;
    private TopMenuBar menuBar;
    private CatalogDisplay catalogDisplay;
    private TableDisplay tableDisplay;
    private FileDisplay fileDisplay;

    public ApplicationAssembler() {
        model = initModel();

        fileStorage = new TrajectoryFileStorage();
//        fileStorage.setCurrentFileIndex(0);  // fixme на время без FileChooser

        fileChooser = new JFileChooser(new File("./data"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        catalogDisplay = new CatalogDisplay(fileStorage, initCatalogActionListener());
        tableDisplay = new TableDisplay(model);
        fileDisplay = new FileDisplay(fileStorage);

        menuBar = new TopMenuBar(initMenuActionListener());

        if (!fileStorage.isEmpty()) {
            model.setTrajectoryRowList(FileDataLoader.parseToTrajectoryRowList(fileStorage.getCurrentFile().getData()));
            fileDisplay.updateDisplayedInfo();
            model.fireTableDataChanged();
        }
    }


    public void updateEntireInfo() {
        model.setTrajectoryRowList(FileDataLoader.parseToTrajectoryRowList(fileStorage.getCurrentFile().getData()));
        tableDisplay.revalidateAndRepaint();
        fileDisplay.updateDisplayedInfo();
        catalogDisplay.refreshFileList();
    }

//    // fixme инициализация файлов пока не реализован FileChooser
//    public TrajectoryFileStorage initFileStorage() {
//        String filePath1 = "data/traject1.txt";
////        String filePath2 = "data/traject2.txt";
//        TrajectoryFileStorage fileStorage = new TrajectoryFileStorage();
//        try {
//            fileStorage.add(new TrajectoryFile(filePath1));
////            fileStorage.add(new TrajectoryFile(filePath2));
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//
//        return fileStorage;
//    }

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
                updateEntireInfo();
            }
        });

        return model;
    }

    public ActionListener initCatalogActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                TrajectoryFile currentFile = fileStorage.findFileByName(actionCommand);
                fileStorage.updateCurrentFileIndexByFile(currentFile);
                updateEntireInfo();
                mainFrame.appendFileToFrameTitle(currentFile.getName());
                model.fireTableDataChanged();
            }
        };
    }

    public ActionListener initMenuActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                TrajectoryNameSetDialog trajectoryNameSetDialog;

                switch (actionCommand) {
                    case TopMenuBar.MENU_OPEN:  // пункт меню ОТКРЫТЬ
                        int returnVal = fileChooser.showOpenDialog(mainFrame);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {

                            trajectoryNameSetDialog = new TrajectoryNameSetDialog(mainFrame);
                            boolean isTrajectoryNameAccepted = trajectoryNameSetDialog.showWithResult();

                            if (isTrajectoryNameAccepted) {
                                String trajectoryName = trajectoryNameSetDialog.getTextFieldString();

                                File chosenFile = fileChooser.getSelectedFile();
                                try {
                                    TrajectoryFile trajectoryFile = new TrajectoryFile(chosenFile, trajectoryName);
//                                    trajectoryNameSetDialog.hide();
                                    fileStorage.add(trajectoryFile);
                                    updateEntireInfo();
                                    mainFrame.appendFileToFrameTitle(trajectoryFile.getName());
                                } catch (FileNotFoundException ex) {
                                    throw new RuntimeException(ex);
                                } catch (FileAlreadyExistsException ex) {
                                    new FileExceptionDialog(trajectoryNameSetDialog.getDialog(), ex).show();
                                    this.actionPerformed(e);
                                }
                            }
                        }
                        break;
                }
            }
        };
    }

    public void showGUI() {
        mainFrame.showMainFrame();
    }

    public void assemble() {
        mainFrame = new MainFrame(menuBar.getMenuBar(), catalogDisplay.getComponent(), tableDisplay.getComponent(), fileDisplay.getComponent());
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
