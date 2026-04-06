package gui.student;

import javax.swing.*;
import java.awt.*;

public class ViewAttendance extends JFrame {


    public ViewAttendance(){
        setTitle(" View Your Daily Attendence ");
        setSize(750,800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(20,20));

        JPanel header = new JPanel();
        header.setBackground(new Color(45, 52, 54));
        header.add(new JLabel(new ImageIcon("logo.png")));
        getContentPane().setBackground(Color.WHITE);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(Color.WHITE);

        setVisible(true);
    }

    public static void main(String[] args) {
        ViewAttendance v = new ViewAttendance();
    }

}
