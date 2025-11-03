package org.example.service;

import org.example.util.EmailUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OTPService {

    private static final Map<String, String> otpStorage = new HashMap<>();

    public static String generateOTP(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);

        EmailUtil.sendOTPEmail(email, otp);
        System.out.println("📩 OTP sent successfully to " + email);

        return otp;
    }

    public static boolean verifyOTP(String email, String enteredOtp) {
        String storedOtp = otpStorage.get(email);

        if (storedOtp != null && storedOtp.equals(enteredOtp)) {
            otpStorage.remove(email);
            System.out.println("✅ OTP verified successfully for " + email);
            return true;
        }

        return false;
    }


}
