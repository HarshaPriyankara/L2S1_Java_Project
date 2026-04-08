package GUI.to;

import javax.swing.*;
import java.awt.*;

public class MedicalManagement extends JPanel {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer;

    public MedicalManagement(TechnicalOfficerDashboard dashboard) {
        setLayout(new BorderLayout());
        mainContainer = new JPanel(cardLayout);

        mainContainer.add(createMenuPanel(), "MedicalMenu");
        mainContainer.add(new AddMedicalPanel(cardLayout, mainContainer), "AddForm");
        mainContainer.add(new UpdateMedicalPanel(cardLayout, mainContainer), "UpdateForm");
        mainContainer.add(new DeleteMedicalPanel(cardLayout, mainContainer), "DeleteForm");

        add(mainContainer, BorderLayout.CENTER);
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(20, 0, 20, 0);

        JButton btnAdd = createBigButton("Add New Medical");
        JButton btnUpdate = createBigButton("Update Medical");
        JButton btnDelete = createBigButton("Delete Medical Record");

        btnAdd.addActionListener(e -> cardLayout.show(mainContainer, "AddForm"));
        btnUpdate.addActionListener(e -> cardLayout.show(mainContainer, "UpdateForm"));
        btnDelete.addActionListener(e -> cardLayout.show(mainContainer, "DeleteForm"));

        panel.add(btnAdd, gbc);
        panel.add(btnUpdate, gbc);
        panel.add(btnDelete, gbc);
        return panel;
    }

    private JButton createBigButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(600, 100));
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        return btn;
    }
}