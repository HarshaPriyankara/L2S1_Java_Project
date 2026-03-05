public class BMIValidator {

 
    public static String validate(String weightStr, String heightStr) {

        //  Empty fields handeling
        if (weightStr == null || weightStr.trim().isEmpty()) {
            return "Weight field cannot be empty!";
        }
        if (heightStr == null || heightStr.trim().isEmpty()) {
            return "Height field cannot be empty!";
        }
        // Must be valid numbers
        double weight, height;
        try {
            weight = Double.parseDouble(weightStr);
        } catch (NumberFormatException e) {
            return "Weight must be a valid number!";
        }

        try {
            height = Double.parseDouble(heightStr);
        } catch (NumberFormatException e) {
            return "Height must be a valid number!";
        }

        // Must be positive values
        if (weight <= 0) {
            return "Weight must be a positive number!";
        }
        if (height <= 0) {
            return "Height must be a positive number!";
        }

        // All checks passed — return null means no error
        return null;


    }
}
