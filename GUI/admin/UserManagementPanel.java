package GUI.admin;

import Controllers.AdminProfileController;
import Models.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserManagementPanel extends JPanel {
    private final AdminProfileController controller = new AdminProfileController();
    private final JPanel contentPanel = new JPanel();
    private static final Color BUTTON_COLOR = new Color(46, 125, 192);
    private static final Color CARD_COLOR   = new Color(85, 179, 232);


    // create main base panel through constructor
    public UserManagementPanel() {
        setLayout(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
        showCards();
    }

    //creates card for each facility and add action event when clicking
    private void showCards() {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));
        contentPanel.add(makeCard("Create New User", e -> showCreateForm()));
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(makeCard("Update User Details", e -> showUpdateForm()));
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(makeCard("Delete User", e -> showDeleteForm()));
        refresh();
    }

    //for create cards
    private JPanel makeCard(String title, ActionListener action) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        card.add(lbl);
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { action.actionPerformed(null); }
        });
        return card;
    }


    //create new user creating form
    private void showCreateForm() {
        prepareForm("Create New User", BUTTON_COLOR);

        JTextField txtId = addField("User ID");
        JTextField txtFN = addField("First Name");
        JTextField txtLN = addField("Last Name");
        JTextField txtEm = addField("Email");
        JTextField txtDb = addField("DOB (YYYY-MM-DD)");
        JTextField txtPh = addField("Contact No");
        JTextField txtAd = addField("Address");
        JTextField txtPic = addPhotoPicker("Profile Photo");
        JComboBox<String> cmbRole = addRoleCombo();
        JPasswordField txtPw = addPasswordField("Password");

        // back and save buttons
        contentPanel.add(Box.createVerticalStrut(20));

        backButton(buttonRow());
        JButton save = actionButton(buttonRow(), "Save User", BUTTON_COLOR);

        //set user details into user object
        save.addActionListener(e -> {
            User u = new User();
            u.setUserID(txtId.getText().trim());
            u.setFname(txtFN.getText().trim());
            u.setLname(txtLN.getText().trim());
            u.setEmail(txtEm.getText().trim());
            u.setContactNo(txtPh.getText().trim());
            u.setAddress(txtAd.getText().trim());
            u.setRole(toRoleValue((String) cmbRole.getSelectedItem()));
            u.setPassword(new String(txtPw.getPassword()).trim());
            u.setProfilePicPath(txtPic.getText().trim());

            try {
                u.setDob(java.time.LocalDate.parse(txtDb.getText().trim()));  //convert string to date
            } catch (java.time.format.DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.");
                return;
            }

            handleResponse(controller.registerUser(u));
        });
        refresh();
    }


    private void showUpdateForm() {
        prepareForm("Update User Details", BUTTON_COLOR);

        // button for load user by id
        JTextField txtSearch = addField("Search User ID");
        JButton fetchBtn = actionButton(buttonRow(), "Load Data", BUTTON_COLOR);

        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(new JSeparator()); //vertical line
        contentPanel.add(Box.createVerticalStrut(15));

        //fields
        JTextField txtNewId   = addField("New User ID");
        JTextField txtFN      = addField("First Name");
        JTextField txtLN      = addField("Last Name");
        JTextField txtEmail   = addField("Email Address");
        JTextField txtDb      = addField("DOB (YYYY-MM-DD)");
        JTextField txtContact = addField("Contact No");
        JTextField txtAddr    = addField("Address");
        JComboBox<String> cmbRole = addRoleCombo();
        JTextField txtPic     = addPhotoPicker("Update Photo");
        JPasswordField txtPw  = addPasswordField("New Password(optional)");

        // disable editing untill user data loaded
        setFieldsEnabled(false, txtNewId, txtFN, txtLN, txtEmail, txtDb, txtContact, txtAddr, txtPic, txtPw, cmbRole);

        JButton updateBtn = actionButton(buttonRow(), "Update User", BUTTON_COLOR);
        updateBtn.setEnabled(false);

        // Load Logic
        fetchBtn.addActionListener(e -> {
            User u = controller.fetchUser(txtSearch.getText());
            if (u != null) {
                txtNewId.setText(u.getUserID());
                txtFN.setText(u.getFname());
                txtLN.setText(u.getLname());
                txtEmail.setText(u.getEmail());
                txtDb.setText(u.getDateOfBirth().toString());
                txtContact.setText(u.getContactNo());
                txtAddr.setText(u.getAddress());
                txtPic.setText(u.getProfilePicPath());
                cmbRole.setSelectedItem(toRoleLabel(u.getRole()));

                setFieldsEnabled(true, txtNewId, txtFN, txtLN, txtEmail, txtDb, txtContact, txtAddr, txtPic, txtPw, cmbRole);
                updateBtn.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "User not found.");
            }
        });

        // Update Logic
        updateBtn.addActionListener(e -> {
            User u = new User();
            u.setUserID(txtNewId.getText().trim());
            u.setOriginalUserID(txtSearch.getText().trim());
            u.setFname(txtFN.getText().trim());
            u.setLname(txtLN.getText().trim());
            u.setEmail(txtEmail.getText().trim());
            u.setContactNo(txtContact.getText().trim());
            u.setAddress(txtAddr.getText().trim());
            u.setRole(toRoleValue((String) cmbRole.getSelectedItem()));
            u.setProfilePicPath(txtPic.getText().trim());

            try {
                u.setDob(java.time.LocalDate.parse(txtDb.getText().trim()));
            } catch (java.time.format.DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.");
                return;
            }

            String newPw = new String(txtPw.getPassword()).trim();
            if (newPw.isEmpty()) {
                //if empty set old password as new
                u.setPassword(controller.fetchUser(txtSearch.getText().trim()).getPassword());
            } else {
                u.setPassword(newPw);
            }

            String res = controller.updateProfile(u);
            handleResponse(res);
        });
        refresh();
    }

    private void showDeleteForm() {
        prepareForm("Delete User", Color.RED);
        JTextField txtId = addField("Enter ID to Delete");
        JButton del = actionButton(buttonRow(), "Delete Permanent", Color.RED);
        del.addActionListener(e -> handleResponse(controller.deleteUser(txtId.getText())));
        refresh();
    }


    private void prepareForm(String title, Color c) {
        contentPanel.removeAll();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        lbl.setForeground(c);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lbl);
        contentPanel.add(Box.createVerticalStrut(20));

    }


    //for back button
    private JPanel buttonRow() {
        JPanel r = new JPanel(new FlowLayout(FlowLayout.LEFT));
        r.setBackground(Color.WHITE);
        r.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(r);
        return r;
    }

    private void backButton(JPanel r) {
        JButton b = new JButton("Back");
        b.addActionListener(e -> showCards());
        r.add(b);
    }


    private JButton actionButton(JPanel r, String label, Color bg) {
        JButton b = new JButton(label);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        r.add(b);
        return b;
    }


    private void handleResponse(String res) {
        JOptionPane.showMessageDialog(this, res.split(":")[1].trim());
        if (res.startsWith("SUCCESS"))
            showCards(); //if success call again cardInterface
    }

    //for better GUI view
    private void refresh() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    //role selection field
    private JComboBox<String> addRoleCombo() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel("Role");
        lbl.setPreferredSize(new Dimension(150, 30));
        JComboBox<String> c = new JComboBox<>(new String[]{"Admin", "Student", "Lecturer", "Technical Officer"});
        c.setPreferredSize(new Dimension(350, 30));

        row.add(lbl); row.add(c);
        contentPanel.add(row);
        return c;
    }

    private String toRoleValue(String label) {
        if (label == null) return "";
        switch (label) {
            case "Admin":
                return "admin";
            case "Student":
                return "student";
            case "Lecturer":
                return "lecturer";
            case "Technical Officer":
                return "techofficer";
            default:
                return label.trim().toLowerCase();
        }
    }

    private String toRoleLabel(String role) {
        if (role == null) return "Student";
        String value = role.trim().toLowerCase();
        switch (value) {
            case "admin":
                return "Admin";
            case "student":
            case "undergraduate":
                return "Student";
            case "lecture":
            case "lecturer":
                return "Lecturer";
            case "technical officer":
            case "techofficer":
                return "Technical Officer";
            default:
                return "Student";
        }
    }


    //create photo picker for profile photos
    private JTextField addPhotoPicker(String labelText) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(150, 30));
        JTextField path = new JTextField();
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

        row.add(lbl); row.add(path); row.add(btn);
        contentPanel.add(row);
        return path;
    }


    //passed fields for disable
    private void setFieldsEnabled(boolean en, JComponent... comps) {
        for (JComponent c : comps) c.setEnabled(en);
    }



    private JTextField addField(String labelText) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(150, 30));
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(350, 30));

        row.add(lbl); row.add(f);
        contentPanel.add(row);
        return f;
    }



    private JPasswordField addPasswordField(String labelText) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(150, 30));
        JPasswordField f = new JPasswordField();
        f.setPreferredSize(new Dimension(350, 30));

        row.add(lbl); row.add(f);
        contentPanel.add(row);
        return f;
    }




}
