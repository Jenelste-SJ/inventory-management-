package org.example.service;

import org.example.dao.UserDAOImpl;
import org.example.model.User;

public class UserService {
    private final UserDAOImpl userDAO = new UserDAOImpl();

    public void register(String username, String password, String email) {
        User newUser = new User(username, password, "User", email);
        userDAO.addUser(newUser);

        OTPService.generateAndSendOTP(email);
    }

    public User login(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            System.out.println("❌ User not found.");
            return null;
        }

        if (!user.isVerified()) {
            System.out.println("⚠️ Please verify your email before logging in.");
            return null;
        }

        if (user.getPassword().equals(password)) {
            System.out.println("✅ Login successful! Welcome, " + user.getUsername());
            return user;
        } else {
            System.out.println("❌ Incorrect password.");
            return null;
        }
    }

    public void verifyEmail(String username, String otp) {
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            System.out.println("❌ User not found!");
            return;
        }

        if (OTPService.verifyOTP(user.getEmail(), otp)) {
            userDAO.updateUserVerificationStatus(username, true);
        } else {
            System.out.println("❌ Invalid OTP! Please try again.");
        }
    }
}
