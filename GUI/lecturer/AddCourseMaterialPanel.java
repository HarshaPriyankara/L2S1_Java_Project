package GUI.lecturer;

import DAO.CourseMaterialDAO;
import GUI.common.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class AddCourseMaterialPanel extends JPanel {

    private final String lecturerId;
    private final CourseMaterialDAO courseMaterialDAO = new CourseMaterialDAO();
    private JTextField txtTitle;
    private JTextField txtCourseCode;
    private JTextField txtUploadedBy;
    private JTextArea txtLink;
    private JTextField txtMaterialId;
    private DefaultTableModel tableModel;
    private JTable materialTable;
    private static final Color BUTTON_COLOR = UITheme.PRIMARY;
    private static final Color CLEAR_BTN_COLOR = UITheme.SURFACE_MUTED;

    public AddCourseMaterialPanel(String lecturerId) {
        this.lecturerId = lecturerId;

        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.APP_BACKGROUND);
        setBorder(UITheme.createContentBorder());

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);

        loadMaterials();
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.SURFACE);
        panel.setBorder(UITheme.createSectionBorder("Add / Modify Course Materials"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Material ID:"), gbc);
        gbc.gridx = 1;
        txtMaterialId = new JTextField(12);
        txtMaterialId.setEditable(false);
        txtMaterialId.setBackground(UITheme.SURFACE_MUTED);
        panel.add(txtMaterialId, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Material Title:"), gbc);
        gbc.gridx = 1;
        txtTitle = new JTextField(22);
        UITheme.styleTextField(txtTitle);
        panel.add(txtTitle, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Course Code:"), gbc);
        gbc.gridx = 1;
        txtCourseCode = new JTextField(22);
        UITheme.styleTextField(txtCourseCode);
        panel.add(txtCourseCode, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Uploaded By:"), gbc);
        gbc.gridx = 1;
        txtUploadedBy = new JTextField(lecturerId, 22);
        txtUploadedBy.setEditable(false);
        txtUploadedBy.setBackground(UITheme.SURFACE_MUTED);
        panel.add(txtUploadedBy, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("File Path:"), gbc);
        gbc.gridx = 1;
        txtLink = new JTextArea(4, 22);
        txtLink.setLineWrap(true);
        txtLink.setWrapStyleWord(true);
        UITheme.styleTextArea(txtLink);
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });
        panel.add(new JScrollPane(txtLink), gbc);

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setBackground(UITheme.SURFACE);
        JButton btnSave = new JButton("Upload Material");
        JButton btnUpdate = new JButton("Update Material");
        JButton btnClear = new JButton("Clear");
        styleButton(btnSave, BUTTON_COLOR);
        styleButton(btnUpdate, UITheme.SUCCESS);
        styleButton(btnClear, CLEAR_BTN_COLOR);
        row.add(btnSave);
        row.add(btnUpdate);
        row.add(btnClear);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(row, gbc);

        btnSave.addActionListener(e -> saveMaterial());
        btnUpdate.addActionListener(e -> updateMaterial());
        btnClear.addActionListener(e -> clearFields());

        return panel;
    }

    private JScrollPane buildTablePanel() {
        String[] columns = {"Material ID", "Title", "Course Code", "Uploaded At", "File Path"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        materialTable = new JTable(tableModel);
        materialTable.setRowHeight(28);
        UITheme.styleTable(materialTable);
        materialTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        materialTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateSelectedMaterial();
            }
        });

        return new JScrollPane(materialTable);
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

        String newPath = saveFileToFolder(sourcePath, title);
        if (newPath == null) {
            JOptionPane.showMessageDialog(this, "File upload failed!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isSavedInDB = CourseMaterialDAO.addMaterial(title, cCode, uploadedBy, newPath);
        if (isSavedInDB) {
            JOptionPane.showMessageDialog(this, "Material uploaded.");
            clearFields();
            loadMaterials();
        } else {
            JOptionPane.showMessageDialog(this, "Error! Check if Course Code or Lecturer ID is valid.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMaterial() {
        if (txtMaterialId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a material from the table to update.");
            return;
        }

        String title = txtTitle.getText().trim();
        String cCode = txtCourseCode.getText().trim();
        String sourcePath = txtLink.getText().trim();
        if (title.isEmpty() || cCode.isEmpty() || sourcePath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title, course code, and file path are required.");
            return;
        }

        String finalPath = sourcePath.contains(":") || sourcePath.startsWith(".")
                ? saveFileToFolder(sourcePath, title)
                : sourcePath;
        if (finalPath == null) {
            JOptionPane.showMessageDialog(this, "Unable to save updated file.");
            return;
        }

        boolean updated = courseMaterialDAO.updateMaterial(
                Integer.parseInt(txtMaterialId.getText().trim()),
                title,
                cCode,
                finalPath
        );

        if (updated) {
            JOptionPane.showMessageDialog(this, "Material updated.");
            clearFields();
            loadMaterials();
        } else {
            JOptionPane.showMessageDialog(this, "Material update failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMaterials() {
        tableModel.setRowCount(0);
        for (Object[] row : courseMaterialDAO.getMaterialsByLecturer(lecturerId)) {
            tableModel.addRow(row);
        }
    }

    private void populateSelectedMaterial() {
        int row = materialTable.getSelectedRow();
        if (row < 0) {
            return;
        }

        txtMaterialId.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        txtTitle.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        txtCourseCode.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        txtLink.setText(String.valueOf(tableModel.getValueAt(row, 4)));
    }

    private void styleButton(JButton btn, Color bg) {
        if (UITheme.SURFACE_MUTED.equals(bg)) {
            UITheme.styleNeutralButton(btn);
        } else if (UITheme.SUCCESS.equals(bg)) {
            UITheme.styleSuccessButton(btn);
        } else {
            UITheme.stylePrimaryButton(btn);
        }
        UITheme.setWideButtonSize(btn);
    }

    private String saveFileToFolder(String sourcePath, String title) {
        try {
            File sourceFile = new File(sourcePath);
            if (!sourceFile.exists()) {
                return null;
            }

            String fileName = sourceFile.getName();
            String extension = "";
            int i = fileName.lastIndexOf('.');
            if (i > 0) {
                extension = fileName.substring(i);
            }

            String sanitizedTitle = title.replaceAll("[^a-zA-Z0-9]", "_");
            String newFileName = sanitizedTitle + extension;

            File directory = new File("uploads/course_materials");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File destinationFile = new File(directory, newFileName);
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return destinationFile.getPath().replace("\\", "/");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void clearFields() {
        txtMaterialId.setText("");
        txtTitle.setText("");
        txtCourseCode.setText("");
        txtUploadedBy.setText(lecturerId);
        txtLink.setText("");
        materialTable.clearSelection();
    }
}
