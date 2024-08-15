package etu.nic.git.trajectories_swing.dialogs;

import etu.nic.git.trajectories_swing.MainFrame;

import javax.swing.*;
import java.awt.*;

public class FileExceptionDialog {
    private final JDialog dialog;

    public FileExceptionDialog(Window owner, Exception exception) {
        dialog = new JDialog(owner, "Выбор файла", Dialog.ModalityType.DOCUMENT_MODAL);

        JLabel dialogLabel = new JLabel(exception.getMessage());
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
