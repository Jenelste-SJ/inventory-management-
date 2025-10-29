package org.example.service;

import org.example.util.EmailUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPService {

    // Temporary in-memory OTP storage
    private static final Map<String, String> otpStorage = new HashMap<>();

    /**
     * Generates a 6-digit OTP, stores it temporarily, and sends it to the user's email.
     */
    public static String generateOTP(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);

        EmailUtil.sendOTPEmail(email, otp);
        System.out.println("📩 OTP sent successfully to " + email);

        return otp;
    }

    /**
     * Verifies the entered OTP for a given email.
     */
    public static boolean verifyOTP(String email, String enteredOtp) {
        String storedOtp = otpStorage.get(email);

        if (storedOtp != null && storedOtp.equals(enteredOtp)) {
            otpStorage.remove(email); // remove once verified
            System.out.println("✅ OTP verified successfully for " + email);
            return true;
        }

        System.out.println("❌ Invalid or expired OTP for " + email);
        return false;
    }


}
