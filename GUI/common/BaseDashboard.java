package GUI.common;

import Models.User;
import javax.swing.*;
import java.awt.*;

import java.awt.image.BufferedImage;

// Using 'abstract' fulfills the Abstraction requirement
public abstract class BaseDashboard extends JFrame {

    // Encapsulation: Using protected allows child classes to access these [cite: 102]
    protected final Color DARK_BG = new Color(0x2E2E2E);
    protected final Color BUTTON_COLOR = new Color(46, 125, 192);
    protected final CardLayout cardLayout = new CardLayout();
    protected final JPanel contentPanel = new JPanel(cardLayout);
    protected final String loggedInID;

    protected final User currentUser;

    public BaseDashboard(String title, User user) {
        this.currentUser = user;
        this.loggedInID = user.getUserID();

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

        // 1. Profile Picture at the top
        String imagePath = currentUser.getProfilePicPath();
        JLabel photoLabel = new JLabel();
        photoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (imagePath != null && !imagePath.isEmpty()) {
            photoLabel.setIcon(createCircularIcon(imagePath));
        } else {
            setDefaultAvatar(photoLabel);
        }
        sidebar.add(photoLabel);
        sidebar.add(Box.createVerticalStrut(15));

        // 2. User ID Label
        JLabel idLabel = new JLabel("User ID: " + loggedInID);
        idLabel.setForeground(Color.LIGHT_GRAY);
        idLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(idLabel);
        sidebar.add(Box.createVerticalStrut(30));

        // 3. Child class buttons (Admin, Student, etc.)
        addNavigationButtons(sidebar);

        // 4. Logout button at the very bottom
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

    // Helper Method 1: Processes the image into a circle
    private ImageIcon createCircularIcon(String path) {
        int size = 100;
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();

            BufferedImage master = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = master.createGraphics();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Create the circular clip
            g2.setClip(new java.awt.geom.Ellipse2D.Double(0, 0, size, size));
            g2.drawImage(img, 0, 0, size, size, null);
            g2.setClip(null);

            // Add a border that matches your button color
            g2.setColor(BUTTON_COLOR);
            g2.setStroke(new BasicStroke(3f));
            g2.drawOval(1, 1, size - 3, size - 3);

            g2.dispose();
            return new ImageIcon(master);
        } catch (Exception e) {
            return null; // Fallback to default if image fails to load
        }
    }

    // Helper Method 2: Draws a default avatar if no picture exists
    private void setDefaultAvatar(JLabel label) {
        int size = 100;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background Circle
        g2.setColor(new Color(0x555555));
        g2.fillOval(0, 0, size, size);

        // Body Shapes
        g2.setColor(new Color(0xAAAAAA));
        g2.fillOval(35, 20, 30, 30); // Head
        g2.fillArc(15, 58, 70, 55, 0, 180); // Shoulders

        g2.dispose();
        label.setIcon(new ImageIcon(img));
    }
}