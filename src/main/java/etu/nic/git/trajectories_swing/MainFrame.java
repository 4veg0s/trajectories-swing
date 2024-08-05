package etu.nic.git.trajectories_swing;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException {
        super();
    }
    public MainFrame(JComponent tableDisplay, JComponent fileDisplay) {
        super("Траектории");
        JPanel background = new JPanel(new BorderLayout());
        background.add(BorderLayout.WEST, fileDisplay); // fileDisplay
        background.add(BorderLayout.CENTER, tableDisplay);   // tableDisplay
        this.getContentPane().add(background);
        this.setBounds(new Rectangle(100, 100, 1200, 800));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void showMainFrame() {
        this.setVisible(true);
    }
}
