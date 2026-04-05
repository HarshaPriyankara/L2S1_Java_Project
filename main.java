import GUI.admin.AdminDashboard;
import GUI.common.LoginForm;

public class main{
    public static void main(String[] main){

        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });

    }
}