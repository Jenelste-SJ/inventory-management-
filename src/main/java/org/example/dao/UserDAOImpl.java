package org.example.dao;

import org.example.model.User;
import org.example.util.DBConnection;

import java.sql.*;


public class UserDAOImpl implements UserDAO {


    public void addUser(User user)
    {
        String sql = "INSERT INTO user(username, password, role) VALUES (?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());

            stmt.executeUpdate();
            System.out.println(" User successfully inserted");
        } catch (Exception e) {
            System.out.println("User insertion failed");
        }
    }


    public User getUserByName(String username) {
        String sql = "SELECT * FROM user where username=(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                }
            } catch (Exception e) {
                System.out.println("Error fetching User details" + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}