package etu.nic.git.trajectories_swing;


import etu.nic.git.trajectories_swing.dialog.DefaultOKDialog;
import etu.nic.git.trajectories_swing.dialog.FileNotFoundDialog;
import etu.nic.git.trajectories_swing.dialog.InvalidFileFormatDialog;
import etu.nic.git.trajectories_swing.dialog.ReplaceTrajectoryFileDialog;
import etu.nic.git.trajectories_swing.dialog.SaveTrajectoryFileChangesDialog;
import etu.nic.git.trajectories_swing.dialog.TrajectoryNameSetDialog;
import etu.nic.git.trajectories_swing.display.AbstractDisplay;
import etu.nic.git.trajectories_swing.display.CatalogDisplay;
import etu.nic.git.trajectories_swing.display.ChartDisplay;
import etu.nic.git.trajectories_swing.display.FileDisplay;
import etu.nic.git.trajectories_swing.display.TableDisplay;
import etu.nic.git.trajectories_swing.exception.InvalidFileFormatException;
import etu.nic.git.trajectories_swing.menu.CatalogPopupMenu;
import etu.nic.git.trajectories_swing.menu.TableDisplayPopupMenu;
import etu.nic.git.trajectories_swing.menu.TablePopupMenu;
import etu.nic.git.trajectories_swing.menu.TopMenuBar;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;
import etu.nic.git.trajectories_swing.file.FileDataLoader;
import etu.nic.git.trajectories_swing.file.TrajectoryFile;
import etu.nic.git.trajectories_swing.file.TrajectoryFileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JButton;
import javax.swing.JFileChooser;
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

