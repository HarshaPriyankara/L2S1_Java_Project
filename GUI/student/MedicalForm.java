package GUI.student;

import javax.swing.*;
import java.awt.*;

public class MedicalForm extends JFrame {

    public MedicalForm() {
        this("");
    }

    public MedicalForm(String studentId) {
        setTitle("Medical Submission Form");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(new MedicalPanel(studentId), BorderLayout.CENTER);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MedicalForm::new);
    }
}
