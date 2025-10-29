package org.example.service;

import org.example.util.EmailUtil;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OTPServiceTest {

    private MockedStatic<EmailUtil> mockedEmailUtil;

    @BeforeEach
    void setUp() {
        // Mock the static EmailUtil.sendOTPEmail() method
        mockedEmailUtil = mockStatic(EmailUtil.class);
    }

    @AfterEach
    void tearDown() {
        if (mockedEmailUtil != null) {
            mockedEmailUtil.close();
        }
    }

    @Test
    void testGenerateOTP_ShouldSendEmailAndStoreOTP() {
        String email = "test@example.com";

        // Mock email sending (no real email)
        mockedEmailUtil.when(() -> EmailUtil.sendOTPEmail(anyString(), anyString())).thenAnswer(invocation -> null);

        // Generate OTP
        String otp = OTPService.generateOTP(email);

        assertNotNull(otp);
        assertEquals(6, otp.length());
        mockedEmailUtil.verify(() -> EmailUtil.sendOTPEmail(eq(email), eq(otp)), times(1));

        // Check that OTP can be verified correctly
        boolean result = OTPService.verifyOTP(email, otp);
        assertTrue(result, "OTP should verify correctly");

        // Verify that OTP is removed after successful verification
        boolean resultAfterRemoval = OTPService.verifyOTP(email, otp);
        assertFalse(resultAfterRemoval, "OTP should not verify twice");
    }

    @Test
    void testVerifyOTP_WithInvalidOtp_ShouldFail() {
        String email = "wrong@example.com";

        // Mock email sending
        mockedEmailUtil.when(() -> EmailUtil.sendOTPEmail(anyString(), anyString())).thenAnswer(invocation -> null);

        // Generate OTP
        OTPService.generateOTP(email);

        // Verify with wrong OTP
        boolean result = OTPService.verifyOTP(email, "123456");
        assertFalse(result, "Invalid OTP should not verify");
    }
}
