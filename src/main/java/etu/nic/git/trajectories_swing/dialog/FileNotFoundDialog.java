package etu.nic.git.trajectories_swing.dialog;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Dialog;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileNotFoundDialog {
    private final JDialog dialog;

    /**
     * Диалоговое окно для показа диалогового окна с текстом "Файл с таким именем не найден"
     * @param owner
     */
    public FileNotFoundDialog(Window owner) {
        dialog = new JDialog(owner, "Выбор файла", Dialog.ModalityType.DOCUMENT_MODAL);

        JLabel dialogLabel = new JLabel("Файл с таким именем не найден");
        dialogLabel.setHorizontalAlignment(SwingConstants.CENTER);


        Box verticalBox = new Box(BoxLayout.Y_AXIS);

        JButton okButton = new JButton("OK");
        JPanel buttonPanel = new JPanel();

        // слушатель, скрывающий диалоговое окно при нажатии на кнопку
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

    /**
     * Метод показывает диалоговое окно
     */
    public void show() {
        dialog.setVisible(true);
    }
}
