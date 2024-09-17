package etu.nic.git.trajectories_swing.dialogs;


import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReplaceTrajectoryFileDialog {
    private final JDialog dialog;
    private boolean closedOnConfirm;

    /**
     * Диалоговое окно, всплывающее в случае, если пользователь пытается задать файлу с траекторной информацией
     * имя, которое уже есть в приложении
     * @param owner
     */
    public ReplaceTrajectoryFileDialog(Window owner) {
        dialog = new JDialog(owner, "Файл траекторной информации", Dialog.ModalityType.DOCUMENT_MODAL);

        JPanel panelGrid = new JPanel(new GridLayout(2, 1));

        JTextArea dialogPrompt = new JTextArea("Траектория с таким именем уже открыта и имеет изменения.\n" +
                "Хотите перезаписать траекторию с потерей изменений?");
        dialogPrompt.setOpaque(false);
        dialogPrompt.setCursor(null);
        dialogPrompt.setFocusable(false);
        dialogPrompt.setEditable(false);
        dialogPrompt.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));

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

    /**
     * Метод делает видимым диалоговое окно
     * @return true, если диалоговое окно было закрыто кнопкой утверждения, false, если кнопкой отрицания
     */
    public boolean showWithResult() {
        dialog.pack();
        dialog.setVisible(true);
        return isClosedOnConfirm();
    }

    public boolean isClosedOnConfirm() {
        return closedOnConfirm;
    }
}
