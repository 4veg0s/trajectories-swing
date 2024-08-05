package etu.nic.git.trajectories_swing.display_components;

import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;
import etu.nic.git.trajectories_swing.tools.FileDataLoader;
import etu.nic.git.trajectories_swing.tools.TrajectoryFileStorage;

import javax.swing.*;
import java.awt.*;

public class FileDisplay {
    private JPanel background;
    private JLabel displayHeader;
    private JLabel filePathLabel;
    private JTextArea fileTextArea;
    private JScrollPane scrollPane;
    private TrajectoryFileStorage fileStorage;

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
            String path = fileStorage.getCurrentFile().getFilePath();
            if (path.length() > 20) {
                String shortenedPath = path.substring(0, 3) +
                        "..." +
                        path.substring(path.length() - 15, path.length() - 1);
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
        background.add(BorderLayout.CENTER, scrollPane);
        background.add(BorderLayout.NORTH, header);
    }

    public JComponent getComponent() {
        return background;
    }

    public void buildGUI() {
        String filePath = "../traject1.txt";

        JPanel fileDisplayPanel = initFileDisplayPanel(filePath);
        loadFileDataToArea(filePath);
    }

    private JPanel initFileDisplayPanel(String filePath) {
        Box header = new Box(BoxLayout.Y_AXIS);

        displayHeader = new JLabel("Файл");
        displayHeader.setAlignmentX(0.5f);
        displayHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        header.add(displayHeader);

        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.HORIZONTAL);
        header.add(separator);

        filePathLabel = new JLabel(filePath);
        filePathLabel.setAlignmentX(0.5f);
        filePathLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        header.add(filePathLabel);

        this.fileTextArea = initFileTextArea();
        JScrollPane scrollPane = initFileTextScrollPane(this.fileTextArea);

        JPanel fileDisplayPanel = new JPanel(new BorderLayout());
        fileDisplayPanel.add(BorderLayout.CENTER, scrollPane);
        fileDisplayPanel.add(BorderLayout.NORTH, header);

        return fileDisplayPanel;
    }

    public void updateDisplayedInfo() {
        loadFileDataToArea(fileStorage.getCurrentFile().getFileData());
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
