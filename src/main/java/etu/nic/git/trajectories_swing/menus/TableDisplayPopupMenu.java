package etu.nic.git.trajectories_swing.menus;

import etu.nic.git.trajectories_swing.file_handling.TrajectoryFileStorage;
import etu.nic.git.trajectories_swing.model.TrajectoryRow;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TableDisplayPopupMenu {
    private static final String MENU_ADD_ROW = "Добавить строку";
    private final JPopupMenu popupMenu;
    private final TrajectoryRowTableModel model;
    private final TrajectoryFileStorage fileStorage;

    public TableDisplayPopupMenu(TrajectoryFileStorage storage, TrajectoryRowTableModel tableModel) {
        this.model = tableModel;
        this.fileStorage = storage;

        popupMenu = new JPopupMenu();

        JMenuItem addRowMenuItem = new JMenuItem(MENU_ADD_ROW);
        addRowMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<TrajectoryRow> trajectoryRowList = model.getTrajectoryRowList();
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
