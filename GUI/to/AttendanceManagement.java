/*package GUI.to;

import javax.swing.*;
import java.awt.*;

public class AttendanceManagement extends JPanel {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer;

    public AttendanceManagement(TechnicalOfficerDashboard dashboard) {
        setLayout(new BorderLayout());
        mainContainer = new JPanel(cardLayout);

        // 1. panels are add CardLayout
        mainContainer.add(createMenuPanel(), "AttendanceMenu");
        mainContainer.add(new AddAttendancePanel(cardLayout, mainContainer), "AddForm");
        mainContainer.add(new UpdateAttendancePanel(cardLayout, mainContainer), "UpdateForm");
        mainContainer.add(new DeleteAttendancePanel(cardLayout, mainContainer), "DeleteForm");

        add(mainContainer, BorderLayout.CENTER);
        cardLayout.show(mainContainer, "AttendanceMenu");
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(20, 0, 20, 0);

        //button
        JButton btnAdd = createBigButton("Add New Attendance");
        JButton btnUpdate = createBigButton("Update Attendance Record");
        JButton btnDelete = createBigButton("Delete Attendance Record"); // මේ බටන් එක අලුතින් එක් කළා

        //get page
        btnAdd.addActionListener(e -> cardLayout.show(mainContainer, "AddForm"));
        btnUpdate.addActionListener(e -> cardLayout.show(mainContainer, "UpdateForm"));
        btnDelete.addActionListener(e -> cardLayout.show(mainContainer, "DeleteForm"));

        panel.add(btnAdd, gbc);
        panel.add(btnUpdate, gbc);
        panel.add(btnDelete, gbc); //set button for panel
        return panel;
    }

    private JButton createBigButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(600, 100));
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}*/
package GUI.to;

import Utils.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import java.time.LocalDate;

public class AttendanceManagement extends JPanel {
    private JComboBox<String> courseComboBox;
    private JComboBox<String> typeComboBox; // Theory or Practical
    private JTextField dateField; // දිනය ඇතුළත් කිරීමට (YYYY-MM-DD)
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private String lecturerID;

    public AttendanceManagement(String lecturerID) {
        this.lecturerID = lecturerID;
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Top Panel: Selection ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        courseComboBox = new JComboBox<>();
        loadLecturerCourses();

        typeComboBox = new JComboBox<>(new String[]{"Theory", "Practical"});

        // Default දිනය අද දිනය ලෙස සකසයි
        dateField = new JTextField(LocalDate.now().toString(), 10);

        JButton btnLoad = new JButton("Load Students");
        styleButton(btnLoad, new Color(46, 125, 192));

        topPanel.add(new JLabel("Course:"));
        topPanel.add(courseComboBox);
        topPanel.add(new JLabel("Type:"));
        topPanel.add(typeComboBox);
        topPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        topPanel.add(dateField);
        topPanel.add(btnLoad);
        add(topPanel, BorderLayout.NORTH);

        // --- Table Panel ---
        // Status තීරුව JComboBox එකක් ලෙස සකස් කරමු (Present/Absent)
        String[] columns = {"Student ID", "Course Code", "Date", "Status", "Hours"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4; // Status සහ Hours පමණක් edit කළ හැක
            }
        };
        attendanceTable = new JTable(tableModel);

        // Status Column එකට Dropdown (Present/Absent) එකක් එකතු කිරීම
        JComboBox<String> statusEditor = new JComboBox<>(new String[]{"Present", "Absent", "Medical"});
        attendanceTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(statusEditor));

        add(new JScrollPane(attendanceTable), BorderLayout.CENTER);

        // --- Bottom Panel: Save ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);

        JButton btnSave = new JButton("Save Attendance");
        styleButton(btnSave, new Color(40, 167, 69));

        bottomPanel.add(btnSave);
        add(bottomPanel, BorderLayout.SOUTH);

        btnLoad.addActionListener(e -> loadAttendanceList());
        btnSave.addActionListener(e -> saveAttendance());
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    private void loadLecturerCourses() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement("SELECT Course_code FROM course WHERE Lecturer_in_charge = ?")) {
            pst.setString(1, lecturerID);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) courseComboBox.addItem(rs.getString("Course_code"));
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void loadAttendanceList() {
        String selectedCourse = (String) courseComboBox.getSelectedItem();
        String selectedDate = dateField.getText();
        if (selectedCourse == null) return;

        tableModel.setRowCount(0);

        // enrollment එකෙන් ශිෂ්‍යයන් ගෙන, දැනටමත් attendance දමා ඇත්නම් ඒවා පෙන්වයි
        String query = "SELECT e.Reg_no, a.Status, a.Session_hours " +
                "FROM enrollment e " +
                "LEFT JOIN attendance a ON e.Reg_no = a.Reg_no AND e.Course_code = a.Course_code AND a.Session_date = ? " +
                "WHERE e.Course_code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, selectedDate);
            pst.setString(2, selectedCourse);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("Reg_no"));
                row.add(selectedCourse);
                row.add(selectedDate);
                row.add(rs.getString("Status") == null ? "Present" : rs.getString("Status"));
                row.add(rs.getString("Session_hours") == null ? "2.0" : rs.getString("Session_hours"));
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void saveAttendance() {
        String selectedCourse = (String) courseComboBox.getSelectedItem();
        String selectedType = (String) typeComboBox.getSelectedItem();
        String selectedDate = dateField.getText();

        try (Connection conn = DBConnection.getConnection()) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String regNo = tableModel.getValueAt(i, 0).toString();
                String status = tableModel.getValueAt(i, 3).toString();
                double hours = Double.parseDouble(tableModel.getValueAt(i, 4).toString());

                // දැනටමත් record එකක් ඇත්දැයි බැලීම
                String checkQuery = "SELECT Attendance_id FROM attendance WHERE Reg_no=? AND Course_code=? AND Session_date=?";
                PreparedStatement checkPst = conn.prepareStatement(checkQuery);
                checkPst.setString(1, regNo);
                checkPst.setString(2, selectedCourse);
                checkPst.setString(3, selectedDate);
                ResultSet rs = checkPst.executeQuery();

                if (rs.next()) {
                    // Update
                    String updateQuery = "UPDATE attendance SET Status=?, Session_hours=?, Session_type=? WHERE Attendance_id=?";
                    PreparedStatement upPst = conn.prepareStatement(updateQuery);
                    upPst.setString(1, status);
                    upPst.setDouble(2, hours);
                    upPst.setString(3, selectedType);
                    upPst.setInt(4, rs.getInt("Attendance_id"));
                    upPst.executeUpdate();
                } else {
                    // Insert
                    String insertQuery = "INSERT INTO attendance (Reg_no, Course_code, Session_date, Session_type, Session_hours, Status) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement inPst = conn.prepareStatement(insertQuery);
                    inPst.setString(1, regNo);
                    inPst.setString(2, selectedCourse);
                    inPst.setString(3, selectedDate);
                    inPst.setString(4, selectedType);
                    inPst.setDouble(5, hours);
                    inPst.setString(6, status);
                    inPst.executeUpdate();
                }
            }
            JOptionPane.showMessageDialog(this, "Attendance saved successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}