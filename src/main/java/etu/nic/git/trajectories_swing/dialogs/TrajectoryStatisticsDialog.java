package etu.nic.git.trajectories_swing.dialogs;

import etu.nic.git.trajectories_swing.model.TrajectoryRow;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;
import etu.nic.git.trajectories_swing.tools.StatisticsTool;

import javax.swing.*;
import java.awt.*;

public class TrajectoryStatisticsDialog {
    private final String DIALOG_TITLE = "Статистическая информация";
    private final JDialog dialog;
    private final TrajectoryRowTableModel model;

    public TrajectoryStatisticsDialog(Window owner, TrajectoryRowTableModel tableModel) {
        this.model = tableModel;
        dialog = new JDialog(owner, DIALOG_TITLE, Dialog.ModalityType.DOCUMENT_MODAL);

        Object[] tableHeader = new String[TrajectoryRow.AMOUNT_OF_PARAMETERS];
        for (int i = 0; i < tableHeader.length; i++) {
            tableHeader[i] = (i == 0) ? "" : TrajectoryRow.PARAMETER_NAMES[i];
        }

        StatisticsTool statisticsTool = new StatisticsTool(model);
        Object[][] data = statisticsTool.getData();

        JTable table = new JTable(data, tableHeader);
        JPanel background = new JPanel(); // fixme
        background.add(table);
    }

    public void show() {
        dialog.setVisible(true);
    }
}
