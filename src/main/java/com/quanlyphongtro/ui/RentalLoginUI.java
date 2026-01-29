package com.quanlyphongtro.ui;

import com.quanlyphongtro.models.TaiKhoan;
import com.quanlyphongtro.repository.TaiKhoanRepository;
import com.quanlyphongtro.service.EmailService;
import com.quanlyphongtro.service.OtpService;
import com.quanlyphongtro.ui.Home.home;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class RentalLoginUI extends JFrame {
    private final ConfigurableApplicationContext context;
    private final TaiKhoanRepository taiKhoanRepository;
    private final EmailService emailService;
    private final OtpService otpService;

    private JTextField txtEmail;
    private JTextField txtCode;

    @Autowired
    public RentalLoginUI(ConfigurableApplicationContext context,
                         TaiKhoanRepository taiKhoanRepository,
                         EmailService emailService,
                         OtpService otpService) {
        this.context = context;
        this.taiKhoanRepository = taiKhoanRepository;
        this.emailService = emailService;
        this.otpService = otpService;
        initUI();
    }

    private void initUI() {
        setTitle("Quên mật khẩu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel chính màu xanh Navy như ảnh
        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(42, 82, 134));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // 1. Nút Back (Quay lại)
        JButton btnBack = new JButton("X");
        btnBack.setFont(new Font("Arial", Font.BOLD, 20));
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(new Color(231, 76, 60)); // Màu đỏ
        btnBack.setBounds(15, 15, 50, 40);
        btnBack.addActionListener(e -> {
            this.dispose(); // Đóng trang này
            Login loginPage = context.getBean(Login.class);
            loginPage.setVisible(true); // Quay lại trang Login
        });
        contentPane.add(btnBack);

        // 2. Logo (Dạng tròn)
        JLabel lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/logo.png"));
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblLogo.setText("LOGO"); // Fallback
        }
        lblLogo.setBounds(100, 20, 200, 120);
        contentPane.add(lblLogo);

        // 3. Email đăng ký
        JLabel lblEmail = new JLabel("Email đăng ký");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setBounds(50, 300, 200, 25);
        contentPane.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(50, 330, 230, 35);
        contentPane.add(txtEmail);

        JButton btnSend = new JButton("Gửi");
        btnSend.setBackground(new Color(40, 167, 69));
        btnSend.setForeground(Color.WHITE);
        btnSend.setBounds(290, 330, 60, 35);
        btnSend.addActionListener(e -> sendOtp());
        contentPane.add(btnSend);

        // 4. Mã Code
        JLabel lblCode = new JLabel("Mã Code");
        lblCode.setForeground(Color.WHITE);
        lblCode.setBounds(50, 375, 200, 25);
        contentPane.add(lblCode);

        txtCode = new JTextField();
        txtCode.setBounds(50, 400, 300, 35);
        contentPane.add(txtCode);

        // 5. Nút Đăng nhập (Xác nhận sau khi nhập code)
        JButton btnConfirm = new JButton("Đăng nhập");
        btnConfirm.setBackground(new Color(40, 167, 69));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnConfirm.setBounds(100, 460, 200, 45);
        btnConfirm.addActionListener(e -> verifyOtpAndLogin());
        contentPane.add(btnConfirm);

        // 6. Ghi chú phía dưới
        JTextArea txtNote = new JTextArea("Vui lòng nhập địa chỉ email bạn đã dùng để đăng ký tài khoản. Chúng tôi sẽ gửi mã xác thực...");
        txtNote.setBounds(40, 520, 320, 50);
        txtNote.setWrapStyleWord(true);
        txtNote.setLineWrap(true);
        txtNote.setOpaque(false);
        txtNote.setEditable(false);
        txtNote.setForeground(Color.WHITE);
        txtNote.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        contentPane.add(txtNote);
    }

    private void sendOtp() {
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Email!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra email có tồn tại trong DB không
        TaiKhoan tk = taiKhoanRepository.findByEmailUser(email);
        if (tk == null) {
            JOptionPane.showMessageDialog(this, "Email không tồn tại trong hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String otp = otpService.generateOtp(email);
            String subject = "Mã xác thực đăng nhập - QuanLyPhongTro";
            String text = "Xin chào " + tk.getUserName() + ",\n\n" +
                          "Mã OTP của bạn là: " + otp + "\n" +
                          "Mã này có hiệu lực trong phiên làm việc hiện tại.\n\n" +
                          "Trân trọng,\nĐội ngũ quản lý.";
            
            emailService.sendSimpleMessage(email, subject, text);
            JOptionPane.showMessageDialog(this, "Mã OTP đã được gửi đến email của bạn!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi gửi mail: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verifyOtpAndLogin() {
        String email = txtEmail.getText().trim();
        String code = txtCode.getText().trim();

        if (email.isEmpty() || code.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập Email và Mã Code!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isValid = otpService.validateOtp(email, code);
        if (isValid) {
            otpService.clearOtp(email);
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            home homeGui = context.getBean(home.class);
            homeGui.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Mã OTP không đúng hoặc đã hết hạn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}