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

        // select unit
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

              JLabel lblWeight = new JLabel("Enter weight:");
        lblWeight.setBounds(30, 70, 100, 25);
        add(lblWeight);

        txtWeight = new JTextField();
        txtWeight.setBounds(150, 70, 180, 25);
        add(txtWeight);

        JLabel lblHeight = new JLabel("Enter height:");
        lblHeight.setBounds(30, 110, 100, 25);
        add(lblHeight);

        txtHeight = new JTextField();
        txtHeight.setBounds(150, 110, 180, 25);
        add(txtHeight);

        // Calculate Button 
        btnCalculate = new JButton("Calculate");
        btnCalculate.setBounds(230, 150, 100, 30);
        add(btnCalculate);

        // Output Section
        JLabel lblBMI = new JLabel("Your BMI:");
        lblBMI.setBounds(30, 200, 100, 25);
        add(lblBMI);

        txtBMI = new JTextField();
        txtBMI.setBounds(150, 200, 180, 25);
        txtBMI.setEditable(false);
        add(txtBMI);

        JLabel lblCat = new JLabel("BMI Category:");
        lblCat.setBounds(30, 240, 100, 25);
        add(lblCat);

        txtCategory = new JTextField();
        txtCategory.setBounds(150, 240, 180, 25);
        txtCategory.setEditable(false);
        add(txtCategory);

        // Reset Button
        btnReset = new JButton("Reset");
        btnReset.setBounds(230, 280, 100, 30);
        add(btnReset);


        //  BMI Panel
        JPanel pnlValues = new JPanel();
        pnlValues.setBorder(BorderFactory.createTitledBorder("BMI VALUES"));
        pnlValues.setBounds(30, 330, 320, 150);
        pnlValues.setLayout(new GridLayout(4, 1));

        pnlValues.add(new JLabel("  Underweight :  less than 18.5"));
        pnlValues.add(new JLabel("  Normal :       between 18.5 and 24.9"));
        pnlValues.add(new JLabel("  Overweight :   between 25 and 29.9"));
        pnlValues.add(new JLabel("  Obese :        30 or greater"));
        add(pnlValues);

    

        // Create button logic
        btnCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCalculate();
            }
        });

        // Reset Buttn
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtWeight.setText("");
                txtHeight.setText("");
                txtBMI.setText("");
                txtCategory.setText("");
            }
        });
    }

    //when click calculate button go to validate page  
    private void handleCalculate() {
        //  Validate input
        String error = BMIValidator.validate(txtWeight.getText(), txtHeight.getText());
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // get inputs
        double weight = Double.parseDouble(txtWeight.getText());
        double height = Double.parseDouble(txtHeight.getText());

        // calculate bmi
        double bmi = BMILogic.calculate(weight, height, rbMetric.isSelected());

        // display result
        txtBMI.setText(String.format("%.2f", bmi));
        txtCategory.setText(BMILogic.getCategory(bmi));
    }
}

