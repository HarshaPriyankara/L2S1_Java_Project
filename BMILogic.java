
public class BMILogic {


    public static double calculate(double weight, double height, boolean isMetric) {
        if (isMetric) {
            // Metric Formula: weight(kg) / [height(m)]^2
            return weight / (height * height);
        } else {
            // English Formula: 703 * weight(lbs) / [height(in)]^2
            return (weight * 703) / (height * height);
        }
    }
