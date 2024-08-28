package etu.nic.git.trajectories_swing.display_components;

import etu.nic.git.trajectories_swing.tools.TrajectoryFileStorage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class FileDisplay {
    private final JPanel background;
    private final JLabel displayHeader;
    private final JLabel filePathLabel;
    private final JTextArea fileTextArea;
    private final JScrollPane scrollPane;
    private final JPanel filePathAndTextArea;
    private final TrajectoryFileStorage fileStorage;

    public FileDisplay(TrajectoryFileStorage storage) {
        fileStorage = storage;

        Box verticalBox = new Box(BoxLayout.Y_AXIS);

        displayHeader = new JLabel("Файл");
        displayHeader.setAlignmentX(0.5f);
        displayHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        verticalBox.add(displayHeader);

        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.HORIZONTAL);
        verticalBox.add(separator);

        if (fileStorage.isEmpty()) {
            filePathLabel = new JLabel("Файл не выбран");
        } else {
            String path = fileStorage.getCurrentFile().getPath();
            if (path.length() > 30) {
                String shortenedPath =
                        path.substring(0, Math.max(path.indexOf("\\"), path.indexOf("/")) + 1) +
                        "..." +
                        path.substring(Math.max(path.lastIndexOf("\\"), path.lastIndexOf("/")));
                filePathLabel = new JLabel(shortenedPath);
                filePathLabel.setToolTipText(path);
            } else {
                filePathLabel = new JLabel(path);
            }
        }
        filePathLabel.setHorizontalAlignment(SwingConstants.CENTER);
        filePathLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 18));

        fileTextArea = initFileTextArea();
        scrollPane = initFileTextScrollPane(fileTextArea);
        hideMainInfo();

        filePathAndTextArea = new JPanel(new BorderLayout());
        filePathAndTextArea.add(BorderLayout.NORTH, filePathLabel);
        filePathAndTextArea.add(BorderLayout.CENTER, scrollPane);

        background = new JPanel(new BorderLayout());
        background.setBorder(new LineBorder(Color.GRAY, 1));
        verticalBox.add(filePathAndTextArea);
        background.add(verticalBox);
    }

    public JComponent getComponent() {
        return background;
    }


    public void updateDisplayedInfo() {
        if (!fileStorage.isEmpty()) {
            loadFileDataToArea(fileStorage.getCurrentFile().getData());
            showMainInfo();
        } else {
            loadFileDataToArea("");
            hideMainInfo();
        }
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
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        return scrollPane;
    }

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

    public void restoreDefaultState() {
        filePathLabel.setText("Файл не выбран");
        fileTextArea.setText("");
        hideMainInfo();
    }

    public void hideMainInfo() {
        filePathLabel.setVisible(false);
        scrollPane.setVisible(false);
    }
    public void showMainInfo() {
        filePathLabel.setVisible(true);
        scrollPane.setVisible(true);
    }
}
