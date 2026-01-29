package com.quanlyphongtro.service.impl;

import com.quanlyphongtro.models.Config;
import com.quanlyphongtro.repository.ConfigRepository;
import com.quanlyphongtro.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@Transactional
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private ConfigRepository configRepository;

    @Override
    public Config getConfig() {
        // Luôn lấy bản ghi đầu tiên, nếu không có trả về object rỗng để tránh null pointer
        return configRepository.findById(1L).orElse(new Config());
    }

    @Override
    public void saveBankConfig(String soTk, String tenTk, String tenNganHang) throws Exception {
        // 1. Validate
        if (soTk.isEmpty() || tenTk.isEmpty() || tenNganHang.isEmpty()) {
            throw new Exception("Vui lòng điền đầy đủ thông tin ngân hàng!");
        }
        if (!soTk.matches("\\d+")) { // Chỉ cho phép số
            throw new Exception("Số tài khoản chỉ được chứa ký tự số!");
        }

        // 2. Save
        Config config = getConfig();
        if (config.getId() == null) config.setId(1L); // Đảm bảo luôn lưu vào ID 1

        config.setSoTaiKhoan(soTk);
        config.setTenTaiKhoan(tenTk.toUpperCase()); // Tên TK thường viết hoa
        config.setTenNganHang(tenNganHang);

        // Giữ nguyên các trường khác nếu null
        if (config.getTenNhaTro() == null) config.setTenNhaTro("Nhà Trọ Mới");

        configRepository.save(config);
    }

    @Override
    public void saveEmailConfig(String email, String password) throws Exception {
        // 1. Validate
        if (email.isEmpty() || password.isEmpty()) {
            throw new Exception("Vui lòng nhập Email và Mật khẩu ứng dụng!");
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(emailRegex, email)) {
            throw new Exception("Định dạng Email không hợp lệ!");
        }

        // 2. Save
        Config config = getConfig();
        if (config.getId() == null) config.setId(1L);

        config.setEmailSystem(email);
        config.setAppPassword(password);

        if (config.getTenNhaTro() == null) config.setTenNhaTro("Nhà Trọ Mới");

        configRepository.save(config);
    }
}