package GUI.student;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class MedicalForm extends JFrame {

        private JTextField txtReferenceId, txtRegNo;
        private JTextArea txtReason;
        private JComboBox<String> cmbSessionType;
        private JSpinner dateSpinner;
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
            add(new JLabel("Reference Number:"), gbc);
            gbc.gridx = 1;
            txtReferenceId = new JTextField(15);
            add(txtReferenceId, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            add(new JLabel("Registration No:"), gbc);
            gbc.gridx = 1;
            txtRegNo = new JTextField(15);
            add(txtRegNo, gbc);

            gbc.gridx = 0;gbc.gridy  =2;
            add(new JLabel("Medical Date:"),gbc);
            gbc.gridx =1;
            SpinnerDateModel model = new SpinnerDateModel();
            dateSpinner = new JSpinner(model);
            JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner,"yyyy-MM-dd");
            dateSpinner.setEditor(editor);
            add(dateSpinner,gbc);


            gbc.gridx = 0; gbc.gridy = 3;
            add(new JLabel("Session Type:"), gbc);
            gbc.gridx = 1;
            String[] types = {
                    "NormalDay",
                    "ExamENG1222",
                    "ExamICT1212",
                    "ExamICT1222",
                    "ExamICT1233",
                    "ExamICT1242",
                    "ExamTCS1212",
                    "ExamICT1253",
                    "ExamTMS1233"
            };            cmbSessionType = new JComboBox<>(types);
            add(cmbSessionType, gbc);

            gbc.gridx = 0; gbc.gridy = 4;
            add(new JLabel("Reason:"), gbc);
            gbc.gridx = 1;
            txtReason = new JTextArea(4, 15);
            add(new JScrollPane(txtReason), gbc);

            gbc.gridx = 0; gbc.gridy = 5;
            add(new JLabel("Document:"), gbc);
            gbc.gridx = 1;
            JButton btnUpload = new JButton("Choose File");
            add(btnUpload, gbc);

            gbc.gridx = 4; gbc.gridy = 6;
            lblFilePath = new JLabel("No file selected");
            lblFilePath.setFont(new Font("Arial", Font.ITALIC, 11));
            add(lblFilePath, gbc);

            gbc.gridx = 0; gbc.gridy = 7;
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
                submitData();
            });

            setVisible(true);
                    }

            private void submitData() {
                String mid = txtReferenceId.getText();
                String reg = txtRegNo.getText();
                String reason = txtReason.getText();
                String session_type = (String) cmbSessionType.getSelectedItem();

                java.util.Date spinnerDate = (java.util.Date) dateSpinner.getValue();
                java.sql.Date sqlDate = new java.sql.Date(spinnerDate.getTime());

                if (mid.isEmpty() || reg.isEmpty() || reason.isEmpty() || selectedFile == null) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields and upload a file!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                try {
                    File uploadDir = new File("uploads");
                    if (!uploadDir.exists()) uploadDir.mkdir();

                    String fileName = reg + "_" + System.currentTimeMillis() + "_" + selectedFile.getName();
                    File destinationFile = new File(uploadDir + "/" + fileName);

                    Connection connection = Utils.DBConnection.getConnection();
                    String sql = "insert into medical_record (Reference_Number,Reg_no,Session_date,Reason,Session_type,Document_path) values (?,?,?,?,?,?)";

                    PreparedStatement pst = connection.prepareStatement(sql);
                    pst.setString(1, mid);
                    pst.setString(2, reg);
                    pst.setDate(3, sqlDate);
                    pst.setString(4, reason);
                    pst.setString(5, session_type);
                    pst.setString(6, destinationFile.getPath());

                    int status = pst.executeUpdate();
                    if (status > 0) {
                        JOptionPane.showMessageDialog(this,"Medical Submitted Successfully!");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
                }
            }



           public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> new MedicalForm());
        }
    }


