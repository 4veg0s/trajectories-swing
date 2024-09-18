package etu.nic.git.trajectories_swing.dialog;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InvalidFileFormatDialog {
    private final JDialog dialog;

    /**
     * Диалоговое окно для отображения информации о неверном формате файла (ошибки в данных или пустой файл)
     * Предназначен для работы с многострочной информацией
     * @param owner
     * @param exception исключение, сообщение из которого выводится в диалоговое окно
     */
    public InvalidFileFormatDialog(Window owner, Exception exception) {
        dialog = new JDialog(owner, "Неверный формат файла", Dialog.ModalityType.DOCUMENT_MODAL);

        JPanel background = new JPanel(
                new BorderLayout()
        );

        String exceptionMessage = exception.getMessage();
        String[] split = exceptionMessage.split("\n");  // разделение сообщения из эксепшена на строки
        int amountOfLines = split.length;   // получение количества строк в ошибке
        int lengthOfLongestLine = 0;    // в этой переменной окажется длина максимально длинной строки
        for (String line : split) {
            lengthOfLongestLine = Math.max(lengthOfLongestLine, line.length());
        }

        Font font12 = new Font(Font.DIALOG, Font.PLAIN, 12);
        if (amountOfLines == 1) {   // если неверный формат файла = файл пуст = однострочное сообщение
            JLabel dialogPrompt = new JLabel(exceptionMessage);
            dialogPrompt.setHorizontalAlignment(SwingConstants.CENTER);
            dialogPrompt.setFont(font12);

            background.add(BorderLayout.CENTER, dialogPrompt);
        } else {    // если сообщение многострочное
            JTextArea dialogPrompt = new JTextArea(exceptionMessage);   // записываем его в текстовую область,
            dialogPrompt.setOpaque(false);  // которую превращаем в компонент отображения информации без возможности редактирования
            dialogPrompt.setCursor(null);
            dialogPrompt.setFocusable(false);
            dialogPrompt.setEditable(false);
            dialogPrompt.setMargin(new Insets(5, 5, 5, 5));
            dialogPrompt.setFont(font12);

            JScrollPane scrollPane = new JScrollPane(dialogPrompt);
            scrollPane.setBorder(null);     // удаление границы у скролл-панели
            background.add(BorderLayout.CENTER, scrollPane);
        }
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

        background.add(BorderLayout.SOUTH, buttonPanel);

        dialog.add(background);

        // получение границ окна-владельца для этого диалога (на практике - mainFrame'а
        Rectangle rectangleBounds = owner.getBounds();
        dialog.setBounds(new Rectangle(rectangleBounds.x + rectangleBounds.width / 2,
                rectangleBounds.y + rectangleBounds.height / 4,
                lengthOfLongestLine * 10,
                Math.min(Math.max(amountOfLines, 6) * font12.getSize() + okButton.getHeight() + 30, 600))   // высота диалогового окна не более 600 пикселей
        );
    }

    /**
     * Метод показывает диалоговое окно
     */
    public void show() {
        dialog.setVisible(true);
    }
}
