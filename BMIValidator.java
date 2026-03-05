public class BMIValidator {

 
    public static String validate(String weightStr, String heightStr) {

        //  Empty fields handeling
        if (weightStr == null || weightStr.trim().isEmpty()) {
            return "Weight field cannot be empty!";
        }
        if (heightStr == null || heightStr.trim().isEmpty()) {
            return "Height field cannot be empty!";
        }


    }
}
