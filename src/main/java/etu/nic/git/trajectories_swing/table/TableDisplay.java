package etu.nic.git.trajectories_swing.table;

import etu.nic.git.trajectories_swing.tools.FileDataLoader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class TableDisplay {
    public void createAndShowGUI() {
        String filePath = "../traject1.txt";
        List<TrajectoryRow> trajectoryRowList = FileDataLoader.parseToTrajectoryRowList(FileDataLoader.loadDataFromFile(filePath));

        TrajectoryRowTableModel tableModel = new TrajectoryRowTableModel(trajectoryRowList);
        JTable table = new JTable(tableModel);

        JFrame frame = new JFrame("Custom JTable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        table.setFillsViewportHeight(true);

        frame.add(new JScrollPane(table));
        frame.setSize(800, 400);
        frame.pack();
        frame.setVisible(true);
    }
}
