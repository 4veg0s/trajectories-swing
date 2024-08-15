package etu.nic.git.trajectories_swing.display_components;

import etu.nic.git.trajectories_swing.tools.TrajectoryFile;
import etu.nic.git.trajectories_swing.tools.TrajectoryFileStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogDisplay {
    private final JLabel displayHeader;
    private final JPanel background;
    private final JPanel buttonPanel;
    private final TrajectoryFileStorage fileStorage;
    private List<JButton> buttons;
    private ActionListener buttonListener;

    public CatalogDisplay(TrajectoryFileStorage storage, ActionListener listener) {
        buttons = new ArrayList<>();
        fileStorage = storage;

        buttonListener = listener;

        List<String> fileNames = fileStorage.getFileList().stream().map(TrajectoryFile::getName).collect(Collectors.toList());

        buttonPanel = new JPanel(new GridLayout(fileNames.size() + 1, 1));

        // создание кнопок по именам файлов в хранилище и добавление их в бокс
        JButton button;
        for (String fileName : fileNames) {
            button = new JButton(fileName);
            button.addActionListener(buttonListener);

            button.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

            buttons.add(button);
            buttonPanel.add(button);
        }

        displayHeader = new JLabel("Каталог");
        displayHeader.setHorizontalAlignment(SwingConstants.CENTER);
        displayHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

        JScrollPane scrollPane = new JScrollPane(buttonPanel);

        background = new JPanel(new BorderLayout());
        background.add(BorderLayout.NORTH, displayHeader);
        background.add(BorderLayout.CENTER, scrollPane);
    }

    public JComponent getComponent() {
        return background;
    }
    public void refreshFileList() {
        List<String> fileNames = fileStorage.getFileList().stream().map(TrajectoryFile::getNameWithAsterisk).collect(Collectors.toList());
        buttonPanel.removeAll();
        buttonPanel.setLayout(new GridLayout(fileNames.size() + 1, 1));
        buttons.clear();
        // создание кнопок по именам файлов в хранилище и добавление их в бокс
        JButton button;
        for (int i = 0; i < fileNames.size(); i++) {
            String fileName = fileNames.get(i);

            button = new JButton(fileName);
            button.addActionListener(buttonListener);

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
        background.revalidate();
        background.repaint();
    }
}
