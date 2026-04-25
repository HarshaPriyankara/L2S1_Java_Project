package GUI.lecturer;

import Controllers.StudentControllers.LecturerMarksOverviewController;
import Controllers.StudentControllers.LecturerMarksOverviewResult;
import GUI.common.UITheme;
import Utils.CourseMarkScheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StudentMarksPanel extends JPanel {
    private static final Color CARD_COLOR = new Color(85, 179, 232);

    private final LecturerMarksOverviewController controller = new LecturerMarksOverviewController();
    private final JComboBox<String> courseComboBox = new JComboBox<>();
    private final JTextField studentSearchField = new JTextField(12);
    private final JComboBox<String> scopeComboBox = new JComboBox<>(new String[]{"Whole Batch", "Individual Student"});
    private final DefaultListModel<String> markTypeModel = new DefaultListModel<>();
    private final JLabel caRuleLabel = new JLabel("CA Eligibility Rule: -");
    private final JLabel attendanceRuleLabel = new JLabel("Attendance + CA Rule: -");
    private final Runnable onBack;
    private final String lecturerId;

    public StudentMarksPanel(String lecturerId, Runnable onBack) {
        this.lecturerId = lecturerId;
        this.onBack = onBack;

        setLayout(new BorderLayout(15, 15));
        setBackground(UITheme.APP_BACKGROUND);
        setBorder(UITheme.createContentBorder());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topPanel.setBackground(UITheme.APP_BACKGROUND);

        JButton backButton = new JButton("Back");
        UITheme.styleNeutralButton(backButton);
        UITheme.setStandardButtonSize(backButton);
        backButton.addActionListener(e -> this.onBack.run());

        UITheme.styleComboBox(courseComboBox);
        UITheme.styleComboBox(scopeComboBox);
        UITheme.styleTextField(studentSearchField);

        JButton loadButton = new JButton("Load Marks Base");
        UITheme.stylePrimaryButton(loadButton);
        UITheme.setWideButtonSize(loadButton);

        topPanel.add(backButton);
        topPanel.add(new JLabel("Course:"));
        topPanel.add(courseComboBox);
        topPanel.add(new JLabel("View Scope:"));
        topPanel.add(scopeComboBox);
        topPanel.add(new JLabel("Reg No:"));
        topPanel.add(studentSearchField);
        topPanel.add(loadButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setBackground(UITheme.APP_BACKGROUND);

        JPanel infoPanel = new JPanel(new BorderLayout(0, 10));
        infoPanel.setBackground(UITheme.APP_BACKGROUND);

        JPanel markTypesPanel = new JPanel(new BorderLayout(0, 8));
        markTypesPanel.setBackground(UITheme.APP_BACKGROUND);
        JLabel typesTitle = new JLabel("Available Assessment Types");
        typesTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        typesTitle.setForeground(UITheme.TEXT_PRIMARY);
        markTypesPanel.add(typesTitle, BorderLayout.NORTH);
        JList<String> markTypesList = new JList<>(markTypeModel);
        markTypesList.setVisibleRowCount(8);
        markTypesPanel.add(new JScrollPane(markTypesList), BorderLayout.CENTER);

        JPanel rulePanel = new JPanel();
        rulePanel.setBackground(UITheme.APP_BACKGROUND);
        rulePanel.setLayout(new BoxLayout(rulePanel, BoxLayout.Y_AXIS));
        rulePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 224, 234)),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)
        ));

        JLabel ruleTitle = new JLabel("Current Rule Base");
        ruleTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        ruleTitle.setForeground(UITheme.TEXT_PRIMARY);
        rulePanel.add(ruleTitle);
        rulePanel.add(Box.createVerticalStrut(12));

        caRuleLabel.setForeground(UITheme.TEXT_PRIMARY);
        attendanceRuleLabel.setForeground(UITheme.TEXT_PRIMARY);
        rulePanel.add(caRuleLabel);
        rulePanel.add(Box.createVerticalStrut(10));
        rulePanel.add(attendanceRuleLabel);

        infoPanel.add(markTypesPanel, BorderLayout.CENTER);
        infoPanel.add(rulePanel, BorderLayout.SOUTH);

        JPanel actionCardsPanel = new JPanel();
        actionCardsPanel.setBackground(UITheme.APP_BACKGROUND);
        actionCardsPanel.setLayout(new BoxLayout(actionCardsPanel, BoxLayout.Y_AXIS));
        actionCardsPanel.add(createActionCard("CA Marks"));
        actionCardsPanel.add(Box.createVerticalStrut(18));
        actionCardsPanel.add(createActionCard("End Exam Eligibility by CA"));
        actionCardsPanel.add(Box.createVerticalStrut(18));
        actionCardsPanel.add(createActionCard("End Exam Eligibility by Attendance + CA"));
        actionCardsPanel.add(Box.createVerticalStrut(18));
        actionCardsPanel.add(createActionCard("Final Marks (CA + END)"));

        centerPanel.add(infoPanel, BorderLayout.CENTER);
        centerPanel.add(actionCardsPanel, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);

        loadButton.addActionListener(e -> loadOverview());
        scopeComboBox.addActionListener(e -> updateSearchState());

        updateSearchState();
        loadInitialData();
    }

    private void loadInitialData() {
        LecturerMarksOverviewResult result = controller.loadOverview(lecturerId, null);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage());
            return;
        }

        courseComboBox.removeAllItems();
        for (String course : result.getLecturerCourses()) {
            courseComboBox.addItem(course);
        }

        if (courseComboBox.getItemCount() > 0) {
            loadOverview();
        }
    }

    private void loadOverview() {
        String courseCode = (String) courseComboBox.getSelectedItem();
        LecturerMarksOverviewResult result = controller.loadOverview(lecturerId, courseCode);
        if (result.hasError()) {
            JOptionPane.showMessageDialog(this, result.getErrorMessage());
            return;
        }

        markTypeModel.clear();
        for (String type : result.getAllowedMarkTypes()) {
            markTypeModel.addElement(type);
        }

        CourseMarkScheme scheme = CourseMarkScheme.forCourse(courseCode);
        caRuleLabel.setText(String.format(
                "CA Eligibility Rule: CA marks should be at least %.2f out of %.2f",
                scheme.getCaPassMark(), scheme.getCaWeight()
        ));
        attendanceRuleLabel.setText(
                "Attendance + CA Rule: base prepared for next step using selected course and view scope"
        );
    }

    private void updateSearchState() {
        boolean individual = "Individual Student".equals(scopeComboBox.getSelectedItem());
        studentSearchField.setEnabled(individual);
        if (!individual) {
            studentSearchField.setText("");
        }
    }

    private JPanel createActionCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setPreferredSize(new Dimension(360, 68));
        card.setMaximumSize(new Dimension(360, 68));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        card.add(label, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(
                        StudentMarksPanel.this,
                        title + " logic will be implemented in the next step.",
                        "Student Marks",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        return card;
    }
}
