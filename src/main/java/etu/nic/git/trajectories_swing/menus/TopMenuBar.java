package etu.nic.git.trajectories_swing.menus;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.Font;
import java.awt.event.ActionListener;

/**
 * Верхний меню-бар
 */
public class TopMenuBar {
    public static final String MENU_OPEN = "Открыть";
    public static final String MENU_SAVE = "Сохранить";
    public static final String MENU_SAVE_AS = "Сохранить как";
    private final JMenuBar menuBar;
    private final JMenu fileMenu;

    /**
     * Создает меню со слушателем выбора его пунктов
     * @param menuActionListener слушатель событий для меню
     */
    public TopMenuBar(ActionListener menuActionListener) {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("Файл");
        fileMenu.setFont(new Font(Font.DIALOG, Font.BOLD, 16));

        menuBar.add(fileMenu);

        // пункт меню "Открыть"
        JMenuItem openMenuItem = new JMenuItem(MENU_OPEN);
        openMenuItem.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        openMenuItem.addActionListener(menuActionListener);
        // пункт меню "Сохранить"
        JMenuItem saveMenuItem = new JMenuItem(MENU_SAVE);
        saveMenuItem.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        saveMenuItem.addActionListener(menuActionListener);
        // пункт меню "Сохранить как..."
        JMenuItem saveAsMenuItem = new JMenuItem(MENU_SAVE_AS);
        saveAsMenuItem.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        saveAsMenuItem.addActionListener(menuActionListener);

        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
    }

    public JMenuBar getMenuBar() {
        return this.menuBar;
    }

    /**
     * Вызов события нажатия на пункт меню "Открыть" для получения соответствующих последствий
     */
    public void fireOpenFileMenuItemClick() {
        JMenuItem openFileMenuItem = fileMenu.getItem(0);
        openFileMenuItem.doClick();
    }
}
