package etu.nic.git.trajectories_swing.display_components;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractDisplay implements Restorable, Updatable {
    protected final JLabel displayHeader;
    protected final JPanel background;
    protected JPanel noFileChosenPanel;
    private final JLabel noFileChosenLabel;

    public AbstractDisplay(String labelCaption) {
        this.displayHeader = new JLabel(labelCaption);
        // настройка лейбла-заголовка для компонента
        displayHeader.setHorizontalAlignment(SwingConstants.CENTER);
        displayHeader.setAlignmentX(0.5f);
        displayHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

        background = new JPanel(new BorderLayout());

        noFileChosenPanel = new JPanel();
        noFileChosenLabel = new JLabel("Файл не выбран");

        noFileChosenPanel.add(BorderLayout.CENTER, noFileChosenLabel);

        background.add(BorderLayout.NORTH, displayHeader);
//        background.add(BorderLayout.CENTER, noFileChosenPanel);
    }

    @Override
    public void updateComponentView() {
//        background.remove(noFileChosenPanel);
    }

    @Override
    public void restoreDefaultState() {
        background.removeAll();
        background.add(BorderLayout.NORTH, displayHeader);
//        background.add(BorderLayout.CENTER, noFileChosenPanel);
    }

    public JComponent getComponent() {
        return background;
    }
}
