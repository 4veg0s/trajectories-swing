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
    private final TrajectoryFileStorage fileStorage;
    private List<JButton> buttons;
    public CatalogDisplay(TrajectoryFileStorage storage, ActionListener buttonListener) {
        buttons = new ArrayList<>();
        fileStorage = storage;

        List<String> fileNames = fileStorage.getFileList().stream().map(TrajectoryFile::getName).collect(Collectors.toList());

        Box fileBox = new Box(BoxLayout.Y_AXIS);

        // создание кнопок по именам файлов в хранилище и добавление их в бокс
        JButton button;
        for (String fileName : fileNames) {
            button = new JButton(fileName);
            button.addActionListener(buttonListener);

            button.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

            buttons.add(button);
            fileBox.add(button);
        }

        displayHeader = new JLabel("Каталог");
        displayHeader.setHorizontalAlignment(SwingConstants.CENTER);
        displayHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

        JScrollPane scrollPane = new JScrollPane(fileBox);

        background = new JPanel(new BorderLayout());
        background.add(BorderLayout.NORTH, displayHeader);
        background.add(BorderLayout.CENTER, scrollPane);
    }

    public JComponent getComponent() {
        return background;
    }
}
