package com.BackEnd.BidPro.Util;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;
@Component
public class OtpUtil {
    private static final String OTP_CHARS = "0123456789"; // Characters used for OTP generation
    private static final int OTP_LENGTH = 4; // Length of the OTP code

    public static String generateOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(OTP_CHARS.charAt(random.nextInt(OTP_CHARS.length())));
        }
        return otp.toString();
    }
}
