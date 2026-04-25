package GUI.admin;

import Models.Timetable;
import DAO.TimetableDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimetableManagement extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbLevel, cmbSemester, cmbDept;
    private JButton btnAddRow, btnRemoveRow, btnSaveAll, btnClear;
    private Map<String, String> deptMap = new HashMap<>();

    public TimetableManagement() {
        // Department mapping
        deptMap.put("Information & Communication Technology", "D1");
        deptMap.put("Engineering Technology", "D2");
        deptMap.put("Bio-Systems Technology", "D3");
        deptMap.put("Multidisciplinary", "D4");

        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Top Panel: Selectors ---
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbDept = new JComboBox<>(deptMap.keySet().toArray(new String[0]));
        cmbLevel = new JComboBox<>(new String[]{"Level 1", "Level 2", "Level 3", "Level 4"});
        cmbSemester = new JComboBox<>(new String[]{"Semester 1", "Semester 2"});

        gbc.gridx = 0; gbc.gridy = 0; topPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1; topPanel.add(cmbDept, gbc);
        gbc.gridx = 2; topPanel.add(new JLabel("Level:"), gbc);
        gbc.gridx = 3; topPanel.add(cmbLevel, gbc);
        gbc.gridx = 4; topPanel.add(new JLabel("Semester:"), gbc);
        gbc.gridx = 5; topPanel.add(cmbSemester, gbc);

        add(topPanel, BorderLayout.NORTH);

        // --- Middle Panel: Table ---
        String[] columns = {"Day", "Course Code", "Start (HH:mm)", "End (HH:mm)", "Venue", "Type (Theory/Practical)"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // --- Bottom Panel: Buttons ---
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
            if (selectedRow != -1) tableModel.removeRow(selectedRow);
        });

        btnClear.addActionListener(e -> tableModel.setRowCount(0));
        btnSaveAll.addActionListener(e -> syncData());

        loadDataToTable();
    }

    private void loadDataToTable() {
        String dId = deptMap.get(cmbDept.getSelectedItem().toString());
        String lvl = cmbLevel.getSelectedItem().toString();
        String sem = cmbSemester.getSelectedItem().toString();

        List<Timetable> list = new TimetableDAO().getFiltered(lvl, sem, dId);
        tableModel.setRowCount(0);

        for (Timetable tt : list) {
            tableModel.addRow(new Object[]{
                    tt.getDay(),
                    tt.getCourseCode(),
                    tt.getStartTime(),
                    tt.getEndTime(),
                    tt.getVenue(),
                    tt.getSessionType()
            });
        }

        if (tableModel.getRowCount() == 0) {
            for (int i = 0; i < 5; i++) {
                tableModel.addRow(new Object[]{"Monday", "", "08:30", "10:30", "", "Theory"});
            }
        }
    }

    private void syncData() {
        if (table.isEditing()) table.getCellEditor().stopCellEditing();

        String dId = deptMap.get(cmbDept.getSelectedItem().toString());
        String lvl = cmbLevel.getSelectedItem().toString();
        String sem = cmbSemester.getSelectedItem().toString();

        List<Timetable> newList = new ArrayList<>();
        try {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String code = tableModel.getValueAt(i, 1).toString().trim();
                if (code.isEmpty()) continue;

                Timetable tt = new Timetable();

                // --- අයින් කළා: ID එක ජාවා වලින් සෙට් කරන පේළිය ---
                // දැන් ඩේටාබේස් එක මේක AUTO_INCREMENT හරහා බලාගන්නවා.

                tt.setCourseCode(code);
                tt.setDay(tableModel.getValueAt(i, 0).toString());

                // වෙලාවල තිත (.) තිබුණොත් ඒක තිත් දෙක (:) බවට පත් කරමු
                String startStr = tableModel.getValueAt(i, 2).toString().replace(".", ":");
                String endStr = tableModel.getValueAt(i, 3).toString().replace(".", ":");

                tt.setStartTime(LocalTime.parse(startStr));
                tt.setEndTime(LocalTime.parse(endStr));

                tt.setVenue(tableModel.getValueAt(i, 4).toString());
                tt.setSessionType(tableModel.getValueAt(i, 5).toString());
                tt.setDepartmentId(dId);
                newList.add(tt);
            }

            // DB එකට යැවීම
            if (Timetable.syncFullTimetable(newList, lvl, sem, dId)) {
                JOptionPane.showMessageDialog(this, "Timetable Updated Successfully!");
                loadDataToTable();
            } else {
                JOptionPane.showMessageDialog(this, "Update Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Check Time Formats (HH:mm)!", "Input Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}