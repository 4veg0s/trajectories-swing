package etu.nic.git.trajectories_swing.display_components;

import etu.nic.git.trajectories_swing.tools.TrajectoryFileStorage;

import javax.swing.*;
import java.awt.*;

public class FileDisplay {
    private final JPanel background;
    private final JLabel displayHeader;
    private final JLabel filePathLabel;
    private final JTextArea fileTextArea;
    private final JScrollPane scrollPane;
    private final TrajectoryFileStorage fileStorage;

    public FileDisplay(TrajectoryFileStorage storage) {
        fileStorage = storage;

        Box header = new Box(BoxLayout.Y_AXIS);

        displayHeader = new JLabel("Файл");
        displayHeader.setAlignmentX(0.5f);
        displayHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        header.add(displayHeader);

        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.HORIZONTAL);
        header.add(separator);

        if (fileStorage.isEmpty()) {
            filePathLabel = new JLabel("Файл не выбран");
        } else {
            String path = fileStorage.getCurrentFile().getPath();
            if (path.length() > 30) {
                String shortenedPath =
                        path.substring(0, Math.max(path.indexOf("\\"), path.indexOf("/")) + 1) +
                        "..." +
                        path.substring(Math.max(path.lastIndexOf("\\"), path.lastIndexOf("/")), path.length() - 1);
                filePathLabel = new JLabel(shortenedPath);
                filePathLabel.setToolTipText(path);
            } else {
                filePathLabel = new JLabel(path);
            }
        }
        filePathLabel.setAlignmentX(0.5f);
        filePathLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        header.add(filePathLabel);

        fileTextArea = initFileTextArea();
        scrollPane = initFileTextScrollPane(fileTextArea);

        background = new JPanel(new BorderLayout());
        background.add(BorderLayout.NORTH, header);
        background.add(BorderLayout.CENTER, scrollPane);
    }

    public JComponent getComponent() {
        return background;
    }


    public void updateDisplayedInfo() {
        loadFileDataToArea(fileStorage.getCurrentFile().getData());
    }

    private JTextArea initFileTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setMargin(new Insets(5, 5,5, 5));
        textArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));

        return textArea;
    }

    private JScrollPane initFileTextScrollPane(JTextArea textArea) {

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        return scrollPane;
    }

    private void loadFileDataToArea(String fileData) {
        this.fileTextArea.setText(fileData);
    }
}
