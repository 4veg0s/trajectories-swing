package etu.nic.git.trajectories_swing.menus;

import etu.nic.git.trajectories_swing.file_handling.TrajectoryFileStorage;
import etu.nic.git.trajectories_swing.model.TrajectoryRow;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Класс, содержащий контекстное меню дисплея таблицы, становящееся доступным,
 * если все строки таблицы открытого файла были удалены
 */
public class TableDisplayPopupMenu {
    private static final String MENU_ADD_ROW = "Добавить строку";
    private final JPopupMenu popupMenu;
    private final TrajectoryRowTableModel model;
    private final TrajectoryFileStorage fileStorage;

    /**
     * Создает объект и внедряет зависимости
     * @param storage хранилище файлов траекторной информации
     * @param tableModel модель данных таблицы
     */
    public TableDisplayPopupMenu(TrajectoryFileStorage storage, TrajectoryRowTableModel tableModel) {
        this.model = tableModel;
        this.fileStorage = storage;

        popupMenu = new JPopupMenu();

        JMenuItem addRowMenuItem = new JMenuItem(MENU_ADD_ROW);
        addRowMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<TrajectoryRow> trajectoryRowList = model.getTrajectoryRowList();

                // если в открытом файле не осталось строк у таблицы (их все удалили), то станет доступно добавление пустой строки в таблицу
                if (!fileStorage.isEmpty() && trajectoryRowList.isEmpty()) {
                    trajectoryRowList.add(new TrajectoryRow());
                }
                model.fireTableDataChanged();
            }
        });

        popupMenu.add(addRowMenuItem);
    }

    public JPopupMenu getPopupMenu() {
        return this.popupMenu;
    }
}
