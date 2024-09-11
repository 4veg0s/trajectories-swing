package etu.nic.git.trajectories_swing.menus;

import etu.nic.git.trajectories_swing.MainFrame;
import etu.nic.git.trajectories_swing.dialogs.TrajectoryStatisticsDialog;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Класс, содержащий все необходимое для контекстного меню таблицы
 */
public class TablePopupMenu {
    private static final String MENU_INSERT_ROW_ABOVE = "Вставить строку выше";
    private static final String MENU_INSERT_ROW_BELOW = "Вставить строку ниже";
    private static final String MENU_DELETE_ROW = "Удалить строку";
    private static final String MENU_SHOW_STATS = "Отобразить статистику";
    private final JPopupMenu popupMenu;
    private final JTable table;
    private final TrajectoryRowTableModel model;
    private int rowAtPoint;

    /**
     * Создает контекстное меню для таблицы
     * @param t таблица, для которой будет создано контекстное меню
     */
    public TablePopupMenu(JTable t) {
        this.table = t;
        model = (TrajectoryRowTableModel) table.getModel();  // достаем модель данных из таблицы
        popupMenu = new JPopupMenu() {
            @Override
            public void show(Component invoker, int x, int y) {
                rowAtPoint = table.rowAtPoint(new Point(x, y));  // номер строки таблицы по соответствующим координатам
                super.show(invoker, x, y);  // отображение контекстного меню
            }
        };

        // элемент меню для вставки строки выше выбранной
        JMenuItem insertRowAboveMenuItem = new JMenuItem(MENU_INSERT_ROW_ABOVE);
        insertRowAboveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.insertRowAbove(rowAtPoint);
                model.fireTableDataChanged();
            }
        });

        // элемент меню для вставки строки ниже выбранной
        JMenuItem insertRowBelowMenuItem = new JMenuItem(MENU_INSERT_ROW_BELOW);
        insertRowBelowMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.insertRowBelow(rowAtPoint);
                model.fireTableDataChanged();
            }
        });

        // элемент меню для удаления выбранной строки
        JMenuItem deleteRowMenuItem = new JMenuItem(MENU_DELETE_ROW);
        deleteRowMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.deleteRow(rowAtPoint);
                model.fireTableDataChanged();
            }
        });

        // элемент меню для отображения статистической информации
        JMenuItem showStatsMenuItem = new JMenuItem(MENU_SHOW_STATS);
        showStatsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TrajectoryStatisticsDialog(SwingUtilities.getWindowAncestor(table), model).show();
            }
        });

        Font font14 = new Font(Font.DIALOG, Font.BOLD, 14);

        insertRowAboveMenuItem.setFont(font14);
        insertRowBelowMenuItem.setFont(font14);
        deleteRowMenuItem.setFont(font14);
        showStatsMenuItem.setFont(font14);

        popupMenu.add(insertRowAboveMenuItem);
        popupMenu.add(insertRowBelowMenuItem);
        popupMenu.add(deleteRowMenuItem);
        popupMenu.add(showStatsMenuItem);
    }

    public JPopupMenu getPopupMenu() {
        return this.popupMenu;
    }
}
