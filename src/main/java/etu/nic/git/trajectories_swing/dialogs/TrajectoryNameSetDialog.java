package etu.nic.git.trajectories_swing.dialogs;

import etu.nic.git.trajectories_swing.MainFrame;
import etu.nic.git.trajectories_swing.file_handling.TrajectoryFile;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrajectoryNameSetDialog {
    private final MainFrame mainFrame;
    private final JDialog dialog;
    private final JButton okButton;
    private final JTextField trajectoryNameField;
    private boolean closedOnOk;

    /**
     * Диалоговое окно, всплывающее в случае, если пользователь хочет открыть новый файл в приложении
     * Окно служит для задания имени траектории, которое в дальнейшем будет отображено в компоненте каталога
     * @param frame
     */
    public TrajectoryNameSetDialog(MainFrame frame) {
        mainFrame = frame;
        dialog = new JDialog(mainFrame, "Файл траекторной информации", Dialog.ModalityType.DOCUMENT_MODAL);

        JPanel panelGrid = new JPanel(new GridLayout(3, 1));

        JLabel trajectoryNameLabel = new JLabel("Введите название траектории");
        trajectoryNameLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
        trajectoryNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // в текстовое поле устанавливается значение по умолчанию: "Траектория" + индекс, инкрементирующийся при открытии нового файла
        trajectoryNameField = new JTextField(TrajectoryFile.TRAJECTORY_NAME_PREFIX + TrajectoryFile.getNextTrajectoryIndex());

        okButton = new JButton("OK");
        JPanel buttonPanel = new JPanel();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closedOnOk = true;
                dialog.setVisible(false);
            }
        });

        buttonPanel.add(okButton);
        okButton.setHorizontalAlignment(SwingConstants.CENTER);

        panelGrid.add(trajectoryNameLabel);
        panelGrid.add(trajectoryNameField);
        panelGrid.add(buttonPanel);

        dialog.add(panelGrid);

        Rectangle mainFrameBounds = mainFrame.getFrameBounds();
        dialog.setBounds(new Rectangle(mainFrameBounds.x + mainFrameBounds.width / 2,
                mainFrameBounds.y + mainFrameBounds.height / 2, 300, 150));
    }

    /**
     * Метод делает диалоговое окно видимым
     * @return true, если диалог закрылся кнопкой "OK", иначе - false
     */
    public boolean showWithResult() {
        dialog.setVisible(true);
        return closedOnOk;
    }


    public String getTextFieldString() {
        return trajectoryNameField.getText();
    }
    public JDialog getDialog() {
        return dialog;
    }
}
