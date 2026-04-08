package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;


public class DBConnection {
    //database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/Faculty_Of_Technology"
            + "?useSSL=false&allowPublicKeyRetrieval=true"
            + "&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "tg1737";


    //single shared connection instance
    private static Connection connection = null;

    //constructor


    public DBConnection() {
    }


    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DB] connection established");
            } catch (ClassNotFoundException e) {
                throw new SQLException(
                        "Class not found: " + e.getMessage()
                );
            }

        }
        return connection;
    }


    //close connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                if(!connection.isClosed()){
                    connection.close();
                    System.out.println("[DB] connection closed");
                }
            } catch (SQLException e) {
                System.out.println("[DB] connection close failed"+e.getMessage());
            } finally {
                connection = null;
            }
        }
    }


    //test connection
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            if(!(connection == null)){
                System.out.println("[DB] connection passed");
                System.out.println("Connected to the database"+connection.getMetaData().getURL());
            }
        } catch (SQLException e) {
            System.err.println("Connection test failed: " + e.getMessage());
        } finally {
            DBConnection.closeConnection();
        }
    }

}