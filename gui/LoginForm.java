package gui;

import dao.UserDAO;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginForm extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginForm() {
        setTitle("Student Management System - Login");
        setSize(600, 600);
        setLayout(new GridLayout(3, 2)); // සරල layout එකක්

        add(new JLabel("Email:"));
        txtEmail = new JTextField();
        add(txtEmail);

        add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        btnLogin = new JButton("Login");
        add(btnLogin);

        // Button Click Event
        btnLogin.addActionListener(e -> {
            try {
                loginAction();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void loginAction() throws SQLException {
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        UserDAO dao = new UserDAO();
        User user = dao.validateUser(email, password);

        if (user != null) {
            String role = user.getRole();
            JOptionPane.showMessageDialog(this, "Login Successful! Role: " + role);

            // Role එක අනුව ඊළඟ Dashboard එකට යන්න
            openDashboard(role);
            this.dispose(); // Login window එක වහන්න
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Email or Password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openDashboard(String role) {
        /*switch (role.toLowerCase()) {
            case "admin":
                new AdminDashboard().setVisible(true);
                break;
            case "lecturer":
                new LecturerDashboard().setVisible(true);
                break;
            case "undergraduate":
                new StudentDashboard().setVisible(true);
                break;
            case "technicalofficer":
                new TechnicalOfficerDashboard().setVisible(true);
                break;
        }*/
    }
}
