package GUI.to;

import Controllers.AttendanceControllers.AttendanceController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class AddAttendancePanel extends JPanel {
    private AttendanceController controller = new AttendanceController();
    private JComboBox<String> courseBox = new JComboBox<>();
    private JComboBox<String> typeBox = new JComboBox<>(new String[]{"Theory", "Practical"});
    private JTable table;
    private DefaultTableModel model;

    public AddAttendancePanel(AttendanceManagement parent) {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnBack = new JButton("← Back");
        btnBack.addActionListener(e -> parent.showPanel("Menu"));

        for (String c : controller.getCourseList()) courseBox.addItem(c);
        JButton btnLoad = new JButton("Load Students");

        top.add(btnBack); top.add(new JLabel("Course:")); top.add(courseBox);
        top.add(new JLabel("Type:")); top.add(typeBox); top.add(btnLoad);

        // Table
        model = new DefaultTableModel(new String[]{"Student ID", "Course", "Status", "Hours"}, 0);
        table = new JTable(model);
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Present", "Absent", "Medical"});
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(statusCombo));

        // Listeners
        btnLoad.addActionListener(e -> controller.loadEnrolledToTable(courseBox.getSelectedItem().toString(), model));

        // Bottom save button
        JButton btnSave = new JButton("Submit All Records");
        btnSave.setBackground(new Color(40, 167, 69));
        btnSave.setForeground(Color.WHITE);
        btnSave.addActionListener(e -> {
            boolean success = controller.saveAttendance(courseBox.getSelectedItem().toString(),
                               LocalDate.now().toString(), typeBox.getSelectedItem().toString(), model.getDataVector());
            if(success) JOptionPane.showMessageDialog(this, "Attendance Saved!");
        });

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnSave, BorderLayout.SOUTH);
    }
}