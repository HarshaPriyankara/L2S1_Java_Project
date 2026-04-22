package GUI.common;

import Controllers.ProfileController;
import Models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProfileManagementPanel extends JPanel {
    private final ProfileController controller = new ProfileController();
    private final JPanel contentPanel = new JPanel();
    private final String loggedInUserId;

    private static final Color BUTTON_COLOR = new Color(46, 125, 192);
    private static final Color CARD_COLOR   = new Color(85, 179, 232);

    public ProfileManagementPanel(String loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
        setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        showUpdateForm();
    }


    private void showUpdateForm() {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        addTitle("Update My Profile", BUTTON_COLOR);

        User existing = controller.getProfileData(loggedInUserId);

        // --- READ ONLY FIELDS (Locked) ---
        addReadOnlyField("User ID", existing.getUserID());
        addReadOnlyField("Password", "******** (Contact Admin to Change)");

        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(new JSeparator());
        contentPanel.add(Box.createVerticalStrut(15));

        // --- EDITABLE FIELDS ---
        JTextField txtFN      = addAlignedField("First Name", existing.getFname());
        JTextField txtLN      = addAlignedField("Last Name", existing.getLname());
        JTextField txtEmail   = addAlignedField("Email", existing.getEmail());
        JTextField txtDob     = addAlignedField("DOB (YYYY-MM-DD)", existing.getDateOfBirth().toString());
        JTextField txtContact = addAlignedField("Contact No", existing.getContactNo());
        JTextField txtAddr    = addAlignedField("Address", existing.getAddress());

        // Added Role Dropdown
        JComboBox<String> cmbRole = addRoleDropdown(existing.getRole());

        // Added Profile Picture Picker
        JTextField txtPic = addPhotoPicker("Profile Photo", existing.getProfilePicPath());

        JPanel row = buttonRow();
        //    backButton(row);
        JButton updBtn = actionButton(row, "Save Changes", BUTTON_COLOR);

        updBtn.addActionListener(e -> {
            User user = new User();
            user.setUserID(loggedInUserId);
            user.setFname(txtFN.getText().trim());
            user.setLname(txtLN.getText().trim());
            user.setEmail(txtEmail.getText().trim());
            user.setContactNo(txtContact.getText().trim());
            user.setAddress(txtAddr.getText().trim());
            user.setRole((String) cmbRole.getSelectedItem());
            user.setProfilePicPath(txtPic.getText().trim());

            String response = controller.updateLecturerProfile(user, txtDob.getText());
            JOptionPane.showMessageDialog(this, response.split(":")[1].trim());
            if (response.startsWith("SUCCESS")) showUpdateForm();
        });

        contentPanel.add(row);
        refresh();
    }

    // --- HELPER METHODS FOR UI ---

    private JTextField addAlignedField(String labelText, String value) {
        JPanel row = createRow();
        row.add(createLabel(labelText));
        JTextField f = new JTextField(value);
        f.setPreferredSize(new Dimension(350, 30));
        row.add(f);
        contentPanel.add(row);
        return f;
    }

    private void addReadOnlyField(String label, String value) {
        JPanel row = createRow();
        row.add(createLabel(label));
        JTextField f = new JTextField(value);
        f.setPreferredSize(new Dimension(350, 30));
        f.setEditable(false);
        f.setBackground(new Color(240, 240, 240));
        row.add(f);
        contentPanel.add(row);
    }

    private JComboBox<String> addRoleDropdown(String currentRole) {
        JPanel row = createRow();
        row.add(createLabel("Role"));
        JComboBox<String> combo = new JComboBox<>(new String[]{"lecture", "Technical Officer", "Admin", "Student"});
        combo.setSelectedItem(currentRole);
        combo.setPreferredSize(new Dimension(350, 30));
        row.add(combo);
        contentPanel.add(row);
        return combo;
    }

    private JTextField addPhotoPicker(String labelText, String currentPath) {
        JPanel row = createRow();
        row.add(createLabel(labelText));
        JTextField path = new JTextField(currentPath);
        path.setPreferredSize(new Dimension(265, 30));
        JButton btn = new JButton("Browse");
        btn.setPreferredSize(new Dimension(80, 30));
        btn.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser();
            if(jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
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

//    private void backButton(JPanel row) {
//        JButton btn = new JButton("Back");
//        btn.setPreferredSize(new Dimension(100, 38));
//        btn.addActionListener(e -> new LecturerDashboard());
//        row.add(btn);
//    }

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