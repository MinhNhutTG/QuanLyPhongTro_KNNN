package com.quanlyphongtro.ui;

import com.quanlyphongtro.service.TaiKhoanService;
import com.quanlyphongtro.ui.Home.home;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

@Component
public class Login extends JFrame {

    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private final TaiKhoanService taiKhoanService;
    private final ConfigurableApplicationContext context;

    @Autowired
    public Login(TaiKhoanService taiKhoanService,
                 ConfigurableApplicationContext context) {
        this.taiKhoanService = taiKhoanService;
        this.context = context;
        initUI();
    }

    private void initUI() {
        setTitle("Đăng nhập hệ thống");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(null); // Layout tự do để chỉnh tọa độ
        setContentPane(contentPane);

        // ===== 1. LOGO =====
        JLabel lblLogo = new JLabel("", SwingConstants.CENTER);
        lblLogo.setBounds(100, 10, 200, 120); // Y=10
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/logo.png"));
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblLogo.setText("LOGO");
        }
        contentPane.add(lblLogo);

        // ===== 2. TITLE =====
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(41, 128, 185));
        lblTitle.setBounds(0, 140, 400, 40); // Y=140
        contentPane.add(lblTitle);

        // ===== 3. CÁC Ô NHẬP LIỆU (ĐẶT TRỰC TIẾP, KHÔNG QUA PANEL) =====
        
        // --- Dòng Username (Y=200) ---
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsername.setBounds(50, 200, 80, 30); // X=50, Y=200
        contentPane.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setBounds(135, 200, 200, 30); // X=140, Y=200
        contentPane.add(txtUsername);

        // --- Dòng Password (Y=245) - Cách dòng trên 45px ---
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setBounds(50, 245, 80, 30); // Y=245
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(135, 245, 200, 30); // Y=245
        contentPane.add(txtPassword);

        // ===== 4. LOGIN BUTTON =====
        // Ô Password kết thúc ở Y = 245 + 30 = 275.
        // Ta đặt nút ở Y=295 (chỉ cách 20px)
        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(41, 128, 185));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Kéo dài nút ra cho cân đối (Width 290)
        btnLogin.setBounds(50, 295, 290, 40); 
        btnLogin.addActionListener(this::handleLogin);
        contentPane.add(btnLogin);

        // ===== 5. FORGOT PASSWORD =====
        // Cách nút Login 10px -> Y=345
        JButton btnForgotPassword = new JButton("Quên mật khẩu?");
        btnForgotPassword.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        btnForgotPassword.setContentAreaFilled(false);
        btnForgotPassword.setBorderPainted(false);
        btnForgotPassword.setForeground(new Color(41, 128, 185));
        btnForgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnForgotPassword.setBounds(50, 345, 290, 25);
        btnForgotPassword.addActionListener(e -> {
            dispose();
            RentalLoginUI forgotUI = context.getBean(RentalLoginUI.class);
            forgotUI.setVisible(true);
        });
        contentPane.add(btnForgotPassword);

        // ===== 6. EXIT BUTTON =====
        JButton btnExit = new JButton("Thoát");
        btnExit.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnExit.setBackground(new Color(231, 76, 60));
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);
        btnExit.setBounds(150, 390, 100, 30); // Y=390
        btnExit.addActionListener(e -> System.exit(0));
        contentPane.add(btnExit);

        // Enter = Login
        getRootPane().setDefaultButton(btnLogin);
    }

    private void handleLogin(ActionEvent e) {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        try {
            if (taiKhoanService.checkLogin(username, password)) {
                dispose();
                home homeUI = context.getBean(home.class);
                homeUI.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Tên đăng nhập hoặc mật khẩu không đúng!",
                        "Lỗi đăng nhập",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Lỗi hệ thống",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}