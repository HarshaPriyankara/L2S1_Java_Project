package GUI.common;

import DAO.NoticeDAO;
import Models.Notice;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.awt.Insets;

public class ViewNotice extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private List<Notice> noticeList;
    private String userRole;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    public ViewNotice(String userRole, JPanel mainContentPanel, CardLayout cardLayout) {
        this.userRole = userRole;
        this.mainContentPanel = mainContentPanel;
        this.cardLayout = cardLayout;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- Top Panel with Back Button ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> cardLayout.show(mainContentPanel, "Home"));
        topPanel.add(btnBack);
        add(topPanel, BorderLayout.NORTH);

        // --- 1. Table Setup (Columns 3ක් පමණයි) ---
        String[] columns = {"Title", "Added Date", "Action"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(40);

        // --- 2. Button Renderer (Column index 2 ට) ---
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JButton btn = new JButton(value != null ? value.toString() : "View");
                btn.setBackground(new Color(46, 125, 192));
                btn.setForeground(Color.WHITE);
                btn.setBorderPainted(false);
                btn.setFont(new Font("SansSerif", Font.BOLD, 12));
                return btn;
            }
        });

        refreshTable();

        // --- 3. Click Event (Index 2 Click කළ විට) ---
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();

                if (col == 2 && row != -1) {
                    // Table එකේ ID එක පේන්න නැති නිසා noticeList එකෙන් ID එක ගන්නවා
                    int noticeId = noticeList.get(row).getId();
                    String title = noticeList.get(row).getTitle();

                    openNoticeFile(String.valueOf(noticeId), title);
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void refreshTable() {
        NoticeDAO dao = new NoticeDAO();
        noticeList = dao.getNoticesByRole(userRole);
        model.setRowCount(0);

        if (noticeList != null) {
            for (Notice n : noticeList) {
                model.addRow(new Object[]{
                        n.getTitle(),
                        n.getAddedDate(),
                        "View"
                });
            }
        }
    }

    // Admin logic එකම පාවිච්චි කරලා Notice එකේ content කියවන method එක
    private void openNoticeFile(String noticeId, String title) {
        JFrame viewFrame = new JFrame("Notice: " + title);
        viewFrame.setSize(600, 500);
        viewFrame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setMargin(new  Insets(10, 10, 10, 10));
        viewFrame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Database එකෙන් Content (Path) එක අරගෙන File එක කියවීම
        String sql = "SELECT Content FROM notice WHERE Notice_id = ?";
        try (Connection conn = Utils.DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, noticeId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String path = rs.getString("Content");
                File file = new File(path);

                if (file.exists()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            textArea.append(line + "\n");
                        }
                        viewFrame.setLocationRelativeTo(null);
                        viewFrame.setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "File not found: " + path);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}