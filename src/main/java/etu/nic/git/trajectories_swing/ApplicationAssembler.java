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
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(ApplicationAssembler.class);
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
//        PropertyConfigurator.configure("./src/main/resources/log4j.properties");

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
        chartDisplay.setMarkersAsLettersOnChart(false);
    }

    private JFileChooser initFileChooser() {
        JFileChooser fileChooser = new JFileChooser(new File("./data"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileNameExtensionFilter("Text file", "txt");
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
                JButton buttonInvoker = (JButton) catalogPopupMenu.getInvoker();

                // получаем файл траектории, на котором было вызвано контекстное меню
                TrajectoryFile selectedFile = fileStorage.findFileByName(TrajectoryFile.stripAsteriskFromNameString(buttonInvoker.getActionCommand()));
                if (selectedFile.hasChanges()) {    // если у этого файла есть несохраненные изменения
                    SaveTrajectoryFileChangesDialog saveTrajectoryFileChangesDialog = new SaveTrajectoryFileChangesDialog(mainFrame);
                    int closingResult = saveTrajectoryFileChangesDialog.showWithResult(); // показываем диалоговое окно и ждем, какую кнопку нажмет пользователь
                    if (closingResult == SaveTrajectoryFileChangesDialog.EXIT_ON_SAVE) {  // если закрылось с нажатием на утвердительный ответ
                        if (model.getTableDataInString().isEmpty()) {   // если мы собрались записывать в файл пустую строку
                            logger.info("Попытка сохранения пустого файла траектории");
                            new DefaultOKDialog(
                                    mainFrame,
                                    "Сохранение файла",
                                    "Невозможно сохранить пустой файл траектории"
                            ).show();   // показываем диалоговое окно с ошибкой
                        } else {
                            TrajectoryFile currentFile = fileStorage.getCurrentFile();
                            currentFile.writeCurrentDataToFileIfHasChanges(); // записываем новые данные в файл
                            catalogDisplay.updateComponentView();  // вызываем рефреш каталога (т.к. только его это затрагивает), чтобы убрать звездочку с файла
                        }
                    } else if (closingResult == SaveTrajectoryFileChangesDialog.EXIT_ON_CANCEL) {
                        return;
                    }
                }
                fileStorage.removeFileByName(TrajectoryFile.stripAsteriskFromNameString(buttonInvoker.getActionCommand())); // удаляем файл из файлового хранилища по имени
                updateEntireInfo();     // обновляем все компоненты
                if (!fileStorage.isEmpty()) {
                    mainFrame.appendFileToFrameTitle(fileStorage.getCurrentFile().getName());   // добавление названия траектории в заголовок главного окна
                } else {
                    mainFrame.restoreTitle();   // возврат заголовка к исходному состоянию
                    if (isInvokeFileChooserWhenNoFilesOpened()) {   // если необходимо показывать JFileChooser при отсутствии файлов в каталоге
                        fileChooserOpen();  // показываем JFileChooser
                    }
                }
                model.fireTableDataChanged();   // вызываем событие модели, приводящее к обновлению информации во всех компонентах
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
                            File chosenFile = fileChooser.getSelectedFile();
                            TrajectoryFile existingFileByPath = fileStorage.findFileByPath(chosenFile.getAbsolutePath());

                            if (existingFileByPath == null) { // если такой конкретный файл еще не загружен,
                                // тогда переходим к заданию имени траектории
                                trajectoryNameSetDialog = new TrajectoryNameSetDialog(mainFrame);
                                boolean isTrajectoryNameAccepted = trajectoryNameSetDialog.showWithResult();

                                if (isTrajectoryNameAccepted) {
                                    String trajectoryName = trajectoryNameSetDialog.getTextFieldString();

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
                                                logger.info("Попытка загрузки траектории с уже существующем в приложении именем");
                                                new DefaultOKDialog(mainFrame, "Выбор файла", "Траектория с таким именем уже загружена").show();
//                                            new TrajectoryExistsDialog(mainFrame).show();
                                            }
                                        } else {
                                            fileStorage.add(newTrajectoryFile);
                                        }


                                        updateEntireInfo();
                                        mainFrame.appendFileToFrameTitle(newTrajectoryFile.getName());
                                    } catch (FileNotFoundException ex) {
                                        logger.info("Попытка загрузки несуществующего файла");
                                        new FileNotFoundDialog(mainFrame).show();
                                    } catch (InvalidFileFormatException ex) {
                                        logger.info("Попытка загрузки файла неверного формата");
                                        new InvalidFileFormatDialog(mainFrame, ex).show();
                                    }
                                }
                            } else {    // если такой файл уже загружен
                                logger.info("Попытка загрузки уже открытого под именем {} файла", existingFileByPath.getName());
                                new DefaultOKDialog(mainFrame, "Выбор файла", "Этот файл уже открыт (" + existingFileByPath.getName() + ")").show();
                            }
                        }
                        break;
                    case TopMenuBar.MENU_SAVE: // пункт меню СОХРАНИТЬ
                        if (!fileStorage.isEmpty()) {
                            if (model.getTableDataInString().isEmpty()) {
                                logger.info("Попытка сохранения пустого файла траектории");
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
     *
     * @param isNeeded true, если нужно открыть окно выбора файла при первом запуске приложения, иначе - false
     */
    public void fileChooserOnFirstOpen(boolean isNeeded) {
        if (isNeeded) {
            fileChooserOpen();
        }
        logger.debug("fileChooserOnFirstOpen: " + isNeeded);
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
        logger.info("mainFrame успешно создан");
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
     *
     * @param invokeFileChooserWhenNoFilesOpened true, если необходимо, чтобы при описанных обстоятельствах открывалось окно FileChooser'а,
     *                                           false - иначе
     */
    public void setInvokeFileChooserWhenNoFilesOpened(boolean invokeFileChooserWhenNoFilesOpened) {
        this.invokeFileChooserWhenNoFilesOpened = invokeFileChooserWhenNoFilesOpened;
    }

}
