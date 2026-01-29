package com.quanlyphongtro.service.impl;

import com.quanlyphongtro.models.TaiKhoan;
import com.quanlyphongtro.repository.TaiKhoanRepository;
import com.quanlyphongtro.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaiKhoanServiceImpl implements TaiKhoanService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Override
    public TaiKhoan getDefaultAccount() {
        // Lấy tài khoản đầu tiên tìm thấy trong DB để hiển thị lên Form
        List<TaiKhoan> list = taiKhoanRepository.findAll();
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void updateTaiKhoan(String username, String newPassword, String email) throws Exception {
        // 1. Validate
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("Vui lòng nhập Tên đăng nhập!");
        }

        // 2. Tìm tài khoản
        TaiKhoan tk = taiKhoanRepository.findById(username)
                .orElseThrow(() -> new Exception("Tài khoản '" + username + "' không tồn tại!"));

        // 3. Cập nhật Email
        if (email != null && !email.trim().isEmpty()) {
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new Exception("Email khôi phục không hợp lệ!");
            }
            tk.setEmailUser(email);
        }

        // 4. Cập nhật Mật khẩu (Chỉ update nếu người dùng có nhập)
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            if (newPassword.length() < 6) {
                throw new Exception("Mật khẩu mới phải có ít nhất 6 ký tự!");
            }
            // Lưu ý: Ở môi trường Production nên dùng BCrypt để mã hóa.
            // Ở đây lưu plain-text theo yêu cầu đơn giản của Desktop App hiện tại.
            tk.setPassWord(newPassword);
        }

        taiKhoanRepository.save(tk);
    }

    @Override
    public void initAdminAccountIfNotExist() {
        if (taiKhoanRepository.count() == 0) {
            TaiKhoan admin = new TaiKhoan();
            admin.setUserName("admin");
            admin.setPassWord("123456"); // Mật khẩu mặc định
            admin.setEmailUser("admin@gmail.com");
            taiKhoanRepository.save(admin);
        }
    }

    @Override
    public boolean checkLogin(String username, String password) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("UserName không được trống");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new Exception("PassWord không được trống");
        }

        TaiKhoan tk = taiKhoanRepository.findById(username).orElse(null);
        if (tk == null) {
            return false;
        }
        
        // Simple string comparison for now as per C# legacy behavior
        return tk.getPassWord().equals(password);
    }
}