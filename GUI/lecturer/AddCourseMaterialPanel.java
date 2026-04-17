package GUI.lecturer;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.io.File;
import java.awt.datatransfer.DataFlavor;

public class AddCourseMaterialPanel extends JPanel {

    private JTextField txtTitle, txtCourseCode, txtUploadedBy;
    private JTextArea txtLink;
    private static final Color BUTTON_COLOR = new Color(46, 125, 192);
    private static final Color CLEAR_BTN_COLOR = new Color(120, 120, 120);

    public AddCourseMaterialPanel() {
        // Layout and Padding
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // 1. Title
        addTitle("Upload Course Materials", BUTTON_COLOR);

        // 2. Input Fields
        txtTitle      = addField("*Material Title");
        txtCourseCode = addField("*Course Code");
        txtUploadedBy = addField("*Uploaded By (Lecturer ID)");

        // Link Field (Drag and Drop Area)
        addCustomLabel("File URL (Drag and Drop Here)");
        txtLink = new JTextArea(5, 20);
        txtLink.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtLink.setLineWrap(true);
        txtLink.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(txtLink);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Drag and Drop Logic
        txtLink.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }
            @Override
            public boolean importData(TransferSupport support) {
                try {
                    List<File> files = (List<File>) support.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);
                    if (!files.isEmpty()) {
                        txtLink.setText(files.get(0).getAbsolutePath());
                        return true;
                    }
                } catch (Exception ex) { ex.printStackTrace(); }
                return false;
            }
        });

        add(scrollPane);
        add(Box.createVerticalStrut(20));

        // 3. Button Row (Save & Clear)
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton btnSave = new JButton("Upload Material");
        styleButton(btnSave, BUTTON_COLOR);

        JButton btnClear = new JButton("Clear All");
        styleButton(btnClear, CLEAR_BTN_COLOR);

        row.add(btnClear);
        row.add(btnSave);
        add(row);

        // Listeners
        btnSave.addActionListener(e -> saveMaterial());
        btnClear.addActionListener(e -> clearFields());
    }

    private void saveMaterial() {
        String title = txtTitle.getText().trim();
        String cCode = txtCourseCode.getText().trim();
        String uploadedBy = txtUploadedBy.getText().trim();
        String sourcePath = txtLink.getText().trim();

        if (title.isEmpty() || cCode.isEmpty() || uploadedBy.isEmpty() || sourcePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        String newPath = saveFileToFolder(sourcePath);

        if (newPath != null) {
            JOptionPane.showMessageDialog(this, "Material uploaded successfully!");
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "File upload failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Helper Methods ──

    private void addTitle(String text, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        lbl.setForeground(color);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(lbl);
        add(Box.createVerticalStrut(24));
    }

    private void addCustomLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(lbl);
        add(Box.createVerticalStrut(5));
    }

    private JTextField addField(String label) {
        addCustomLabel(label);
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(field);
        add(Box.createVerticalStrut(12));
        return field;
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(150, 38));
    }

    private String saveFileToFolder(String sourcePath) {
        try {
            File sourceFile = new File(sourcePath);
            File directory = new File("D:\\University\\Java project\\CourseMaterial\\");
            if (!directory.exists()) directory.mkdirs();

            File destinationFile = new File(directory, sourceFile.getName());
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return destinationFile.getPath().replace("\\", "/");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void clearFields() {
        txtTitle.setText("");
        txtCourseCode.setText("");
        txtUploadedBy.setText("");
        txtLink.setText("");
    }
}