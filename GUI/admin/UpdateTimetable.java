/*
package GUI.admin;

import Models.Timetable;
import DAO.TimetableDAO;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateTimetable extends JPanel {
    private JTextField txtId, txtVenue;
    private JComboBox<String> cmbLevel, cmbSemester, cmbDay, cmbCourse, cmbDept;
    private JButton btnSearch, btnUpdate, btnClear, btnBack;
    private Map<String, String> deptMap = new HashMap<>();
    private Timetable currentRecord = null;
    private boolean isSearching = false;
    private TimetableManagement parent;

    public UpdateTimetable(TimetableManagement parent) {
        this.parent = parent;
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());

        deptMap.put("Information & Communication Technology", "D1");
        deptMap.put("Engineering Technology", "D2");
        deptMap.put("Bio-Systems Technology", "D3");
        deptMap.put("Multidisciplinary Technology (Common)", "D4");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- 1. UI Components Initialize කිරීම (Listeners වලට කලින් මෙය කළ යුතුයි) ---
        txtId = new JTextField(15);
        txtVenue = new JTextField(20);
        cmbDept = new JComboBox<>(deptMap.keySet().toArray(new String[0]));
        cmbLevel = new JComboBox<>(new String[]{"Level 1", "Level 2", "Level 3", "Level 4"});
        cmbSemester = new JComboBox<>(new String[]{"Semester 1", "Semester 2"});
        cmbDay = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});
        cmbCourse = new JComboBox<>(new String[]{"-- Select Course --"});

        btnSearch = new JButton("Search ID");
        btnUpdate = new JButton("Update Record");
        btnUpdate.setEnabled(false);
        btnClear = new JButton("Clear");
        btnBack = new JButton("Back");

        // --- 2. Layout එකට එකතු කිරීම ---
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Timetable ID:"), gbc);
        gbc.gridx = 1; add(txtId, gbc);
        gbc.gridx = 2; add(btnSearch, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Department:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; add(cmbDept, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Level:"), gbc);
        gbc.gridx = 1; add(cmbLevel, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Semester:"), gbc);
        gbc.gridx = 1; add(cmbSemester, gbc);

        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Course Code:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; add(cmbCourse, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 5; add(new JLabel("Day:"), gbc);
        gbc.gridx = 1; add(cmbDay, gbc);

        gbc.gridx = 0; gbc.gridy = 6; add(new JLabel("Venue:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; add(txtVenue, gbc);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnBack);
        btnPanel.add(btnClear);
        btnPanel.add(btnUpdate);
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 3; add(btnPanel, gbc);

        // --- 3. Listeners (Initialize කර අවසන් වූ පසු පමණක්) ---
        cmbDept.addActionListener(e -> { if(!isSearching) updateCourseDropdown(); });
        cmbLevel.addActionListener(e -> { if(!isSearching) updateCourseDropdown(); });
        cmbSemester.addActionListener(e -> { if(!isSearching) updateCourseDropdown(); });

        btnSearch.addActionListener(e -> searchRecord());
        btnUpdate.addActionListener(e -> updateEntry());
        btnClear.addActionListener(e -> clearFields());
        btnBack.addActionListener(e -> parent.showMenu());

        updateCourseDropdown();
    }

    private void searchRecord() {
        String id = txtId.getText().trim();
        if(id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter ID to search!");
            return;
        }

        isSearching = true;
        currentRecord = new TimetableDAO().searchTimetableById(id);

        if (currentRecord != null) {
            for (Map.Entry<String, String> entry : deptMap.entrySet()) {
                if (entry.getValue().equals(currentRecord.getDepartmentId())) {
                    cmbDept.setSelectedItem(entry.getKey()); break;
                }
            }
            // level සහ semester format එක ගලපා ගැනීම (උදා: "2" -> "Level 2")
            cmbLevel.setSelectedItem("Level " + currentRecord.getCourseCode().charAt(3));
            cmbSemester.setSelectedItem("Semester " + currentRecord.getCourseCode().charAt(4));

            updateCourseDropdown();
            cmbCourse.setSelectedItem(currentRecord.getCourseCode());
            cmbDay.setSelectedItem(currentRecord.getDay());
            txtVenue.setText(currentRecord.getVenue());
            btnUpdate.setEnabled(true);
            txtId.setEditable(false);
        } else {
            JOptionPane.showMessageDialog(this, "No record found!");
            btnUpdate.setEnabled(false);
        }
        isSearching = false;
    }

    private void updateCourseDropdown() {
        if (cmbCourse == null) return;
        cmbCourse.removeAllItems();
        String deptId = deptMap.get(cmbDept.getSelectedItem().toString());
        List<String> courses = new TimetableDAO().getCoursesByLevelAndSem(
                cmbLevel.getSelectedItem().toString(),
                cmbSemester.getSelectedItem().toString(),
                deptId
        );
        if(courses.isEmpty()) {
            cmbCourse.addItem("-- No Courses Found --");
        } else {
            for (String c : courses) cmbCourse.addItem(c);
        }
    }

    private void updateEntry() {
        if(currentRecord == null) return;

        currentRecord.setDay(cmbDay.getSelectedItem().toString());
        currentRecord.setVenue(txtVenue.getText().trim());
        currentRecord.setCourseCode(cmbCourse.getSelectedItem().toString());
        currentRecord.setDepartmentId(deptMap.get(cmbDept.getSelectedItem().toString()));

        if (currentRecord.updateTimeTable()) {
            JOptionPane.showMessageDialog(this, "Timetable Updated Successfully!");
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Update Failed!");
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtId.setEditable(true);
        txtVenue.setText("");
        btnUpdate.setEnabled(false);
        currentRecord = null;
        updateCourseDropdown();
    }
}
*/
