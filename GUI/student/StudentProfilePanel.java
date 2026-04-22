package GUI.student;

import Controllers.StudentProfileController;
import Models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentProfilePanel extends JPanel {
    private final StudentProfileController controller = new StudentProfileController();
    private final JPanel contentPanel = new JPanel();
    private final String loggedInStudentId;

    private static final Color BUTTON_COLOR = new Color(46, 125, 192);
    private static final Color CARD_COLOR   = new Color(85, 179, 232);

    public StudentProfilePanel(String loggedInStudentId) {
        this.loggedInStudentId = loggedInStudentId;
        setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        showCards();
    }

    private void showCards() {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));

        JPanel card = makeCard("My Profile Settings");
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { showUpdateForm(); }
        });

        contentPanel.add(card);
        refresh();
    }

    private void showUpdateForm() {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        addTitle("Edit My Information", BUTTON_COLOR);

        User u = controller.getStudentData(loggedInStudentId);

        // READ-ONLY (Name and DOB stay locked)
        addReadOnlyField("Full Name", u.getFname() + " " + u.getLname());
        addReadOnlyField("Date of Birth", u.getDateOfBirth().toString());

        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(new JSeparator());
        contentPanel.add(Box.createVerticalStrut(15));

        // EDITABLE (Email is now here)
        JTextField txtEmail   = addEditableField("Email Address", u.getEmail());
        JTextField txtContact = addEditableField("Contact No", u.getContactNo());
        JTextField txtAddr    = addEditableField("Address", u.getAddress());
        JTextField txtPic     = addPhotoPicker("Profile Picture", u.getProfilePicPath());

        JPanel row = buttonRow();
        backButton(row);
        JButton saveBtn = actionButton(row, "Save Changes", BUTTON_COLOR);

        saveBtn.addActionListener(e -> {
            String response = controller.updateStudentProfile(
                    loggedInStudentId,
                    txtEmail.getText(),
                    txtContact.getText(),
                    txtAddr.getText(),
                    txtPic.getText()
            );

            JOptionPane.showMessageDialog(this, response.split(":")[1].trim());
            if (response.startsWith("SUCCESS")) showCards();
        });

        contentPanel.add(row);
        refresh();
    }

    // --- REUSABLE HELPERS ---

    private void addReadOnlyField(String label, String value) {
        JPanel row = createRow();
        row.add(createLabel(label));
        JTextField f = new JTextField(value);
        f.setPreferredSize(new Dimension(350, 30));
        f.setEditable(false);
        f.setBackground(new Color(245, 245, 245));
        row.add(f);
        contentPanel.add(row);
    }

    private JTextField addEditableField(String label, String value) {
        JPanel row = createRow();
        row.add(createLabel(label));
        JTextField f = new JTextField(value);
        f.setPreferredSize(new Dimension(350, 30));
        row.add(f);
        contentPanel.add(row);
        return f;
    }

    private JTextField addPhotoPicker(String labelText, String currentPath) {
        JPanel row = createRow();
        row.add(createLabel(labelText));
        JTextField path = new JTextField(currentPath);
        path.setPreferredSize(new Dimension(265, 30));
        path.setEditable(false);
        JButton btn = new JButton("Browse");
        btn.setPreferredSize(new Dimension(80, 30));
        btn.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser();
            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                path.setText(jfc.getSelectedFile().getAbsolutePath().replace("\\", "/"));
            }
        });
        row.add(path); row.add(btn);
        contentPanel.add(row);
        return path;
    }

    private JPanel createRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        return row;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setPreferredSize(new Dimension(150, 30));
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        return lbl;
    }

    private void addTitle(String text, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        lbl.setForeground(color);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lbl);
        contentPanel.add(Box.createVerticalStrut(20));
    }

    private JPanel makeCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        card.add(label);
        return card;
    }

    private JPanel buttonRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 20));
        row.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        return row;
    }

    private void backButton(JPanel row) {
        JButton btn = new JButton("Back");
        btn.setPreferredSize(new Dimension(100, 38));
        btn.addActionListener(e -> showCards());
        row.add(btn);
    }

    private JButton actionButton(JPanel row, String label, Color bg) {
        JButton btn = new JButton(label);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(140, 38));
        row.add(btn);
        return btn;
    }

    private void refresh() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}