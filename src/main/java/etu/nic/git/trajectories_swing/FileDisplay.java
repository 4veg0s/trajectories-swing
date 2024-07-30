package etu.nic.git.trajectories_swing;

import etu.nic.git.trajectories_swing.tools.FileDataLoader;

import javax.swing.*;
import java.awt.*;

public class FileDisplay {
    private JFrame frame;
    private JPanel fileDataPanel;
    private JTextArea fileTextArea;
    private JScrollPane scrollPane;

    public void buildGUI() {
        this.frame = new JFrame();
        this.fileTextArea = initFileTextArea();
        this.scrollPane = initFileTextScrollPane(this.fileTextArea);
        loadFileDataToArea("../traject1.txt");
        fileDataPanel = new JPanel(new BorderLayout());
        fileDataPanel.add(BorderLayout.CENTER, this.scrollPane);
        this.frame.getContentPane().add(fileDataPanel);
        this.frame.setBounds(new Rectangle(100, 100, 400, 500));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
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
