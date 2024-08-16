package etu.nic.git.trajectories_swing.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InvalidFileFormatDialog {
    private final JDialog dialog;

    public InvalidFileFormatDialog(Window owner, Exception exception) {
        dialog = new JDialog(owner, "Неверный формат файла", Dialog.ModalityType.DOCUMENT_MODAL);

        Box verticalBox = new Box(BoxLayout.Y_AXIS);

        String exceptionMessage = exception.getMessage();
        String[] split = exceptionMessage.split("\n");
        int amountOfLines = split.length;
        int lengthOfLongestLine = 0;
        for (String line : split) {
            lengthOfLongestLine = Math.max(lengthOfLongestLine, line.length());
        }

        JTextArea dialogPrompt = new JTextArea(exceptionMessage);
        dialogPrompt.setOpaque(false);
        dialogPrompt.setCursor(null);
        dialogPrompt.setFocusable(false);
        dialogPrompt.setEditable(false);
        Font font12 = new Font(Font.DIALOG, Font.PLAIN, 12);
        dialogPrompt.setFont(font12);

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

        verticalBox.add(new JScrollPane(dialogPrompt));
        verticalBox.add(buttonPanel);

        dialog.add(verticalBox);

        Rectangle rectangleBounds = owner.getBounds();
        dialog.setBounds(new Rectangle(rectangleBounds.x + rectangleBounds.width / 2,
                rectangleBounds.y + rectangleBounds.height / 4,
                lengthOfLongestLine * 10,
                Math.max(amountOfLines, 20) * font12.getSize() + okButton.getHeight() + 20));
    }

    public void show() {
        dialog.setVisible(true);
    }
}
