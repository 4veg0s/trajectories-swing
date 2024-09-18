package etu.nic.git.trajectories_swing.display;

import etu.nic.git.trajectories_swing.menu.TableDisplayPopupMenu;
import etu.nic.git.trajectories_swing.model.TrajectoryRow;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;


import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import java.awt.Font;
import java.awt.BorderLayout;
import java.util.Arrays;

/**
 * Класс содержащий все необходимое для отображения таблицы с траекторной информацией
 */
public class TableDisplay extends AbstractDisplay {
    private static final String DISPLAY_NAME = "Таблица";
    private final JTable table;
    private final JScrollPane tableScrollPane;
    private final TrajectoryRowTableModel model;
    private final TableDisplayPopupMenu tableDisplayPopupMenu;

    /**
     * Создает объект и внедряет зависимости
     * @param tableModel модель данных траекторной таблицы
     * @param popupMenu контекстное меню таблицы
     */
    public TableDisplay(TrajectoryRowTableModel tableModel, TableDisplayPopupMenu popupMenu) {
        super(DISPLAY_NAME);

        model = tableModel;
        tableDisplayPopupMenu = popupMenu;

        // инициализируем данные в модели списком из одной пустой траектории
        model.setTrajectoryRowList(Arrays.asList(new TrajectoryRow()));

        table = new JTable(model); // создание таблицы на основе модели данных
        table.getTableHeader().setReorderingAllowed(false);     // запрещаем перетаскивание столбцов
        table.getTableHeader().setFont(new Font(Font.DIALOG, Font.BOLD, 16));   // шрифт заголовка таблицы
        table.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));   // шрифт данных в таблице
        table.setRowHeight(20);     // высота строки в таблице

        tableScrollPane = new JScrollPane(table);
        tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        tableScrollPane.setBorder(null);    // удаление границы у скролл-панели

        hideMainInfo();

        background.add(BorderLayout.NORTH, displayHeader);

        background.add(BorderLayout.CENTER, tableScrollPane);
    }

    @Override
    public void restoreDefaultState() {
        model.setTrajectoryRowList(Arrays.asList(new TrajectoryRow()));
        hideMainInfo();
    }

    /**
     * Скрывает компоненты дисплея
     */
    public void hideMainInfo() {
        tableScrollPane.setVisible(false);
    }

    /**
     * Показывает компоненты дисплея
     */
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

    /**
     * Метод добавляет к пустому пространству под хедером таблицы контекстное меню для добавления одной строки,
     * если в таблице не осталось строк, и удаляет меню, если строки в таблице есть
     */
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
