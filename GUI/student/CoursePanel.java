package GUI.student;

import DAO.CourseDAO;
import DAO.CourseMaterialDAO;
import GUI.common.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class CoursePanel extends JPanel {
    private final String studentID;
    private final CourseDAO courseDAO = new CourseDAO();
    private final CourseMaterialDAO courseMaterialDAO = new CourseMaterialDAO();
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JLabel lblStatus;

    public CoursePanel(String studentID) {
        this.studentID = studentID;
        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.APP_BACKGROUND);
        setBorder(UITheme.createContentBorder());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildStatusLabel(), BorderLayout.SOUTH);

        loadStudentData();
    }

    private JLabel buildHeader() {
        JLabel lblTitle = UITheme.createSectionTitle("My Enrolled Courses");
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        return lblTitle;
    }

    private JScrollPane buildTablePanel() {
        String[] columns = {"Course Code", "Course Name", "Type", "Credits", "Lecturer", "Materials"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        courseTable = new JTable(tableModel);
        courseTable.setRowHeight(32);
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UITheme.styleTable(courseTable);
        courseTable.getColumnModel().getColumn(5).setCellRenderer(new ActionButtonRenderer());
        courseTable.getColumnModel().getColumn(5).setCellEditor(new ActionButtonEditor(courseTable));

        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBorder(UITheme.createSectionBorder("Course Details"));
        return scrollPane;
    }

    private JLabel buildStatusLabel() {
        lblStatus = new JLabel("", SwingConstants.CENTER);
        lblStatus.setFont(new Font("SansSerif", Font.ITALIC, 14));
        lblStatus.setForeground(UITheme.TEXT_MUTED);
        return lblStatus;
    }

    private void loadStudentData() {
        try {
            List<String[]> data = courseDAO.getStudentCourses(studentID);
            tableModel.setRowCount(0);

            if (data == null || data.isEmpty()) {
                courseTable.setVisible(false);
                lblStatus.setText("You haven't registered for any courses yet.");
            } else {
                courseTable.setVisible(true);
                lblStatus.setText("");
                for (String[] row : data) {
                    tableModel.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], "View"});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            lblStatus.setForeground(UITheme.DANGER);
            lblStatus.setText("Error loading data: " + e.getMessage());
        }
    }

    private void showMaterialsDialog(String courseCode) {
        List<Object[]> materials = courseMaterialDAO.getMaterialsByCourse(courseCode);
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Course Materials - " + courseCode, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout(12, 12));
        dialog.getContentPane().setBackground(UITheme.APP_BACKGROUND);
        ((JComponent) dialog.getContentPane()).setBorder(UITheme.createContentBorder());

        JLabel title = UITheme.createSectionTitle("Course Materials - " + courseCode);
        dialog.add(title, BorderLayout.NORTH);

        if (materials.isEmpty()) {
            JLabel emptyLabel = new JLabel("No materials uploaded for this course yet.", SwingConstants.CENTER);
            emptyLabel.setForeground(UITheme.TEXT_MUTED);
            emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            dialog.add(emptyLabel, BorderLayout.CENTER);
        } else {
            DefaultTableModel materialModel = new DefaultTableModel(new String[]{"Title", "Uploaded At", "File Path", "Open"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 3;
                }
            };
            JTable materialTable = new JTable(materialModel);
            materialTable.setRowHeight(32);
            UITheme.styleTable(materialTable);
            for (Object[] material : materials) {
                materialModel.addRow(new Object[]{material[0], material[1], material[2], "Open"});
            }
            materialTable.getColumnModel().getColumn(3).setCellRenderer(new ActionButtonRenderer());
            materialTable.getColumnModel().getColumn(3).setCellEditor(new OpenMaterialEditor(materialTable));
            dialog.add(new JScrollPane(materialTable), BorderLayout.CENTER);
        }

        JButton closeButton = new JButton("Close");
        UITheme.styleNeutralButton(closeButton);
        UITheme.setStandardButtonSize(closeButton);
        closeButton.addActionListener(e -> dialog.dispose());
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(UITheme.APP_BACKGROUND);
        footer.add(closeButton);
        dialog.add(footer, BorderLayout.SOUTH);

        dialog.setSize(760, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void openMaterialFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "File path is empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "File not found: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Desktop.getDesktop().open(file);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to open file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class ActionButtonRenderer extends JButton implements TableCellRenderer {
        ActionButtonRenderer() {
            UITheme.stylePrimaryButton(this);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setText(value == null ? "View" : value.toString());
            return this;
        }
    }

    private class ActionButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JButton button = new JButton("View");
        private final JTable table;
        private int row;

        ActionButtonEditor(JTable table) {
            this.table = table;
            UITheme.stylePrimaryButton(button);
            button.addActionListener(e -> {
                fireEditingStopped();
                int modelRow = table.convertRowIndexToModel(row);
                String courseCode = String.valueOf(tableModel.getValueAt(modelRow, 0));
                showMaterialsDialog(courseCode);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            button.setText(value == null ? "View" : value.toString());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }

    private class OpenMaterialEditor extends AbstractCellEditor implements TableCellEditor {
        private final JButton button = new JButton("Open");
        private final JTable table;
        private int row;

        OpenMaterialEditor(JTable table) {
            this.table = table;
            UITheme.stylePrimaryButton(button);
            button.addActionListener(e -> {
                fireEditingStopped();
                int modelRow = table.convertRowIndexToModel(row);
                String filePath = String.valueOf(table.getModel().getValueAt(modelRow, 2));
                openMaterialFile(filePath);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            button.setText(value == null ? "Open" : value.toString());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }
}
