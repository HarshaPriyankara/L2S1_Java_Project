package GUI.admin;

import Controllers.TimetableControllers.TimetableController;
import Controllers.TimetableControllers.TimetableOperationResult;
import Controllers.TimetableControllers.TimetableRowInput;
import Models.Timetable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TimetableManagement extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbDay, cmbCourse, cmbType;
    private JTextField txtStart, txtEnd, txtVenue;
    private JButton btnAddRow, btnRemoveRow, btnSaveAll, btnClear;

    // Fixed Department for BICT
    private final String FIXED_DEPT = "dep01";
    private final TimetableController timetableController = new TimetableController();

    public TimetableManagement() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Header ---
        JLabel lblTitle = new JLabel("BICT TIMETABLE MANAGEMENT - ADMIN PANEL");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(41, 128, 185));
        add(lblTitle, BorderLayout.NORTH);

        // --- Table Setup ---
        String[] columns = {"Day", "Course Code", "Course Name", "Start (HH:mm)", "End (HH:mm)", "Venue", "Type"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; } // Table can not edit at once
        };

        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getColumnModel().getColumn(2).setPreferredWidth(200);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Input Form Panel ---
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 5));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Add New Schedule Entry", 0, 0, new Font("Segoe UI", Font.BOLD, 14), new Color(41, 128, 185)));

        // Input Components
        cmbDay = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});

        // Course List
        cmbCourse = new JComboBox<>(new String[]{
                "ICT2113 - Data Structures and Algorithms",
                "ICT2122 - Object Oriented Programming",
                "ENG2122 - English III",
                "TCS2122 - Soft Skills",
                "ICT2132 - OOP Practical",
                "ICT2142 - OOAD",
                "ICT2152 - E-Commerce"
        });

        txtStart = new JTextField("08:30");
        txtEnd = new JTextField("10:30");
        txtVenue = new JTextField();
        cmbType = new JComboBox<>(new String[]{"Theory", "Practical"});

        // Adding to Form
        formPanel.add(new JLabel("  Day:"));
        formPanel.add(cmbDay);
        formPanel.add(new JLabel("  Course:"));
        formPanel.add(cmbCourse);
        formPanel.add(new JLabel("  Start (HH:mm):"));
        formPanel.add(txtStart);
        formPanel.add(new JLabel("  End (HH:mm):"));
        formPanel.add(txtEnd);
        formPanel.add(new JLabel("  Venue:"));
        formPanel.add(txtVenue);
        formPanel.add(new JLabel("  Type:"));
        formPanel.add(cmbType);

        // --- Buttons Panel ---
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionPanel.setBackground(Color.WHITE);

        btnAddRow = new JButton("Add to List (+)");
        btnRemoveRow = new JButton("Remove Selected (-)");
        btnSaveAll = new JButton("Save All to Database");
        btnClear = new JButton("Clear Table");

        // Styling Buttons
        styleButton(btnAddRow, new Color(52, 152, 219));   // Blue
        styleButton(btnSaveAll, new Color(46, 204, 113)); // Green
        styleButton(btnRemoveRow, new Color(231, 76, 60)); // Red

        actionPanel.add(btnAddRow);
        actionPanel.add(btnRemoveRow);
        actionPanel.add(btnClear);
        actionPanel.add(btnSaveAll);

        // Bottom Container (Form + Buttons)
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.WHITE);
        southPanel.add(formPanel, BorderLayout.NORTH);
        southPanel.add(actionPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        // --- Listeners ---

        // add data to table using form
        btnAddRow.addActionListener(e -> {
            String day = cmbDay.getSelectedItem().toString();
            String fullCourse = cmbCourse.getSelectedItem().toString();

            //code split and divide course and name
            String[] parts = fullCourse.split(" - ");
            String code = parts[0];
            String name = parts[1];

            tableModel.addRow(new Object[]{
                    day, code, name, txtStart.getText(), txtEnd.getText(), txtVenue.getText(), cmbType.getSelectedItem()
            });
        });

        //selected raw delete
        btnRemoveRow.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                tableModel.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to remove.");
            }
        });

        btnClear.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Clear all rows?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
                tableModel.setRowCount(0);
            }
        });

        btnSaveAll.addActionListener(e -> syncData());

        // Initial Load
        loadDataToTable();
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
    }

    private void loadDataToTable() {
        List<Timetable> list = timetableController.loadTimetable(FIXED_DEPT);
        tableModel.setRowCount(0);

        for (Timetable tt : list) {
            tableModel.addRow(new Object[]{
                    tt.getDay(),
                    tt.getCourseCode(),
                    tt.getCourseName(),
                    tt.getStartTime(),
                    tt.getEndTime(),
                    tt.getVenue(),
                    tt.getSessionType()
            });
        }
    }

    private void syncData() {
        if (table.isEditing()) table.getCellEditor().stopCellEditing();

        List<TimetableRowInput> rowInputs = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String code = String.valueOf(tableModel.getValueAt(i, 1)).trim();
            if (code.isEmpty()) continue;

            rowInputs.add(new TimetableRowInput(
                    String.valueOf(tableModel.getValueAt(i, 0)),
                    code,
                    String.valueOf(tableModel.getValueAt(i, 3)), // Start Time
                    String.valueOf(tableModel.getValueAt(i, 4)), // End Time
                    String.valueOf(tableModel.getValueAt(i, 5)), // Venue
                    String.valueOf(tableModel.getValueAt(i, 6))  // Type
            ));
        }

        TimetableOperationResult result = timetableController.syncTimetable(rowInputs, FIXED_DEPT);

        if (result.isSuccess()) {
            JOptionPane.showMessageDialog(this, result.getMessage());
            loadDataToTable(); // Refresh table from DB
        } else {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}