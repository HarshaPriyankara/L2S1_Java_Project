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
        JPanel panel = new JPanel();
        setLayout(new BorderLayout());

        JLabel imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon("Images/login2.jpg"); // your image path
        Image img = icon.getImage().getScaledInstance(500, 600, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(img));

        add(imageLabel, BorderLayout.WEST);

        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.white); // set background color
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        formPanel.add(Box.createVerticalGlue());// set middle form

        JLabel lblEmail = new JLabel("Username:");
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
        formPanel.add(txtUsername);

        formPanel.add(Box.createVerticalStrut(20)); // set gap between 2 fields
        // 3. Password Label
        JLabel lblPassword = new JLabel("Password:");
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
        formPanel.add(txtPassword);

        formPanel.add(Box.createVerticalStrut(30)); // gap between pwd field and button
        // 5. Login Button
        btnLogin = new JButton("Login");
        btnLogin.setPreferredSize(new Dimension(150, 45)); // button size
        btnLogin.setBackground(new Color(52, 152, 219)); // set background color blue
        btnLogin.setForeground(Color.WHITE); // set font color white
        btnLogin.setMaximumSize(new Dimension(150, 45));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
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
            JOptionPane.showMessageDialog(this, result.getMessage());
            result.getDashboard().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Login", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
