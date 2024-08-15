package etu.nic.git.trajectories_swing.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReplaceTrajectoryFileDialog {
    private final JDialog dialog;
    private boolean closedOnConfirm;
    public ReplaceTrajectoryFileDialog(Window owner) {
        dialog = new JDialog(owner, "Файл траекторной информации", Dialog.ModalityType.DOCUMENT_MODAL);

        JPanel panelGrid = new JPanel(new GridLayout(2, 1));

        JLabel dialogPrompt = new JLabel("<HTML>Траектория с таким именем уже открыта и имеет изменения.<br>" +
                "Хотите перезаписать траекторию с потерей изменений?");
        dialogPrompt.setHorizontalTextPosition(SwingConstants.LEFT);
        JButton confirmButton = new JButton("Подтвердить");
        JButton cancelButton = new JButton("Отменить");
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

        panelGrid.add(dialogPrompt);
        panelGrid.add(buttonPanel);

        dialog.add(panelGrid);

        Rectangle rectangleBounds = owner.getBounds();
        dialog.setBounds(new Rectangle(rectangleBounds.x + rectangleBounds.width / 2,
                rectangleBounds.y + rectangleBounds.height / 2, 400, 200));
    }

    public boolean showWithResult() {
        dialog.setVisible(true);
        return isClosedOnConfirm();
    }

    public boolean isClosedOnConfirm() {
        return closedOnConfirm;
    }
}
