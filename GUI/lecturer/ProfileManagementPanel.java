package GUI.lecturer;

import DAO.UserDAO;
import Models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ProfileManagementPanel extends JPanel {

    private static final Color BUTTON_COLOR = new Color(46, 125, 192);
    private static final Color CARD_COLOR   = new Color(85, 179, 232);

    private final JPanel contentPanel = new JPanel();

    // Pass in the logged-in lecturer's ID so we can pre-fill it
    private final String loggedInUserId;

    public ProfileManagementPanel(String loggedInUserId) {
        this.loggedInUserId = loggedInUserId;

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

    // ── Card screen ────────────────────────────────────────────────────────────

    private void showCards() {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));
        contentPanel.add(makeCard("Update My Profile"));
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
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(65, 200, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(CARD_COLOR);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if ("Update My Profile".equals(title)) {
                    showUpdateForm();
                }
            }
        });
        return card;
    }

    // ── Update form ────────────────────────────────────────────────────────────

    private void showUpdateForm() {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        addTitle("Update My Profile", BUTTON_COLOR);

//        // ── Read-only User ID display ──────────────────────────────────────────
//        JLabel idLbl = new JLabel("User ID");
//        idLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
//        idLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
//
//        JTextField txtId = new JTextField(loggedInUserId);
//        txtId.setFont(new Font("SansSerif", Font.PLAIN, 13));
//        txtId.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
//        txtId.setAlignmentX(Component.LEFT_ALIGNMENT);
//        txtId.setEditable(false);                        // username is locked
//        txtId.setBackground(new Color(230, 230, 230));   // grey to signal read-only
//
//        contentPanel.add(idLbl);
//        contentPanel.add(Box.createVerticalStrut(5));
//        contentPanel.add(txtId);
//        contentPanel.add(Box.createVerticalStrut(16));

        // ── Editable profile fields ────────────────────────────────────────────
        JTextField txtFN      = addField("First Name");
        JTextField txtLN      = addField("Last Name");
        JTextField txtEmail   = addField("Email");
        JTextField txtDob     = addField("Date of Birth (YYYY-MM-DD)");
        JTextField txtContact = addField("Contact No");
        JTextField txtAddr    = addField("Address");

        // ── Pre-load the lecturer's existing data immediately ──────────────────
        UserDAO dao = new UserDAO();
        User existing = dao.getUserById(loggedInUserId);

        if (existing != null) {
            txtFN.setText(existing.getFname());
            txtLN.setText(existing.getLname());
            txtEmail.setText(existing.getEmail());
            txtDob.setText(existing.getDateOfBirth() != null
                    ? existing.getDateOfBirth().toString() : "");
            txtContact.setText(existing.getContactNo());
            txtAddr.setText(existing.getAddress());
        } else {
            JOptionPane.showMessageDialog(this,
                    "Could not load your profile data.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        // ── Button row ─────────────────────────────────────────────────────────
        JPanel row = buttonRow();
        backButton(row);
        JButton updBtn = actionButton(row, "Save Changes", BUTTON_COLOR);
        contentPanel.add(row);
        refresh();

        // ── Save handler ───────────────────────────────────────────────────────
        updBtn.addActionListener(e -> {
            if (anyEmpty(txtFN, txtLN, txtEmail, txtDob, txtContact, txtAddr)) return;

            LocalDate dob;
            try {
                dob = LocalDate.parse(txtDob.getText().trim());
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid date format. Use YYYY-MM-DD.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Re-fetch to keep password and role unchanged
            UserDAO saveDao = new UserDAO();
            User current = saveDao.getUserById(loggedInUserId);

            User user = new User();
            user.setUserID(loggedInUserId);        // ID never changes
            user.setOriginalUserID(loggedInUserId);
            user.setFname(txtFN.getText().trim());
            user.setLname(txtLN.getText().trim());
            user.setEmail(txtEmail.getText().trim());
            user.setDob(dob);
            user.setContactNo(txtContact.getText().trim());
            user.setAddress(txtAddr.getText().trim());

            // Preserve role and password from the existing record — lecturer cannot change these
            if (current != null) {
                user.setRole(current.getRole());
                user.setPassword(current.getPassword());
            }

            if (saveDao.updateUser(user)) {
                JOptionPane.showMessageDialog(this,
                        "Profile updated successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                showCards();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Update failed. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private void refresh() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }

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
                JOptionPane.showMessageDialog(this,
                        "Please fill in all fields.",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return true;
            }
        }
        return false;
    }
}