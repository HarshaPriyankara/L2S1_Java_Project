package gui.student;

import javax.swing.*;
import java.awt.*;
import java.io.File;

    public class MedicalForm extends JFrame {

        private JTextField txtMedicalId, txtRegNo;
        private JTextArea txtReason;
        private JLabel lblFilePath;
        private File selectedFile;

        public MedicalForm(){
            setTitle("Medical Submission Form");
            setSize(650,700);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0; gbc.gridy = 0;
            add(new JLabel("Medical ID:"), gbc);

            gbc.gridx = 1;
            txtMedicalId = new JTextField(15);
            add(txtMedicalId, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            add(new JLabel("Registration No:"), gbc);

            gbc.gridx = 1;
            txtRegNo = new JTextField(15);
            add(txtRegNo, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            add(new JLabel("Reason:"), gbc);

            gbc.gridx = 1;
            txtReason = new JTextArea(4, 15);
            add(new JScrollPane(txtReason), gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            add(new JLabel("Document:"), gbc);

            gbc.gridx = 1;
            JButton btnUpload = new JButton("Choose File");
            add(btnUpload, gbc);

            gbc.gridy = 4;
            lblFilePath = new JLabel("No file selected");
            lblFilePath.setFont(new Font("Arial", Font.ITALIC, 11));
            add(lblFilePath, gbc);

            gbc.gridx = 0; gbc.gridy = 5;
            gbc.gridwidth = 2;
            JButton btnSubmit = new JButton("Submit Medical");
            btnSubmit.setBackground(new Color(40, 167, 69));
            btnSubmit.setForeground(Color.WHITE);
            add(btnSubmit, gbc);

            // File Upload Logic
            btnUpload.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    lblFilePath.setText("Selected: " + selectedFile.getName());
                }
            });

            // Submit Logic
            btnSubmit.addActionListener(e -> {
                String mid = txtMedicalId.getText();
                String reg = txtRegNo.getText();
                String reason = txtReason.getText();

                if (mid.isEmpty() || reg.isEmpty() || reason.isEmpty() || selectedFile == null) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields and upload a file!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Methana thamai database ekata save karana code eka liyanna ona
                    System.out.println("Submitting: " + mid + " | " + reg);
                    JOptionPane.showMessageDialog(this, "Medical Submitted Successfully!");
                }
            });

            setVisible(true);
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                new MedicalForm().setVisible(true);
            });
        }
    }


