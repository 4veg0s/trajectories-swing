package etu.nic.git.trajectories_swing.menus;

import javax.swing.*;
import java.awt.*;

public class TopMenuBar {
    private final JMenuBar menuBar;
    private final JMenu fileMenu;

    public TopMenuBar() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("Файл");
        fileMenu.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

        menuBar.add(fileMenu);


        JMenuItem openMenuItem = new JMenuItem("Открыть");
        JMenuItem saveMenuItem = new JMenuItem("Сохранить");
        JMenuItem saveAsMenuItem = new JMenuItem("Сохранить как");

        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
    }

    public JMenuBar getMenuBar() {
        return this.menuBar;
    }
}
