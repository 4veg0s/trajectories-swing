package etu.nic.git.trajectories_swing;

import etu.nic.git.trajectories_swing.model.TrajectoryRow;
import etu.nic.git.trajectories_swing.model.TrajectoryRowTableModel;
import etu.nic.git.trajectories_swing.tools.FileDataLoader;
import etu.nic.git.trajectories_swing.tools.TrajectoryFile;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

public class AllInOneTest {

    private JFrame frame;
    private JPanel fileDataPanel;
    private JLabel fileNameLabel;
    private JTextArea fileTextArea;
    TrajectoryFile trajectoryFile;

    public static void main(String[] args) {
        new AllInOneTest().buildFrame();
    }
    public JPanel buildTableDisplay() {
        String filePath = "../traject1.txt";

        // fixme: перенести метод в TrajectoryRow
        List<TrajectoryRow> trajectoryRowList = FileDataLoader.parseToTrajectoryRowList(FileDataLoader.loadDataFromFile(filePath));

        TrajectoryRowTableModel tableModel = new TrajectoryRowTableModel(trajectoryRowList);
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                TrajectoryRowTableModel model = (TrajectoryRowTableModel) e.getSource();
                model.sortByTime(); // если было изменение значения ячейки в таблице,
                // то произойдет сортировка по времени для данных модели
                trajectoryFile.setFileData(model.getTableDataInString());
                loadFileDataToArea();
            }
        });

        JTable table = new JTable(tableModel);

        setJTableColumnsWidth(table, 800, 10, 15, 15, 15, 15, 15, 15);
//        table.putClientProperty("terminateEditOnFocusLost", true);


        table.setRowHeight(20);
        table.setSize(1200, 800);

        JPanel tablePanel = new JPanel();
        tablePanel.add(new JScrollPane(table));
        tablePanel.setSize(800, 600);
        return tablePanel;
    }

    public static void setJTableColumnsWidth(JTable table, int tablePreferredWidth,
                                             double... percentages) {
        double total = 0;
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            total += percentages[i];
        }

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth((int) (tablePreferredWidth * (percentages[i] / total)));
        }
    }

    public void buildFrame() {
        this.frame = new JFrame();
        JPanel background = new JPanel(new BorderLayout());
        background.add(BorderLayout.WEST, buildFileDisplay());
        background.add(BorderLayout.CENTER, buildTableDisplay());
        frame.getContentPane().add(background);
        this.frame.setBounds(new Rectangle(100, 100, 1200, 800));
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }

    public JPanel buildFileDisplay() {
        String filePath = "../traject1.txt";

        trajectoryFile = new TrajectoryFile(filePath);

        JPanel fileDisplayPanel = initFileDisplayPanel(trajectoryFile);
        loadFileDataToArea();

        return fileDisplayPanel;
    }

    private JPanel initFileDisplayPanel(TrajectoryFile trajectoryFile) {
        Box header = new Box(BoxLayout.Y_AXIS);

        JLabel fileHeaderLabel = new JLabel("Файл");
        fileHeaderLabel.setAlignmentX(0.5f);
        fileHeaderLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        header.add(fileHeaderLabel);

        JSeparator separator = new JSeparator();
        separator.setOrientation(SwingConstants.HORIZONTAL);
        header.add(separator);

        fileNameLabel = new JLabel(trajectoryFile.getFilePath());
        fileNameLabel.setAlignmentX(0.5f);
        fileNameLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        header.add(fileNameLabel);

        this.fileTextArea = initFileTextArea();
        JScrollPane scrollPane = initFileTextScrollPane(this.fileTextArea);

        JPanel fileDisplayPanel = new JPanel(new BorderLayout());
        fileDisplayPanel.add(BorderLayout.CENTER, scrollPane);
        fileDisplayPanel.add(BorderLayout.NORTH, header);

        return fileDisplayPanel;
    }

    private JTextArea initFileTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setMargin(new Insets(5, 5,5, 5));
        textArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 10));

        return textArea;
    }

    private JScrollPane initFileTextScrollPane(JTextArea textArea) {

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        return scrollPane;
    }

    private void loadFileDataToArea() {
        String text = trajectoryFile.getFileData();
        this.fileTextArea.setText(text);
    }
}
