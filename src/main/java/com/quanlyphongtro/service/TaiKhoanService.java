package com.quanlyphongtro.service;

import com.quanlyphongtro.models.TaiKhoan;
import org.springframework.stereotype.Service;

@Service
public interface TaiKhoanService {
    TaiKhoan getDefaultAccount();
    void updateTaiKhoan(String username, String newPassword, String email) throws Exception;
    void initAdminAccountIfNotExist();
    boolean checkLogin(String username, String password) throws Exception;
}
