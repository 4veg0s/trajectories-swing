package etu.nic.git.trajectories_swing.menus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TopMenuBar {
    public static final String MENU_OPEN = "Открыть";
    public static final String MENU_SAVE = "Сохранить";
    public static final String MENU_SAVE_AS = "Сохранить как";
    private final JMenuBar menuBar;
    private final JMenu fileMenu;

    public TopMenuBar(ActionListener menuActionListener) {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("Файл");
        fileMenu.setFont(new Font(Font.DIALOG, Font.BOLD, 16));

        menuBar.add(fileMenu);

        JMenuItem openMenuItem = new JMenuItem(MENU_OPEN);
        openMenuItem.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        openMenuItem.addActionListener(menuActionListener);
        JMenuItem saveMenuItem = new JMenuItem(MENU_SAVE);
        saveMenuItem.setFont(new Font(Font.DIALOG, Font.BOLD, 13));
        saveMenuItem.addActionListener(menuActionListener);
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
    public void fireOpenFileMenuItemClick() {
        JMenuItem openFileMenuItem = fileMenu.getItem(0);
        openFileMenuItem.doClick();
    }
}
