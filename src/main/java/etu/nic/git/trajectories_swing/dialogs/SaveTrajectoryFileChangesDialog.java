package etu.nic.git.trajectories_swing.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveTrajectoryFileChangesDialog {
    public static final int EXIT_ON_SAVE = 1;
    public static final int EXIT_ON_DONT_SAVE = 2;
    public static final int EXIT_ON_CANCEL = 0;
    private final JDialog dialog;
    private boolean closedOnConfirm;
    private int closingResult = EXIT_ON_CANCEL;
    public SaveTrajectoryFileChangesDialog(Window owner) {
        dialog = new JDialog(owner, "Файл траекторной информации", Dialog.ModalityType.DOCUMENT_MODAL);

        JPanel panelGrid = new JPanel(new GridLayout(2, 1));

        JLabel dialogLabel = new JLabel("<HTML><center>Выбранный файл имеет несохраненные изменения<br>" +
                "Хотите их сохранить перед закрытием?");
        dialogLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dialogLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));

        JButton saveButton = new JButton("Сохранить");
        JButton dontSaveButton = new JButton("Не сохранять");
        JPanel buttonPanel = new JPanel();

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setClosingResult(EXIT_ON_SAVE);
                closedOnConfirm = true;
                dialog.setVisible(false);
            }
        });
        dontSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setClosingResult(EXIT_ON_DONT_SAVE);
                closedOnConfirm = false;
                dialog.setVisible(false);
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(dontSaveButton);
        saveButton.setHorizontalAlignment(SwingConstants.CENTER);

        panelGrid.add(dialogLabel);
        panelGrid.add(buttonPanel);

        dialog.add(panelGrid);

        Rectangle rectangleBounds = owner.getBounds();
        dialog.setBounds(new Rectangle(rectangleBounds.x + rectangleBounds.width / 2,
                rectangleBounds.y + rectangleBounds.height / 2, 400, 200));
    }

    public int showWithResult() {
        dialog.pack();
        dialog.setVisible(true);
        return getClosingResult();
    }

    public boolean isClosedOnConfirm() {
        return closedOnConfirm;
    }

    public int getClosingResult() {
        return closingResult;
    }

    public void setClosingResult(int closingResult) {
        this.closingResult = closingResult;
    }
}
