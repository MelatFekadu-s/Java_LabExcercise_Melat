import java.sql.*;

public class DatabaseHelper {
    private Connection conn;

    public DatabaseHelper() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:chat.db"); 
            String sql = "CREATE TABLE IF NOT EXISTS messages (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "sender TEXT," +
                         "content TEXT," +
                         "type TEXT," +
                         "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
            conn.createStatement().execute(sql);
            System.out.println("Database ready.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveMessage(String sender, String content, String type) {
        String sql = "INSERT INTO messages(sender, content, type) VALUES(?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sender);
            stmt.setString(2, content);
            stmt.setString(3, type);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
