
// Starts the BMI Calculator GUI application
import javax.swing.SwingUtilities;


public class BMICalculator {

    public static void main(String[] args) {
        // Use invokeLater to ensure GUI is created on the correct thread
        SwingUtilities.invokeLater(() -> {
            new BMIView().setVisible(true);
        });
    }
}
