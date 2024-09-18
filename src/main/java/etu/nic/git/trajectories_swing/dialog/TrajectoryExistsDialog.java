package etu.nic.git.trajectories_swing.dialog;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Dialog;
import java.awt.Rectangle;
import java.awt.Window;

public class TrajectoryExistsDialog {
    private JDialog dialog;

    /**
     * Диалоговое окно, всплывающее в случае, если пользователь пытается открыть новый файл и задает название траектории,
     * которое уже содержится в приложении
     * @param owner
     */
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
