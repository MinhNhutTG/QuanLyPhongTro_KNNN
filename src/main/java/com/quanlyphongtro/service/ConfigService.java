package com.quanlyphongtro.service;

import com.quanlyphongtro.models.Config;
import org.springframework.stereotype.Service;

@Service
public interface ConfigService {
    Config getConfig(); // Lấy cấu hình (nếu chưa có thì tạo mới)
    void saveBankConfig(String soTk, String tenTk, String tenNganHang) throws Exception;
    void saveEmailConfig(String email, String password) throws Exception;
}
