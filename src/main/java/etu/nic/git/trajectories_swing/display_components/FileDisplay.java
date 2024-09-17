package etu.nic.git.trajectories_swing.display_components;

import etu.nic.git.trajectories_swing.file_handling.TrajectoryFileStorage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;


/**
 * Класс содержащий все необходимое для отображения информации из файла в текстовой области
 */
public class FileDisplay extends AbstractDisplay {
    private static final String DISPLAY_NAME = "Файл";
    private final JLabel filePathLabel;
    private final JTextArea fileTextArea;
    private final JScrollPane scrollPane;
    private final JPanel filePathAndTextArea;
    private final JSeparator separator;
    private final TrajectoryFileStorage fileStorage;

    /**
     * Создает объект дисплея и внедряет зависимость
     * @param storage хранилище Файлов траекторий
     */
    public FileDisplay(TrajectoryFileStorage storage) {
        super(DISPLAY_NAME);

        fileStorage = storage;

        Box verticalBox = new Box(BoxLayout.Y_AXIS);

        verticalBox.add(displayHeader);

        // разделитель между двумя лейблами
        separator = new JSeparator();
        separator.setOrientation(SwingConstants.HORIZONTAL);
        verticalBox.add(separator);

        if (fileStorage.isEmpty()) {    // если хранилище пустое
            filePathLabel = new JLabel("Файл не выбран");
        } else {    // если хранилище не пустое, то в лейбл записываем сокращенный путь к файлу
            String path = fileStorage.getCurrentFile().getPath();
            if (path.length() > 30) {
                String shortenedPath =
                        path.substring(0, Math.max(path.indexOf("\\"), path.indexOf("/")) + 1) +
                        "..." +
                        path.substring(Math.max(path.lastIndexOf("\\"), path.lastIndexOf("/")));
                filePathLabel = new JLabel(shortenedPath);
                filePathLabel.setToolTipText(path); // если путь до файла слишком длинный, то урезаем его
                                                    // и устанавливаем тултип для лейбла с полным путем до файла
            } else {
                filePathLabel = new JLabel(path);
            }
        }
        filePathLabel.setHorizontalAlignment(SwingConstants.CENTER);
        filePathLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 18));

        fileTextArea = initFileTextArea();
        scrollPane = initFileTextScrollPane(fileTextArea);
        scrollPane.setBorder(null);

        hideMainInfo();

        filePathAndTextArea = new JPanel(new BorderLayout());
        filePathAndTextArea.add(BorderLayout.NORTH, filePathLabel);
        filePathAndTextArea.add(BorderLayout.CENTER, scrollPane);

        verticalBox.add(filePathAndTextArea);
        background.add(verticalBox);
    }

    @Override
    public void updateComponentView() {
        if (!fileStorage.isEmpty()) {
            loadFileDataToArea(fileStorage.getCurrentFile().getData());
            showMainInfo();
        } else {
            loadFileDataToArea("");
            hideMainInfo();
        }
    }

    /**
     * Инициализирует текстовую область для отображения файла
     * @return текстовая область
     */
    private JTextArea initFileTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setMargin(new Insets(5, 5,5, 5));
        textArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));

        return textArea;
    }

    /**
     * Инициализирует скролл панель для текстовой области
     * @param textArea текстовая область
     * @return скролл панель
     */
    private JScrollPane initFileTextScrollPane(JTextArea textArea) {

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        return scrollPane;
    }

    /**
     * Загружает данные из файла в текстовую область
     * @param fileData данные из файла
     */
    private void loadFileDataToArea(String fileData) {
        this.fileTextArea.setText(fileData);
        String path = fileStorage.getCurrentFile().getPath();
        if (path.length() > 30) {
            String shortenedPath =
                    path.substring(0, Math.max(path.indexOf("\\"), path.indexOf("/")) + 1) +
                            "..." +
                            path.substring(Math.max(path.lastIndexOf("\\"), path.lastIndexOf("/")));
            filePathLabel.setText(shortenedPath);
            filePathLabel.setToolTipText(path);
        } else {
            filePathLabel.setText(path);
        }
    }

    @Override
    public void restoreDefaultState() {
        filePathLabel.setText("Файл не выбран");
        fileTextArea.setText("");
        hideMainInfo();
    }

    /**
     * Скрывает компоненты дисплея
     */
    public void hideMainInfo() {
        filePathLabel.setVisible(false);
        scrollPane.setVisible(false);
        separator.setVisible(false);
    }

    /**
     * Показывает компоненты дисплея
     */
    public void showMainInfo() {
        filePathLabel.setVisible(true);
        scrollPane.setVisible(true);
        separator.setVisible(true);
    }
}
