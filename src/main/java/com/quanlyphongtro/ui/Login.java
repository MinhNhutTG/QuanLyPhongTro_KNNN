package com.quanlyphongtro.ui;

import com.quanlyphongtro.service.TaiKhoanService;
import com.quanlyphongtro.ui.Home.home;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Login extends JFrame {

    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private final TaiKhoanService taiKhoanService;
    private final ConfigurableApplicationContext context;

    @Autowired
    public Login(TaiKhoanService taiKhoanService, ConfigurableApplicationContext context) {
        this.taiKhoanService = taiKhoanService;
        this.context = context;
        initUI();
    }

    private void initUI() {
        setTitle("Đăng nhập hệ thống");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Logo
        JLabel lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        // Try to load logo
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/logo.png"));
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblLogo.setText("LOGO"); // Fallback
        }
        lblLogo.setBounds(100, 20, 200, 120);
        contentPane.add(lblLogo);

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(0, 150, 400, 40);
        contentPane.add(lblTitle);

        // Username
        JLabel lblUserIcon = new JLabel("Username:"); // Placeholder for icon
        lblUserIcon.setBounds(40, 210, 40, 30);
        contentPane.add(lblUserIcon);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBounds(90, 210, 250, 30);
        contentPane.add(txtUsername);
        
        // Password
        JLabel lblPassIcon = new JLabel("Password:"); // Placeholder for icon
        lblPassIcon.setBounds(40, 260, 40, 30);
        contentPane.add(lblPassIcon);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(90, 260, 250, 30);
        contentPane.add(txtPassword);

        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(41, 128, 185));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBounds(90, 320, 250, 40);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(this::handleLogin);
        contentPane.add(btnLogin);

        // Thêm vào sau phần nút Login
        JButton btnForgotPassword = new JButton("Quên mật khẩu?");
        btnForgotPassword.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        btnForgotPassword.setContentAreaFilled(false);
        btnForgotPassword.setBorderPainted(false);
        btnForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnForgotPassword.setForeground(new Color(41, 128, 185));
        btnForgotPassword.setBounds(90, 365, 250, 20); // Đặt vị trí dưới nút Đăng nhập

        btnForgotPassword.addActionListener(e -> {
            this.dispose(); // Đóng trang Login
            RentalLoginUI forgotPage = context.getBean(RentalLoginUI.class);
            forgotPage.setVisible(true);
        });
        contentPane.add(btnForgotPassword);

        JButton btnExit = new JButton("Thoát");
        btnExit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnExit.setBackground(new Color(231, 76, 60));
        btnExit.setForeground(Color.WHITE);
        btnExit.setBounds(150, 400, 100, 30);
        btnExit.addActionListener(e -> System.exit(0));
        contentPane.add(btnExit);
        
        // Default Enter key to Login
        getRootPane().setDefaultButton(btnLogin);
    }

    private void handleLogin(ActionEvent e) {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        try {
            if (taiKhoanService.checkLogin(username, password)) {
                // JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                this.dispose();
                
                // Open Home Screen
                // Ensure Home logic checks context or is created via Spring
                home homeGui = context.getBean(home.class);
                homeGui.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không đúng!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
