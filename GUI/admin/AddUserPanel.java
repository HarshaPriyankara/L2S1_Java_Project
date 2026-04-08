package GUI.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddUserPanel extends JFrame {
    private static final Color darkBg      = new Color(0x2E2E2E);
    private static final Color buttonColor = new Color(46, 125, 192);
    private static final     Color cardColor   = new Color(125, 196, 236);

    //content panel - right_side
    JPanel contentPanel;

    public  AddUserPanel() {
        // JFrame setup
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setBackground(darkBg);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(60, 24, 24, 24));

        JButton btnUser      = createNavButton("User Management");
        JButton btnCourse    = createNavButton("Course Management");
        JButton btnNotice    = createNavButton("Notice Management");
        JButton btnTimetable = createNavButton("Timetable Management");
        JButton btnLogout    = createNavButton("Logout");

        sidebar.add(btnUser);
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(btnCourse);
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(btnNotice);
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(btnTimetable);
        sidebar.add(Box.createVerticalStrut(16));
        sidebar.add(btnLogout);


        // Content
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));

        //show default when open
        showCards();

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add sidebar and content to frame
        add(sidebar, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);


        setVisible(true);
    }



    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(buttonColor);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    //create main content 3 cards
    private void showCards() {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));

        JPanel card1 = createCard("Create New User");
        JPanel card2 = createCard("Update User Details");
        JPanel card3 = createCard("Delete User");

        contentPanel.add(card1);
        contentPanel.add(Box.createVerticalStrut(24));
        contentPanel.add(card2);
        contentPanel.add(Box.createVerticalStrut(24));
        contentPanel.add(card3);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    //create that 3 cards
    private JPanel createCard(String title) {
        JPanel card = new JPanel();
        card.setBackground(cardColor);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
        card.add(label, BorderLayout.CENTER);

        // When card is clicked
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (title.equals("Create New User")) {
                    showCreateUserForm();
                }
                if (title.equals("Update User Details")) {
                    showUpdateUserForm();
                }
                if (title.equals("Delete User")) {
                    showDeleteUserForm();
                }
            }
        });

        return card;
    }


    //forms
    private void showCreateUserForm() {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // Page title
        JLabel pageTitle = new JLabel("Create New User");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        pageTitle.setForeground(buttonColor);
        pageTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(pageTitle);
        contentPanel.add(Box.createVerticalStrut(24));

        // User ID
        JLabel lblUserId = new JLabel("User ID");
        lblUserId.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblUserId.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtUserId = new JTextField();
        txtUserId.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtUserId.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtUserId.setAlignmentX(Component.LEFT_ALIGNMENT);

        // First Name
        JLabel lblFName = new JLabel("First Name");
        lblFName.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblFName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtFName = new JTextField();
        txtFName.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtFName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtFName.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Last Name
        JLabel lblLName = new JLabel("Last Name");
        lblLName.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblLName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtLName = new JTextField();
        txtLName.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtLName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtLName.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Date of Birth
        JLabel lblDob = new JLabel("Date of Birth (YYYY-MM-DD)");
        lblDob.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblDob.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtDob = new JTextField();
        txtDob.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtDob.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtDob.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Address
        JLabel lblAddress = new JLabel("Address");
        lblAddress.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblAddress.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtAddress = new JTextField();
        txtAddress.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtAddress.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtAddress.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Email
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtEmail = new JTextField();
        txtEmail.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtEmail.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Role
        JLabel lblRole = new JLabel("Role");
        lblRole.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblRole.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"Admin", "Student", "Lecturer", "Technical Officer"});
        cmbRole.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cmbRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cmbRole.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Password
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add labels and fields to panel
        contentPanel.add(lblUserId);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(txtUserId);
        contentPanel.add(Box.createVerticalStrut(12));

        contentPanel.add(lblFName);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(txtFName);
        contentPanel.add(Box.createVerticalStrut(12));

        contentPanel.add(lblLName);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(txtLName);
        contentPanel.add(Box.createVerticalStrut(12));

        contentPanel.add(lblDob);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(txtDob);
        contentPanel.add(Box.createVerticalStrut(12));

        contentPanel.add(lblAddress);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(txtAddress);
        contentPanel.add(Box.createVerticalStrut(12));

        contentPanel.add(lblEmail);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(txtEmail);
        contentPanel.add(Box.createVerticalStrut(12));

        contentPanel.add(lblRole);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(cmbRole);
        contentPanel.add(Box.createVerticalStrut(12));

        contentPanel.add(lblPassword);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(txtPassword);
        contentPanel.add(Box.createVerticalStrut(24));

        // Buttons row
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnRow.setBackground(Color.WHITE);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnBack.setBackground(new Color(0xAAAAAA));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.setPreferredSize(new Dimension(100, 38));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCards();
            }
        });

        JButton btnSave = new JButton("Save User");
        btnSave.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnSave.setBackground(buttonColor);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBorderPainted(false);
        btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSave.setPreferredSize(new Dimension(120, 38));
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String userId   = txtUserId.getText().trim();
                String fName    = txtFName.getText().trim();
                String lName    = txtLName.getText().trim();
                String dob      = txtDob.getText().trim();
                String address  = txtAddress.getText().trim();
                String email    = txtEmail.getText().trim();
                String role     = (String) cmbRole.getSelectedItem();
                String password = new String(txtPassword.getPassword()).trim();

                // Check empty fields
                if (userId.isEmpty() || fName.isEmpty() || lName.isEmpty() ||
                        dob.isEmpty() || address.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // for database code

                JOptionPane.showMessageDialog(null,
                        "User saved successfully!\n" +
                                "ID: "      + userId  + "\n" +
                                "Name: "    + fName   + " " + lName + "\n" +
                                "DOB: "     + dob     + "\n" +
                                "Address: " + address + "\n" +
                                "Email: "   + email   + "\n" +
                                "Role: "    + role,
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                showCards();
            }
        });

        btnRow.add(btnBack);
        btnRow.add(Box.createHorizontalStrut(12));
        btnRow.add(btnSave);

        contentPanel.add(btnRow);

        contentPanel.revalidate();
        contentPanel.repaint();
    }


    //update form

    private void showUpdateUserForm() {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // Page title
        JLabel pageTitle = new JLabel("Update User Details");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        pageTitle.setForeground(buttonColor);
        pageTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(pageTitle);
        contentPanel.add(Box.createVerticalStrut(24));

        // User ID
        JLabel lblUserId = new JLabel("User ID");
        lblUserId.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblUserId.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtUserId = new JTextField();
        txtUserId.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtUserId.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtUserId.setAlignmentX(Component.LEFT_ALIGNMENT);

        // First Name
        JLabel lblFName = new JLabel("First Name");
        lblFName.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblFName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtFName = new JTextField();
        txtFName.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtFName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtFName.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Last Name
        JLabel lblLName = new JLabel("Last Name");
        lblLName.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblLName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtLName = new JTextField();
        txtLName.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtLName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtLName.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Date of Birth
        JLabel lblDob = new JLabel("Date of Birth (YYYY-MM-DD)");
        lblDob.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblDob.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtDob = new JTextField();
        txtDob.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtDob.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtDob.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Address
        JLabel lblAddress = new JLabel("Address");
        lblAddress.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblAddress.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtAddress = new JTextField();
        txtAddress.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtAddress.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtAddress.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Email
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtEmail = new JTextField();
        txtEmail.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtEmail.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Role
        JLabel lblRole = new JLabel("Role");
        lblRole.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblRole.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"Admin", "Student", "Lecturer", "Technical Officer"});
        cmbRole.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cmbRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cmbRole.setAlignmentX(Component.LEFT_ALIGNMENT);

        // New Password
        JLabel lblPassword = new JLabel("New Password");
        lblPassword.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add labels and fields to panel
        contentPanel.add(lblUserId);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(txtUserId);
        contentPanel.add(Box.createVerticalStrut(12));

        contentPanel.add(lblFName);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(txtFName);
        contentPanel.add(Box.createVerticalStrut(12));

        contentPanel.add(lblLName);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(txtLName);
        contentPanel.add(Box.createVerticalStrut(12));

        contentPanel.add(lblDob);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(txtDob);
        contentPanel.add(Box.createVerticalStrut(12));

        contentPanel.add(lblAddress);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(txtAddress);
        contentPanel.add(Box.createVerticalStrut(12));

        contentPanel.add(lblEmail);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(txtEmail);
        contentPanel.add(Box.createVerticalStrut(12));

        contentPanel.add(lblRole);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(cmbRole);
        contentPanel.add(Box.createVerticalStrut(12));

        contentPanel.add(lblPassword);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(txtPassword);
        contentPanel.add(Box.createVerticalStrut(24));

        // Buttons row
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnRow.setBackground(Color.WHITE);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnBack.setBackground(new Color(0xAAAAAA));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.setPreferredSize(new Dimension(100, 38));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCards();
            }
        });

        JButton btnUpdate = new JButton("Update User");
        btnUpdate.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnUpdate.setBackground(buttonColor);
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);
        btnUpdate.setBorderPainted(false);
        btnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnUpdate.setPreferredSize(new Dimension(130, 38));
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String userId   = txtUserId.getText().trim();
                String fName    = txtFName.getText().trim();
                String lName    = txtLName.getText().trim();
                String dob      = txtDob.getText().trim();
                String address  = txtAddress.getText().trim();
                String email    = txtEmail.getText().trim();
                String role     = (String) cmbRole.getSelectedItem();
                String password = new String(txtPassword.getPassword()).trim();

                // Check empty fields
                if (userId.isEmpty() || fName.isEmpty() || lName.isEmpty() ||
                        dob.isEmpty() || address.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Please fill in all fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // TODO: Add your database update code here

                JOptionPane.showMessageDialog(null,
                        "User updated successfully!\n" +
                                "ID: "      + userId  + "\n" +
                                "Name: "    + fName   + " " + lName + "\n" +
                                "DOB: "     + dob     + "\n" +
                                "Address: " + address + "\n" +
                                "Email: "   + email   + "\n" +
                                "Role: "    + role,
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                showCards();
            }
        });

        btnRow.add(btnBack);
        btnRow.add(Box.createHorizontalStrut(12));
        btnRow.add(btnUpdate);

        contentPanel.add(btnRow);

        contentPanel.revalidate();
        contentPanel.repaint();
    }


    //delete form

    private void showDeleteUserForm() {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        // Page title
        JLabel pageTitle = new JLabel("Delete User");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        pageTitle.setForeground(new Color(0xCC0000));
        pageTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(pageTitle);
        contentPanel.add(Box.createVerticalStrut(10));

        // Small description
        JLabel lblDesc = new JLabel("Enter the User ID of the user you want to delete.");
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblDesc.setForeground(Color.GRAY);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lblDesc);
        contentPanel.add(Box.createVerticalStrut(30));

        // User ID label
        JLabel lblUserId = new JLabel("User ID");
        lblUserId.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblUserId.setAlignmentX(Component.LEFT_ALIGNMENT);

        // User ID field
        JTextField txtUserId = new JTextField();
        txtUserId.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtUserId.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtUserId.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(lblUserId);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(txtUserId);
        contentPanel.add(Box.createVerticalStrut(30));

        // Buttons row
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnRow.setBackground(Color.WHITE);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnBack.setBackground(new Color(0xAAAAAA));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setBorderPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.setPreferredSize(new Dimension(100, 38));
        btnBack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCards();
            }
        });

        JButton btnDelete = new JButton("Delete User");
        btnDelete.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnDelete.setBackground(new Color(0xCC0000));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.setBorderPainted(false);
        btnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDelete.setPreferredSize(new Dimension(130, 38));
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String userId = txtUserId.getText().trim();

                // Check if user ID is empty
                if (userId.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Please enter a User ID.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Ask for confirmation before deleting
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete user with ID: " + userId + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {

                    // TODO: Add your database delete code here

                    JOptionPane.showMessageDialog(null,
                            "User with ID: " + userId + " has been deleted successfully.",
                            "Deleted", JOptionPane.INFORMATION_MESSAGE);

                    showCards();
                }
            }
        });

        btnRow.add(btnBack);
        btnRow.add(Box.createHorizontalStrut(12));
        btnRow.add(btnDelete);

        contentPanel.add(btnRow);

        contentPanel.revalidate();
        contentPanel.repaint();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddUserPanel::new);
    }
}