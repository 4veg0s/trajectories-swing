package etu.nic.git.trajectories_swing.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileNotFoundDialog {
    private final JDialog dialog;

    public FileNotFoundDialog(Window owner) {
        dialog = new JDialog(owner, "Выбор файла", Dialog.ModalityType.DOCUMENT_MODAL);

        JLabel dialogLabel = new JLabel("Файл с таким именем не найден");
        dialogLabel.setHorizontalAlignment(SwingConstants.CENTER);


        Box verticalBox = new Box(BoxLayout.Y_AXIS);


        JButton okButton = new JButton("OK");
        JPanel buttonPanel = new JPanel();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });

        buttonPanel.add(okButton);
        okButton.setHorizontalAlignment(SwingConstants.CENTER);

        verticalBox.add(dialogLabel);
        verticalBox.add(buttonPanel);

        dialog.add(verticalBox);

        Rectangle rectangleBounds = owner.getBounds();
        dialog.setBounds(new Rectangle(rectangleBounds.x + rectangleBounds.width / 2,
                rectangleBounds.y + rectangleBounds.height / 2, 300, 100));
    }

    public void show() {
        dialog.setVisible(true);
    }
}
