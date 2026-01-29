package com.quanlyphongtro.service;

public interface OtpService {
    String generateOtp(String email);
    boolean validateOtp(String email, String otp);
    void clearOtp(String email);
}
