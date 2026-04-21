package GUI.admin;

import Models.Timetable;
import DAO.TimetableDAO;
import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTimetable extends JPanel {
    private JTextField txtId, txtVenue;
    private JComboBox<String> cmbLevel, cmbSemester, cmbDay, cmbCourse, cmbDept, cmbType;
    private JComboBox<String> cmbStartH, cmbStartM, cmbEndH, cmbEndM;
    private JButton btnAdd, btnClear, btnBack;
    private TimetableManagement parent;
    private Map<String, String> deptMap = new HashMap<>();

    public AddTimetable(TimetableManagement parent) {
        this.parent = parent;
        deptMap.put("Information & Communication Technology", "D1");
        deptMap.put("Engineering Technology", "D2");
        deptMap.put("Bio-Systems Technology", "D3");
        deptMap.put("Multidisciplinary", "D4");

        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Title ---
        JLabel lblTitle = new JLabel("Add New Timetable Entry");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // --- Fields ---
        addFormField("Timetable ID:", txtId = new JTextField(20), gbc, 1);

        cmbDept = new JComboBox<>(deptMap.keySet().toArray(new String[0]));
        addFormField("Select Department:", cmbDept, gbc, 2);

        cmbLevel = new JComboBox<>(new String[]{"Level 1", "Level 2", "Level 3", "Level 4"});
        addFormField("Level:", cmbLevel, gbc, 3);

        cmbSemester = new JComboBox<>(new String[]{"Semester 1", "Semester 2"});
        addFormField("Semester:", cmbSemester, gbc, 4);

        cmbCourse = new JComboBox<>(new String[]{"-- Select Course --"});
        addFormField("Course Code:", cmbCourse, gbc, 5);

        cmbDay = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});
        addFormField("Day:", cmbDay, gbc, 6);

        cmbType = new JComboBox<>(new String[]{"Theory", "Practical"});
        addFormField("Session Type:", cmbType, gbc, 7);

        // Time Selection Panel
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        timePanel.setBackground(Color.WHITE);
        cmbStartH = new JComboBox<>(generateTimeArray(24));
        cmbStartM = new JComboBox<>(new String[]{"00", "30"});
        cmbEndH = new JComboBox<>(generateTimeArray(24));
        cmbEndM = new JComboBox<>(new String[]{"00", "30"});

        timePanel.add(new JLabel("Start:")); timePanel.add(cmbStartH); timePanel.add(new JLabel(":")); timePanel.add(cmbStartM);
        timePanel.add(new JLabel("  End:")); timePanel.add(cmbEndH); timePanel.add(new JLabel(":")); timePanel.add(cmbEndM);
        addFormField("Time Period:", timePanel, gbc, 8);

        addFormField("Venue:", txtVenue = new JTextField(20), gbc, 9);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(Color.WHITE);
        btnAdd = new JButton("Add Entry");
        btnClear = new JButton("Clear");
        btnBack = new JButton("Back");

        btnPanel.add(btnBack); btnPanel.add(btnClear); btnPanel.add(btnAdd);
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 2; add(btnPanel, gbc);

        // --- Listeners ---
        cmbDept.addActionListener(e -> updateCourseDropdown());
        cmbLevel.addActionListener(e -> updateCourseDropdown());
        cmbSemester.addActionListener(e -> updateCourseDropdown());

        btnAdd.addActionListener(e -> saveEntry());
        btnClear.addActionListener(e -> clearFields());
        btnBack.addActionListener(e -> parent.showMenu());

        updateCourseDropdown();
    }

    private void updateCourseDropdown() {
        if (cmbCourse == null) return;
        cmbCourse.removeAllItems();

        String lvl = cmbLevel.getSelectedItem().toString();
        String sem = cmbSemester.getSelectedItem().toString();
        String deptId = deptMap.get(cmbDept.getSelectedItem().toString());

        List<String> courses = new TimetableDAO().getCoursesByLevelAndSem(lvl, sem, deptId);
        if (courses.isEmpty()) {
            cmbCourse.addItem("-- No Courses Found --");
        } else {
            for (String code : courses) cmbCourse.addItem(code);
        }
    }

    private void saveEntry() {
        if(txtId.getText().isEmpty() || txtVenue.getText().isEmpty() || cmbCourse.getSelectedItem().toString().startsWith("--")) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        Timetable tt = new Timetable();
        tt.setTimeTableId(txtId.getText().trim());
        tt.setDepartmentId(deptMap.get(cmbDept.getSelectedItem().toString()));
        tt.setCourseCode(cmbCourse.getSelectedItem().toString());
        tt.setDay(cmbDay.getSelectedItem().toString());
        tt.setSessionType(cmbType.getSelectedItem().toString());
        tt.setVenue(txtVenue.getText().trim());

        tt.setStartTime(LocalTime.of(Integer.parseInt(cmbStartH.getSelectedItem().toString()), Integer.parseInt(cmbStartM.getSelectedItem().toString())));
        tt.setEndTime(LocalTime.of(Integer.parseInt(cmbEndH.getSelectedItem().toString()), Integer.parseInt(cmbEndM.getSelectedItem().toString())));

        if(tt.createTimeTable()) {
            JOptionPane.showMessageDialog(this, "Entry Added Successfully!");
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Error! Maybe ID already exists.");
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtVenue.setText("");
        updateCourseDropdown();
    }

    private void addFormField(String label, JComponent comp, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y; add(new JLabel(label), gbc);
        gbc.gridx = 1; add(comp, gbc);
    }

    private String[] generateTimeArray(int n) {
        String[] arr = new String[n];
        for(int i=0; i<n; i++) arr[i] = String.format("%02d", i);
        return arr;
    }
}