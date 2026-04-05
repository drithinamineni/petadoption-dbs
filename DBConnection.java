import java.sql.*;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";
        String user = "system";       
        String password = "oracle";
        return DriverManager.getConnection(url, user, password);
    }
}