package etu.nic.git.trajectories_swing.display_components;

import etu.nic.git.trajectories_swing.model.TrajectoryRow;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;
import etu.nic.git.trajectories_swing.tools.FileDataLoader;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.Arrays;
import java.util.List;

public class TableDisplay {
    private final JLabel displayHeader;
    private final JTable table;
    private final JScrollPane scrollPane;
    private final JPanel background;
    private final TrajectoryRowTableModel model;

    public TableDisplay(TrajectoryRowTableModel tableModel) {
        model = tableModel;

        // настройка лейбла-заголовка для компонента
        displayHeader = new JLabel("Таблица");
        displayHeader.setHorizontalAlignment(SwingConstants.CENTER);
        displayHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

        model.setTrajectoryRowList(Arrays.asList(new TrajectoryRow()));

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font(Font.DIALOG, Font.BOLD, 16));   // шрифт заголовка таблицы
        table.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));   // шрифт данных в таблице
        table.setRowHeight(20);     // высота строки в таблице


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

    // fixme может пригодиться
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

    public void revalidateAndRepaint() {
        background.revalidate();
        background.repaint();
    }

    public void restoreDefaultState() {
        model.setTrajectoryRowList(Arrays.asList(new TrajectoryRow()));
    }
}
