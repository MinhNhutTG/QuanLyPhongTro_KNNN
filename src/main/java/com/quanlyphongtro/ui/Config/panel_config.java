package com.quanlyphongtro.ui.Config;

import com.quanlyphongtro.config.SpringContext;
import com.quanlyphongtro.models.Config;
import com.quanlyphongtro.models.TaiKhoan;
import com.quanlyphongtro.service.ConfigService;
import com.quanlyphongtro.service.TaiKhoanService;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class panel_config extends JPanel {

    private static final long serialVersionUID = 1L;

    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color BACKGROUND_COLOR = new Color(240, 242, 245);

    // Map để lưu trữ tham chiếu đến các JTextField dựa trên tên field
    // Key: Tên nhãn (VD: "Số tài khoản"), Value: JTextField tương ứng
    private final Map<String, JTextField> inputsMap = new HashMap<>();

    private final ConfigService configService;
    private final TaiKhoanService taiKhoanService; // [NEW]
    // Định nghĩa các hằng số Key cho Map để tránh gõ sai chính tả
    private final String KEY_SO_TK = "Số tài khoản";
    private final String KEY_TEN_TK = "Tên chủ tài khoản";
    private final String KEY_TEN_NH = "Tên ngân hàng";
    private final String KEY_EMAIL = "Email gửi tin";
    private final String KEY_APP_PASS = "Mật khẩu ứng dụng (App Password)";
    private final String KEY_USER = "Tên đăng nhập";
    private final String KEY_PASS_NEW = "Mật khẩu mới";
    private final String KEY_EMAIL_RECOVERY = "Email khôi phục";

    public panel_config() {
        // Lấy Bean Service
        this.configService = SpringContext.getBean(ConfigService.class);
        this.taiKhoanService = SpringContext.getBean(TaiKhoanService.class); // [NEW]
        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // --- 1. HEADER ---
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setOpaque(false);
        JLabel lblTitle = new JLabel("Cài Đặt Hệ Thống");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(33, 37, 41));
        header.add(lblTitle);
        add(header, BorderLayout.NORTH);

        // --- 2. MAIN CONTENT ---
        JPanel pnlContent = new JPanel(new GridLayout(0, 2, 20, 20));
        pnlContent.setOpaque(false);

        // Card Ngân hàng
        pnlContent.add(createConfigCard("TÀI KHOẢN NGÂN HÀNG",
                new String[]{KEY_SO_TK, KEY_TEN_TK, KEY_TEN_NH}, "🏦", "BANK"));

        // Card Email
        pnlContent.add(createConfigCard("EMAIL HỆ THỐNG",
                new String[]{KEY_EMAIL, KEY_APP_PASS}, "🤖", "EMAIL"));

        // Card Tài khoản (Lưu ý: Phần này cần TaiKhoanService, tạm thời để placeholder)
        pnlContent.add(createConfigCard("TÀI KHOẢN ĐĂNG NHẬP",
                new String[]{KEY_USER, KEY_PASS_NEW, KEY_EMAIL_RECOVERY}, "👤", "ACCOUNT"));

        add(new JScrollPane(pnlContent) {{
            setOpaque(false);
            getViewport().setOpaque(false);
            setBorder(null);
        }}, BorderLayout.CENTER);

        // Load dữ liệu khi mở panel
        loadData();
    }

    // Hàm tạo Card được nâng cấp để xử lý sự kiện Lưu
    private JPanel createConfigCard(String title, String[] fields, String iconText, String type) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(480, 280));
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(218, 220, 224), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // Title
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(PRIMARY_COLOR);
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        card.add(lblTitle, BorderLayout.NORTH);

        // Center Content
        JPanel pnlCenter = new JPanel(new BorderLayout(20, 0));
        pnlCenter.setOpaque(false);

        // Left Panel (Icon + Button)
        JPanel pnlLeft = new JPanel(new BorderLayout(0, 10));
        pnlLeft.setOpaque(false);
        pnlLeft.setPreferredSize(new Dimension(100, 0));

        JLabel lblIcon = new JLabel(iconText, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        pnlLeft.add(lblIcon, BorderLayout.CENTER);

        JButton btnSave = new JButton("Lưu lại");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSave.setBackground(SUCCESS_COLOR);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setPreferredSize(new Dimension(80, 35));

        // GẮN SỰ KIỆN LƯU DỰA TRÊN LOẠI CARD
        btnSave.addActionListener(e -> handleSave(type));

        pnlLeft.add(btnSave, BorderLayout.SOUTH);
        pnlCenter.add(pnlLeft, BorderLayout.WEST);

        // Right Panel (Inputs)
        JPanel pnlInputs = new JPanel();
        pnlInputs.setLayout(new BoxLayout(pnlInputs, BoxLayout.Y_AXIS));
        pnlInputs.setOpaque(false);

        for (String fieldName : fields) {
            JLabel lbl = new JLabel(fieldName);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setForeground(new Color(100, 100, 100));
            pnlInputs.add(lbl);

            JTextField txt = new JTextField();
            txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            txt.setPreferredSize(new Dimension(0, 35));
            txt.setBorder(new CompoundBorder(
                    new LineBorder(new Color(200, 200, 200)),
                    new EmptyBorder(0, 8, 0, 8)
            ));

            // QUAN TRỌNG: Lưu tham chiếu JTextField vào Map để lấy dữ liệu sau này
            inputsMap.put(fieldName, txt);

            pnlInputs.add(txt);
            pnlInputs.add(Box.createVerticalStrut(10));
        }

        pnlCenter.add(pnlInputs, BorderLayout.CENTER);
        card.add(pnlCenter, BorderLayout.CENTER);

        // Hover Effect
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(new CompoundBorder(new LineBorder(PRIMARY_COLOR, 1, true), new EmptyBorder(15, 15, 15, 15)));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(new CompoundBorder(new LineBorder(new Color(218, 220, 224), 1, true), new EmptyBorder(15, 15, 15, 15)));
            }
        });

        return card;
    }

    // --- LOGIC LOAD DỮ LIỆU ---
    private void loadData() {
        new SwingWorker<Void, Void>() {
            Config config;
            TaiKhoan taiKhoan; // [NEW]

            @Override
            protected Void doInBackground() throws Exception {
                // Load song song cả 2 dữ liệu
                config = configService.getConfig();
                taiKhoan = taiKhoanService.getDefaultAccount(); // [NEW]
                return null;
            }

            @Override
            protected void done() {
                try {
                    // 1. Fill Config Data (Ngân hàng & Email)
                    if (config != null) {
                        setTextIfNotNull(KEY_SO_TK, config.getSoTaiKhoan());
                        setTextIfNotNull(KEY_TEN_TK, config.getTenTaiKhoan());
                        setTextIfNotNull(KEY_TEN_NH, config.getTenNganHang());
                        setTextIfNotNull(KEY_EMAIL, config.getEmailSystem());
                        setTextIfNotNull(KEY_APP_PASS, config.getAppPassword());
                    }

                    // 2. [NEW] Fill Account Data
                    if (taiKhoan != null) {
                        setTextIfNotNull(KEY_USER, taiKhoan.getUserName());
                        setTextIfNotNull(KEY_EMAIL_RECOVERY, taiKhoan.getEmailUser());

                        // User Name là khóa chính nên thường không cho sửa, setEditable(false) nếu muốn
                        if (inputsMap.containsKey(KEY_USER)) {
                            inputsMap.get(KEY_USER).setEditable(false);
                            inputsMap.get(KEY_USER).setBackground(new Color(230, 230, 230));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(panel_config.this, "Lỗi tải dữ liệu: " + e.getMessage());
                }
            }
        }.execute();
    }

    private void setTextIfNotNull(String key, String value) {
        if (inputsMap.containsKey(key) && value != null) {
            inputsMap.get(key).setText(value);
        }
    }

    // --- LOGIC XỬ LÝ LƯU ---
    private void handleSave(String type) {
        try {
            switch (type) {
                case "BANK":
                    String soTk = inputsMap.get(KEY_SO_TK).getText().trim();
                    String tenTk = inputsMap.get(KEY_TEN_TK).getText().trim();
                    String tenNh = inputsMap.get(KEY_TEN_NH).getText().trim();

                    configService.saveBankConfig(soTk, tenTk, tenNh);
                    JOptionPane.showMessageDialog(this, "Đã cập nhật thông tin Ngân hàng!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case "EMAIL":
                    String email = inputsMap.get(KEY_EMAIL).getText().trim();
                    String pass = inputsMap.get(KEY_APP_PASS).getText().trim();

                    configService.saveEmailConfig(email, pass);
                    JOptionPane.showMessageDialog(this, "Đã cập nhật Email hệ thống!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case "ACCOUNT":
                    String user = inputsMap.get(KEY_USER).getText().trim();
                    String newPass = inputsMap.get(KEY_PASS_NEW).getText().trim();
                    String emailRec = inputsMap.get(KEY_EMAIL_RECOVERY).getText().trim();

                    // Gọi Service update
                    taiKhoanService.updateTaiKhoan(user, newPass, emailRec);

                    String msg = "Cập nhật tài khoản thành công!";
                    if (newPass.isEmpty()) {
                        msg += "\n(Mật khẩu không thay đổi do bạn để trống ô Mật khẩu mới)";
                    }
                    JOptionPane.showMessageDialog(this, msg, "Thành công", JOptionPane.INFORMATION_MESSAGE);

                    // Clear ô pass sau khi lưu để bảo mật
                    inputsMap.get(KEY_PASS_NEW).setText("");
                    break;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi Validate", JOptionPane.ERROR_MESSAGE);
        }
    }
}