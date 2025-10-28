package org.example.service;

import org.example.util.EmailUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPService {

    // Temporary in-memory OTP storage (email → otp)
    private static final Map<String, String> otpStorage = new HashMap<>();

    /**
     * Generates a 6-digit OTP, stores it, and sends it via email.
     */
    public static void generateAndSendOTP(String email) {
        // Generate random 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);

        // Send OTP via EmailUtil
        try {
            EmailUtil.sendOTPEmail(email, otp);
            System.out.println("📩 OTP sent to " + email);

        } catch (Exception e) {
            System.out.println("❌ Failed to send OTP to " + email + ": " + e.getMessage());
        }
    }

    /**
     * Verifies if the entered OTP matches the one sent.
     */
    public static boolean verifyOTP(String email, String enteredOTP) {
        String correctOTP = otpStorage.get(email);

        if (correctOTP != null && correctOTP.equals(enteredOTP)) {
            otpStorage.remove(email); // Remove after successful verification
            System.out.println("✅ OTP verified successfully for " + email);
            return true;
        } else {
            System.out.println("❌ Invalid OTP entered for " + email);
            return false;
        }
    }


}
