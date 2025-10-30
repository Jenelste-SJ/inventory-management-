package org.example.dao;

import org.example.model.User;
import org.example.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAOImpl implements UserDAO {

    @Override
    public void addUser(User user) {
        String sql = "INSERT INTO user (username, password, role, email, isVerified) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getEmail());
            stmt.setBoolean(5, user.isVerified());
            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println("❌ Error registering user: " + e.getMessage());
        }
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getBoolean("isVerified")
                );
            }

        } catch (Exception e) {
            System.out.println("❌ Error fetching user: " + e.getMessage());
        }
        return null;
    }


    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getBoolean("isVerified")
                );
            }

        } catch (Exception e) {
            System.out.println("❌ Error fetching user by email: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void updateUserVerificationStatus(String username, boolean status) {
        String sql = "UPDATE user SET isVerified = ? WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, status);
            stmt.setString(2, username);
            stmt.executeUpdate();

            System.out.println("✅ Email verified successfully for: " + username);
        } catch (Exception e) {
            System.out.println("❌ Error updating verification status: " + e.getMessage());
        }
    }
}
