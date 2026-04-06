package GUI.student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ViewMedical extends JFrame {



    public ViewMedical() {

         setTitle("View Medicals");
        setSize(650,700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        String[] columns ={"Medial Id","Reference Number","Medical Date","Submitted Date","Session Type","Status"};

        DefaultTableModel model = new DefaultTableModel(columns,0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        table.setRowHeight(35);
        table.getTableHeader().setBackground(Color.green);
        table.getTableHeader().setForeground(Color.white);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);



        add(scrollPane);
        try{

            Connection connection = Utils.DBConnection.getConnection();
            String sql = "Select * from medical_record WHERE Reg_no ='TG0001'";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

             while (resultSet.next()) {
                 String id = resultSet.getString("medical_id"); // මෙතැන ඔබේ DB column names යොදන්න
                 String ref = resultSet.getString("Reference_Number");
                 String mDate = resultSet.getString("Session_date");
                 String sDate = resultSet.getString("submitted_on");
                 String type = resultSet.getString("Session_type");
                 String status = resultSet.getString("Approved");

                 Object[] row = {id, ref, mDate, sDate, type, status};
                 model.addRow(row);


            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,e.getMessage());
        }

        setVisible(true);

    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new ViewMedical());

    }
}

