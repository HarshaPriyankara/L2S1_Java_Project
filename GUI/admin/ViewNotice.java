package GUI.admin;

import DAO.AdminDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.lang.ref.PhantomReference;


public class ViewNotice extends JPanel {

    private NoticeManagementPanel parentPanel;
    private JTable noticeTable;

    public ViewNotice(NoticeManagementPanel parentPanel) {

        this.parentPanel = parentPanel;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> parentPanel.showMainButtons());
        topPanel.add(btnBack);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Title", "Target Roles", "Added Date", "Action"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Action column එක විතරක් Edit කරන්න දෙන්න (Button එක ඔබන්න ඕන නිසා)
            }
        };

        noticeTable = new JTable(model);
        noticeTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        noticeTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), noticeTable));

        noticeTable.setRowHeight(30);
        loadTableData(model);
        add(new JScrollPane(noticeTable), BorderLayout.CENTER);
    }


    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private JTable table;

        public ButtonEditor(JCheckBox checkBox, JTable table) {
            super(checkBox);
            this.table = table;
            button = new JButton("View");
            button.addActionListener(e -> {
                int row = table.getSelectedRow();
                int id = (int) table.getValueAt(row, 0);
                String title = (String) table.getValueAt(row, 1);

                AdminDAO dao = new AdminDAO();
                String path = dao.getNoticeContentPath(id);

                if (path != null) {
                    Frame frame = (Frame) SwingUtilities.getWindowAncestor(table);
                    new NoticeViewer(frame, title, path).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(table, "File path not found in Database!");
                }
                fireEditingStopped();
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("View");
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }


    private void loadTableData(DefaultTableModel model) {
        AdminDAO dao = new AdminDAO();
        List<Object[]> data = dao.getAllNotices();
        for (Object[] row : data) {
            // Database එකෙන් එන columns 4 ට අමතරව 5 වෙනි column එකට "View" අගය එකතු කළා
            Object[] rowWithButton = {row[0], row[1], row[2], row[3], "View"};
            model.addRow(rowWithButton);
        }


    }

}