package GUI.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.ref.PhantomReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewNotice extends JFrame {



    public  ViewNotice(){

        setTitle("View Notice");
        setSize(1000,600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        String[] navItems = {"Create Notice", "Delete Notice", "Update Notice", "View Notice", "Back"};

        for (String item : navItems) {
            JButton navBtn = createSidebarButton(item);
            navBtn.addActionListener(e -> {
                if (item.equals("Create Notice")) {
                    new CreateNotice().setVisible(true);
                    this.dispose();
                } else if (item.equals("Back")) {
                    this.dispose();
                }
            });
            sidebar.add(navBtn);
        }
        add(sidebar, BorderLayout.WEST);

        add(CreateMainCon(),BorderLayout.CENTER);


    }

    private JPanel CreateMainCon(){
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"NOTICE ID", "TARGET ROLE", "CREATED DATE", "TITLE", "FILE PATH", "---"};

        DefaultTableModel model = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row ,int column){
                return column == 5;
            }
        };
        String sql = "SELECT notice_id, target_role, created_date, title, Content FROM notice";

        try(Connection connection = Utils.DBConnection.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();){

            while (rs.next()){

                Object[] row  = {
                        rs.getString("Notice_id"),
                        rs.getString("Target_role"),
                        rs.getString("Created_date"),
                        rs.getString("Title"),
                        rs.getString("Content"),
                        "View"
                };
                model.addRow(row);
            }

        }catch (SQLException e){
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }

        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.getColumnModel().getColumn(5).setCellRenderer(new ViewNoticeHelper());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        mainContent.add(new JScrollPane(table), BorderLayout.CENTER);
        return mainContent;
    }



    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setBackground(new Color(52, 73, 94));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        return btn;
    }



    public  static void  main(String[] args){

        javax.swing.SwingUtilities.invokeLater(()->{
            new ViewNotice().setVisible(true);
        });
    }
}
