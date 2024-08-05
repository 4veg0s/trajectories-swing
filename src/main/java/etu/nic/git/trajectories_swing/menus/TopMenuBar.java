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
        fileMenu.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

        menuBar.add(fileMenu);

        JMenuItem openMenuItem = new JMenuItem(MENU_OPEN);
        openMenuItem.addActionListener(menuActionListener);
        JMenuItem saveMenuItem = new JMenuItem(MENU_SAVE);
        saveMenuItem.addActionListener(menuActionListener);
        JMenuItem saveAsMenuItem = new JMenuItem(MENU_SAVE_AS);
        saveAsMenuItem.addActionListener(menuActionListener);

        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
    }

    public JMenuBar getMenuBar() {
        return this.menuBar;
    }
}
