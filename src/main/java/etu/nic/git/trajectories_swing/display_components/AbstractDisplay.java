package etu.nic.git.trajectories_swing.display_components;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public abstract class AbstractDisplay implements Restorable, Updatable {
    protected final JLabel displayHeader;
    protected final JPanel background;

    public AbstractDisplay(String displayHeaderCaption) {
        this.displayHeader = new JLabel(displayHeaderCaption);

        // настройка лейбла-заголовка для компонента
        displayHeader.setHorizontalAlignment(SwingConstants.CENTER);
        displayHeader.setAlignmentX(0.5f);
        displayHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

        background = new JPanel(new BorderLayout());
        background.setBorder(new LineBorder(Color.GRAY, 1));
    }

    public JComponent getComponent() {
        return background;
    }
}
