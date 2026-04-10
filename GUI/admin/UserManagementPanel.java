package GUI.admin;

import DAO.UserDAO;
import Models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class UserManagementPanel extends JPanel {

    private static final Color BUTTON_COLOR = new Color(46, 125, 192);
    private static final Color CARD_COLOR   = new Color(85, 179, 232);

    private final JPanel contentPanel = new JPanel();

    public UserManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        showCards();
    }

    // ── Card menu ────────────────────────────────────────────────────────────

    private void showCards() {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));
        contentPanel.add(makeCard("Create New User"));
        contentPanel.add(Box.createVerticalStrut(24));
        contentPanel.add(makeCard("Update User Details"));
        contentPanel.add(Box.createVerticalStrut(24));
        contentPanel.add(makeCard("Delete User"));
        refresh();
    }

    private JPanel makeCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        card.add(label, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e)  { card.setBackground(new Color(65, 200, 255)); }
            public void mouseExited(MouseEvent e)   { card.setBackground(CARD_COLOR); }
            public void mouseClicked(MouseEvent e)  {
                switch (title) {
                    case "Create New User"     -> showCreateForm();
                    case "Update User Details" -> showUpdateForm();
                    case "Delete User"         -> showDeleteForm();
                }
            }
        });
        return card;
    }

    // ── Create form ──────────────────────────────────────────────────────────

    private void showCreateForm() {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        addTitle("Create New User", BUTTON_COLOR);

        JTextField txtId      = addField("User ID");
        JTextField txtFN      = addField("First Name");
        JTextField txtLN      = addField("Last Name");
        JTextField txtEmail   = addField("Email");
        JTextField txtDob     = addField("Date of Birth (YYYY-MM-DD)");
        JTextField txtContact = addField("Contact No");
        JTextField txtAddr    = addField("Address");
        JComboBox<String> cmbRole = addRoleCombo();
        JPasswordField txtPw  = addPasswordField("Password");

        JPanel row = buttonRow();
        backButton(row);
        JButton save = actionButton(row, "Save User", BUTTON_COLOR);

        save.addActionListener(e -> {
            if (anyEmpty(txtId, txtFN, txtLN, txtEmail, txtDob, txtContact, txtAddr)) return;

            LocalDate dob;
            try {
                dob = LocalDate.parse(txtDob.getText().trim());
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = new User();
            user.setUserID(txtId.getText().trim());
            user.setFname(txtFN.getText().trim());
            user.setLname(txtLN.getText().trim());
            user.setEmail(txtEmail.getText().trim());
            user.setDob(dob);
            user.setContactNo(txtContact.getText().trim());
            user.setAddress(txtAddr.getText().trim());
            user.setRole((String) cmbRole.getSelectedItem());
            user.setPassword(new String(txtPw.getPassword()).trim());

            UserDAO dao = new UserDAO();
            if (dao.createUser(user)) {
                JOptionPane.showMessageDialog(this,
                        "User saved!\nID: " + txtId.getText(), "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                showCards();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to save user. ID or email may already exist.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        contentPanel.add(row);
        refresh();
    }

    // ── Update form ──────────────────────────────────────────────────────────

    private void showUpdateForm() {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        addTitle("Update User Details", BUTTON_COLOR);

        // ── Step 1: ID lookup row ──────────────────────────────────────────
        JLabel idLbl = new JLabel("User ID");
        idLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        idLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel idRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        idRow.setBackground(Color.WHITE);
        idRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        idRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JTextField txtId = new JTextField();
        txtId.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtId.setPreferredSize(new Dimension(300, 35));

        JButton fetchBtn = new JButton("Load User");
        fetchBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        fetchBtn.setBackground(BUTTON_COLOR);
        fetchBtn.setForeground(Color.WHITE);
        fetchBtn.setFocusPainted(false);
        fetchBtn.setBorderPainted(false);
        fetchBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fetchBtn.setPreferredSize(new Dimension(110, 35));

        idRow.add(txtId);
        idRow.add(Box.createHorizontalStrut(10));
        idRow.add(fetchBtn);

        contentPanel.add(idLbl);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(idRow);
        contentPanel.add(Box.createVerticalStrut(16));

        // ── Step 2: Editable fields (initially empty / disabled) ──────────
        JTextField txtNewId   = addField("New User ID (leave blank to keep current)");
        JTextField txtFN      = addField("First Name");
        JTextField txtLN      = addField("Last Name");
        JTextField txtEmail   = addField("Email");
        JTextField txtDob     = addField("Date of Birth (YYYY-MM-DD)");
        JTextField txtContact = addField("Contact No");
        JTextField txtAddr    = addField("Address");
        JComboBox<String> cmbRole = addRoleCombo();
        JPasswordField txtPw  = addPasswordField("New Password (leave blank to keep current)");

        // Disable all fields until a user is loaded
        setFieldsEnabled(false, txtFN, txtLN, txtEmail, txtDob, txtContact, txtAddr);
        cmbRole.setEnabled(false);
        txtPw.setEnabled(false);

        // ── Step 3: Button row ─────────────────────────────────────────────
        JPanel row = buttonRow();
        backButton(row);
        JButton upd = actionButton(row, "Update User", BUTTON_COLOR);
        upd.setEnabled(false);
        contentPanel.add(row);
        refresh();

        // ── Load user on fetch ─────────────────────────────────────────────
        fetchBtn.addActionListener(e -> {
            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a User ID.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            UserDAO dao = new UserDAO();
            User existing = dao.getUserById(id);
            if (existing == null) {
                JOptionPane.showMessageDialog(this, "No user found with that ID.",
                        "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Populate fields with existing data
            txtFN.setText(existing.getFname());
            txtLN.setText(existing.getLname());
            txtEmail.setText(existing.getEmail());
            txtDob.setText(existing.getDateOfBirth() != null
                    ? existing.getDateOfBirth().toString() : "");
            txtContact.setText(existing.getContactNo());
            txtAddr.setText(existing.getAddress());
            if (existing.getRole() != null) {
                cmbRole.setSelectedItem(existing.getRole());
            }
            txtPw.setText("");   // always blank — user types new one only if changing

            // Enable editing
            setFieldsEnabled(true, txtFN, txtLN, txtEmail, txtDob, txtContact, txtAddr);
            cmbRole.setEnabled(true);
            txtPw.setEnabled(true);
            upd.setEnabled(true);
            txtId.setEditable(false);   // lock ID once loaded
            fetchBtn.setEnabled(false);
        });

        // ── Save update ────────────────────────────────────────────────────
        upd.addActionListener(e -> {
            if (anyEmpty(txtFN, txtLN, txtEmail, txtDob, txtContact, txtAddr)) return;

            LocalDate dob;
            try {
                dob = LocalDate.parse(txtDob.getText().trim());
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            UserDAO dao = new UserDAO();
            User existing = dao.getUserById(txtId.getText().trim());

            String originalId = txtId.getText().trim();
            String newId = txtNewId.getText().trim();
            String effectiveId = newId.isEmpty() ? originalId : newId;

// If ID is changing, check the new ID doesn't already exist
            if (!newId.isEmpty() && !newId.equals(originalId)) {
                if (dao.userExists(newId)) {
                    JOptionPane.showMessageDialog(this,
                            "User ID " + newId + " already exists.",
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            User user = new User();
            user.setUserID(effectiveId);
            user.setOriginalUserID(originalId);   // needed for WHERE clause — see below
            user.setFname(txtFN.getText().trim());
            user.setLname(txtLN.getText().trim());
            user.setEmail(txtEmail.getText().trim());
            user.setDob(dob);
            user.setContactNo(txtContact.getText().trim());
            user.setAddress(txtAddr.getText().trim());
            user.setRole((String) cmbRole.getSelectedItem());

            // Keep old password if the field is left blank
            String newPw = new String(txtPw.getPassword()).trim();
            user.setPassword(newPw.isEmpty() && existing != null
                    ? existing.getPassword() : newPw);

            if (dao.updateUser(user)) {
                JOptionPane.showMessageDialog(this,
                        "User updated!\nID: " + txtId.getText(), "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                showCards();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // helper to bulk-enable/disable plain text fields
    private void setFieldsEnabled(boolean enabled, JTextField... fields) {
        for (JTextField f : fields) f.setEnabled(enabled);
    }

    // ── Delete form ──────────────────────────────────────────────────────────

    private void showDeleteForm() {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        addTitle("Delete User", new Color(0xCC0000));

        JLabel desc = new JLabel("Enter the User ID of the user you want to delete.");
        desc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        desc.setForeground(Color.GRAY);
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(desc);
        contentPanel.add(Box.createVerticalStrut(20));

        JTextField txtId = addField("User ID");

        JPanel row = buttonRow();
        backButton(row);
        JButton del = actionButton(row, "Delete User", new Color(0xCC0000));

        del.addActionListener(e -> {
            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a User ID.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            UserDAO dao = new UserDAO();
            if (!dao.userExists(id)) {
                JOptionPane.showMessageDialog(this, "No user found with that ID.",
                        "Not Found", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int ok = JOptionPane.showConfirmDialog(this,
                    "Delete user ID: " + id + "?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ok == JOptionPane.YES_OPTION) {
                if (dao.deleteUser(id)) {
                    JOptionPane.showMessageDialog(this,
                            "User " + id + " deleted.", "Deleted",
                            JOptionPane.INFORMATION_MESSAGE);
                    showCards();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Delete failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        contentPanel.add(row);
        refresh();
    }

    // ── Helpers (unchanged) ──────────────────────────────────────────────────

    private void addTitle(String text, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        lbl.setForeground(color);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lbl);
        contentPanel.add(Box.createVerticalStrut(24));
    }

    private JTextField addField(String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lbl);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(field);
        contentPanel.add(Box.createVerticalStrut(12));
        return field;
    }

    private JPasswordField addPasswordField(String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField field = new JPasswordField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lbl);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(field);
        contentPanel.add(Box.createVerticalStrut(12));
        return field;
    }

    private JComboBox<String> addRoleCombo() {
        JLabel lbl = new JLabel("Role");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> combo = new JComboBox<>(
                new String[]{"Admin", "Student", "Lecturer", "Technical Officer"});
        combo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lbl);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(combo);
        contentPanel.add(Box.createVerticalStrut(12));
        return combo;
    }

    private JPanel buttonRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        row.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        return row;
    }

    private void backButton(JPanel row) {
        JButton btn = new JButton("Back");
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(new Color(0xAAAAAA));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 38));
        btn.addActionListener(e -> showCards());
        row.add(btn);
        row.add(Box.createHorizontalStrut(12));
    }

    private JButton actionButton(JPanel row, String label, Color bg) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 38));
        row.add(btn);
        return btn;
    }

    private boolean anyEmpty(JTextField... fields) {
        for (JTextField f : fields) {
            if (f.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return true;
            }
        }
        return false;
    }

    private void refresh() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}