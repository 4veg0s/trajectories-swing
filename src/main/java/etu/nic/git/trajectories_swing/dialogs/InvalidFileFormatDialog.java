package etu.nic.git.trajectories_swing.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InvalidFileFormatDialog {
    private final JDialog dialog;

    public InvalidFileFormatDialog(Window owner, Exception exception) {
        dialog = new JDialog(owner, "Неверный формат файла", Dialog.ModalityType.DOCUMENT_MODAL);

        JPanel background = new JPanel(
                new BorderLayout()
        );

        String exceptionMessage = exception.getMessage();
        String[] split = exceptionMessage.split("\n");
        int amountOfLines = split.length;
        int lengthOfLongestLine = 0;
        for (String line : split) {
            lengthOfLongestLine = Math.max(lengthOfLongestLine, line.length());
        }

        Font font12 = new Font(Font.DIALOG, Font.PLAIN, 12);
        if (amountOfLines == 1) {
            JLabel dialogPrompt = new JLabel(exceptionMessage);
            dialogPrompt.setHorizontalAlignment(SwingConstants.CENTER);
            dialogPrompt.setFont(font12);

            background.add(BorderLayout.CENTER, dialogPrompt);
        } else {
            JTextArea dialogPrompt = new JTextArea(exceptionMessage);
            dialogPrompt.setOpaque(false);
            dialogPrompt.setCursor(null);
            dialogPrompt.setFocusable(false);
            dialogPrompt.setEditable(false);
            dialogPrompt.setMargin(new Insets(5, 5, 5, 5));
            dialogPrompt.setFont(font12);

            JScrollPane scrollPane = new JScrollPane(dialogPrompt);
            scrollPane.setBorder(null);
            background.add(BorderLayout.CENTER, scrollPane);
        }
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

        background.add(BorderLayout.SOUTH, buttonPanel);

        dialog.add(background);

        Rectangle rectangleBounds = owner.getBounds();
        dialog.setBounds(new Rectangle(rectangleBounds.x + rectangleBounds.width / 2,
                rectangleBounds.y + rectangleBounds.height / 4,
                lengthOfLongestLine * 10,
                Math.min(Math.max(amountOfLines, 6) * font12.getSize() + okButton.getHeight() + 30, 600))   // высота диалогового окна не более 600 пикселей
        );
    }

    public void show() {
        dialog.setVisible(true);
    }
}
