package com.quanlyphongtro.service.impl;

import com.quanlyphongtro.models.Config;
import com.quanlyphongtro.repository.ConfigRepository;
import com.quanlyphongtro.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private ConfigRepository configRepository;

    private JavaMailSender getJavaMailSender() {
        Config config = configRepository.findAll().stream().findFirst().orElse(null);
        if (config == null || config.getEmailSystem() == null || config.getAppPassword() == null) {
            throw new RuntimeException("Chưa cấu hình Email hệ thống trong cơ sở dữ liệu!");
        }

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(config.getEmailSystem());
        mailSender.setPassword(config.getAppPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        // props.put("mail.debug", "true");

        return mailSender;
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        JavaMailSender emailSender = getJavaMailSender();
        Config config = configRepository.findAll().stream().findFirst().orElseThrow();
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(config.getEmailSystem());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendMimeMessage(String to, String subject, String text, File attachment) {
        try {
            JavaMailSender emailSender = getJavaMailSender();
            Config config = configRepository.findAll().stream().findFirst().orElseThrow();

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(config.getEmailSystem());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // true = html

            if (attachment != null) {
                helper.addAttachment(attachment.getName(), attachment);
            }

            emailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi khi gửi email: " + e.getMessage());
        }
    }
}
