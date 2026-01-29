package com.quanlyphongtro.ui;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;

@Component
public class RentalLoginUI extends JFrame {
    private final ConfigurableApplicationContext context;

    public RentalLoginUI(ConfigurableApplicationContext context) {
        this.context = context;
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
        JButton btnBack = new JButton("←");
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
        lblLogo.setBounds(100, 80, 200, 200);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/logo_tron.png"));
            Image img = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblLogo.setText("LOGO ROUND");
        }
        contentPane.add(lblLogo);

        // 3. Email đăng ký
        JLabel lblEmail = new JLabel("Email đăng ký");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setBounds(50, 300, 200, 25);
        contentPane.add(lblEmail);

        JTextField txtEmail = new JTextField();
        txtEmail.setBounds(50, 330, 230, 35);
        contentPane.add(txtEmail);

        JButton btnSend = new JButton("Gửi");
        btnSend.setBackground(new Color(40, 167, 69));
        btnSend.setForeground(Color.WHITE);
        btnSend.setBounds(290, 330, 60, 35);
        contentPane.add(btnSend);

        // 4. Mã Code
        JLabel lblCode = new JLabel("Mã Code");
        lblCode.setForeground(Color.WHITE);
        lblCode.setBounds(50, 375, 200, 25);
        contentPane.add(lblCode);

        JTextField txtCode = new JTextField();
        txtCode.setBounds(50, 400, 300, 35);
        contentPane.add(txtCode);

        // 5. Nút Đăng nhập (Xác nhận sau khi nhập code)
        JButton btnConfirm = new JButton("Đăng nhập");
        btnConfirm.setBackground(new Color(40, 167, 69));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnConfirm.setBounds(100, 460, 200, 45);
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
}