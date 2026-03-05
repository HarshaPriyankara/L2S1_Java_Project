import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BMIView extends JFrame {

    //field declare
    private JTextField txtWeight, txtHeight, txtBMI, txtCategory;
    private JRadioButton rbEnglish, rbMetric;
    private JButton btnCalculate, btnReset;
    private ButtonGroup unitGroup;

    public BMIView() {
        //create title
        setTitle("BMI CALCULATOR");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        // select units
        JLabel lblUnits = new JLabel("Select Units:");
        lblUnits.setBounds(30, 30, 100, 25);
        add(lblUnits);

        rbEnglish = new JRadioButton("English");
        rbEnglish.setBounds(150, 30, 80, 25);

        rbMetric = new JRadioButton("Metrics", true); // Default selected
        rbMetric.setBounds(240, 30, 80, 25);

        unitGroup = new ButtonGroup();
        unitGroup.add(rbEnglish);
        unitGroup.add(rbMetric);

        add(rbEnglish);
        add(rbMetric);
