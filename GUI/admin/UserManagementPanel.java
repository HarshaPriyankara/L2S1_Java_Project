package GUI.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
                    case "Create New User"    -> showCreateForm();
                    case "Update User Details"-> showUpdateForm();
                    case "Delete User"        -> showDeleteForm();
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

        JTextField txtId   = addField("User ID");
        JTextField txtFN   = addField("First Name");
        JTextField txtLN   = addField("Last Name");
        JTextField txtDob  = addField("Date of Birth (YYYY-MM-DD)");
        JTextField txtAddr = addField("Address");
        JTextField txtMail = addField("Email");
        JComboBox<String> cmbRole = addRoleCombo();
        JPasswordField txtPw = addPasswordField("Password");

        JPanel row = buttonRow();
        backButton(row);
        JButton save = actionButton(row, "Save User", BUTTON_COLOR);
        save.addActionListener(e -> {
            if (anyEmpty(txtId, txtFN, txtLN, txtDob, txtAddr, txtMail)) return;
            JOptionPane.showMessageDialog(this, "User saved!\nID: " + txtId.getText(), "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            showCards();
        });

        contentPanel.add(row);
        refresh();
    }

    // ── Update form ──────────────────────────────────────────────────────────

    private void showUpdateForm() {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        addTitle("Update User Details", BUTTON_COLOR);

        JTextField txtId   = addField("User ID");
        JTextField txtFN   = addField("First Name");
        JTextField txtLN   = addField("Last Name");
        JTextField txtDob  = addField("Date of Birth (YYYY-MM-DD)");
        JTextField txtAddr = addField("Address");
        JTextField txtMail = addField("Email");
        JComboBox<String> cmbRole = addRoleCombo();
        JPasswordField txtPw = addPasswordField("New Password");

        JPanel row = buttonRow();
        backButton(row);
        JButton upd = actionButton(row, "Update User", BUTTON_COLOR);
        upd.addActionListener(e -> {
            if (anyEmpty(txtId, txtFN, txtLN, txtDob, txtAddr, txtMail)) return;
            JOptionPane.showMessageDialog(this, "User updated!\nID: " + txtId.getText(), "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            showCards();
        });

        contentPanel.add(row);
        refresh();
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
            int ok = JOptionPane.showConfirmDialog(this,
                    "Delete user ID: " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (ok == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "User " + id + " deleted.", "Deleted",
                        JOptionPane.INFORMATION_MESSAGE);
                showCards();
            }
        });

        contentPanel.add(row);
        refresh();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

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