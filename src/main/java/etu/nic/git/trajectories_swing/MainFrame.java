package etu.nic.git.trajectories_swing;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException {
        super();
    }
    public MainFrame(JComponent catalogDisplay, JComponent tableDisplay, JComponent fileDisplay) {
        super("Траектории");
        JPanel background = new JPanel(new BorderLayout());

        // разделитель для гуи
        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.HORIZONTAL);
        // верхняя часть окна с каталогом и таблицей
        Box leftRow = new Box(BoxLayout.Y_AXIS);
        leftRow.add(catalogDisplay);
        leftRow.add(separator);
        leftRow.add(fileDisplay);

        // нижняя часть окна
        Box rightRow = new Box(BoxLayout.Y_AXIS);
        rightRow.add(tableDisplay);
        rightRow.add(separator);
//        TODO: rightRow.add(chartDisplay);

        background.add(BorderLayout.WEST, leftRow);
        background.add(BorderLayout.CENTER, rightRow);

        this.getContentPane().add(background);
        this.setBounds(new Rectangle(100, 100, 1200, 800));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void showMainFrame() {
        this.setVisible(true);
    }
}
