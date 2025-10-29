package org.example.service;

import org.example.dao.UserDAOImpl;
import org.example.model.User;
import org.example.util.EmailUtil;

import java.util.Scanner;

public class UserService {
    private final UserDAOImpl userDAO = new UserDAOImpl();

    public void register(String username, String password, String email) {
        User newUser = new User(username, password, "User", email);
        userDAO.addUser(newUser);

        OTPService.generateOTP(email);
        System.out.println("Please check your email for OTP verification.");
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

    public void resendOTP() {
        Scanner sc = new Scanner(System.in);
        System.out.print("📧 Enter your registered email: ");
        String email = sc.nextLine();

        User user = userDAO.getUserByEmail(email);
        if (user == null) {
            System.out.println("❌ No user found with this email. Please register first.");
            return;
        }

        if (user.isVerified()) {
            System.out.println("✅ Email already verified. No need to resend OTP.");
            return;
        }

        // Generate and send a new OTP
        String newOtp = OTPService.generateOTP(email);
        EmailUtil.sendOTPEmail(email, newOtp);
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
