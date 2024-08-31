package etu.nic.git.trajectories_swing.display_components;

import etu.nic.git.trajectories_swing.file_handling.TrajectoryFile;
import etu.nic.git.trajectories_swing.file_handling.TrajectoryFileStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogDisplay extends AbstractDisplay {
    private static final String DISPLAY_NAME = "Каталог";
    private final JPanel buttonPanel;
    private final TrajectoryFileStorage fileStorage;
    private final JPopupMenu popupMenu;
    private List<JButton> buttons;
    private ActionListener buttonListener;

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

    public void refreshFileList() {
        for (JButton button : buttons) {
            buttonPanel.remove(button);
        }
        buttonPanel.setLayout(
                new GridLayout(fileStorage.getFileList().size() + Math.max(8 - fileStorage.getFileList().size(), 0), 1)     // чтобы кнопки были невысокими
        );
        buttons.clear();
        if (!fileStorage.isEmpty()) {
            List<String> fileNames = fileStorage.getFileList().stream().map(TrajectoryFile::getNameWithAsterisk).collect(Collectors.toList());
            // создание кнопок по именам файлов в хранилище и добавление их в бокс
            JButton button;
            for (int i = 0; i < fileNames.size(); i++) {
                String fileName = fileNames.get(i);

                button = new JButton(fileName);
                button.addActionListener(buttonListener);
                button.setComponentPopupMenu(popupMenu);

                button.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
                if (i == fileStorage.getCurrentFileIndex()) {
                    button.setBackground(new Color(53, 128, 187));
                    button.setForeground(Color.WHITE);
                } else {
                    button.setBackground(Color.WHITE);
                }

                buttons.add(button);
                buttonPanel.add(button);
            }
        }
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
