package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // ✔️ FIXED: make them static + correct DB name
    static String url = "jdbc:mysql://localhost:3306/faculty_of_technology?useSSL=false&serverTimezone=UTC";
    static String user = "root";
    static String password = "";

    // single shared connection
    private static Connection connection = null;

    // get connection
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("[DB] connection established");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver not found: " + e.getMessage());
            }
        }
        return connection;
    }

    // close connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("[DB] connection closed");
                }
            } catch (SQLException e) {
                System.out.println("[DB] close failed: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }

    // test connection
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("[DB] connection passed");
                System.out.println("Connected to: " + conn.getMetaData().getURL());
            }
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
        } finally {
            DBConnection.closeConnection();
        }
    }
}