package online_ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();

        try (Connection conn = JDBCConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tickets")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("user_id");
                String issueDescription = rs.getString("issue_description");
                String status = rs.getString("status");
                String createdAt = rs.getString("created_at");
                String resolvedAt = rs.getString("resolved_at");

                Ticket ticket = new Ticket(id, userId, issueDescription, status, createdAt, resolvedAt);
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    public void raiseTicket(int userId, String issueDescription) {
        try (Connection conn = JDBCConnection.getConnection()) {
            String insertQuery = "INSERT INTO tickets (user_id, issue_description, status) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertQuery);
            pstmt.setInt(1, userId);
            pstmt.setString(2, issueDescription);
            pstmt.setString(3, "OPEN");
            pstmt.executeUpdate();

            System.out.println("Your Ticket has been raised successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//    String updateQuery = "SELECT issue_description from tickets WHERE id = ?";

    public String issueName(int ticketId) {
        String issueDescription = null;

        try (Connection conn = JDBCConnection.getConnection()) {
            String query = "SELECT issue_description FROM tickets WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, ticketId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                issueDescription = rs.getString("issue_description");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return issueDescription;
    }
    
    
    public void resolveTicket(int ticketId) {
        try (Connection conn = JDBCConnection.getConnection()) {
            String updateQuery = "UPDATE tickets SET status = 'RESOLVED', resolved_at = CURRENT_TIMESTAMP WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateQuery);
            pstmt.setInt(1, ticketId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("The Ticket You had raised has been resolved successfully!");
            } else {
                System.out.println("Ticket not found or already resolved.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
