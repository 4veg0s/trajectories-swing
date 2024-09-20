package etu.nic.git.trajectories_swing.display;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

/**
 * Абстрактный класс, являющийся основой для всех компонент-дисплеев в приложении,
 * содержащий повторяющуюся логику
 */
public abstract class AbstractDisplay implements Restorable, Updatable {
    protected final JLabel displayHeader;
    protected final JPanel background;

    /**
     * @param displayHeaderCaption заголовок компоненты-дисплея
     */
    public AbstractDisplay(String displayHeaderCaption) {
        this.displayHeader = new JLabel(displayHeaderCaption);

        // настройка лейбла-заголовка для компонента
        displayHeader.setHorizontalAlignment(SwingConstants.CENTER);
        displayHeader.setAlignmentX(0.5f);
        displayHeader.setFont(new Font(Font.DIALOG, Font.BOLD, 20));

        background = new JPanel(new BorderLayout());
        background.setBorder(new LineBorder(Color.GRAY, 1));
    }

    /**
     * Метод для получения панели, которая содержит все элементы,
     * отображаемые на соответствующем дисплее
     * @return компонент, содержащий все элементы дисплея (на практике - всегда JPanel)
     */
    public JComponent getComponent() {
        return background;
    }
}
