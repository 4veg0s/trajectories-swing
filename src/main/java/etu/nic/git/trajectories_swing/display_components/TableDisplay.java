package etu.nic.git.trajectories_swing.display_components;

import etu.nic.git.trajectories_swing.menus.TableDisplayPopupMenu;
import etu.nic.git.trajectories_swing.model.TrajectoryRow;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.Arrays;

public class TableDisplay extends AbstractDisplay {
    private static final String DISPLAY_NAME = "Таблица";
    private final JTable table;
    private final JScrollPane tableScrollPane;
    private final TrajectoryRowTableModel model;
    private final TableDisplayPopupMenu tableDisplayPopupMenu;

    public TableDisplay(TrajectoryRowTableModel tableModel, TableDisplayPopupMenu popupMenu) {
        super(DISPLAY_NAME);

        model = tableModel;
        tableDisplayPopupMenu = popupMenu;

        model.setTrajectoryRowList(Arrays.asList(new TrajectoryRow()));

        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font(Font.DIALOG, Font.BOLD, 16));   // шрифт заголовка таблицы
        table.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));   // шрифт данных в таблице
        table.setRowHeight(20);     // высота строки в таблице

        tableScrollPane = new JScrollPane(table);
        tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tableScrollPane.setBorder(null);

        hideMainInfo();

        background.add(BorderLayout.NORTH, displayHeader);

        background.add(BorderLayout.CENTER, tableScrollPane);
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

    @Override
    public void restoreDefaultState() {
        model.setTrajectoryRowList(Arrays.asList(new TrajectoryRow()));
        hideMainInfo();
    }

    public void hideMainInfo() {
        tableScrollPane.setVisible(false);
    }
    public void showMainInfo() {
        tableScrollPane.setVisible(true);
    }

    @Override
    public void updateComponentView() {
        showMainInfo();
        background.revalidate();
        background.repaint();
        this.addPopupMenuIfNeeded();
    }

    private void addPopupMenuIfNeeded() {
        if (model.getTrajectoryRowList().isEmpty()) {
            tableScrollPane.setComponentPopupMenu(tableDisplayPopupMenu.getPopupMenu());
        } else {
            tableScrollPane.setComponentPopupMenu(null);
        }
    }

    public void setTablePopupMenu(JPopupMenu menu) {
        table.setComponentPopupMenu(menu);
    }
    public JTable getTable() {
        return this.table;
    }
}
