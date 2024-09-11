package etu.nic.git.trajectories_swing.menus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Класс, содержащий все необходимое для контекстного меню каталога
 */
public class CatalogPopupMenu {
    private static final String MENU_CLOSE_FILE = "Закрыть файл";
    private final JPopupMenu popupMenu;

    /**
     * Создает объект контекстного меню с переданным слушателем событий
     * @param popupMenuActionListener слушатель событий
     */
    public CatalogPopupMenu(ActionListener popupMenuActionListener) {
        popupMenu = new JPopupMenu();

        JMenuItem closeFileMenuItem = new JMenuItem(MENU_CLOSE_FILE);
        closeFileMenuItem.addActionListener(popupMenuActionListener);

        popupMenu.add(closeFileMenuItem);
    }

    public JPopupMenu getPopupMenu() {
        return this.popupMenu;
    }

    /**
     * Возвращает компонент, на котором было вызвано контекстное меню
     * @return искомый, компонент
     */
    public Component getInvoker() {
        return popupMenu.getInvoker();
    }
}
