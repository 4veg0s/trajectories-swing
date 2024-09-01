package etu.nic.git.trajectories_swing.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DefaultOKDialog {
    private final JDialog dialog;

    public DefaultOKDialog(Window owner, String title, String oneLineMessage) {
        dialog = new JDialog(owner, title, Dialog.ModalityType.DOCUMENT_MODAL);

        JLabel dialogLabel = new JLabel(oneLineMessage);
        dialogLabel.setHorizontalAlignment(SwingConstants.CENTER);

//        JPanel background = new JPanel(new GridLayout(2, 1));
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
//        background.add(dialogLabel);
//        background.add(buttonPanel);

//        dialog.add(background);
        dialog.add(verticalBox);

        Rectangle rectangleBounds = owner.getBounds();
        dialog.setBounds(new Rectangle(rectangleBounds.x + rectangleBounds.width / 2,
                rectangleBounds.y + rectangleBounds.height / 2, dialogLabel.getText().length() * 8, 90));
    }

    public void show() {
//        dialog.pack();
        dialog.setVisible(true);
    }
}
