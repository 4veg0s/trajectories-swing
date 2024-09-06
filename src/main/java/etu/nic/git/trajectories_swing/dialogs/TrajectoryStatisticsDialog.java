package etu.nic.git.trajectories_swing.dialogs;

import etu.nic.git.trajectories_swing.model.StatisticsTableModel;
import etu.nic.git.trajectories_swing.model.TrajectoryRow;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;
import etu.nic.git.trajectories_swing.tools.StatisticsTool;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrajectoryStatisticsDialog {
    private final String DIALOG_TITLE = "Статистическая информация";
    private final JDialog dialog;
    private final TrajectoryRowTableModel model;

    /**
     * Диалоговое окно, всплывающее в случае, если пользователь выбрал пункт контекстного меню таблицы
     * "Отобразить статистику"
     * Окно содержит таблицу с вычисленными (по возможности) требуемыми статистическими данными:
     * среднее, дисперсия, выборочный момент 2-го порядка, выборочный момент 3-го порядка
     * @param owner
     * @param tableModel модель таблицы, отображающей данные драектории из файла
     */
    public TrajectoryStatisticsDialog(Window owner, TrajectoryRowTableModel tableModel) {
        this.model = tableModel;
        dialog = new JDialog(owner, DIALOG_TITLE, Dialog.ModalityType.DOCUMENT_MODAL);

        // создание модели таблицы со статистической информацией
        StatisticsTableModel statisticsTableModel = new StatisticsTableModel(model);

        // создание таблицы со статистической информацией на основе соответствующей модели
        JTable table = new JTable(statisticsTableModel);
        setJTableColumnsWidth(table, 1000, 20, 10, 10, 10, 10, 10, 10);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font(Font.DIALOG, Font.BOLD, 16));   // шрифт заголовка таблицы
        table.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));   // шрифт данных в таблице
        table.setRowHeight(20);     // высота строки в таблице
        table.setPreferredScrollableViewportSize(table.getPreferredSize());

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setSize(table.getSize());

        tableScrollPane.setBorder(null);
        tableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        tableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        Box verticalBox = new Box(BoxLayout.Y_AXIS);

        JButton okButton = new JButton("OK");
        JPanel buttonPanel = new JPanel();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });

        buttonPanel.add(okButton);
        okButton.setHorizontalAlignment(SwingConstants.CENTER);

        verticalBox.add(tableScrollPane);
        verticalBox.add(buttonPanel);

        dialog.add(verticalBox);

//        JPanel background = new JPanel();
//        background.add(tableScrollPane);
//        dialog.add(background);
        Rectangle rectangleBounds = owner.getBounds();
        dialog.setBounds(new Rectangle(rectangleBounds.x + rectangleBounds.width / 4,
                rectangleBounds.y + rectangleBounds.height / 2, 1100, 200));
    }

    /**
     * Метод устанавливает ширины столбцов в таблице в процентном соотношении, передаваемом в аргументах
     * @param table таблица, для которой будет выполнятся изменение ширин столбцов
     * @param tablePreferredWidth полная ширина таблицы в пикселях
     * @param percentages проценты от полной ширины таблицы для каждого из столбцов
     */
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

    /**
     * Метод показывает диалоговое окно
     */
    public void show() {
        dialog.pack();
        dialog.setVisible(true);
    }
}
