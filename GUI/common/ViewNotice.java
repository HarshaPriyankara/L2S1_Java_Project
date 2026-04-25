package GUI.common;

import Controllers.NoticeControllers.NoticeContentResult;
import Controllers.NoticeControllers.NoticeViewController;
import Models.Notice;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

// INHERITANCE: Extending JPanel 
public class ViewNotice extends JPanel {

    // ENCAPSULATION: All internal fields are kept private
    private JTable table;
    private DefaultTableModel model;
    private List<Notice> noticeList;
    private String userRole;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    private final NoticeViewController noticeController = new NoticeViewController();

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

        // --- 1. Table Setup ---
        String[] columns = {"Title", "Added Date", "Action"};
        
        // POLYMORPHISM: Overriding anonymous inner class method to disable cell editing
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(40);

        // --- 2. Button Renderer ---
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

        // --- 3. Click Event ---
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                int col = table.getSelectedColumn();

                if (col == 2 && row != -1) {
                    int noticeId = noticeList.get(row).getId();
                    String title = noticeList.get(row).getTitle();

                    openNoticeFile(noticeId, title);
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void refreshTable() {
        noticeList = noticeController.getNoticesByRole(userRole);
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

    private void openNoticeFile(int noticeId, String title) {
        JFrame viewFrame = new JFrame("Notice: " + title);
        viewFrame.setSize(600, 500);
        viewFrame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        viewFrame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        NoticeContentResult result = noticeController.loadNoticeContent(noticeId);
        if (result.isSuccess()) {
            textArea.setText(result.getContent());
            viewFrame.setLocationRelativeTo(null);
            viewFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Notice", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
