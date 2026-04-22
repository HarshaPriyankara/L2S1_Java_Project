package GUI.to;

import javax.swing.*;
import java.awt.*;

public class AttendanceManagement extends JPanel {
    private CardLayout cardLayout = new CardLayout();
    private JPanel container = new JPanel(cardLayout);

    public AttendanceManagement() {
        setLayout(new BorderLayout());
        container.add(new AttendanceMainMenuPanel(this), "Menu");
        container.add(new AddAttendancePanel(this), "Add");
        container.add(new UpdateDeleteAttendancePanel(this), "UpdateDelete");

        add(container, BorderLayout.CENTER);
        showPanel("Menu");
    }

    public void showPanel(String name) {
        cardLayout.show(container, name);
    }
}