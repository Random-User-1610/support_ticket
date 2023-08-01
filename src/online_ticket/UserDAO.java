package online_ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection conn = JDBCConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String role = rs.getString("role");
                String password = rs.getString("password");

                User user = new User(id, username, role, password);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public User getUserByUsername(String username) {
        try (Connection conn = JDBCConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String role = rs.getString("role");
                String password = rs.getString("password");
                return new User(id, username, role, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // User not found
    }

    public void createUser(String username, String role, String password) {
        try (Connection conn = JDBCConnection.getConnection()) {
            String insertQuery = "INSERT INTO users (username, role, password) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);
            pstmt.setString(1, username);
            pstmt.setString(2, role);
            pstmt.setString(3, password);
            pstmt.executeUpdate();

            System.out.println("User created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
