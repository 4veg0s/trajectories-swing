package etu.nic.git.trajectories_swing;

import etu.nic.git.trajectories_swing.display_components.CatalogDisplay;
import etu.nic.git.trajectories_swing.display_components.ChartDisplay;
import etu.nic.git.trajectories_swing.display_components.FileDisplay;
import etu.nic.git.trajectories_swing.display_components.TableDisplay;
import etu.nic.git.trajectories_swing.menus.TopMenuBar;
import etu.nic.git.trajectories_swing.tools.GridBagLayoutConstraints;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Rectangle;


/**
 * Главное окно приложения
 */
public class MainFrame extends JFrame {
    private static final String FRAME_TITLE = "Траектории";

    public MainFrame() throws HeadlessException {
        super();
    }

    /**
     * Создает объект фрейма и добавляет на него дисплеи и меню
     *
     * @param menuBar        меню-бар
     * @param catalogDisplay компонента отображающая каталог
     * @param tableDisplay   компонента отображающая таблицу
     * @param fileDisplay    компонента отображающая файл
     * @param chartDisplay   компонента отображающая график
     */
    public MainFrame(TopMenuBar menuBar, CatalogDisplay catalogDisplay, TableDisplay tableDisplay, FileDisplay fileDisplay, ChartDisplay chartDisplay) {
        super("Траектории");

        this.setJMenuBar(menuBar.getMenuBar());

        JPanel background = new JPanel(new GridBagLayout());

        background.add(catalogDisplay.getComponent(), GridBagLayoutConstraints.catalogDisplayConstraints());
        background.add(tableDisplay.getComponent(), GridBagLayoutConstraints.tableDisplayConstraints());
        background.add(fileDisplay.getComponent(), GridBagLayoutConstraints.fileDisplayConstraints());
        background.add(chartDisplay.getComponent(), GridBagLayoutConstraints.chartDisplayConstraints());

        this.getContentPane().add(background);
        this.setBounds(new Rectangle(100, 50, 1400, 900));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void showMainFrame() {
        this.setVisible(true);
    }

    /**
     * Добавление имени траектории к заголовку окна, если оно не null, иначе - возвращает к дефолтному состоянию
     *
     * @param name имя траектории
     */
    public void appendFileToFrameTitle(String name) {
        if (name != null) {
            this.setTitle(FRAME_TITLE + " - " + name);
        } else {
            restoreTitle();
        }
    }

    /**
     * Восстанавливает дефолтное значение заголовка окна
     */
    public void restoreTitle() {
        this.setTitle(FRAME_TITLE);
    }

    /**
     * Получает границы отображаемого главного окна приложения
     *
     * @return границы
     */
    public Rectangle getFrameBounds() {
        return this.getBounds();
    }
}
