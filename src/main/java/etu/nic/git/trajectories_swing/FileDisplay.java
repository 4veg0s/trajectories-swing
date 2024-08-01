package etu.nic.git.trajectories_swing;

import etu.nic.git.trajectories_swing.tools.FileDataLoader;

import javax.swing.*;
import java.awt.*;

public class FileDisplay {
    private JFrame frame;
    private JPanel fileDataPanel;
    private JLabel fileNameLabel;
    private JTextArea fileTextArea;

    public void buildGUI() {
        String filePath = "../traject1.txt";
        this.frame = new JFrame();

        JPanel fileDisplayPanel = initFileDisplayPanel(filePath);
        loadFileDataToArea(filePath);
        this.frame.getContentPane().add(fileDisplayPanel);
        this.frame.setBounds(new Rectangle(100, 100, 400, 500));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }

    private JPanel initFileDisplayPanel(String filePath) {
        Box header = new Box(BoxLayout.Y_AXIS);

        JLabel fileHeaderLabel = new JLabel("Файл");
        fileHeaderLabel.setAlignmentX(0.5f);
        fileHeaderLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        header.add(fileHeaderLabel);

        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.HORIZONTAL);
        header.add(separator);

        fileNameLabel = new JLabel(filePath);
        fileNameLabel.setAlignmentX(0.5f);
        fileNameLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        header.add(fileNameLabel);

        this.fileTextArea = initFileTextArea();
        JScrollPane scrollPane = initFileTextScrollPane(this.fileTextArea);

        JPanel fileDisplayPanel = new JPanel(new BorderLayout());
        fileDisplayPanel.add(BorderLayout.CENTER, scrollPane);
        fileDisplayPanel.add(BorderLayout.NORTH, header);

        return fileDisplayPanel;
    }

    private JTextArea initFileTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setMargin(new Insets(5, 5,5, 5));
        textArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 10));

        return textArea;
    }

    private JScrollPane initFileTextScrollPane(JTextArea textArea) {

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        return scrollPane;
    }

    private void loadFileDataToArea(String fileName) {
        String text = FileDataLoader.loadDataFromFile(fileName);
        this.fileTextArea.setText(text);
    }
}
