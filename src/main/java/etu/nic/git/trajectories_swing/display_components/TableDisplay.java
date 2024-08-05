package etu.nic.git.trajectories_swing.display_components;

import etu.nic.git.trajectories_swing.model.TrajectoryRow;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;
import etu.nic.git.trajectories_swing.tools.FileDataLoader;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class TableDisplay {
    private JLabel displayHeader;
    private JTable table;
    private JScrollPane scrollPane;
    private JPanel background;
    private TrajectoryRowTableModel model;

    public TableDisplay(TrajectoryRowTableModel tableModel) {
        model = tableModel;

        // настройка лейбла-заголовка для компонента
        displayHeader = new JLabel("Таблица");
        displayHeader.setAlignmentX(0.5f);
        displayHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

        model.setTrajectoryRowList(Arrays.asList(new TrajectoryRow()));

        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        background = new JPanel(new BorderLayout());
        background.add(BorderLayout.CENTER, scrollPane);
        background.add(BorderLayout.NORTH, displayHeader);
    }

    public JComponent getComponent() {
        return background;
    }

    public void createAndShowGUI() {
        String filePath = "../traject1.txt";

        // fixme: перенести метод в TrajectoryRow
        List<TrajectoryRow> trajectoryRowList = FileDataLoader.parseToTrajectoryRowList(FileDataLoader.loadDataFromFile(filePath));

        TrajectoryRowTableModel tableModel = new TrajectoryRowTableModel(trajectoryRowList);
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                TrajectoryRowTableModel model = (TrajectoryRowTableModel) e.getSource();
                model.sortByTime(); // если было изменение значения ячейки в таблице,
                // то произойдет сортировка по времени для данных модели

            }
        });
        table = new JTable(tableModel);

        setJTableColumnsWidth(table, 1200, 10, 15, 15, 15, 15, 15, 15);
//        table.putClientProperty("terminateEditOnFocusLost", true);



        table.setRowHeight(20);
        table.setSize(1200, 800);

        JFrame frame = new JFrame("Custom JTable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        table.setFillsViewportHeight(true);
    }

    public static void setJTableColumnsWidth(JTable table, int tablePreferredWidth,
                                             double... percentages) {
        double total = 0;
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            total += percentages[i];
        }

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth((int) (tablePreferredWidth * (percentages[i] / total)));
        }
    }
}
