package com.quanlyphongtro.service.impl;

import com.quanlyphongtro.models.ChiTietHopDong;
import com.quanlyphongtro.models.HopDongThue;
import com.quanlyphongtro.models.KhachThue;
import com.quanlyphongtro.repository.ChiTietHopDongRepository;
import com.quanlyphongtro.repository.HopDongThueRepository;
import com.quanlyphongtro.service.ChiTietHopDongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChiTietHopDongServiceImpl implements ChiTietHopDongService {

    @Autowired
    private ChiTietHopDongRepository chiTietHopDongRepository;
    
    @Autowired
    private HopDongThueRepository hopDongThueRepository;

    @Override
    public KhachThue getKhachThueByHopDong(String idHopDong) {
        HopDongThue hopDong = hopDongThueRepository.findById(idHopDong).orElse(null);
        if (hopDong == null) return null;

        List<ChiTietHopDong> list = chiTietHopDongRepository.findByHopDong(hopDong);
        
        // Ưu tiên lấy người đại diện
        for (ChiTietHopDong ct : list) {
            String role = ct.getVaiTro(); // e.g., "Đại diện"
            if (role != null && role.toLowerCase().contains("đại diện")) {
                return ct.getKhachThue();
            }
        }
        
        // Nếu không có, lấy người đầu tiên
        if (!list.isEmpty()) {
            return list.get(0).getKhachThue();
        }
        
        return null;
    }
}
