package GUI.admin;

import javax.swing.*;
import java.awt.*;

// INHERITANCE: Inheriting JDialog features to create a popup Notice Viewer.
public class NoticeViewer extends JDialog {
    
    // ENCAPSULATION: We do not expose internal components directly, only configuring them in constructor.
    public NoticeViewer(Frame parent, String title, String content) {
        super(parent, "Notice Details: " + title, true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false); // Encapsulation of state: Not editable 
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setText(content);

        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        add(btnClose, BorderLayout.SOUTH);
    }
}
