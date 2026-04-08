package GUI.admin;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


// Draws the Button in the cell
    class ViewNoticeHelper extends JButton implements TableCellRenderer {
        public ViewNoticeHelper() { setOpaque(true); }
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            setText((v == null) ? "View" : v.toString());
            return this;
        }
    }

    // Handles the Click event on the Button
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private  int currentRow;
        private  JTable currentTable;


        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                fireEditingStopped();
                // Put your "View Notice" logic here!
              //  JOptionPane.showMessageDialog(button, "Opening file...");

                // Index 0 because Notice ID is the first column in array
                Object noticeId = currentTable.getValueAt(currentRow, 0);

                JFrame viewFile = new JFrame("Notice Details"+ noticeId);
                viewFile.setSize(700, 600);
                viewFile.setLayout(new BorderLayout());

                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                viewFile.add(new JScrollPane(textArea), BorderLayout.CENTER);
                viewFile.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                viewFile.setLocationRelativeTo(null);



                String sql = "SELECT Content FROM notice WHERE Notice_id =?";
                Connection connection = null;
                try {
                    connection = Utils.DBConnection.getConnection();
                    PreparedStatement pst = connection.prepareStatement(sql);
                     pst.setString(1, noticeId.toString());
                     ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        String filePath = rs.getString("Content"); // Get value from 'Content' column                            if (!((line =br.readLine()) != null)){
                        File notice = new File(filePath);

                        if (notice.exists()) {
                        try (BufferedReader br = new BufferedReader(new FileReader(notice))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                textArea.append(line + "\n"); // Add text to the window
                            }
                                viewFile.setVisible(true); // File eka thibboth witharak window eka pennanna
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No notice found with that ID.");

                    }

                } else{
                            JOptionPane.showMessageDialog(null, "No notice found in database.");
                        }

                    } catch (SQLException | IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
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

