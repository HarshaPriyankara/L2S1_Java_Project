package GUI.lecturer;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.io.File;
import java.awt.datatransfer.DataFlavor;

public class AddCourseMaterialPanel extends JPanel {

    private JTextField txtMaterialID, txtTitle, txtCourseCode, txtUploadDate;
    private JTextArea txtLink; // JTextArea for file area
    private static final Color BUTTON_COLOR = new Color(46, 125, 192);

    public AddCourseMaterialPanel() {
        // Layout and Padding
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // 1. Title
        addTitle("Upload Course Materials", BUTTON_COLOR);

        // 2. Input Fields
        txtMaterialID = addField("*Material ID");
        txtTitle      = addField("*Material Title");
        txtCourseCode = addField("*Course Code");
        txtUploadDate = addField("Upload Date");
        txtUploadDate.setEditable(false);
        txtUploadDate.setBackground(new Color(245, 245, 245));

        String today = java.time.LocalDate.now().toString();
        txtUploadDate.setText(today);
        // Link Field
        addCustomLabel("File URL (Drag and Drop Here)");

        txtLink = new JTextArea(5, 20);
        txtLink.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtLink.setLineWrap(true);
        txtLink.setWrapStyleWord(true);
        txtLink.setToolTipText("Drag and drop a file here to get the path automatically");

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

        // 3. Button Row
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton btnSave = new JButton("Upload Material");
        styleButton(btnSave, BUTTON_COLOR);

        row.add(btnSave);
        add(row);

        btnSave.addActionListener(e -> saveMaterial());
    }

    private void saveMaterial() {
        String mid = txtMaterialID.getText().trim();
        String title = txtTitle.getText().trim();
        String cCode = txtCourseCode.getText().trim();
        String date = txtUploadDate.getText().trim();
        String sourcePath = txtLink.getText().trim();

        if (mid.isEmpty() || title.isEmpty() || sourcePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty!");
            return;
        }

        String newPath = saveFileToFolder(sourcePath);

        if (newPath != null) {

            JOptionPane.showMessageDialog(this, "File saved to: " + newPath);
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
            if (!directory.exists()) {
                directory.mkdirs(); //if not folder create new
            }

            File destinationFile = new File(directory, sourceFile.getName());

            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return destinationFile.getPath();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void clearFields() {
        txtMaterialID.setText("");
        txtTitle.setText("");
        txtCourseCode.setText("");
        txtLink.setText("");
    }
}