// File: src/main/java/com/quanlyphongtro/service/ThongKeService.java
package com.quanlyphongtro.service;

import com.quanlyphongtro.dto.ThongKeDto;
import com.quanlyphongtro.dto.ThongKeImpl; // Import class mới
import com.quanlyphongtro.repository.HoaDonRepository;
import com.quanlyphongtro.repository.PhongRepository;
import com.quanlyphongtro.repository.ThongKeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThongKeService {

    @Autowired private ThongKeRepository thongKeRepo;
    @Autowired private PhongRepository phongRepo;
    @Autowired private HoaDonRepository hoaDonRepo;

    // --- CÁC HÀM CARD (GIỮ NGUYÊN) ---
    public long getTotalPhong() { return phongRepo.count(); }
    public long getPhongDaThue() { return phongRepo.countByTrangThai("Đang thuê"); }
    public long getSoHoaDonThang() {
        LocalDate now = LocalDate.now();
        return hoaDonRepo.countHoaDonInMonth(now.getMonthValue(), now.getYear());
    }
    public String getDoanhThuThangHienTai() {
        LocalDate now = LocalDate.now();
        BigDecimal total = hoaDonRepo.sumDoanhThuInMonth(now.getMonthValue(), now.getYear());
        if (total == null) return "0";
        double amount = total.doubleValue();
        if (amount >= 1_000_000_000) return String.format("%.1fB", amount / 1_000_000_000);
        else if (amount >= 1_000_000) return String.format("%.1fM", amount / 1_000_000);
        else if (amount >= 1_000) return String.format("%.1fK", amount / 1_000);
        else return String.format("%.0f", amount);
    }



    // 1. Tổng quan (Không đổi vì query này đơn giản)
    public List<ThongKeDto> getDoanhThuPhong() {
        return thongKeRepo.getDoanhThuTungPhong();
    }

    // 2. Doanh thu Tháng
    public List<ThongKeDto> getDoanhThuThang() {
        List<Object[]> rawData = thongKeRepo.getDoanhThuTheoThangRaw(LocalDate.now().getYear());
        List<ThongKeDto> result = new ArrayList<>();

        for (Object[] row : rawData) {
            String nhan = "Tháng " + row[0]; // row[0] là số tháng (int)
            BigDecimal tien = (BigDecimal) row[1];
            result.add(new ThongKeImpl(nhan, tien, null));
        }
        return result;
    }

    // 3. Doanh thu Quý
    public List<ThongKeDto> getDoanhThuQui() {
        List<Object[]> rawData = thongKeRepo.getDoanhThuTheoQuiRaw(LocalDate.now().getYear());
        List<ThongKeDto> result = new ArrayList<>();

        for (Object[] row : rawData) {
            // row[0] có thể là Double do hàm CEIL, cần ép kiểu
            int qui = ((Number) row[0]).intValue();
            BigDecimal tien = (BigDecimal) row[1];
            result.add(new ThongKeImpl("Quý " + qui, tien, null));
        }
        return result;
    }

    // 4. Điện Nước
    public List<ThongKeDto> getTieuThuDienNuoc() {
        List<Object[]> rawData = thongKeRepo.getTieuThuDienNuocRaw(LocalDate.now().getYear());
        List<ThongKeDto> result = new ArrayList<>();

        for (Object[] row : rawData) {
            String nhan = "T" + row[0];
            // Kết quả SUM có thể là Long hoặc BigDecimal tùy DB, ép về Number cho an toàn
            BigDecimal dien = row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO;
            BigDecimal nuoc = row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO;

            result.add(new ThongKeImpl(nhan, dien, nuoc));
        }
        return result;
    }
}