package etu.nic.git.trajectories_swing;

import etu.nic.git.trajectories_swing.dialogs.*;
import etu.nic.git.trajectories_swing.display_components.*;
import etu.nic.git.trajectories_swing.exceptions.InvalidFileFormatException;
import etu.nic.git.trajectories_swing.menus.CatalogPopupMenu;
import etu.nic.git.trajectories_swing.menus.TableDisplayPopupMenu;
import etu.nic.git.trajectories_swing.menus.TablePopupMenu;
import etu.nic.git.trajectories_swing.menus.TopMenuBar;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;
import etu.nic.git.trajectories_swing.file_handling.FileDataLoader;
import etu.nic.git.trajectories_swing.file_handling.TrajectoryFile;
import etu.nic.git.trajectories_swing.file_handling.TrajectoryFileStorage;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationAssembler {
    private TrajectoryFileStorage fileStorage;
    private final TrajectoryRowTableModel model;
    private final JFileChooser fileChooser;
    private MainFrame mainFrame;
    private final TopMenuBar topMenuBar;
    private final CatalogDisplay catalogDisplay;
    private final CatalogPopupMenu catalogPopupMenu;
    private final TablePopupMenu tablePopupMenu;
    private final TableDisplayPopupMenu tableDisplayPopupMenu;
    private TableDisplay tableDisplay;
    private FileDisplay fileDisplay;
    private final ChartDisplay chartDisplay;
    private boolean invokeFileChooserWhenNoFilesOpened = true;
    private final List<AbstractDisplay> displayList;

    public ApplicationAssembler() {
        model = initModel();

        fileStorage = new TrajectoryFileStorage();

        fileChooser = initFileChooser();

        displayList = new ArrayList<>();

        catalogPopupMenu = new CatalogPopupMenu(initCatalogPopupActionListener());
        catalogDisplay = new CatalogDisplay(fileStorage, catalogPopupMenu.getPopupMenu(), initCatalogSelectActionListener());
        displayList.add(catalogDisplay);

        tableDisplayPopupMenu = new TableDisplayPopupMenu(fileStorage, model);
        tableDisplay = new TableDisplay(model, tableDisplayPopupMenu);
        displayList.add(tableDisplay);
        tablePopupMenu = new TablePopupMenu(tableDisplay.getTable());
        tableDisplay.setTablePopupMenu(tablePopupMenu.getPopupMenu());

        fileDisplay = new FileDisplay(fileStorage);
        displayList.add(fileDisplay);

        chartDisplay = new ChartDisplay(model, fileStorage);
        displayList.add(chartDisplay);

        topMenuBar = new TopMenuBar(initMenuActionListener());

        // смотреть JavaDoc методов
        setInvokeFileChooserWhenNoFilesOpened(true);
        chartDisplay.setMarkersAsLettersOnChart(true);
    }

    private JFileChooser initFileChooser() {
        JFileChooser fileChooser = new JFileChooser(new File("./data"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileNameExtensionFilter("Text file","txt");
        fileChooser.setFileFilter(filter);
        return fileChooser;
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
                int returnVal;
                switch (actionCommand) {
                    case TopMenuBar.MENU_OPEN:  // пункт меню ОТКРЫТЬ
                        returnVal = fileChooser.showOpenDialog(mainFrame);
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
                                    new FileNotFoundDialog(mainFrame).show();
                                } catch (InvalidFileFormatException ex) {
                                    new InvalidFileFormatDialog(mainFrame, ex).show();
                                }
                            }
                        }
                        break;
                    case TopMenuBar.MENU_SAVE: // пункт меню СОХРАНИТЬ
                        if (!fileStorage.isEmpty()) {

                            // TODO: обработать случай, когда открыто несколько траекторий с одного файла, и происходит сохранение в одном из них
                            if (model.getTableDataInString().isEmpty()) {
                                new DefaultOKDialog(
                                        mainFrame,
                                        "Сохранение файла",
                                        "Невозможно сохранить пустой файл траектории"
                                ).show();
                            } else {
                                TrajectoryFile currentFile = fileStorage.getCurrentFile();
                                currentFile.writeCurrentDataToFileIfHasChanges(); // записываем новые данные в файл
                                catalogDisplay.updateComponentView();  // вызываем рефреш каталога (т.к. только его это затрагивает), чтобы убрать звездочку с файла
                            }
                        }
                        break;
                    case TopMenuBar.MENU_SAVE_AS: // пункт меню СОХРАНИТЬ КАК...
                        if (!fileStorage.isEmpty()) {
                            returnVal = fileChooser.showSaveDialog(mainFrame);
                            if (returnVal == JFileChooser.APPROVE_OPTION) {
                                File newFile = fileChooser.getSelectedFile();   // получаем выбранный файл
                                TrajectoryFile currentFile = fileStorage.getCurrentFile();  // получаем текущий открытый в каталоге файл
                                String oldPath = currentFile.getPath(); // считываем предыдущий путь до файла для последующего сравнения
                                currentFile.setPath(newFile.getAbsolutePath()); // устанавливаем новый путь до файла траектории (может быть и идентичным, но логику не меняет)
                                currentFile.writeCurrentDataToFileIfHasChangesOrIsNewFile(oldPath); // записываем данные прошлого файла в новый файл
                                catalogDisplay.updateComponentView();  // вызываем рефреш каталога, чтобы убрать звездочку с файла, если она есть
                                fileDisplay.updateComponentView();  // обновляем файловый дисплей, чтобы добиться изменения пути до файла в соответствующем лейбле
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
