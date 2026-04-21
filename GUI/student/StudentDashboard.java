package GUI.student;

import GUI.common.LoginForm;
import GUI.common.ViewNotice;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {

    private static final Color DARK_BG      = new Color(0x2E2E2E);
    private static final Color BUTTON_COLOR = new Color(46, 125, 192);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel   = new JPanel(cardLayout);


    public StudentDashboard(String loggedInID) {
        setTitle("Student Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(contentPanel,   BorderLayout.CENTER);

        // Register all top-level panels once
        contentPanel.add(new UpdateProfilePanel(),"Update Profile");
        contentPanel.add(new AttendancePanel(), "Attendance Details");
        contentPanel.add(new MedicalPanel(),"Medical Details");
        contentPanel.add(new CoursePanel(), "Course Details");
        contentPanel.add(new GradePanel(),"Grades/GPA");
        contentPanel.add(new TimetablePanel(), "Timetable Details");
        contentPanel.add(new ViewNotice("Undergraduate", contentPanel, cardLayout), "Notice");


        contentPanel.add(buildHomePanel(),"Home");

        cardLayout.show(contentPanel, "Home");
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(DARK_BG);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(60, 16, 24, 16));

        sidebar.add(navButton("Update Profile",      () -> cardLayout.show(contentPanel, "Update Profile")));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(navButton("Attendance Details",    () -> cardLayout.show(contentPanel, "Attendance Details")));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(navButton("Medical Details",    () -> cardLayout.show(contentPanel, "Medical Details")));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(navButton("Course Details", () -> cardLayout.show(contentPanel, "Course Details")));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(navButton("Grades/GPA", () -> cardLayout.show(contentPanel, "Grades/GPA")));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(navButton("Timetable Details", () -> cardLayout.show(contentPanel, "Timetable Details")));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(navButton("Notice", () -> cardLayout.show(contentPanel, "Notice")));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(navButton("Logout", () -> {
            new LoginForm().setVisible(true); // open login form
            dispose(); // close current window
        }));

        return sidebar;
    }


    private JButton navButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(BUTTON_COLOR);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private JPanel buildHomePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("Welcome, Student");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        lbl.setForeground(new Color(0x555555));
        p.add(lbl);
        return p;
    }

    public static void main(String[] args) {
        new StudentDashboard("tg1725").setVisible(true);
    }
}
