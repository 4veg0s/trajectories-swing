package etu.nic.git.trajectories_swing;

import etu.nic.git.trajectories_swing.dialogs.InvalidFileFormatDialog;
import etu.nic.git.trajectories_swing.dialogs.ReplaceTrajectoryFileDialog;
import etu.nic.git.trajectories_swing.dialogs.TrajectoryExistsDialog;
import etu.nic.git.trajectories_swing.dialogs.TrajectoryNameSetDialog;
import etu.nic.git.trajectories_swing.display_components.*;
import etu.nic.git.trajectories_swing.exceptions.InvalidFileFormatException;
import etu.nic.git.trajectories_swing.menus.CatalogPopupMenu;
import etu.nic.git.trajectories_swing.menus.TopMenuBar;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;
import etu.nic.git.trajectories_swing.file_handling.FileDataLoader;
import etu.nic.git.trajectories_swing.file_handling.TrajectoryFile;
import etu.nic.git.trajectories_swing.file_handling.TrajectoryFileStorage;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationAssembler {
    private TrajectoryFileStorage fileStorage;
    private TrajectoryRowTableModel model;
    private JFileChooser fileChooser;
    private MainFrame mainFrame;
    private TopMenuBar topMenuBar;
    private CatalogDisplay catalogDisplay;
    private CatalogPopupMenu catalogPopupMenu;
    private TableDisplay tableDisplay;
    private FileDisplay fileDisplay;
    private ChartDisplay chartDisplay;
    private boolean invokeFileChooserWhenNoFilesOpened = true;
    private List<AbstractDisplay> displayList;

    public ApplicationAssembler() {
        model = initModel();

        fileStorage = new TrajectoryFileStorage();

        fileChooser = new JFileChooser(new File("./data"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        catalogPopupMenu = new CatalogPopupMenu(initCatalogPopupActionListener());

        displayList = new ArrayList<>();

        catalogDisplay = new CatalogDisplay(fileStorage, catalogPopupMenu.getPopupMenu(), initCatalogSelectActionListener());
        displayList.add(catalogDisplay);

        tableDisplay = new TableDisplay(model);
        displayList.add(tableDisplay);

        fileDisplay = new FileDisplay(fileStorage);
        displayList.add(fileDisplay);

        chartDisplay = new ChartDisplay(model, fileStorage);
        displayList.add(chartDisplay);

        topMenuBar = new TopMenuBar(initMenuActionListener());

        // смотреть JavaDoc метода
        setInvokeFileChooserWhenNoFilesOpened(true);
    }


    public void updateEntireInfo() {
        if (!fileStorage.isEmpty()) {
            model.setTrajectoryRowList(FileDataLoader.parseToTrajectoryRowList(fileStorage.getCurrentFile().getData()));
            for (AbstractDisplay display : displayList) {
                display.updateComponentView();
            }
        } else {
            for (AbstractDisplay display : displayList) {
                display.restoreDefaultState();
            }
            mainFrame.restoreTitle();
        }
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
                if (!fileStorage.isEmpty()) {
                    fileStorage.updateFileDataByIndex(
                            fileStorage.getCurrentFileIndex(),
                            model.getTableDataInString()
                    );    // при изменении данных модели подгрузятся
                    // изменения и в объект, соответствующий этому файлу
                }
                updateEntireInfo();
            }
        });

        return model;
    }

    public ActionListener initCatalogSelectActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                TrajectoryFile currentFile = fileStorage.findFileByName(
                        TrajectoryFile.stripAsteriskFromNameString(actionCommand)
                );
                fileStorage.updateCurrentFileIndexByFile(currentFile);
                updateEntireInfo();
                mainFrame.appendFileToFrameTitle(currentFile.getName());
                model.fireTableDataChanged();
            }
        };
    }
    public ActionListener initCatalogPopupActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                JButton buttonInvoker = (JButton) catalogPopupMenu.getInvoker();

                fileStorage.removeFileByName(TrajectoryFile.stripAsteriskFromNameString(buttonInvoker.getActionCommand()));
                updateEntireInfo();
                if (!fileStorage.isEmpty()) {
                    mainFrame.appendFileToFrameTitle(fileStorage.findFileByIndex(fileStorage.getCurrentFileIndex()).getName());
                } else {
                    mainFrame.restoreTitle();
                    if (isInvokeFileChooserWhenNoFilesOpened()) {
                        fileChooserOpen();
                    }
                }
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
                                    TrajectoryFile newTrajectoryFile = new TrajectoryFile(chosenFile, trajectoryName);

                                    TrajectoryFile.checkTrajectoryDataValidity(newTrajectoryFile.getData());

                                    TrajectoryFile existingFile = fileStorage.findFileByName(trajectoryName);
                                    if (existingFile != null) {   // если такая траектория уже загружена
                                        if (existingFile.hasChanges()) {
                                            ReplaceTrajectoryFileDialog replaceTrajectoryFileDialog = new ReplaceTrajectoryFileDialog(mainFrame);
                                            boolean isClosedWithConfirm = replaceTrajectoryFileDialog.showWithResult();
                                            if (isClosedWithConfirm) {
                                                fileStorage.replaceFileByName(trajectoryName, newTrajectoryFile);
                                            } else {
                                                this.actionPerformed(e);
                                            }
                                        } else {
                                            new TrajectoryExistsDialog(mainFrame).show();
                                        }
                                    } else {
                                        fileStorage.add(newTrajectoryFile);
                                    }

                                    updateEntireInfo();
                                    mainFrame.appendFileToFrameTitle(newTrajectoryFile.getName());
                                } catch (FileNotFoundException ex) {
                                    throw new RuntimeException(ex);
                                } catch (InvalidFileFormatException ex) {
                                    new InvalidFileFormatDialog(mainFrame, ex).show();
                                }
                            }
                        }
                        break;
                }
            }
        };
    }

    /**
     * Метод имитирует клик по пункту файлового меню "Открыть" с соответствующими последствиями
     * @param isNeeded true, если нужно открыть окно выбора файла при первом запуске приложения, иначе - false
     */
    public void fileChooserOnFirstOpen(boolean isNeeded) {
        if (isNeeded) {
            fileChooserOpen();
        }
    }

    public void fileChooserOpen() {
        topMenuBar.fireOpenFileMenuItemClick();
    }

    public void showGUI() {
        mainFrame.showMainFrame();
        fileChooserOnFirstOpen(true);
    }

    public void assemble() {
        mainFrame = new MainFrame(topMenuBar, catalogDisplay, tableDisplay, fileDisplay, chartDisplay);
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

    public boolean isInvokeFileChooserWhenNoFilesOpened() {
        return invokeFileChooserWhenNoFilesOpened;
    }

    /**
     * Устанавливает параметр, отвечающий за открытие окна выбора нового файла при отсутсивии открытых файлов
     * @param invokeFileChooserWhenNoFilesOpened true, если необходимо, чтобы при описанных обстоятельствах открывалось окно FileChooser'а,
     *                                          false - иначе
     */
    public void setInvokeFileChooserWhenNoFilesOpened(boolean invokeFileChooserWhenNoFilesOpened) {
        this.invokeFileChooserWhenNoFilesOpened = invokeFileChooserWhenNoFilesOpened;
    }
}
