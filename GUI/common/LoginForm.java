package GUI.common;

import Controllers.AuthControllers.LoginController;
import Controllers.AuthControllers.LoginResult;

import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private final LoginController loginController = new LoginController();

    public LoginForm() {
        setTitle("Student Management System - Login");
        setSize(1000, 600);
        setLocationRelativeTo(null);// Align middle of screen
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.APP_BACKGROUND);

        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon("Images/login2.jpg"); // your image path
        Image img = icon.getImage().getScaledInstance(500, 600, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(img));

        add(imageLabel, BorderLayout.WEST);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(UITheme.SURFACE);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        formPanel.add(Box.createVerticalGlue());// set middle form

        JLabel titleLabel = UITheme.createSectionTitle("Welcome Back");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(8));

        JLabel subtitleLabel = new JLabel("Sign in to continue to the Faculty System");
        subtitleLabel.setForeground(UITheme.TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(subtitleLabel);
        formPanel.add(Box.createVerticalStrut(24));

        JLabel lblEmail = new JLabel("Username:");
        lblEmail.setForeground(UITheme.TEXT_PRIMARY);
        lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblEmail);

        formPanel.add(Box.createVerticalStrut(5)); // set gap between label and field

        // 2. Email TextField
        txtUsername = new JTextField();
        // set field size
        Dimension size = new Dimension(300, 40);
        txtUsername.setPreferredSize(size);
        txtUsername.setMaximumSize(size);
        txtUsername.setMinimumSize(size);
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        UITheme.styleTextField(txtUsername);
        formPanel.add(txtUsername);

        formPanel.add(Box.createVerticalStrut(20)); // set gap between 2 fields
        // 3. Password Label
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(UITheme.TEXT_PRIMARY);
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblPassword);

        formPanel.add(Box.createVerticalStrut(5));

        // 4. Password Field
        txtPassword = new JPasswordField();
        // set field size
        txtPassword.setPreferredSize(size);
        txtPassword.setMaximumSize(size);
        txtPassword.setMinimumSize(size);
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPassword.setBackground(UITheme.SURFACE);
        txtPassword.setForeground(UITheme.TEXT_PRIMARY);
        txtPassword.setCaretColor(UITheme.TEXT_PRIMARY);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 214, 224)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        formPanel.add(txtPassword);

        formPanel.add(Box.createVerticalStrut(30)); // gap between pwd field and button
        // 5. Login Button
        btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(150, 45)); // button size
        btnLogin.setMaximumSize(new Dimension(150, 45));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        UITheme.stylePrimaryButton(btnLogin);
        formPanel.add(btnLogin);

        add(formPanel, BorderLayout.CENTER);
        formPanel.add(Box.createVerticalGlue());// set middle form


        // Button Click Event
        btnLogin.addActionListener(e -> loginAction());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loginAction() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        LoginResult result = loginController.authenticate(username, password);
        if (result.isSuccess()) {

            result.getDashboard().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Login", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
