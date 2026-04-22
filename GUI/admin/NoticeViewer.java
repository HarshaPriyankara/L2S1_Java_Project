package GUI.admin;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// INHERITANCE: Inheriting JDialog features to create a popup Notice Viewer.
public class NoticeViewer extends JDialog {
    
    // ENCAPSULATION: We do not expose internal components directly, only configuring them in constructor.
    public NoticeViewer(Frame parent, String title, String filePath) {
        super(parent, "Notice Details: " + title, true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false); // Encapsulation of state: Not editable 
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setMargin(new Insets(10, 10, 10, 10));

        // ABSTRACTION: Reusable mechanism to read generic file content and display it
        readFileContent(filePath, textArea);

        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        add(btnClose, BorderLayout.SOUTH);
    }
    
    // ABSTRACTION: Subdivided file reading operation securely isolated.
    private void readFileContent(String filePath, JTextArea textArea) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n");
            }
        } catch (IOException e) {
            textArea.setText("Error reading notice file: " + e.getMessage());
        }
    }
}
