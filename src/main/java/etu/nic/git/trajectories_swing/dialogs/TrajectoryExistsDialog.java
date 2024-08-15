package etu.nic.git.trajectories_swing.dialogs;

import javax.swing.*;
import java.awt.*;

public class TrajectoryExistsDialog {
    private JDialog dialog;
    public TrajectoryExistsDialog(Window owner) {
        dialog = new JDialog(owner, "Выбор файла", Dialog.ModalityType.DOCUMENT_MODAL);

        JLabel dialogLabel = new JLabel("Траектория с таким именем уже загружена");
        dialogLabel.setHorizontalAlignment(SwingConstants.CENTER);

        dialog.add(dialogLabel);
        Rectangle rectangleBounds = owner.getBounds();
        dialog.setBounds(new Rectangle(rectangleBounds.x + rectangleBounds.width / 2,
                rectangleBounds.y + rectangleBounds.height / 2, 300, 100));

    }
    public void show() {
        dialog.setVisible(true);
    }
}
