package etu.nic.git.trajectories_swing;

import etu.nic.git.trajectories_swing.display_components.CatalogDisplay;
import etu.nic.git.trajectories_swing.display_components.ChartDisplay;
import etu.nic.git.trajectories_swing.display_components.FileDisplay;
import etu.nic.git.trajectories_swing.display_components.TableDisplay;
import etu.nic.git.trajectories_swing.menus.TopMenuBar;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final String FRAME_TITLE = "Траектории";

    public MainFrame() throws HeadlessException {
        super();
    }
    public MainFrame(TopMenuBar menuBar, CatalogDisplay catalogDisplay, TableDisplay tableDisplay, FileDisplay fileDisplay, ChartDisplay chartDisplay) {
        super("Траектории");

        this.setJMenuBar(menuBar.getMenuBar());

        JPanel background = new JPanel(new BorderLayout());

        // разделитель для гуи
        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.HORIZONTAL);
        // верхняя часть окна с каталогом и таблицей
        Box leftRow = new Box(BoxLayout.Y_AXIS);
        leftRow.add(catalogDisplay.getComponent());
        leftRow.add(separator);
        leftRow.add(fileDisplay.getComponent());

        // нижняя часть окна
        Box rightRow = new Box(BoxLayout.Y_AXIS);
        rightRow.add(tableDisplay.getComponent());
        rightRow.add(separator);
        rightRow.add(chartDisplay.getComponent());

        background.add(BorderLayout.WEST, leftRow);
        background.add(BorderLayout.CENTER, rightRow);

        this.getContentPane().add(background);
        this.setBounds(new Rectangle(100, 100, 1200, 800));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void showMainFrame() {
        this.setVisible(true);
    }

    public void appendFileToFrameTitle(String name) {
        if (name != null) {
            this.setTitle(FRAME_TITLE + " - " + name);
        } else {
            this.setTitle(FRAME_TITLE);
        }
    }

    public void restoreTitle() {
        this.setTitle(FRAME_TITLE);
    }

    public Rectangle getFrameBounds() {
        return this.getBounds();
    }
}
