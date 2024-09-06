package etu.nic.git.trajectories_swing.display_components;

import etu.nic.git.trajectories_swing.file_handling.TrajectoryFile;
import etu.nic.git.trajectories_swing.file_handling.TrajectoryFileStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс содержащий все необходимое для отображения каталога траекторий
 * в виде вертикального списка кнопок
 */
public class CatalogDisplay extends AbstractDisplay {
    private static final String DISPLAY_NAME = "Каталог";
    private final JPanel buttonPanel;
    private final TrajectoryFileStorage fileStorage;
    private final JPopupMenu popupMenu;
    private List<JButton> buttons;
    private ActionListener buttonListener;

    /**
     * Создает объект и внедряет зависимости
     * @param storage хранилище файлов траекторий
     * @param popup контекстное меню каталога
     * @param buttonSelectListener слушатель события нажатия на кнопку в каталоге
     */
    public CatalogDisplay(TrajectoryFileStorage storage, JPopupMenu popup, ActionListener buttonSelectListener) {
        super(DISPLAY_NAME);

        buttons = new ArrayList<>();
        fileStorage = storage;
        popupMenu = popup;

        buttonListener = buttonSelectListener;

        buttonPanel = new JPanel();

        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);    // ускорение прокрутки скроллбара

        background.add(BorderLayout.NORTH, displayHeader);

        background.add(BorderLayout.CENTER, scrollPane);
    }

    /**
     * Очищает панель от кнопок и добавляет новые в соответствии с содержанием файлового хранилища
     */
    public void refreshFileList() {
        // удаление всех старых кнопок
        for (JButton button : buttons) {
            buttonPanel.remove(button);
        }
        buttonPanel.setLayout(
                new GridLayout(fileStorage.getFileList().size() + Math.max(8 - fileStorage.getFileList().size(), 0), 1)     // чтобы кнопки были невысокими
        );
        buttons.clear();    // очистка списка кнопок
        if (!fileStorage.isEmpty()) {   // если в хранилище есть файлы
            List<String> fileNames = fileStorage.getFileList().stream().map(TrajectoryFile::getNameWithAsterisk).collect(Collectors.toList());
            // создание кнопок по именам файлов в хранилище и добавление их на панель
            JButton button;
            for (int i = 0; i < fileNames.size(); i++) {
                String fileName = fileNames.get(i);

                button = new JButton(fileName);
                button.addActionListener(buttonListener);   // слушатель нажатия на кнопку
                button.setComponentPopupMenu(popupMenu);    // контекстное меню "закрыть файл"

                button.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
                if (i == fileStorage.getCurrentFileIndex()) {   // кнопка текущего выбранного файла окрашивается в синий
                    button.setBackground(new Color(53, 128, 187));
                    button.setForeground(Color.WHITE);
                } else {
                    button.setBackground(Color.WHITE);  // остальные кнопки окрашиваются в белый
                }

                buttons.add(button);
                buttonPanel.add(button);
            }
        }
        // ревалидация и перерисовка панели с кнопками и лейблом
        background.revalidate();
        background.repaint();
    }

    @Override
    public void updateComponentView() {
        refreshFileList();
    }

    @Override
    public void restoreDefaultState() {
        buttonPanel.removeAll();
        buttonPanel.setLayout(new GridLayout(fileStorage.getFileList().size() + 1, 1));
        buttons.clear();
        background.revalidate();
        background.repaint();
    }
}
