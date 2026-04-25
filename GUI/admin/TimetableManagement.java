package GUI.admin;

import Controllers.TimetableControllers.TimetableController;
import Controllers.TimetableControllers.TimetableOperationResult;
import Controllers.TimetableControllers.TimetableRowInput;
import Models.Timetable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimetableManagement extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbLevel, cmbSemester, cmbDept;
    private JButton btnAddRow, btnRemoveRow, btnSaveAll, btnClear;
    private final Map<String, String> deptMap = new HashMap<>();
    private final TimetableController timetableController = new TimetableController();

    public TimetableManagement() {
        deptMap.put("Information & Communication Technology", "D1");
        deptMap.put("Engineering Technology", "D2");
        deptMap.put("Bio-Systems Technology", "D3");
        deptMap.put("Multidisciplinary", "D4");

        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbDept = new JComboBox<>(deptMap.keySet().toArray(new String[0]));
        cmbLevel = new JComboBox<>(new String[]{"Level 1", "Level 2", "Level 3", "Level 4"});
        cmbSemester = new JComboBox<>(new String[]{"Semester 1", "Semester 2"});

        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        topPanel.add(cmbDept, gbc);
        gbc.gridx = 2;
        topPanel.add(new JLabel("Level:"), gbc);
        gbc.gridx = 3;
        topPanel.add(cmbLevel, gbc);
        gbc.gridx = 4;
        topPanel.add(new JLabel("Semester:"), gbc);
        gbc.gridx = 5;
        topPanel.add(cmbSemester, gbc);

        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Day", "Course Code", "Start (HH:mm)", "End (HH:mm)", "Venue", "Type (Theory/Practical)"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bottomPanel.setBackground(Color.WHITE);

        btnAddRow = new JButton("Add Row (+)");
        btnRemoveRow = new JButton("Remove Selected (-)");
        btnSaveAll = new JButton("Save All Timetable");
        btnClear = new JButton("Clear Table");

        btnSaveAll.setBackground(new Color(46, 125, 192));
        btnSaveAll.setForeground(Color.WHITE);
        btnSaveAll.setFont(new Font("SansSerif", Font.BOLD, 13));

        bottomPanel.add(btnAddRow);
        bottomPanel.add(btnRemoveRow);
        bottomPanel.add(btnClear);
        bottomPanel.add(btnSaveAll);

        add(bottomPanel, BorderLayout.SOUTH);

        cmbDept.addActionListener(e -> loadDataToTable());
        cmbLevel.addActionListener(e -> loadDataToTable());
        cmbSemester.addActionListener(e -> loadDataToTable());

        btnAddRow.addActionListener(e -> tableModel.addRow(new Object[]{"Monday", "", "08:30", "10:30", "", "Theory"}));
        btnRemoveRow.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
            }
        });
        btnClear.addActionListener(e -> tableModel.setRowCount(0));
        btnSaveAll.addActionListener(e -> syncData());

        loadDataToTable();
    }

    private void loadDataToTable() {
        String deptId = deptMap.get(String.valueOf(cmbDept.getSelectedItem()));
        String level = String.valueOf(cmbLevel.getSelectedItem());
        String semester = String.valueOf(cmbSemester.getSelectedItem());

        List<Timetable> list = timetableController.loadTimetable(level, semester, deptId);
        tableModel.setRowCount(0);

        for (Timetable timetable : list) {
            tableModel.addRow(new Object[]{
                    timetable.getDay(),
                    timetable.getCourseCode(),
                    timetable.getStartTime(),
                    timetable.getEndTime(),
                    timetable.getVenue(),
                    timetable.getSessionType()
            });
        }

        if (tableModel.getRowCount() == 0) {
            for (int i = 0; i < 5; i++) {
                tableModel.addRow(new Object[]{"Monday", "", "08:30", "10:30", "", "Theory"});
            }
        }
    }

    private void syncData() {
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        String deptId = deptMap.get(String.valueOf(cmbDept.getSelectedItem()));
        String level = String.valueOf(cmbLevel.getSelectedItem());
        String semester = String.valueOf(cmbSemester.getSelectedItem());

        List<TimetableRowInput> rowInputs = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            rowInputs.add(new TimetableRowInput(
                    String.valueOf(tableModel.getValueAt(i, 0)),
                    String.valueOf(tableModel.getValueAt(i, 1)),
                    String.valueOf(tableModel.getValueAt(i, 2)),
                    String.valueOf(tableModel.getValueAt(i, 3)),
                    String.valueOf(tableModel.getValueAt(i, 4)),
                    String.valueOf(tableModel.getValueAt(i, 5))
            ));
        }

        TimetableOperationResult result = timetableController.syncTimetable(rowInputs, level, semester, deptId);
        if (result.isSuccess()) {
            JOptionPane.showMessageDialog(this, result.getMessage());
            loadDataToTable();
        } else {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
