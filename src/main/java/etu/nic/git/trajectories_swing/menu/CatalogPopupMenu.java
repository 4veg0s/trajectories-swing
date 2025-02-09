package etu.nic.git.trajectories_swing.menu;


import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.ActionListener;

/**
 * Класс, содержащий все необходимое для контекстного меню каталога
 */
public class CatalogPopupMenu {
    private static final String MENU_CLOSE_FILE = "Закрыть файл";
    private static final String MENU_SAVE_TO_REMOTE = "Сохранить на сервере";
    private static final String MENU_DELETE_FROM_REMOTE = "Удалить с сервера";
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

    public void setSaveToRemotePopupListener(ActionListener popupMenuActionListener) {
        JMenuItem saveToRemoteMenuItem = new JMenuItem(MENU_SAVE_TO_REMOTE);
        saveToRemoteMenuItem.addActionListener(popupMenuActionListener);

        popupMenu.add(saveToRemoteMenuItem);
    }
    public void setDeleteFromRemotePopupListener(ActionListener popupMenuActionListener) {
        JMenuItem deleteFromRemoteMenuItem = new JMenuItem(MENU_DELETE_FROM_REMOTE);
        deleteFromRemoteMenuItem.addActionListener(popupMenuActionListener);

        popupMenu.add(deleteFromRemoteMenuItem);
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
