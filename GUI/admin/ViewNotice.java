package GUI.admin;

import DAO.AdminDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

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

        String[] columns = {"ID", "Title", "Target Roles", "Added Date", "View", "Update", "Delete"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column >= 4;
            }
        };

        noticeTable = new JTable(model);

        // Column 4: View Button
        noticeTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer("View", null));
        noticeTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), noticeTable));

        // Column 5: Update Button
        noticeTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer("Update", new Color(40, 167, 69)));
        noticeTable.getColumnModel().getColumn(5).setCellEditor(new UpdateButtonEditor(new JCheckBox(), noticeTable, parentPanel));

        // Column 6: Delete Button
        noticeTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer("Delete", new Color(220, 53, 69)));
        noticeTable.getColumnModel().getColumn(6).setCellEditor(new DeleteButtonEditor(new JCheckBox(), noticeTable, model));

        noticeTable.setRowHeight(35);
        loadTableData(model);
        add(new JScrollPane(noticeTable), BorderLayout.CENTER);
    }

    private void loadTableData(DefaultTableModel model) {
        AdminDAO dao = new AdminDAO();
        List<Object[]> data = dao.getAllNotices();
        model.setRowCount(0);
        for (Object[] row : data) {
            Object[] rowWithButton = {row[0], row[1], row[2], row[3], "View", "Update", "Delete"};
            model.addRow(rowWithButton);
        }
    }

    // --- Renderer ---
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text, Color bg) {
            setText(text);
            if (bg != null) {
                setBackground(bg);
                setForeground(Color.WHITE);
                setOpaque(true);
                setBorderPainted(false);
            }
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // --- View Button Editor ---
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        public ButtonEditor(JCheckBox checkBox, JTable table) {
            super(checkBox);
            button = new JButton("View");
            button.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    int id = (int) table.getValueAt(row, 0);
                    String title = (String) table.getValueAt(row, 1);
                    AdminDAO dao = new AdminDAO();
                    String path = dao.getNoticeContentPath(id);
                    if (path != null) {
                        Frame frame = (Frame) SwingUtilities.getWindowAncestor(table);
                        new NoticeViewer(frame, title, path).setVisible(true);
                    }
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }
    }

    // --- Update Button Editor ---
    class UpdateButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private int currentRow;

        public UpdateButtonEditor(JCheckBox checkBox, JTable table, NoticeManagementPanel parent) {
            super(checkBox);
            button = new JButton("Update");
            button.setBackground(new Color(40, 167, 69));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> {
                int id = (int) table.getValueAt(currentRow, 0);
                String title = (String) table.getValueAt(currentRow, 1);
                parent.showUpdateNotice(id, title);
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.currentRow = row;
            return button;
        }
    }

    // --- Delete Button Editor ---
    class DeleteButtonEditor extends DefaultCellEditor {
        protected JButton button;

        public DeleteButtonEditor(JCheckBox checkBox, JTable table, DefaultTableModel model) {
            super(checkBox);
            button = new JButton("Delete");
            button.setBackground(new Color(220, 53, 69));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    int id = (int) table.getValueAt(row, 0);
                    String title = (String) table.getValueAt(row, 1);
                    int confirm = JOptionPane.showConfirmDialog(button, "Delete " + title + "?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        AdminDAO dao = new AdminDAO();
                        String path = dao.getNoticeContentPath(id);
                        if (dao.deleteNotice(id, path)) {
                            model.removeRow(row);
                            JOptionPane.showMessageDialog(button, "Deleted successfully!");
                        }
                    }
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }
    }

}