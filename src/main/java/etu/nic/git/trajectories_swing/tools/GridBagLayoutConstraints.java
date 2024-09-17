package etu.nic.git.trajectories_swing.tools;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/**
 * Класс, содержащий константы констрейнтов расположения дисплеев в главном окне для {@link GridBagLayout}
 */
public class GridBagLayoutConstraints {
    private static final int FRAME_RESOLUTION_X = 16;
    private static final int FRAME_RESOLUTION_Y = 9;
    private static final int LEFT_COLUMN_WIDTH = 6;
    private static final int TOP_ROW_HEIGHT = 2;
    private static final int RIGHT_COLUMN_WIDTH = FRAME_RESOLUTION_X - LEFT_COLUMN_WIDTH;
    private static final int BOTTOM_ROW_HEIGHT = FRAME_RESOLUTION_Y - TOP_ROW_HEIGHT;

    /**
     * Получение констрейнтов для каталога
     * @return констрейнты для {@link GridBagLayout}
     */
    public static GridBagConstraints catalogDisplayConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.3;
        constraints.weighty = 0.3;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = LEFT_COLUMN_WIDTH;
        constraints.gridheight = TOP_ROW_HEIGHT;
        return constraints;
    }

    /**
     * Получение констрейнтов для таблицы
     * @return констрейнты для {@link GridBagLayout}
     */
    public static GridBagConstraints tableDisplayConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.3;
        constraints.weighty = 0.3;
        constraints.gridx = LEFT_COLUMN_WIDTH;
        constraints.gridy = 0;
        constraints.gridwidth = RIGHT_COLUMN_WIDTH;
        constraints.gridheight = TOP_ROW_HEIGHT;
        return constraints;
    }

    /**
     * Получение констрейнтов для компоненты отображения файла
     * @return констрейнты для {@link GridBagLayout}
     */
    public static GridBagConstraints fileDisplayConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridx = 0;
        constraints.gridy = TOP_ROW_HEIGHT;
        constraints.gridwidth = LEFT_COLUMN_WIDTH;
        constraints.gridheight = BOTTOM_ROW_HEIGHT;
        return constraints;
    }

    /**
     * Получение констрейнтов для компоненты отображения графика
     * @return констрейнты для {@link GridBagLayout}
     */
    public static GridBagConstraints chartDisplayConstraints() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridx = LEFT_COLUMN_WIDTH;
        constraints.gridy = TOP_ROW_HEIGHT;
        constraints.gridwidth = RIGHT_COLUMN_WIDTH;
        constraints.gridheight = BOTTOM_ROW_HEIGHT;
        return constraints;
    }
}
