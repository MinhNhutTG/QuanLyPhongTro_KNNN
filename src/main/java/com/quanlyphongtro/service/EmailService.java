package com.quanlyphongtro.service;

import java.io.File;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendMimeMessage(String to, String subject, String text, File attachment);
}
