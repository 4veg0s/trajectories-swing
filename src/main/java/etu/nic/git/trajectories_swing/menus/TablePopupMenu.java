package etu.nic.git.trajectories_swing.menus;

import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TablePopupMenu {
    private static final String MENU_INSERT_ROW_ABOVE = "Вставить строку выше";
    private static final String MENU_INSERT_ROW_BELOW = "Вставить строку ниже";
    private static final String MENU_DELETE_ROW = "Удалить строку";
    // * Вставить строку ниже
    //  * Вставить строку выше
    //  * Удалить строку
    //  * Отобразить статистику:
    private final JPopupMenu popupMenu;
    private final JTable table;
    private final TrajectoryRowTableModel model;
    private int rowAtPoint;

    public TablePopupMenu(JTable t) {
        this.table = t;
        model = (TrajectoryRowTableModel) table.getModel();
        popupMenu = new JPopupMenu() {
            @Override
            public void show(Component invoker, int x, int y) {
                //int rowAtPoint = table.rowAtPoint(
                //    SwingUtilities.convertPoint(this, new Point(x, y), table));
                //FilesManager.this.generateTablePopupMenu(rowAtPoint);
                rowAtPoint = table.rowAtPoint(new Point(x, y));

                System.out.println(rowAtPoint);
                super.show(invoker, x, y);
            }
        };

        JMenuItem insertRowAboveMenuItem = new JMenuItem(MENU_INSERT_ROW_ABOVE);
        insertRowAboveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.insertRowAbove(rowAtPoint);
                model.fireTableDataChanged();
            }
        });
        JMenuItem insertRowBelowMenuItem = new JMenuItem(MENU_INSERT_ROW_BELOW);
        insertRowBelowMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.insertRowBelow(rowAtPoint);
                model.fireTableDataChanged();
            }
        });
        JMenuItem deleteRowMenuItem = new JMenuItem(MENU_DELETE_ROW);

        popupMenu.add(insertRowAboveMenuItem);
        popupMenu.add(insertRowBelowMenuItem);
        popupMenu.add(deleteRowMenuItem);


    }

    public JPopupMenu getPopupMenu() {
        return this.popupMenu;
    }
}
