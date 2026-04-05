package gui.student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class ViewMedical extends JFrame {



    public ViewMedical() {

        JFrame frame = new JFrame();
        setTitle("View Medicals");
        setSize(650,700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns ={"Serial Number","Reference Number","Medical Date","Medical Certificate Number","Submitted Date","Status"};

       // DefaultTableModel model = new DefaultTableModel(data,columns);
        setVisible(true);

    }


    public static void main(String[] args) {

        ViewMedical v = new ViewMedical();


    }
}

