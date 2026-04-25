package GUI.common;

import Controllers.ProfileControllers.LecTOProfileController;
import Models.User;
import javax.swing.*;
import java.awt.*;

public class ProfileManagementPanel extends JPanel {
    private final LecTOProfileController controller = new LecTOProfileController();
    private final JPanel contentPanel = new JPanel();
    private final String loggedInUserId;
    private final boolean allowRoleEdit;
    private final JPanel dashboardContentPanel;
    private final CardLayout dashboardCardLayout;

    private static final Color BUTTON_COLOR = UITheme.PRIMARY;
    private static final Color CARD_COLOR   = UITheme.ACCENT;

    public ProfileManagementPanel(String loggedInUserId, boolean allowRoleEdit) {
        this(loggedInUserId, allowRoleEdit, null, null);
    }

    public ProfileManagementPanel(String loggedInUserId, boolean allowRoleEdit,
                                  JPanel dashboardContentPanel, CardLayout dashboardCardLayout) {
        this.loggedInUserId = loggedInUserId;
        this.allowRoleEdit = allowRoleEdit;
        this.dashboardContentPanel = dashboardContentPanel;
        this.dashboardCardLayout = dashboardCardLayout;
        setLayout(new BorderLayout());
        setBackground(UITheme.APP_BACKGROUND);
        contentPanel.setBackground(UITheme.APP_BACKGROUND);
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

        User existing = controller.getUserData(loggedInUserId);

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
        String dobValue = existing.getDateOfBirth() == null ? "" : existing.getDateOfBirth().toString();
        JTextField txtDob     = addAlignedField("DOB (YYYY-MM-DD)", dobValue);
        JTextField txtContact = addAlignedField("Contact No", existing.getContactNo());
        JTextField txtAddr    = addAlignedField("Address", existing.getAddress());

        final JComboBox<String> cmbRole;
        if (allowRoleEdit) {
            cmbRole = addRoleDropdown(existing.getRole());
        } else {
            cmbRole = null;
            addReadOnlyField("Role", existing.getRole());
        }

        // Added Profile Picture Picker
        JTextField txtPic = addPhotoPicker("Profile Photo", existing.getProfilePicPath());

        JPanel row = buttonRow();
        backButton(row);
        JButton updBtn = actionButton(row, "Save Changes", BUTTON_COLOR);

        updBtn.addActionListener(e -> {
            User user = new User();
            user.setUserID(loggedInUserId);
            user.setFname(txtFN.getText().trim());
            user.setLname(txtLN.getText().trim());
            user.setEmail(txtEmail.getText().trim());
            user.setContactNo(txtContact.getText().trim());
            user.setAddress(txtAddr.getText().trim());
            user.setRole(allowRoleEdit ? (String) cmbRole.getSelectedItem() : existing.getRole());
            user.setProfilePicPath(txtPic.getText().trim());

            try {
                user.setDob(java.time.LocalDate.parse(txtDob.getText().trim()));
            } catch (java.time.format.DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.");
                return;
            }

            String response = controller.updateProfile(user);
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
        UITheme.styleTextField(f);
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
        f.setBackground(UITheme.SURFACE_MUTED);
        row.add(f);
        contentPanel.add(row);
    }

    private JComboBox<String> addRoleDropdown(String currentRole) {
        JPanel row = createRow();
        row.add(createLabel("Role"));
        JComboBox<String> combo = new JComboBox<>(new String[]{"lecture", "Technical Officer", "Admin", "Student"});
        combo.setSelectedItem(currentRole);
        combo.setPreferredSize(new Dimension(350, 30));
        UITheme.styleComboBox(combo);
        row.add(combo);
        contentPanel.add(row);
        return combo;
    }

    private JTextField addPhotoPicker(String labelText, String currentPath) {
        JPanel row = createRow();
        row.add(createLabel(labelText));
        JTextField path = new JTextField(currentPath);
        path.setPreferredSize(new Dimension(265, 30));
        UITheme.styleTextField(path);
        JButton btn = new JButton("Browse");
        btn.setPreferredSize(new Dimension(80, 30));
        UITheme.styleNeutralButton(btn);
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
        row.setBackground(UITheme.APP_BACKGROUND);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        return row;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setPreferredSize(new Dimension(150, 30));
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setForeground(UITheme.TEXT_PRIMARY);
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
        row.setBackground(UITheme.APP_BACKGROUND);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        return row;
    }


    private void backButton(JPanel row) {
        JButton btn = new JButton("Back");
        btn.setPreferredSize(new Dimension(100, 38));
        UITheme.styleNeutralButton(btn);
        btn.addActionListener(e -> {
            if (dashboardCardLayout != null && dashboardContentPanel != null) {
                dashboardCardLayout.show(dashboardContentPanel, "Home");
            }
        });
        row.add(btn);
    }

    private JButton actionButton(JPanel row, String label, Color bg) {
        JButton btn = new JButton(label);
        btn.setPreferredSize(new Dimension(140, 38));
        if (UITheme.DANGER.equals(bg)) {
            UITheme.styleDangerButton(btn);
        } else if (UITheme.SURFACE_MUTED.equals(bg)) {
            UITheme.styleNeutralButton(btn);
        } else {
            UITheme.stylePrimaryButton(btn);
        }
        row.add(btn);
        return btn;
    }

    private void refresh() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
