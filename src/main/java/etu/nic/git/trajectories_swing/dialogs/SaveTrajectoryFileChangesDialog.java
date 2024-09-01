package etu.nic.git.trajectories_swing.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaveTrajectoryFileChangesDialog {
    private final JDialog dialog;
    private boolean closedOnConfirm;
    public SaveTrajectoryFileChangesDialog(Window owner) {
        dialog = new JDialog(owner, "Файл траекторной информации", Dialog.ModalityType.DOCUMENT_MODAL);

        JPanel panelGrid = new JPanel(new GridLayout(2, 1));

        JLabel dialogLabel = new JLabel("<HTML><center>Выбранный файл имеет несохраненные изменения<br>" +
                "Хотите их сохранить перед закрытием?");
        dialogLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dialogLabel.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));

        JButton confirmButton = new JButton("Сохранить");
        JButton cancelButton = new JButton("Не сохранять");
        JPanel buttonPanel = new JPanel();

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closedOnConfirm = true;
                dialog.setVisible(false);
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closedOnConfirm = false;
                dialog.setVisible(false);
            }
        });

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        confirmButton.setHorizontalAlignment(SwingConstants.CENTER);

        panelGrid.add(dialogLabel);
        panelGrid.add(buttonPanel);

        dialog.add(panelGrid);

        Rectangle rectangleBounds = owner.getBounds();
        dialog.setBounds(new Rectangle(rectangleBounds.x + rectangleBounds.width / 2,
                rectangleBounds.y + rectangleBounds.height / 2, 400, 200));
    }

    public boolean showWithResult() {
        dialog.pack();
        dialog.setVisible(true);
        return isClosedOnConfirm();
    }

    public boolean isClosedOnConfirm() {
        return closedOnConfirm;
    }
}
