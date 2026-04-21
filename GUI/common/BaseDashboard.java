package GUI.common;

import javax.swing.*;
import java.awt.*;

// Using 'abstract' fulfills the Abstraction requirement
public abstract class BaseDashboard extends JFrame {

    // Encapsulation: Using protected allows child classes to access these [cite: 102]
    protected final Color DARK_BG = new Color(0x2E2E2E);
    protected final Color BUTTON_COLOR = new Color(46, 125, 192);
    protected final CardLayout cardLayout = new CardLayout();
    protected final JPanel contentPanel = new JPanel(cardLayout);
    protected final String loggedInID;

    public BaseDashboard(String title, String loggedInID) {
        this.loggedInID = loggedInID;

        setTitle(title);
        setSize(1200, 750); // Standardized size for all users
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Standardized layout structure
        add(buildSidebar(), BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Child classes will define their own specific panels
        setupUserPanels();

        // Every dashboard starts with a Home panel
        contentPanel.add(buildHomePanel(), "Home");
        cardLayout.show(contentPanel, "Home");
    }

    // Abstract method: Every dashboard MUST implement its own panel setup
    protected abstract void setupUserPanels();

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(DARK_BG);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(40, 15, 20, 15));

        // Add common user info (like the ID label)
        JLabel idLabel = new JLabel("User ID: " + loggedInID);
        idLabel.setForeground(Color.LIGHT_GRAY);
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(idLabel);
        sidebar.add(Box.createVerticalStrut(30));

        // Let the child class add its specific navigation buttons
        addNavigationButtons(sidebar);

        // Add a common Logout button at the bottom for all users
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(createNavButton("Logout", () -> {
            new LoginForm().setVisible(true);
            this.dispose();
        }));

        return sidebar;
    }

    // Abstract method: Each role adds different buttons
    protected abstract void addNavigationButtons(JPanel sidebar);

    // Common method to create styled buttons (Encapsulation) [cite: 102]
    protected JButton createNavButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(BUTTON_COLOR);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    protected JPanel buildHomePanel(){
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        JLabel lbl = new JLabel("Welcome to Faculty System");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 26));
        p.add(lbl);
        return p;
    }
}