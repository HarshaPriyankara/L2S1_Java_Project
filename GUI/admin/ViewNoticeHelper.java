package GUI.admin;

import DAO.NoticeDAO;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.*;

// INHERITANCE: Extending JButton and implementing Context interface for cell rendering.
// This is a prime example of Inheritance via both class extension and interface implementation.
class ViewNoticeHelper extends JButton implements TableCellRenderer {
    public ViewNoticeHelper() { setOpaque(true); }
    public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
        setText((v == null) ? "View" : v.toString());
        return this;
    }
}

// INHERITANCE: Inherits standard editing behaviors via DefaultCellEditor
class ButtonEditor extends DefaultCellEditor {
    // ENCAPSULATION: State variables hidden 
    protected JButton button;
    private String label;
    private int currentRow;
    private JTable currentTable;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> {
            fireEditingStopped();

            Object noticeIdObj = currentTable.getValueAt(currentRow, 0);
            int noticeId = Integer.parseInt(noticeIdObj.toString());

            JFrame viewFile = new JFrame("Notice Details " + noticeId);
            viewFile.setSize(700, 600);
            viewFile.setLayout(new BorderLayout());

            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            viewFile.add(new JScrollPane(textArea), BorderLayout.CENTER);
            viewFile.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            viewFile.setLocationRelativeTo(null);

            // ABSTRACTION: Direct Database logic has been hidden behind the NoticeDAO
            NoticeDAO dao = new NoticeDAO();
            String filePath = dao.getNoticeContentPath(noticeId);

            if (filePath != null) {
                File notice = new File(filePath);

                if (notice.exists()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(notice))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            textArea.append(line + "\n");
                        }
                        viewFile.setVisible(true);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No notice found with that ID or file is missing.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No notice found in database.");
            }
        });
    }

    public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
        this.currentTable = t;
        this.currentRow = r;

        label = (v == null) ? "View" : v.toString();
        button.setText(label);
        return button;
    }

    public Object getCellEditorValue() { return label; }
}