/**
 * Класс-сборщик приложения, создающий и внедряющий все основные зависимости
 */
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

    /**
     * Инициализация объекта {@link JFileChooser}
     *
     * @return {@link JFileChooser}
     */
    private JFileChooser initFileChooser() {
        JFileChooser fileChooser = new JFileChooser(new File("./data"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileNameExtensionFilter("Text file", "txt");
        fileChooser.setFileFilter(filter);
        return fileChooser;
    }

    /**
     * Метод вызывает обновление всех дисплеев для отображения изменений либо восстанавливает дефолтное отображение всех дисплеев
     */
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

    /**
     * Инициализация модели данных таблицы траекторной информации со слушателем на изменения в ней
     *
     * @return модель данных таблицы
     */
    public TrajectoryRowTableModel initModel() {
        TrajectoryRowTableModel model = new TrajectoryRowTableModel();
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                TrajectoryRowTableModel model = (TrajectoryRowTableModel) e.getSource();
                model.sortByTime();
                if (!fileStorage.isEmpty()) {
                    fileStorage.updateFileDataByIndex(
                            fileStorage.getCurrentFileIndex(),
                            model.getTableDataInString()
                    );
                }
                updateEntireInfo();
            }
        });

        return model;
    }

    /**
     * Инициализация слушателя события каталога
     *
     * @return
     */
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

    /**
     * Инициализация слушателя событий контекстного меню каталога
     *
     * @return слушатель
     */
    public ActionListener initCatalogPopupActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton buttonInvoker = (JButton) catalogPopupMenu.getInvoker();

                TrajectoryFile selectedFile = fileStorage.findFileByName(TrajectoryFile.stripAsteriskFromNameString(buttonInvoker.getActionCommand()));
                if (selectedFile.hasChanges()) {
                    SaveTrajectoryFileChangesDialog saveTrajectoryFileChangesDialog = new SaveTrajectoryFileChangesDialog(mainFrame);
                    int closingResult = saveTrajectoryFileChangesDialog.showWithResult();
                    if (closingResult == SaveTrajectoryFileChangesDialog.EXIT_ON_SAVE) {
                        if (model.getTableDataInString().isEmpty()) {
                            logger.info("Попытка сохранения пустого файла траектории");
                            new DefaultOKDialog(
                                    mainFrame,
                                    "Сохранение файла",
                                    "Невозможно сохранить пустой файл траектории"
                            ).show();
                        } else {
                            TrajectoryFile currentFile = fileStorage.getCurrentFile();
                            currentFile.writeCurrentDataToFileIfHasChanges();
                            catalogDisplay.updateComponentView();
                        }
                    } else if (closingResult == SaveTrajectoryFileChangesDialog.EXIT_ON_CANCEL) {
                        return;
                    }
                }
                fileStorage.removeFileByName(TrajectoryFile.stripAsteriskFromNameString(buttonInvoker.getActionCommand()));
                updateEntireInfo();
                if (!fileStorage.isEmpty()) {
                    mainFrame.appendFileToFrameTitle(fileStorage.getCurrentFile().getName());
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

    /**
     * Инициализация слушателя событий для главного меню "Файл"
     *
     * @return слушатель
     */
    public ActionListener initMenuActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();
                TrajectoryNameSetDialog trajectoryNameSetDialog;
                int returnVal;
                switch (actionCommand) {
                    case TopMenuBar.MENU_OPEN:
                        returnVal = fileChooser.showOpenDialog(mainFrame);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File chosenFile = fileChooser.getSelectedFile();
                            TrajectoryFile existingFileByPath = fileStorage.findFileByPath(chosenFile.getAbsolutePath());

                            if (existingFileByPath == null) {
                                trajectoryNameSetDialog = new TrajectoryNameSetDialog(mainFrame);
                                boolean isTrajectoryNameAccepted = trajectoryNameSetDialog.showWithResult();

                                if (isTrajectoryNameAccepted) {
                                    String trajectoryName = trajectoryNameSetDialog.getTextFieldString();

                                    try {
                                        if (chosenFile.exists()) {
                                            TrajectoryFile newTrajectoryFile = new TrajectoryFile(chosenFile, trajectoryName);
                                            TrajectoryFile.checkTrajectoryDataValidity(newTrajectoryFile.getData());
                                            TrajectoryFile existingFile = fileStorage.findFileByName(trajectoryName);
                                            if (existingFile != null) {
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
                                                }
                                            } else {
                                                fileStorage.add(newTrajectoryFile);
                                            }
                                            updateEntireInfo();
                                            mainFrame.appendFileToFrameTitle(newTrajectoryFile.getName());
                                        } else {
                                            logger.info("Попытка создать несуществующий файл траектории: {}", chosenFile.getAbsolutePath());
                                            throw new FileNotFoundException();
                                        }

                                    } catch (FileNotFoundException ex) {
                                        logger.info("Попытка загрузки несуществующего файла");
                                        new FileNotFoundDialog(mainFrame).show();
                                    } catch (
                                            InvalidFileFormatException ex) {
                                        logger.info("Попытка загрузки файла неверного формата");
                                        new InvalidFileFormatDialog(mainFrame, ex).show();
                                    }
                                }
                            } else {
                                logger.info("Попытка загрузки уже открытого под именем {} файла", existingFileByPath.getName());
                                new DefaultOKDialog(mainFrame, "Выбор файла", "Этот файл уже открыт (" + existingFileByPath.getName() + ")").show();
                            }
                        }
                        break;
                    case TopMenuBar.MENU_SAVE:
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
                                currentFile.writeCurrentDataToFileIfHasChanges();
                                catalogDisplay.updateComponentView();
                            }
                        }
                        break;
                    case TopMenuBar.MENU_SAVE_AS:
                        if (!fileStorage.isEmpty()) {
                            returnVal = fileChooser.showSaveDialog(mainFrame);
                            if (returnVal == JFileChooser.APPROVE_OPTION) {
                                File newFile = fileChooser.getSelectedFile();
                                TrajectoryFile currentFile = fileStorage.getCurrentFile();
                                String oldPath = currentFile.getPath();
                                currentFile.setPath(newFile.getAbsolutePath());
                                currentFile.writeCurrentDataToFileIfHasChangesOrIsNewFile(oldPath);
                                catalogDisplay.updateComponentView();
                                fileDisplay.updateComponentView();
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

    /**
     * Вызывает у FileChooser'а окно "Открыть" файл
     */
    public void fileChooserOpen() {
        topMenuBar.fireOpenFileMenuItemClick();
    }

    /**
     * Показывает главное окно
     */
    public void showGUI() {
        mainFrame.showMainFrame();
        fileChooserOnFirstOpen(true);
    }

    /**
     * Создает главное окно
     */
    public void assemble() {
        mainFrame = new MainFrame(topMenuBar, catalogDisplay, tableDisplay, fileDisplay, chartDisplay);
        logger.info("mainFrame успешно создан");
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
