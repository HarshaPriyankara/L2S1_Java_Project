package GUI.lecturer;

import Controllers.CourseMaterialControllers.CourseMaterialController;
import Controllers.CourseMaterialControllers.CourseMaterialFormData;
import Controllers.CourseMaterialControllers.CourseMaterialOperationResult;
import Controllers.CourseMaterialControllers.CourseMaterialRow;
import GUI.common.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;

public class AddCourseMaterialPanel extends JPanel {

    private final String lecturerId;
    private final CourseMaterialController courseMaterialController = new CourseMaterialController();
    private JTextField txtTitle;
    private JComboBox<String> cmbCourseCode;
    private JTextField txtUploadedBy;
    private JTextArea txtLink;
    private int selectedMaterialId = -1;
    private final java.util.List<Integer> materialIds = new java.util.ArrayList<>();
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
        panel.add(new JLabel("Material Title:"), gbc);
        gbc.gridx = 1;
        txtTitle = new JTextField(22);
        UITheme.styleTextField(txtTitle);
        panel.add(txtTitle, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Course Code:"), gbc);
        gbc.gridx = 1;
        cmbCourseCode = new JComboBox<>();
        cmbCourseCode.setPreferredSize(new Dimension(220, 32));
        UITheme.styleComboBox(cmbCourseCode);
        panel.add(cmbCourseCode, gbc);
        loadLecturerCourseCodes();

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Uploaded By:"), gbc);
        gbc.gridx = 1;
        txtUploadedBy = new JTextField(lecturerId, 22);
        txtUploadedBy.setEditable(false);
        txtUploadedBy.setBackground(UITheme.SURFACE_MUTED);
        panel.add(txtUploadedBy, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
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
                    Object transferredData = support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    if (transferredData instanceof List<?> files && !files.isEmpty() && files.get(0) instanceof File file) {
                        txtLink.setText(file.getAbsolutePath());
                        return true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });
        panel.add(new JScrollPane(txtLink), gbc);

        JButton btnBrowse = new JButton("Browse");
        styleButton(btnBrowse, UITheme.SURFACE_MUTED);
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(btnBrowse, gbc);

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setBackground(UITheme.SURFACE);
        JButton btnSave = new JButton("Upload Material");
        JButton btnUpdate = new JButton("Update Material");
        JButton btnDelete = new JButton("Delete Material");
        JButton btnClear = new JButton("Clear");
        styleButton(btnSave, BUTTON_COLOR);
        styleButton(btnUpdate, UITheme.SUCCESS);
        styleButton(btnDelete, UITheme.DANGER);
        styleButton(btnClear, CLEAR_BTN_COLOR);
        row.add(btnSave);
        row.add(btnUpdate);
        row.add(btnDelete);
        row.add(btnClear);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(row, gbc);

        btnBrowse.addActionListener(ignored -> browseFile());
        btnSave.addActionListener(ignored -> saveMaterial());
        btnUpdate.addActionListener(ignored -> updateMaterial());
        btnDelete.addActionListener(ignored -> deleteMaterial());
        btnClear.addActionListener(ignored -> clearFields());

        return panel;
    }

    private JScrollPane buildTablePanel() {
        String[] columns = {"Title", "Course Code", "Uploaded At", "File Path"};
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
        materialTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                populateSelectedMaterial();
            }
        });

        return new JScrollPane(materialTable);
    }

    private void saveMaterial() {
        CourseMaterialOperationResult result = courseMaterialController.addMaterial(buildFormData());
        showActionResult(result);
        if (result.isSuccess()) {
            clearFields();
            loadMaterials();
            selectFirstMaterial();
        }
    }

    private void updateMaterial() {
        CourseMaterialOperationResult result = courseMaterialController.updateMaterial(buildFormData());
        showActionResult(result);
        if (result.isSuccess()) {
            clearFields();
            loadMaterials();
        }
    }

    private void deleteMaterial() {
        if (selectedMaterialId < 0) {
            JOptionPane.showMessageDialog(this, "Select a material from the table to delete.");
            return;
        }

        String title = txtTitle.getText().trim();
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete " + title + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        CourseMaterialOperationResult result = courseMaterialController.deleteMaterial(
                selectedMaterialId,
                lecturerId,
                txtLink.getText().trim()
        );
        showActionResult(result);
        if (result.isSuccess()) {
            clearFields();
            loadMaterials();
        }
    }

    private void loadMaterials() {
        tableModel.setRowCount(0);
        materialIds.clear();
        for (CourseMaterialRow row : courseMaterialController.loadMaterialsByLecturer(lecturerId)) {
            materialIds.add(row.getMaterialId());
            tableModel.addRow(new Object[]{
                    row.getTitle(),
                    row.getCourseCode(),
                    row.getUploadedAt(),
                    row.getFilePath()
            });
        }
    }

    private void selectFirstMaterial() {
        if (materialTable.getRowCount() == 0) {
            return;
        }

        materialTable.setRowSelectionInterval(0, 0);
        materialTable.scrollRectToVisible(materialTable.getCellRect(0, 0, true));
    }

    private void populateSelectedMaterial() {
        int selectedRow = materialTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        int row = materialTable.convertRowIndexToModel(selectedRow);

        selectedMaterialId = materialIds.get(row);
        txtTitle.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        cmbCourseCode.setSelectedItem(String.valueOf(tableModel.getValueAt(row, 1)));
        txtLink.setText(String.valueOf(tableModel.getValueAt(row, 3)));
    }

    private void styleButton(JButton btn, Color bg) {
        if (UITheme.SURFACE_MUTED.equals(bg)) {
            UITheme.styleNeutralButton(btn);
        } else if (UITheme.SUCCESS.equals(bg)) {
            UITheme.styleSuccessButton(btn);
        } else if (UITheme.DANGER.equals(bg)) {
            UITheme.styleDangerButton(btn);
        } else {
            UITheme.stylePrimaryButton(btn);
        }
        UITheme.setWideButtonSize(btn);
    }

    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            txtLink.setText(fileChooser.getSelectedFile().getAbsolutePath().replace("\\", "/"));
        }
    }

    private void loadLecturerCourseCodes() {
        cmbCourseCode.removeAllItems();
        for (String courseCode : courseMaterialController.getCourseCodesByLecturer(lecturerId)) {
            cmbCourseCode.addItem(courseCode);
        }
    }

    private String selectedCourseCode() {
        Object selected = cmbCourseCode.getSelectedItem();
        return selected == null ? "" : selected.toString();
    }

    private CourseMaterialFormData buildFormData() {
        return new CourseMaterialFormData(
                selectedMaterialId,
                txtTitle.getText(),
                selectedCourseCode(),
                txtUploadedBy.getText(),
                txtLink.getText()
        );
    }

    private void showActionResult(CourseMaterialOperationResult result) {
        JOptionPane.showMessageDialog(
                this,
                result.getMessage(),
                result.isSuccess() ? "Success" : "Error",
                result.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE
        );
    }

    private void clearFields() {
        selectedMaterialId = -1;
        txtTitle.setText("");
        if (cmbCourseCode.getItemCount() > 0) {
            cmbCourseCode.setSelectedIndex(0);
        }
        txtUploadedBy.setText(lecturerId);
        txtLink.setText("");
        materialTable.clearSelection();
    }
}
