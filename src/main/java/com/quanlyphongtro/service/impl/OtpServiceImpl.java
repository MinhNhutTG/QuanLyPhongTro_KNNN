package com.quanlyphongtro.service.impl;

import com.quanlyphongtro.service.OtpService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {

    // Key: Email, Value: OTP
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Override
    public String generateOtp(String email) {
        // Generate 6-digit OTP
        int otpValue = 100000 + random.nextInt(900000);
        String otp = String.valueOf(otpValue);
        otpStorage.put(email, otp);
        return otp;
    }

    @Override
    public boolean validateOtp(String email, String otp) {
        if (email == null || otp == null) {
            return false;
        }
        String storedOtp = otpStorage.get(email);
        return otp.equals(storedOtp);
    }

    @Override
    public void clearOtp(String email) {
        otpStorage.remove(email);
    }
}
